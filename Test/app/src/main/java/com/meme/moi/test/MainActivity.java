package com.meme.moi.test;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    private static final int REQUEST_CODE = 9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TAG","onCreate");

        Button bouton = (Button)findViewById(R.id.button);
        Button start = (Button) findViewById(R.id.startButton);
        Button stop = (Button) findViewById(R.id.stopButton);
        Button logo = (Button) findViewById(R.id.boutonlogo);
        Button livre = (Button) findViewById(R.id.buttonLivre);
        Button json = (Button) findViewById(R.id.butJson);
        Button async = (Button) findViewById(R.id.butAsync);
        async.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent monIntent = new Intent(MainActivity.this,SecondActivity.class);
//                Intent monIntent = new Intent(MainActivity.this,TestActivityArrayAdapterListView.class);
                Intent monIntent = new Intent(MainActivity.this,AsyncTaskActivity.class);
                startActivityForResult(monIntent, REQUEST_CODE);
            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent monIntent = new Intent(MainActivity.this,SecondActivity.class);
//                Intent monIntent = new Intent(MainActivity.this,TestActivityArrayAdapterListView.class);
                Intent monIntent = new Intent(MainActivity.this,TestActivitySimpleAdapterListView.class);
                startActivityForResult(monIntent, REQUEST_CODE);
            }
        });
        livre.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View v) {
                Intent monIntent = new Intent(MainActivity.this,LivreActivity.class);
                startActivityForResult(monIntent, REQUEST_CODE);
            }

        });
        json.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monIntent = new Intent(MainActivity.this,JsonActivity.class);
                startActivityForResult(monIntent, REQUEST_CODE);
            }


        });
        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent monIntent = new Intent(MainActivity.this,SecondActivity.class);
                Intent monIntent = new Intent(MainActivity.this,TestActivityArrayAdapterListView.class);
                startActivityForResult(monIntent, REQUEST_CODE);
            }
        });

        final Intent intentSabiisu = new Intent(MainActivity.this,MyService.class);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View v) {

                startService(intentSabiisu);
            }

        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intentSabiisu);
            }

        });

        Button contacts = (Button)findViewById(R.id.contacts);
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monIntent = new Intent(MainActivity.this,ContentAdapter.class);
                startActivity(monIntent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data.hasExtra("returnKey1")) {
                //Traitement
                Log.d("centrale", data.getExtras().getString("returnKey1"));
                Toast.makeText(this, data.getExtras().getString("returnKey1"),
                        Toast.LENGTH_SHORT).show();
            }
        }
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
    public void onStart(){
        super.onStart();
        Log.d("TAG_Started","onStart");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("TAG_Resumed","onResume");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d("TAG_stoppé","onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPause","onPause");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("TAG_destroyed","onDestroy");
    }



}
