package com.example.tr.tourhear;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;

public class RegisterUser {
	public static final String TAG = "reg.io";
	public static final String KeyAccount = "flag_account_string";
	public static final String KeyPassword = "flag_password_string";
	public static final String KeyNick = "flag_nick_string";
	public static final String KeyInviteCode = "flag_invite_string";
	public static final String KeyHasRegistered = "flag_has_registered_bool";
	public static final String KeyIsVisitor = "flag_visitor_bool";
	public static final String KeyAuthCode = "flag_auth_code";

	private Context mContext = null;
	private int mResource = 0;
	private MySharedPreferences myIO;
	private Handler handler;

	public RegisterUser(Context context, Handler handler) {
		this.mContext = context;
		this.handler = handler;

		myIO = new MySharedPreferences(context);
	}

	public String getUserAccount() {
		return myIO.Read(KeyAccount);
	}
	public String getUserPassword() {
		return myIO.Read(KeyPassword);
	}

	public void saveUserInfo(String name, String nick, String pass) {
		myIO.Write(KeyAccount, name);
		myIO.Write(KeyNick, nick);
		myIO.Write(KeyPassword, pass);
	}

	public void setNickName(String nick) {
		myIO.Write(KeyNick, nick);
	}

	public void removeUserInfo() {
		myIO.Remove(KeyHasRegistered);
		myIO.Remove(KeyAccount);
		myIO.Remove(KeyPassword);
	}

	public void setVisitor(boolean yn) {
		myIO.setBoolean(KeyIsVisitor, yn);
		return;
	}

	public void setUserRegistered() {
		myIO.setBoolean(KeyHasRegistered, true);
		return;
	}

	private class MySharedPreferences {
		public static final String TourLinkRegisterInfo = "register.info.tourlink";

		private Context mContext = null;

		public MySharedPreferences(Context c) {
			mContext = c;
		}

		public boolean getBoolean(String key) {
			SharedPreferences sharedPreferences = mContext
					.getSharedPreferences(TourLinkRegisterInfo,
							Context.MODE_PRIVATE);
			boolean rBool = sharedPreferences.getBoolean(key, false);

			return rBool;
		}

		public void setBoolean(String key, boolean tf) {
			SharedPreferences sharedPreferences = mContext
					.getSharedPreferences(TourLinkRegisterInfo,
							Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			editor.putBoolean(key, tf);
			editor.commit();
		}

		public String Read(String key) {
			SharedPreferences sharedPreferences = mContext
					.getSharedPreferences(TourLinkRegisterInfo,
							Context.MODE_PRIVATE);
			String rStr = sharedPreferences.getString(key, null);
			return rStr;
		}

		public void Write(String key, String value) {
			SharedPreferences sharedPreferences = mContext
					.getSharedPreferences(TourLinkRegisterInfo,
							Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			editor.putString(key, value);
			editor.commit();
		}

		public void Remove(String key) {
			SharedPreferences sharedPreferences = mContext
					.getSharedPreferences(TourLinkRegisterInfo,
							Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			editor.remove(key);
			editor.commit();
		}
	}
}
