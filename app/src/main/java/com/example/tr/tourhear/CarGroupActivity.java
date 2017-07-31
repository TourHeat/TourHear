package com.example.tr.tourhear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

/**
 * Created by ZhangYan on 2017/7/22.
 */

public class CarGroupActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_new_car_group);
        LinearLayout bt_back=(LinearLayout)findViewById(R.id.btn_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void setSchedule(View view) {
        Intent intent = new Intent(CarGroupActivity.this,SetMySchedule.class);
        startActivity(intent);
        //finish();
    }
    //获取颜色
    private int getItemColor(int colorID) {
        return getResources().getColor(colorID);
    }
}
