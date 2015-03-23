package com.meme.moi.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Skyro on 20/01/2015.
 */
public class SecondActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);



        Button bouton2 = (Button)findViewById(R.id.button2);
        final TextView maVue = (TextView)findViewById(R.id.textView);
        bouton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maVue.append("lo lo lo ");
                Intent data = new Intent();
                data.putExtra("returnKey1","Test retour");
                setResult(RESULT_OK,data);
                finish();
            }
        });
    }

}

