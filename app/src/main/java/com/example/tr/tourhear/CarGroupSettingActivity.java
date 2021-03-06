package com.example.tr.tourhear;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
     //   final RelativeLayout set_here = (RelativeLayout) findViewById(R.id.SrelativeLayout15);
        final RelativeLayout urgent = (RelativeLayout) findViewById(R.id.SrelativeLayout16);
        final RelativeLayout urgent_sos = (RelativeLayout) findViewById(R.id.SrelativeLayout17);
        final RelativeLayout show_list = (RelativeLayout) findViewById(R.id.SrelativeLayout23);
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
                finish();
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
        /*set_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置集合点
                Intent intent_set_here = new Intent(CarGroupSettingActivity.this, CarGroupMapActivity.class);
                startActivity(intent_set_here);
            }
        });*/
        urgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //紧急发布
                AlertDialog alertDialog = new AlertDialog.Builder(CarGroupSettingActivity.this,R.style.AlertDialog).create();
                alertDialog.show();
                Window window = alertDialog.getWindow();
                window.setContentView(R.layout.dialog_info);
            }
        });
        urgent_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //紧急发布
                AlertDialog alertDialog = new AlertDialog.Builder(CarGroupSettingActivity.this,R.style.AlertDialog).create();
                alertDialog.show();
                Window window = alertDialog.getWindow();
                window.setContentView(R.layout.dialog_info_call);

            }
        });
        show_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = new String[]{"1km","2km","3km","5km"};
                new AlertDialog.Builder(CarGroupSettingActivity.this).setTitle("过远报警距离").setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                        }
                    }
                }).show();
            }
        });
    }
}
