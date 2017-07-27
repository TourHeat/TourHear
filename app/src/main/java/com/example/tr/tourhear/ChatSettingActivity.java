package com.example.tr.tourhear;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import static com.example.tr.tourhear.R.id.img2;

/**
 * Created by ZhangYan on 2017/7/22.
 */

public class ChatSettingActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_chat_setting);
        LinearLayout bt_back=(LinearLayout)findViewById(R.id.btn_back);
        ImageView show_list=(ImageView) findViewById(img2);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        show_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = new String[]{"创建群聊","创建车队"};
                new AlertDialog.Builder(ChatSettingActivity.this).setTitle("创建").setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                Intent intent1=new Intent(ChatSettingActivity.this,ChatGroupActivity.class);
                                startActivity(intent1);
                                break;
                            case 1:
                                Intent intent2=new Intent(ChatSettingActivity.this,CarGroupActivity.class);
                                startActivity(intent2);
                                break;
                        }
                    }
                }).show();
            }
        });
    }
}
