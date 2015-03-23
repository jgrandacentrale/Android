package com.meme.moi.appmeteo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;


public class Method extends Activity {

    public static void onClickVille(View v) {
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
            ArrayList<String> favorisCharges = MainActivity.MY_MAIN_ACTIVITY.getFavorisCharges();
            for (int i=0 ;i<favorisCharges.size();i++){
                String test = favorisCharges.get(i).split(";")[0];
                if (test==repere){
                    position = i;
                }
            }
            String meteoString = favorisCharges.get(position);
            Intent vueDetaillee = new Intent(MainActivity.MY_MAIN_ACTIVITY,DetailMeteo.class);
            vueDetaillee.putExtra("meteoString",meteoString);
            MainActivity.MY_MAIN_ACTIVITY.startActivity(vueDetaillee);
        }catch(Exception e){
            Log.d("debug",e.getMessage());
        }

    }
}
