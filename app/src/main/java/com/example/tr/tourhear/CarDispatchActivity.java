package com.example.tr.tourhear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

/**
 * Created by ZhangYan on 2017/7/24.
 */

public class CarDispatchActivity extends Activity {//车辆调度

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.car_dispatch);
        LinearLayout bt_back = (LinearLayout) findViewById(R.id.btn_back);
        LinearLayout bt_more = (LinearLayout) findViewById(R.id.btn_more);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CarDispatchActivity.this,CarDispatchMapActivity.class);
                startActivity(intent);
            }
        });
    }
}
