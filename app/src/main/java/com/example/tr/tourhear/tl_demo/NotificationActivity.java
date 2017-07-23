package com.example.tr.tourhear.tl_demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class NotificationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("notify.act", "NotificationActivity onCreate finish()");
        finish();
    }

}
