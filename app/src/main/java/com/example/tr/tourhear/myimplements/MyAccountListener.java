package com.example.tr.tourhear.myimplements;

import com.algebra.sdk.OnAccountListener;
import com.algebra.sdk.entity.Contact;
import com.algebra.sdk.entity.UserProfile;

import java.util.List;

/**
 * Created by TR on 2017/7/19.
 */

public class MyAccountListener implements OnAccountListener {

    @Override
    public void onLogin(int uid, int result, UserProfile uProfile) {

    }

    @Override
    public void onCreateUser(int i, int i1, String s) {

    }

    @Override
    public void onLogout() {

    }

    @Override
    public void onSetNickName(int i) {

    }

    @Override
    public void onChangePassWord(int i, boolean b) {

    }

    @Override
    public void onAskUnbind(int i) {

    }

    @Override
    public void onAuthRequestReply(int i, int i1, String s) {

    }

    @Override
    public void onAuthBindingReply(int i, int i1, String s) {

    }

    @Override
    public void onAuthRequestPassReply(int i, int i1, String s) {

    }

    @Override
    public void onAuthResetPassReply(int i, int i1) {

    }

    @Override
    public void onFriendsSectionGet(int i, int i1, int i2, int i3, List<Contact> list) {

    }

    @Override
    public void onFriendStatusUpdate(int i, int i1, int i2) {

    }

    @Override
    public void onShakeScreenAck(int i, int i1, int i2) {

    }

    @Override
    public void onShakeScreenReceived(int i, String s, String s1) {

    }

    @Override
    public void onSetStatusReturn(int i, boolean b) {

    }

    @Override
    public void onHearbeatLost(int i, int i1) {

    }

    @Override
    public void onKickedOut(int i, int i1) {

    }

    @Override
    public void onSelfStateChange(int i, int i1) {

    }

    @Override
    public void onSelfLocationAvailable(int i, Double aDouble, Double aDouble1, Double aDouble2) {

    }

    @Override
    public void onSelfLocationReported(int i) {

    }

    @Override
    public void onUserLocationNotify(int i, String s, Double aDouble, Double aDouble1) {

    }

    @Override
    public void onLogger(int i, String s) {

    }

    @Override
    public void onSmsRequestReply(int i) {

    }
}
