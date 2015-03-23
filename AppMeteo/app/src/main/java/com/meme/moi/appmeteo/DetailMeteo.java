package com.meme.moi.appmeteo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class DetailMeteo extends ActionBarActivity {
    String nomVille;
    String infoMeteo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detal_meteo);
        Bundle extras = getIntent().getExtras();
        if(extras.containsKey("meteoString")){
            infoMeteo = extras.getString("meteoString");
            String[] semaineMeteo = infoMeteo.split("!");
            nomVille = semaineMeteo[0].split(";")[0];
            this.setTitle(nomVille);
            ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

            for (int i=1;i<semaineMeteo.length;i++){

                String[] todayMeteo = semaineMeteo[i].split(";");
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("jour",todayMeteo[0]);
                map.put("temperature",todayMeteo[1]+"°C");

                String ciel = "";
                if (Boolean.getBoolean(todayMeteo[2])){
                    ciel = "snow";
                } else if (Boolean.getBoolean(todayMeteo[3])){
                    ciel = "rain";
                } else if (Boolean.getBoolean(todayMeteo[4])){
                    ciel =  "cloud";
                } else {
                    ciel = "sun";
                }
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
                map.put("vent",todayMeteo[5]+" km/h");
                listItem.add(map);
            }
            ListView listView = (ListView) findViewById(R.id.list);
            try{

                SimpleAdapter adapter = new SimpleAdapter (this.getApplicationContext(), listItem, R.layout.detail_item,new String[] {"jour","temperature","ciel","vent"}, new int[] {R.id.jour,R.id.temperature,R.id.ciel,R.id.vent});
                listView.setAdapter(adapter);
            }catch (Exception e){

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detal_meteo, menu);
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
