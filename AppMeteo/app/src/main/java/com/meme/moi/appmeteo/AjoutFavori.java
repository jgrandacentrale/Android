package com.meme.moi.appmeteo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AjoutFavori extends ActionBarActivity {

    final String apiKey = "AIzaSyARqma7_aSW16UAxb2IedozWPPlKc46OTw";
    final String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=";
    final String param = "types=(cities)&language=fr";
    String result ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_favori);

        SearchView champs = (SearchView) findViewById(R.id.searchView);


        champs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String request = url+query+"&"+param+"&key="+apiKey;
                try
                {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    new CompleteVille(request,params).execute();
                } catch (Exception e){
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>2){
                    String request = url+newText+"&"+param+"&key="+apiKey;
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    new CompleteVille(request,params).execute();
                }
                return false;
            }
        });

        final ListView maListView = (ListView) findViewById(R.id.listView);
        maListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    String nomVille = (String) maListView.getItemAtPosition(position);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    new ChercheVille(nomVille,params).execute();
                }
                catch (Exception e){
                }
            }
        });
    }

    private class CompleteVille extends AsyncTask<String, Void, String> {
        //private ProgressDialog pDialog;
        String URL;
        List<NameValuePair> parameters;

        public CompleteVille(String url, List<NameValuePair> params) {
            this.URL = url;
            this.parameters = params;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpEntity httpEntity = null;
                HttpResponse httpResponse = null;
                HttpPost httpPost = new HttpPost(URL);
                if (parameters != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(parameters));
                }
                httpResponse = httpClient.execute(httpPost);
                httpEntity = httpResponse.getEntity();
                String line = EntityUtils.toString(httpEntity);
                return line;
            } catch (Exception e) {
            }
            return "Echec";
        }

        @Override
        protected void onPostExecute(String result) {
            //pDialog.dismiss();
            ListView listeVilles = (ListView) findViewById(R.id.listView);
            listeVilles.setAdapter(null);

            try {
                JSONObject theObject = new JSONObject(result);
                JSONArray villes = theObject.getJSONArray("predictions");

                ArrayList<String> arrayVilles = new ArrayList<String>();
                for(int i=0; i<villes.length(); i++){
                    String description = villes.getJSONObject(i).getString("description");
                    description = description.split(",")[0];
                    arrayVilles.add(description);
                }
                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),R.layout.mytextview,arrayVilles);
                listeVilles.setAdapter(adapter);
            } catch (Exception e) {
            }
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
//            pDialog = new ProgressDialog(AjoutFavori.this);
//            pDialog.setMessage("Processing Request...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
            super.onPreExecute();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ajout_favori, menu);
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

    private class ChercheVille extends AsyncTask<String, Void, String> {
        //private ProgressDialog pDialog;
        String ville;
        List<NameValuePair> parameters;

        public ChercheVille(String nomVille, List<NameValuePair> params) {
            this.ville = nomVille;
            this.parameters = params;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpEntity httpEntity = null;
                HttpResponse httpResponse = null;
                HttpPost httpPost = new HttpPost("http://api.openweathermap.org/data/2.5/weather?q="+ville+",France");
                if (parameters != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(parameters));
                }
                httpResponse = httpClient.execute(httpPost);
                httpEntity = httpResponse.getEntity();
                String line = EntityUtils.toString(httpEntity);
                JSONObject coordonnees = new JSONObject(line).getJSONObject("coord");
                String lonLat = coordonnees.getString("lon")+";"+coordonnees.getString("lat");
                return lonLat;
            } catch (Exception e) {
            }
            return " raté ";
        }

        @Override
        protected void onPostExecute(String lonLat) {
            //pDialog.dismiss();
            result=ville+";"+lonLat;
            Intent data = new Intent();
            data.putExtra("retour",result);
            setResult(RESULT_OK,data);
            finish();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
//            pDialog = new ProgressDialog(AjoutFavori.this);
//            pDialog.setMessage("Processing Request...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
            super.onPreExecute();
        }
    }
}
