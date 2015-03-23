package com.meme.moi.appmeteo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ChargementJSON extends Activity {


    protected ArrayList<String> reponse;
    int done = 9999;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chargement_json);
        reponse = new ArrayList<String>();
        Bundle extras = getIntent().getExtras();

        if(extras.containsKey("liste")){

            ArrayList<String> liste = extras.getStringArrayList("liste");

            ProgressBar pBar = (ProgressBar) findViewById(R.id.progressBar);
            pBar.setMax(liste.size()*7);
            pBar.setIndeterminate(false);

            pBar.setProgress(0);
            done = liste.size();
            for (int i=0;i<liste.size();i++){
                String infos[] = liste.get(i).split(";");

                String ville = infos[0];
                String longitude = infos[1];
                String latitude = infos[2];
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                 new WebServiceRequestor(ville,longitude,latitude,params).execute();
            }
        }
    }

    private class WebServiceRequestor extends AsyncTask<String, Void, String> {
        //private ProgressDialog pDialog;
        String nomVille;
        String lon;
        String lat;
        String URL;
        List<NameValuePair> parameters;

        public WebServiceRequestor(String ville ,String longitude, String latitude, List<NameValuePair> params)
        {

            this.nomVille = ville;
            this.lon = longitude;
            this.lat = latitude;
            this.URL = "http://www.infoclimat.fr/public-api/gfs/json?_ll="+latitude+","+longitude+"&_auth=VE4FEgJ8U3Fec1tsAXcKI1M7U2YIfgkuVytRMgtuA34CaVc2Dm4BZwVrB3oCLQo8BCkAYww3UmIKYQtzDH4EZVQ%2BBWkCaVM0XjFbPgEuCiFTfVMyCCgJLlc8UTULeANhAmRXNQ5zAWIFaQdsAiwKPQQxAH8MLFJrCm0LbgxnBGFUNwVlAmFTNF4zWyYBLgo7U2VTMwg%2FCTVXNFE3C2IDaAJpVzIOagFkBWwHewIyCjsENQBkDDpSbgprC2gMfgR4VE4FEgJ8U3Fec1tsAXcKI1M1U20IYw%3D%3D&_c=198cf5d4d6a56c5ef917be91c3485a46";
            this.parameters = params;


        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                String retour = "";
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpEntity httpEntity = null;
                HttpResponse httpResponse = null;
                HttpPost httpPost = new HttpPost(URL);

                if (parameters != null)
                {
                    httpPost.setEntity(new UrlEncodedFormEntity(parameters));
                }
                httpResponse = httpClient.execute(httpPost);
                httpEntity = httpResponse.getEntity();
                String line= EntityUtils.toString(httpEntity);

                JSONObject theObject = new JSONObject(line);

                Calendar calendar = Calendar.getInstance();

                calendar.add(Calendar.DATE,-1);

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat format2 = new SimpleDateFormat("HH");
                for (int i=1;i<7;i++){
                    calendar.add(Calendar.DATE,1);

                    int heure = Integer.parseInt(format2.format(calendar.getTime()).toString());
                    if (heure < 21){
                        heure += 3-heure%3;
                    } else {
                        heure = 21;
                    }
                    String heureString;
                    if (heure<10){
                        heureString = " 0"+Integer.toString(heure);
                    } else {
                        heureString = " "+Integer.toString(heure);
                    }
                    String date = format.format(calendar.getTime()).toString()+heureString+":00:00";
                    JSONObject jsonDuJour = theObject.getJSONObject(date);

                    String jour;
                    switch(calendar.get(Calendar.DAY_OF_WEEK)%7){
                        case 0 : jour = "Lundi";
                            break;
                        case 1 : jour = "Mardi";
                            break;
                        case 2 : jour = "Mercredi";
                            break;
                        case 3 : jour = "Jeudi";
                            break;
                        case 4 : jour = "Vendredi";
                            break;
                        case 5 : jour = "Samedi";
                            break;
                        case 6 : jour = "Dimanche";
                            break;
                        default : jour = "Erreur";
                            break;
                    }
                    double temperature = Double.parseDouble(jsonDuJour.getJSONObject("temperature").getString("2m"));
                    temperature -= 273.15;
                    temperature = Math.round(temperature*100)/100;
                    boolean pluie = Double.parseDouble(jsonDuJour.getString("pluie"))>0.3;
                    boolean snow = jsonDuJour.getString("risque_neige")=="oui";
                    boolean cloud = Double.parseDouble(jsonDuJour.getJSONObject("nebulosite").getString("totale"))>=30;
                    String vent = jsonDuJour.getJSONObject("vent_rafales").getString("10m") ;

                    retour += jour+";"+ String.valueOf(temperature)+";"+String.valueOf(snow)+";"+String.valueOf(pluie)+";"+String.valueOf(cloud)+";"+vent+"!";
                    this.publishProgress();
                }

                return retour;
            } catch (Exception e){

            }
            return "Echec";
        }
        @Override
        protected void onPostExecute(String result)
        { //pDialog.dismiss();

            try {
                reponse.add(nomVille+";"+lon+";"+lat+"!"+result);
                done -=1;
            } catch (Exception e){

            }
            if (done == 0){
                try{
                    Intent data = new Intent();
                    data.putStringArrayListExtra("retour",reponse);
                    setResult(RESULT_OK,data);
                    finish();
                } catch (Exception e){

                }
            }
            else{

            }
            super.onPostExecute(result);
        }
        @Override
        protected void onPreExecute() {

//            pDialog = new ProgressDialog(ChargementJSON.this);
//            pDialog.setMessage("Processing Request...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
            super.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(Void... values) {

            ProgressBar pBar = (ProgressBar) findViewById(R.id.progressBar);
            try{
                int i = pBar.getProgress();
                i++;

                pBar.setProgress(i);

            } catch (Exception e) {

            }
            pBar.setProgress( pBar.getProgress()+1);

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chargement_json, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
