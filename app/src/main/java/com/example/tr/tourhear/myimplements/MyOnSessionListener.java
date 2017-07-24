package com.example.tr.tourhear.myimplements;


import com.algebra.sdk.OnSessionListener;
import com.algebra.sdk.entity.Contact;

import java.util.List;

/**
 * Created by TR on 2017/7/23.
 */

public class MyOnSessionListener implements OnSessionListener {
    @Override
    public void onSessionEstablished(int selfUserId, int type, int sessionId) {

    }

    @Override
    public void onSessionReleased(int i, int i1, int i2) {

    }

    @Override
    public void onSessionGet(int selfUserId, int type, int sessionId, int initiator) {

    }

    @Override
    public void onSessionPresenceAdded(int i, int i1, List<Contact> list) {

    }

    @Override
    public void onSessionPresenceRemoved(int i, int i1, List<Integer> list) {

    }

    @Override
    public void onDialogEstablished(int i, int i1, int i2, List<Integer> list) {

    }

    @Override
    public void onDialogLeaved(int i, int i1) {

    }

    @Override
    public void onDialogPresenceAdded(int i, int i1, List<Integer> list) {

    }

    @Override
    public void onDialogPresenceRemoved(int i, int i1, List<Integer> list) {

    }
}
