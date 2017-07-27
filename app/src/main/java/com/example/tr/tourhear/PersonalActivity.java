package com.example.tr.tourhear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by ZhangYan on 2017/7/18.
 */

public class PersonalActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.personal_activity);
        LinearLayout bt_back=(LinearLayout)findViewById(R.id.btn_back);
        final RelativeLayout address = (RelativeLayout) findViewById(R.id.SrelativeLayout7);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PersonalActivity.this,AddressActivity.class);
                startActivity(intent);
            }
        });
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
