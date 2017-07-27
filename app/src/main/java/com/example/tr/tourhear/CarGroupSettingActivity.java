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
 * Created by ZhangYan on 2017/7/23.
 */

public class CarGroupSettingActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.car_group_setting);
        LinearLayout bt_back = (LinearLayout) findViewById(R.id.btn_back);
        final RelativeLayout group_member = (RelativeLayout) findViewById(R.id.GroupMember);
        final RelativeLayout person_select = (RelativeLayout) findViewById(R.id.SrelativeLayout3);
        final RelativeLayout car_select = (RelativeLayout) findViewById(R.id.SrelativeLayout4);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        group_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //队伍成员
                Intent intent_group_member = new Intent(CarGroupSettingActivity.this, GroupMemberActivity.class);
                startActivity(intent_group_member);
            }
        });
        person_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //人员调度
                Intent intent_person_select = new Intent(CarGroupSettingActivity.this, PersonSelectActivity.class);
                startActivity(intent_person_select);
            }
        });
        car_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //车辆调度
                Intent intent_car_select = new Intent(CarGroupSettingActivity.this, CarSelectActivity.class);
                startActivity(intent_car_select);
            }
        });
    }
}
