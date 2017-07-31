package com.example.tr.tourhear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

/**
 * Created by ZhangYan on 2017/7/24.
 */

public class CarDispatchActivity extends Activity {
    private boolean voiceable = true;//车辆调度

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.car_dispatch);


    }

    public void back(View view) {
        finish();
    }

    public void setVoiceState(View view) {
        ImageView v = (ImageView) findViewById(R.id.voice);
        if(voiceable) {
            voiceable = voiceable?false:true;
            v.setBackground(getResources().getDrawable(R.drawable.novoice));
        } else {
            voiceable = voiceable?false:true;
            v.setBackground(getResources().getDrawable(R.drawable.voice));
        }
    }
    public void openMap (View view) {
        Intent intent=new Intent(CarDispatchActivity.this,ShowRouteActivity.class);
        startActivity(intent);
    }

}
