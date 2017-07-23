package com.example.tr.tourhear;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
    }

    public void back(View view){

        finish();
    }
}
