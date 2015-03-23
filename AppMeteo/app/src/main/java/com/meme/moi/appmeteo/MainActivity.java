package com.meme.moi.appmeteo;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {
    private static final int REQUEST_CHARGEMENT = 1;
    private static final int REQUEST_CHARGEMENT_LOCAL = 3;
    public static MainActivity MY_MAIN_ACTIVITY;
    protected ArrayList<String> favorisCharges = new ArrayList<String>();
    SharedPreferences preferenceSettings;
    SharedPreferences.Editor preferenceEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Bienvenue sur AppMétéo");
        MY_MAIN_ACTIVITY = this;

        preferenceSettings = PreferenceManager.getDefaultSharedPreferences(this);
        preferenceEditor = preferenceSettings.edit();

        String memoire = preferenceSettings.getString("favorisString","");

        if (memoire.contains(";")){
            String[] memoireSplitted = memoire.split(",");
            ArrayList<String> liste = new ArrayList<>();
            for (int i=0; i<memoireSplitted.length;i++){
                if(memoireSplitted[i]!=""){
                    String infoVille = memoireSplitted[i].split("!")[0];
                    liste.add(infoVille);
                }
            }

            Intent chargementJson = new Intent(MainActivity.this,ChargementJSON.class);
            chargementJson.putStringArrayListExtra("liste",liste);
            startActivityForResult(chargementJson,REQUEST_CHARGEMENT);
        }

        final Button favoris = (Button) findViewById(R.id.Favoris);
        favoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent favorisActivity = new Intent(MainActivity.this,Favoris.class);
                    if (favorisCharges.size()>0){
                        favorisActivity.putStringArrayListExtra("liste",favorisCharges);
                    }

                    startActivity(favorisActivity);
                }catch (Exception e){

                }

            }
        });
        Button actualiser = (Button) findViewById(R.id.Actualiser);
        actualiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restart();
            }
        });


        try{
            final LocationManager monGPS = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Button maPosition = (Button) findViewById(R.id.Pos);


            if ( !monGPS.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                Toast.makeText(getApplicationContext(),"Le GPS n'est pas activé.",Toast.LENGTH_SHORT);
                maPosition.setEnabled(false);
            } else{
                maPosition.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String donneesLocales = "";
                        donneesLocales = "Ici"+Double.toString(Math.round(monGPS.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude()*100)/100.0)+Double.toString(Math.round(monGPS.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude()*100)/100.0);
                        ArrayList<String> liste = new ArrayList<>();
                        liste.add(donneesLocales);

                        Intent monIntent = new Intent(MainActivity.this,ChargementJSON.class);
                        monIntent.putExtra("liste",liste);

                        startActivityForResult(monIntent, REQUEST_CHARGEMENT_LOCAL);
                        Intent meteoLocale = new Intent(MainActivity.this,DetailMeteo.class);
                        startActivity(meteoLocale);
                    }
                });
            }
        } catch (Exception e){

            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);
        }

    }

    protected void restart(){
        MainActivity.this.recreate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CHARGEMENT) {
            if (data.hasExtra("retour")) {

                favorisCharges = data.getExtras().getStringArrayList("retour");

                String memoire = "";

                for (int i=0;i<favorisCharges.size();i++){

                    memoire+=favorisCharges.get(i)+",";
                }
                try{

                    preferenceEditor.putString("favorisString",memoire);
                    preferenceEditor.commit();
                }catch (Exception e){

                }
                remplirListView();
            }
        }
    }

    public void remplirListView(){
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
        for (int i=0;i<favorisCharges.size();i++){
            try{
                Favori favori = new Favori(favorisCharges.get(i));
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("ville",favori.getVille());
                map.put("temperature",favori.getTemperatureToday()+"°C");
                String ciel = favori.getSkyToday();
                switch (ciel) {
                    case "sun":
                        map.put("ciel", String.valueOf(R.drawable.sun));
                        break;
                    case "cloud":
                        map.put("ciel", String.valueOf(R.drawable.cloud));
                        break;
                    case "rain":
                        map.put("ciel", String.valueOf(R.drawable.rain));
                        break;
                    case "snow":
                        map.put("ciel", String.valueOf(R.drawable.snow));
                        break;
                    case "storm":
                        map.put("ciel", String.valueOf(R.drawable.storm));
                        break;
                    case "sun&cloud":
                        map.put("ciel", String.valueOf(R.drawable.mild_cloud));
                        break;
                    default:
                        map.put("ciel", String.valueOf(R.drawable.no_info));
                }
                map.put("repere",Integer.toString(i));
                listItem.add(map);
            }catch (Exception e){

            }
        }

        ListView listView = (ListView) findViewById(R.id.list);
        try{

            SimpleAdapter adapter = new SimpleAdapter (this.getApplicationContext(), listItem, R.layout.main_item,new String[] {"ville","temperature","ciel","repere"}, new int[] {R.id.ville,R.id.temperature,R.id.ciel,R.id.repere});
            listView.setAdapter(adapter);
        }catch (Exception e){

        }

        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String meteoString = favorisCharges.get(position);
                Intent vueDetaillee = new Intent(MainActivity.this,DetailMeteo.class);
                vueDetaillee.putExtra("meteoString",meteoString);
                startActivity(vueDetaillee);
            }
        });
    }

    //@Override
    public void onListItemClick(View v) {

        String repere = "";
        int position =0;
        try {
            LinearLayout linLay = (LinearLayout) v.getParent();
            Button rep = (Button) linLay.getChildAt(0);
            repere = rep.getText().toString();
        }catch (Exception e){

        }
        if(repere!=""){
            position = Integer.parseInt(repere);
        }
        String meteoString = favorisCharges.get(position);
        Intent vueDetaillee = new Intent(MainActivity.this,DetailMeteo.class);
        vueDetaillee.putExtra("meteoString",meteoString);
        startActivity(vueDetaillee);
    }

    public void onClickNomVille(View v) {
        try{

            Button t = (Button) v;
            String repere = "";
            int position =0;
            try {
                repere = t.getText().toString();
            }catch (Exception e){

            }
            if(repere!=""){
                position = Integer.parseInt(repere);
            }
            for (int i=0 ;i<favorisCharges.size();i++){
                String test = favorisCharges.get(i).split(";")[0];
                if (test==repere){
                    position = i;
                }
            }
            String meteoString = favorisCharges.get(position);
            Intent vueDetaillee = new Intent(MainActivity.this,DetailMeteo.class);
            vueDetaillee.putExtra("meteoString",meteoString);
            startActivity(vueDetaillee);
        }catch(Exception e){

        }

    }

    public ArrayList<String> getFavorisCharges(){
        return favorisCharges;
    }
}
