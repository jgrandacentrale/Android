package com.meme.moi.test;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class TestActivitySimpleAdapterListView extends ListActivity {
    private static final String TAG = "MyActivity";
/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//Création de la ArrayList qui nous permettra d'alimenter la listView
    ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
//On déclare la HashMap qui contiendra les informations pour un item
    HashMap<String, String> map;
//Création d'une HashMap pour insérer les informations du premier item de notre listView
    map = new HashMap<String, String>();
    map.put("title", "Android");
    map.put("description", "Hello Android");
    map.put("img", String.valueOf(R.drawable.logo_android));
    listItem.add(map);
//On refait la même chose pour remplir le reste des items de la liste
    map = new HashMap<String, String>();
    map.put("title", "iOS");
    map.put("description", "Hello iOS");
    map.put("img", String.valueOf(R.drawable.logo_apple));
    listItem.add(map);
    map = new HashMap<String, String>();
    map.put("title", "Bada");
    map.put("description", "Hello Bada");
    map.put("img", String.valueOf(R.drawable.logo_samsung));
    listItem.add(map);
    map = new HashMap<String, String>();
    map.put("title", "Windows Phone 7");
    map.put("description", "Hello Windows Phone 7");
    map.put("img", String.valueOf(R.drawable.logo_windows));
    listItem.add(map);
//Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list
//    (listItem) dans la vue affichageitem
    SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.layoutitem, new String[] {"img", "title", "description"}, new int[] {R.id.img, R.id.title, R.id.description});
//On attribut à notre listActivity l'adapter que l'on vient de créer
    setListAdapter(mSchedule);
    Log.i(TAG, "Lancement activity");
}
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
//Récupèrer la Map qui contient les informations de l'item (titre, decription et image)
        HashMap<String, String> map = (HashMap<String, String>) getListAdapter().getItem(position);
        Toast.makeText(this, map.get("title") + " selected", Toast.LENGTH_LONG).show();
    }
}