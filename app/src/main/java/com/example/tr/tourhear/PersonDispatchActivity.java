package com.example.tr.tourhear;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.example.tr.tourhear.utils.Constants;
import com.example.tr.tourhear.view.CircleImageView;

import java.io.File;
import java.io.IOException;

import static com.example.tr.tourhear.utils.Constants.CALL_MODE_MULT;
import static com.example.tr.tourhear.utils.Constants.CALL_MODE_SIGNAL;

/**
 * Created by ZhangYan on 2017/7/24.
 */

public class PersonDispatchActivity extends Activity implements Runnable {//人员调度
    private boolean voiceable = true;//车辆调度
    private VoiceLineView voiceLineView;
    private int CALL_MODE = CALL_MODE_SIGNAL;
    private ImageView mike1;//麦克风1
    private ImageView mike2;//麦克风1
    private ImageView mike3;//麦克风1
    private ImageView mike4;//麦克风1
    private TextView btn_speack;

    private ImageView iconVoice;
    private LinearLayout layout_whospeak;
    private TextView layout_whospeak_name;
    private CircleImageView layout_whospeak_headportrait;
    private LinearLayout bottom;
    //声纹
    private MediaRecorder mMediaRecorder;
    private boolean isAlive = true;
    private boolean mikeOpen[] = {true,false,false,false};
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mMediaRecorder==null) return;
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / 100;
            double db = 0;// 分贝
            //默认的最大音量是100,可以修改，但其实默认的，在测试过程中就有不错的表现
            //你可以传自定义的数字进去，但需要在一定的范围内，比如0-200，就需要在xml文件中配置maxVolume
            //同时，也可以配置灵敏度sensibility
            if (ratio > 1)
                db = 20 * Math.log10(ratio);
            //只要有一个线程，不断调用这个方法，就可以使波形变化
            //主要，这个方法必须在ui线程中调用
            voiceLineView.setVolume((int) (db));
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.person_dispatch);
        mikeOpen[0] = true;mikeOpen[1] = false;mikeOpen[2] = false;mikeOpen[2] = false;
        initView();
        initVoice();

    }

    private void initVoice() {
        //麦克风图标
        mike1 = (ImageView) findViewById(R.id.ImageOthersMike1);
        mike2 = (ImageView) findViewById(R.id.ImageOthersMike2);
        mike3 = (ImageView) findViewById(R.id.ImageOthersMike3);
        mike4 = (ImageView) findViewById(R.id.ImageOthersMike4);
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "hello.log");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mMediaRecorder.setOutputFile(file.getAbsolutePath());
        mMediaRecorder.setMaxDuration(1000 * 60 * 10);
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mMediaRecorder.start();
        }
        catch (Exception e) {

        }


        Thread thread = new Thread(this);
        thread.start();
    }

    private void initView() {
        voiceLineView = (VoiceLineView) findViewById(R.id.boxing);
        //底部
        bottom = (LinearLayout) findViewById(R.id.bottom);
        //对讲图标
        iconVoice = (ImageView) findViewById(R.id.icon_voice);
        layout_whospeak = (LinearLayout) findViewById(R.id.whospeaking);//谁正在说话
        layout_whospeak_name = (TextView) layout_whospeak.findViewById(R.id.name);//名称
        layout_whospeak_headportrait = (CircleImageView) findViewById(R.id.speacker_headportrait);//头像

        btn_speack = (TextView) findViewById(R.id.btn_speak);
        btn_speack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    //打开对讲
                    iconVoice.setBackground(getResources().getDrawable(R.drawable.tab_message_press));
                    bottom.setBackgroundColor(getResources().getColor(R.color.infosColor));
                    layout_whospeak_headportrait.setImageDrawable(getSpeakerHeadPortrait(0));
                    layout_whospeak_name.setText("我"+"正在说话...");//我正在说话
                    layout_whospeak.setVisibility(View.VISIBLE);
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    layout_whospeak.setVisibility(View.INVISIBLE);
                    iconVoice.setBackground(getResources().getDrawable(R.drawable.tab_message));
                    bottom.setBackgroundColor(getResources().getColor(R.color.white));
                }

                return true;
            }
        });
    }
    private Drawable getSpeakerHeadPortrait(int i) {
        //HomeFragment.get
        int h = Constants.headPortaits[1%5];
        return getResources().getDrawable(h);
//        if (i == 0) {
//            return getResources().getDrawable(R.drawable.timg0);
//        }else {
//            return getResources().getDrawable(R.drawable.timg);
//        }
    }
    public void back(View view) {
        finish();
    }

    public void setVoiceState(View view) {
        ImageView v = (ImageView) findViewById(R.id.voice);
        if(voiceable) {
            voiceable = voiceable?false:true;
            v.setBackground(getResources().getDrawable(R.drawable.novoice));
            initMike();
        } else {
            voiceable = voiceable?false:true;
            v.setBackground(getResources().getDrawable(R.drawable.voice));
        }
    }
    public void openMap (View view) {
        Intent intent=new Intent(PersonDispatchActivity.this,CarDispatchMapActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        isAlive = false;
        mMediaRecorder.release();
        mMediaRecorder = null;
        super.onDestroy();
    }
    @Override
    public void run() {
        while (isAlive) {
            handler.sendEmptyMessage(0);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void ChangeCallModel(View view) {
        TextView t = (TextView) findViewById(R.id.call_mode);
        if (CALL_MODE == CALL_MODE_MULT) {
            CALL_MODE = CALL_MODE_SIGNAL;
            t.setText("单呼");
        }else {
            CALL_MODE = CALL_MODE_MULT;
            initMike();
            t.setText("多呼");
        }

    }

    public void changeMikeState(View view) {
        if (CALL_MODE == CALL_MODE_SIGNAL) { //单呼模式切换，麦克风
            initMike();
        }
        switch (view.getId()) {
            case R.id.ImageOthersMike1:
                if (mikeOpen[0]) {
                    mikeOpen[0] =false;
                    mike1.setImageDrawable(getResources().getDrawable(R.mipmap.mic_off));
                } else {
                    mikeOpen[0] =true;
                    mike1.setImageDrawable(getResources().getDrawable(R.mipmap.mic_on));
                }
                break;
            case R.id.ImageOthersMike2:
                if (mikeOpen[1]) {
                    mikeOpen[1] =false;
                    mike2.setImageDrawable(getResources().getDrawable(R.mipmap.mic_off));
                }else {
                    mikeOpen[1] =true;
                    mike2.setImageDrawable(getResources().getDrawable(R.mipmap.mic_on));
                }
                break;
            case R.id.ImageOthersMike3:
                if (mikeOpen[2]) {
                    mikeOpen[2] =false;
                    mike3.setImageDrawable(getResources().getDrawable(R.mipmap.mic_off));
                }else {
                    mikeOpen[2] =true;
                    mike3.setImageDrawable(getResources().getDrawable(R.mipmap.mic_on));
                }
                break;
            case R.id.ImageOthersMike4:
                if (mikeOpen[3]) {
                    mikeOpen[3] =false;
                    mike4.setImageDrawable(getResources().getDrawable(R.mipmap.mic_off));
                }else {
                    mikeOpen[3] =true;
                    mike4.setImageDrawable(getResources().getDrawable(R.mipmap.mic_on));
                }
                break;
        }

    }

    private void initMike() {
        mikeOpen[0] =false;mikeOpen[3] =false;mikeOpen[2] =false;mikeOpen[1] =false;
        mike1.setImageDrawable(getResources().getDrawable(R.mipmap.mic_off));
        mike2.setImageDrawable(getResources().getDrawable(R.mipmap.mic_off));
        mike3.setImageDrawable(getResources().getDrawable(R.mipmap.mic_off));
        mike4.setImageDrawable(getResources().getDrawable(R.mipmap.mic_off));

    }


}
