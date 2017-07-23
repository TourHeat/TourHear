package com.example.tr.tourhear.tl_demo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algebra.sdk.API;
import com.example.tr.tourhear.R;
import com.example.tr.tourhear.entity.MsgCode;

import java.util.HashMap;

public class DialogLogin extends Dialog {
	private Context context;
	private int resource;
	private DialogLogin self;
	private Handler handler;
	private boolean isLoginPhone = false;
	private String myAccount = null;
	private String myPass = null;
	private HashMap<String, String> outputInfo = new HashMap<String, String>();

	public DialogLogin(Context context, int theme, int resource,
			Handler handler, String account, String passwd) {
		super(context, theme);
		this.context = context;
		this.resource = resource;
		this.self = this;
		this.handler = handler;
		this.myAccount = account;
		this.myPass = passwd;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_login);
		RelativeLayout linearLayout = ((RelativeLayout) findViewById(R.id.registerTop));
		linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		LayoutParams layoutParams = linearLayout.getLayoutParams();
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		if (dm.widthPixels > dm.heightPixels)
			layoutParams.width = (int) (dm.widthPixels * 1);// -
																// dm.densityDpi/1;
		else
			layoutParams.width = (int) (dm.widthPixels * 1);
		linearLayout.setLayoutParams(layoutParams);

		// saved info.
		final EditText etAccount = (EditText) findViewById(R.id.user_account);
		final EditText etPass = (EditText) findViewById(R.id.user_pass);
		if (myAccount != null && myPass != null) {
			etAccount.setHint(myAccount);
			etPass.setHint("******");
		}

		TextView tvLogin = (TextView) findViewById(R.id.login_type);
		tvLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isLoginPhone) {
					isLoginPhone = false;
					((TextView) v).setText(R.string.account);
					etAccount.setInputType(InputType.TYPE_CLASS_TEXT);
					if (myAccount != null && myPass != null) {
						etAccount.setHint(myAccount);
						etPass.setHint("******");
					}
				} else {
					isLoginPhone = true;
					((TextView) v).setText(R.string.phone_no);
					etAccount.setInputType(InputType.TYPE_CLASS_NUMBER);
					etAccount.setHint("");
					etPass.setHint("");
				}
			}
		});

		((Button) findViewById(R.id.loginButton))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String account = etAccount.getText().toString();
						String passwd = etPass.getText().toString();
						TextView errorInfo = (TextView) findViewById(R.id.loginErrInfo);

						// no input, login with saved info.
						if (!TextUtils.isEmpty(myAccount)
								&& !TextUtils.isEmpty(myPass)
								&& TextUtils.isEmpty(account)
								&& TextUtils.isEmpty(passwd)) {
							outputInfo.put(RegisterUser.KeyPassword, myPass);
							outputInfo.put(RegisterUser.KeyAccount, myAccount);
							handler.obtainMessage(MsgCode.ASKFORLOGIN, 1, 0,
									outputInfo).sendToTarget();
							self.dismiss();
							return;
						}

						if (!isLoginPhone) {
							if (account == null || account.length() < 5) {
								errorInfo.setText(context.getResources().getString(R.string.need_account));
								return;
							}
						} else {
							if (account == null || account.length() != 11) {
								errorInfo.setText(context.getResources().getString(R.string.error_phoneno));
							}
						}

						if (passwd == null || passwd.length() < 4) {
							errorInfo.setText(context.getResources().getString(R.string.need_passwd));
							return;
						}

						outputInfo.put(com.example.tr.tourhear.tl_demo.RegisterUser.KeyPassword,
								API.md5(passwd));
						outputInfo.put(com.example.tr.tourhear.tl_demo.RegisterUser.KeyAccount, account);
						handler.obtainMessage(MsgCode.ASKFORLOGIN, 1,
								isLoginPhone ? 1 : 0, outputInfo)
								.sendToTarget();
						self.dismiss();
					}
				});

		((TextView) (findViewById(R.id.go_register)))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						handler.obtainMessage(MsgCode.REGISTERED, null)
								.sendToTarget();
						self.dismiss();
					}
				});

		((TextView) (findViewById(R.id.go_reset_pw)))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String account = ((EditText) findViewById(R.id.user_account))
								.getText().toString();
						TextView errorInfo = (TextView) findViewById(R.id.loginErrInfo);
						if (TextUtils.isEmpty(account)
								|| account.length() != 11) {
							errorInfo.setText(context.getResources().getString(R.string.input_phoneno));
							isLoginPhone = true;
							((TextView) findViewById(R.id.login_type)).setText(context.getResources().getString(R.string.phoneno_hint));
							etAccount.setInputType(InputType.TYPE_CLASS_NUMBER);
							etAccount.setHint("");
							return;
						}

						handler.obtainMessage(MsgCode.RESETPASSWD, account)
								.sendToTarget();
						self.dismiss();
					}
				});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		handler.obtainMessage(MsgCode.ASKFOREXIT).sendToTarget();
	}
}
