package com.meme.moi.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String value1 = intent.getStringExtra("Value1");
        Log.i("MyReceiver","value:"+value1);


        throw new UnsupportedOperationException("Not yet implemented");
    }
}
