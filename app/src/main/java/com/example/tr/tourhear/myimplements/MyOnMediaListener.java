package com.example.tr.tourhear.myimplements;


import android.util.Log;

import com.algebra.sdk.OnMediaListener;
import com.algebra.sdk.entity.HistoryRecord;

/**
 * Created by TR on 2017/7/23.
 */

public class MyOnMediaListener implements OnMediaListener {
    @Override
    public void onMediaInitializedEnd(int i, int i1, int i2) {
        Log.i("login","media onMediaInitializedEnd uid"+i+"sid:"+i2+"ct:"+i1);
    }
    //获取新的对讲记录
    @Override
    public void onNewSpeakingCatched(HistoryRecord historyRecord) {
        Log.i("login","media onNewSpeakingCatched: ok"+historyRecord.owner);

    }
    @Override
    public void onPlayStopped(int i) {
      //  super.onPlayStopped(i);
        Log.i("login","media onPlayStopped: ok"+i);
    }
    @Override
    public void onSomeoneSpeaking(int i, int i1, int i2, int i3, int i4) {
        Log.i("login","media onSomeoneSpeaking: ok"+i);
    }

    @Override
    public void onThatoneSayOver(int i, int i1) {
        Log.i("login","media onThatoneSayOver: ok"+i);
    }

    @Override
    public void onPlayLastSpeakingEnd(int i) {
        Log.i("login","media onPlayLastSpeakingEnd: ok"+i);
    }



    @Override
    public void onPttButtonPressed(int i, int i1) {

    }

    @Override
    public void onTalkRequestConfirm(int i, int i1, int i2, int i3, boolean b) {

    }

    @Override
    public void onTalkRequestDeny(int i, int i1, int i2) {

    }

    @Override
    public void onTalkRequestQueued(int i, int i1, int i2) {

    }

    @Override
    public void onTalkReleaseConfirm(int i, int i1) {

    }

    @Override
    public void onTalkTransmitBroken(int i, int i1) {

    }

    @Override
    public void onStartPlaying(int i, int i1, int i2, int i3) {

    }

    @Override
    public void onSomeoneAttempt(int i, int i1, int i2) {

    }

    @Override
    public void onThatAttemptQuit(int i, int i1, int i2) {

    }



    @Override
    public void onPlayLastSpeaking(int i, int i1) {

    }



    @Override
    public void onMediaSenderCutted(int i, int i1) {

    }

    @Override
    public void onMediaSenderReport(long l, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onMediaReceiverReport(long l, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onRecorderMeter(int i, int i1) {

    }

    @Override
    public void onPlayerMeter(int i, int i1) {

    }

    @Override
    public void onBluetoothBatteryGet(int i) {

    }

    @Override
    public void onBluetoothConnect(int i) {

    }
}
