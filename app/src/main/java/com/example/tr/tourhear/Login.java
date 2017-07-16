package com.example.tr.tourhear;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    private static Handler uiHandler = null;
    private Context uiContext = null;
  //  private RegisterUser registerUser = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public static Handler getUiHandler() {
        return uiHandler;
    }
}
