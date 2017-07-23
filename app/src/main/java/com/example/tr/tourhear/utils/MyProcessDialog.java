package com.example.tr.tourhear.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import static java.lang.Thread.sleep;

/**
 * Created by TR on 2017/7/19.
 */

public class MyProcessDialog extends ProgressDialog {
    private ProgressDialog progressDialog;
    public MyProcessDialog(Context context) {
        super(context);
    }

    public void dismissProcessing() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
    public void showProcessing(String hintText) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
        }
        progressDialog.setMessage(hintText);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void showProcessing(final String hintText, final int dur) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
        }
        progressDialog.setMessage(hintText);
        progressDialog.setCancelable(true);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(dur);
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), hintText+"  成功", Toast.LENGTH_SHORT).show();
                }catch (Exception e){

                }
            }
        }).start();
    }

}
