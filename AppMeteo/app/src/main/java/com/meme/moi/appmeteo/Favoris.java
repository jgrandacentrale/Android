package com.meme.moi.appmeteo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Favoris extends ActionBarActivity {

    private static final int REQUEST_CHARGEMENT = 1;
    private static final int REQUEST_AJOUT = 2;
    private ArrayList<Favori> listeFavoris = new ArrayList<Favori>();
    final Context context = this;
    SharedPreferences preferenceSettings;
    SharedPreferences.Editor preferenceEditor;
//    private static final int PREFERENCE_MODE_PRIVATE = 0;

    private void afficherListeFavoris(){
        try{
            ListView listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(null);
            ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
            String memoire ="";

            for (int i=0; i<listeFavoris.size(); i++){
                HashMap<String,String> map = new HashMap<String,String>();
                Favori favori = listeFavoris.get(i);
                memoire+=favori.getStringMeteo()+",";

                map.put("ville", favori.getVille());

                map.put("temperature",favori.getTemperatureToday() + "°C");

                String ciel = favori.getSkyToday();

                switch (ciel) {
                    case "sun" : map.put("ciel",String.valueOf(R.drawable.sun));
                        break;
                    case "cloud" : map.put("ciel",String.valueOf(R.drawable.cloud));
                        break;
                    case "rain" : map.put("ciel",String.valueOf(R.drawable.rain));
                        break;
                    case "snow" : map.put("ciel",String.valueOf(R.drawable.snow));
                        break;
                    case "storm" : map.put("ciel",String.valueOf(R.drawable.storm));
                        break;
                    case "sun&cloud" : map.put("ciel",String.valueOf(R.drawable.mild_cloud));
                        break;
                    default : map.put("ciel",String.valueOf(R.drawable.no_info));
                }
                listItem.add(map);
            }

            SimpleAdapter adapter = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.favori_item,new String[] {"ville","temperature","ciel"}, new int[] {R.id.Ville,R.id.Temperature,R.id.image});

            listView.setAdapter(adapter);
            preferenceEditor.putString("favorisString",memoire);
            preferenceEditor.commit();

        }catch (Exception e){

        }
    }

    private void chargeUnFavori(String villeString){
        ListView listView = (ListView) findViewById(R.id.listView);
        String[] infoVille = villeString.split(";");
        Intent chargerJSON = new Intent(Favoris.this,ChargementJSON.class);
        ArrayList<String> liste = new ArrayList<String>();
        liste.add(villeString);
        chargerJSON.putExtra("liste", liste);
        startActivityForResult(chargerJSON,REQUEST_CHARGEMENT);

    }

    public void onClickTrashCan(View v){
        try {
            LinearLayout grandparent = (LinearLayout) v.getParent();
            LinearLayout parent = (LinearLayout) grandparent.getChildAt(1);
            TextView villeView = (TextView) parent.getChildAt(0);
            final String nomVille = villeView.getText().toString();
            AlertDialog.Builder boite= new AlertDialog.Builder(context);
            boite.setTitle("Vous allez supprimer un favori");
            boite.setMessage("Etes vous sûr(e) de vouloir supprimer" + nomVille + "de vos favoris ?");
            boite.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            effaceUnFavori(nomVille);
                        }
                    }
            );
            boite.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            boite.show();
        }catch (Exception e){

        }

    }

    private void effaceUnFavori(String nomVille){
        String memoire = "";
        for (int i=0;i<listeFavoris.size();i++){
            Favori favori = listeFavoris.get(i);
            if (favori.getVille()!=nomVille){
                memoire+=favori.getStringMeteo()+",";
            }
        }

        preferenceEditor.putString("favorisString",memoire);
        preferenceEditor.commit();
        Intent redémarrer = new Intent(getApplicationContext(),Favoris.class);
        startActivity(redémarrer);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);

        preferenceSettings = PreferenceManager.getDefaultSharedPreferences(this);
        preferenceEditor = preferenceSettings.edit();
        String memoire = preferenceSettings.getString("favorisString","");

        if (memoire.contains(";")){
            String[] memoireSplitted = memoire.split(",");
            for (int i=0; i<memoireSplitted.length;i++){
                if(memoireSplitted[i]!=""){
                    listeFavoris.add(new Favori(memoireSplitted[i]));
                }
            }
        }
        Button ajoutFavoris = (Button) findViewById(R.id.ajout);
        ajoutFavoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ajoutFav = new Intent(Favoris.this,AjoutFavori.class);
                startActivityForResult(ajoutFav, REQUEST_AJOUT);
            }
        });

        if (getIntent().hasExtra("liste")){

            Bundle extras = getIntent().getExtras();

            ArrayList<String> donneesVilles = extras.getStringArrayList("liste");

            try{
                for (int i=0; i< donneesVilles.size(); i++){
                    String meteoUneville = donneesVilles.get(i);

                    Favori villeFavorite = new Favori(meteoUneville);
                    for (int j = 0; j> listeFavoris.size();j++){
                        if (listeFavoris.get(j).getVille()!=villeFavorite.getVille()){
                            listeFavoris.add(villeFavorite);
                        }
                    }

                }
                if (listeFavoris.size()>0){

                    afficherListeFavoris();
                }
            }catch (Exception e){

            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favoris, menu);
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

        if (resultCode == RESULT_OK && requestCode == REQUEST_CHARGEMENT){
            if (data.hasExtra("retour")){

                String stringMeteo = data.getExtras().getStringArrayList("retour").get(0);
                String nomVille = stringMeteo.split("!")[0].split(";")[0];


                String memoire = preferenceSettings.getString("favorisString","");
                memoire+=stringMeteo;

                preferenceEditor.putString("favorisString",memoire);
                preferenceEditor.commit();

                listeFavoris.clear();
                String[] newPreferences = memoire.split(",");
                for (int i=0;i<newPreferences.length;i++){
                    String str = newPreferences[i];
                    if (str.contains(";")){
                        Favori ville = new Favori(str);
                        listeFavoris.add(ville);
                    }
                }
                afficherListeFavoris();
            }
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_AJOUT) {
            if (data.hasExtra("retour")) {
                try{

                    String stringVille = data.getExtras().getString("retour");
                    chargeUnFavori(stringVille);
                }catch (Exception e){

                }
            }
        }
    }
}
