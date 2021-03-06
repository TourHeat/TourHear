package com.example.tr.tourhear;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.algebra.sdk.API;
import com.algebra.sdk.AccountApi;
import com.algebra.sdk.ChannelApi;
import com.algebra.sdk.DeviceApi;
import com.algebra.sdk.entity.Channel;
import com.algebra.sdk.entity.CompactID;
import com.algebra.sdk.entity.Constant;
import com.algebra.sdk.entity.Contact;
import com.algebra.sdk.entity.UserProfile;
import com.example.tr.tourhear.entity.MsgCode;
import com.example.tr.tourhear.myimplements.MyAccountListener;
import com.example.tr.tourhear.myimplements.MyOnChannelListener;
import com.example.tr.tourhear.utils.MyProcessDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.tr.tourhear.utils.Constants.ISDEBUG;

public class Login extends AppCompatActivity {
    private static Handler uiHandler =null;
    private Context uiConntext;

    private boolean needUnbind = true;
    private int selfId = 0;
    private int selfState = Constant.CONTACT_STATE_OFFLINE;
    private String userAccount = null;
    private String userPass = null;
    private String userNick = "???";
    private String userPhone = null;
    private boolean userBoundPhone = false;
    private boolean newBind = true;
    private boolean isVisitor = true;
    private AccountApi accountApi = null;
    private DeviceApi deviceApi = null;
    private ProgressDialog processDialog = null;
    private interface StartStage {
        public static final int INITIALIZING = 0;
        public static final int LOGIN_VISITOR = 2;
        public static final int REGISTER_USER = 3;
        public static final int LOGIN_USER = 4;
        public static final int RESET_PASS = 5;
    }
    private HashMap<String, String> outputInfo = new HashMap<String, String>();
    private EditText account;
    private EditText userPassWord;
    private static int startStep = StartStage.INITIALIZING;
    private Button btn_test;
    private MyProcessDialog myProcessDialog;
    //private
    private boolean isDebug = true;//调试状态
    //获取频道
    private static List<Channel> channelList = new ArrayList<Channel>();
    private ChannelApi channelApi;
    private Channel channel;
    public static Handler getUiHandler(){
        return uiHandler;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        account = (EditText) findViewById(R.id.user_account);//输入登录账号
        userPassWord = (EditText) findViewById(R.id.user_pass);//输入密码
        uiConntext = this;
        startStep = StartStage.INITIALIZING;//当前状态为初始化
        if (!ISDEBUG) {
            newBind = API.init(this);//初始化对讲服务
            uiConntext = this;
            uiHandler = new UIHandler(this);
        }

//        if (startStep == StartStage.INITIALIZING)
//            uiHandler.postDelayed(delayInitApi, 300);
        RegisterUser registerUser = new RegisterUser(Login.this,uiHandler);
        if(registerUser.getUserAccount()!=null && registerUser.getUserAccount()!=""){
            account.setHint(registerUser.getUserAccount());
        }
        if(registerUser.getUserPassword()!=null && registerUser.getUserPassword()!=""){
            userPassWord.setHint(registerUser.getUserPassword());
        }
        if(isDebug){
            account.setText(registerUser.getUserAccount());
            userPassWord.setText(registerUser.getUserPassword());
        }

        //获取保存用户和密码

//        btn_test = (Button) findViewById(R.id.test);
//        btn_test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                uiHandler.obtainMessage(10, 1,
//                        false ? 1 : 0, outputInfo)
//                        .sendToTarget();
//                Contact me = accountApi.whoAmI();
//                Log.i("login","user"+me.name+me.id);
//            }
//        });
        myProcessDialog = new MyProcessDialog(Login.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("login","onresume----");
        if (!ISDEBUG) {
            if (startStep == StartStage.INITIALIZING)
                uiHandler.postDelayed(delayInitApi, 300);
        }
    }

    private static class UIHandler extends  Handler{
        WeakReference<Login> wrActi;//UIHandler所在的活动
        Login mActi = null;

        public UIHandler(Login act){
            wrActi = new WeakReference<Login>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //获取对象
            mActi = wrActi.get();
            if(mActi == null){
                return;
            }
            //消息处理
            switch (msg.what) {
                case MsgCode.ASKFORLOGIN: //请求登录
                    // 判断是否登录
                    Contact me = mActi.accountApi.whoAmI();
                    if(me != null){//若登录了，则退出登录
                        mActi.accountApi.logout(mActi.selfId);
                    }
                    mActi.isVisitor = (msg.arg1 == 2);
                    mActi.userAccount = ((HashMap<String, String>) (msg.obj))
                            .get(RegisterUser.KeyAccount);//获取用户账号
                    mActi.userPass = ((HashMap<String, String>) (msg.obj))
                            .get(RegisterUser.KeyPassword);//获取用户密码
                    //判断是否为游客登录
                    if (mActi.isVisitor) {
                        mActi.accountApi.login(mActi.userAccount, mActi.userPass);
                    } else {//非游客
                        if (msg.arg2 == 1)//电话登录
                            mActi.accountApi.loginPhone(mActi.userAccount, mActi.userPass);
                        else//图灵号登录
                            mActi.accountApi.login(mActi.userAccount, mActi.userPass);
                    }
                    mActi.myProcessDialog.showProcessing("Loging...");
                    break;
                case MsgCode.MC_LOGINFINISHED:
                    //登录成功关闭对话框
                    mActi.myProcessDialog.dismissProcessing();

                    if(msg.arg2 == 0) {
                        if(msg.arg2 == 0){
                            RegisterUser registerUser = new RegisterUser(wrActi.get(),uiHandler);
                            registerUser.saveUserInfo(mActi.userAccount, mActi.userNick, mActi.userPass);
                            Intent intent = new Intent(wrActi.get(),MainActivity.class);
                            wrActi.get().startActivity(intent);
                         //   wrActi.get().finish();
                        }
                    }
                    break;
                case MsgCode.MC_LOGINOK:



                    break;
                case MsgCode.ASKFOREXIT: //退出登录
               //     mActi.setContentView(R.layout.welcome);
                    if (mActi.newBind) {
                        mActi.newBind = false;
                        mActi.needUnbind = true;
                        mActi.finish();
                    } else {
                        mActi.needUnbind = false;
                        mActi.finish();
                    }
                    break;
                default:
                    //  Log.e(TAG, "uiHandler unexpected msg: " + msg.what);
                    break;
            }
        }
    }
    private Runnable delayInitApi = new Runnable() {
        @Override
        public void run() {
            Log.i("login","delayInitApi----"+1);
            accountApi = API.getAccountApi();//获取对讲操作
            deviceApi = API.getDeviceApi();
            if(accountApi!=null && deviceApi !=null){
                Log.i("login","delayInitApi----"+2);

                accountApi.setOnAccountListener(new MyAccountListener(){
                    @Override
                    public void onLogin(int uid, int result, UserProfile uProfile) {
                        super.onLogin(uid, result, uProfile);
                        uiHandler.obtainMessage(MsgCode.MC_LOGINFINISHED, 2, result).sendToTarget();//登录成功，关闭对话框
                        Log.i("login","result: "+result);
                        //登录成功状态
                        if(result == Constant.ACCOUNT_RESULT_OK
                                || result == Constant.ACCOUNT_RESULT_ALREADY_LOGIN) {
                            //获取当前用户属性
                            Log.i("login","delayInitApi----"+3);
                            if(uProfile != null) {
                                userBoundPhone = !uProfile.userPhone.equals("none");
                                userAccount = new String(uProfile.userName);
                                userNick = new String(uProfile.userNick);
                                isVisitor = (uProfile.userType == Constant.USER_TYPE_VISITOR);
                            }
                            //成功登录，状态在线
                            uiHandler.obtainMessage(MsgCode.MC_LOGINOK, uid,
                                    Constant.CONTACT_STATE_ONLINE,result).sendToTarget();
                        }
                        if (result == Constant.ACCOUNT_RESULT_ERR_USER_NOTEXIST) {
                            Toast.makeText(uiConntext, "账号不存在", Toast.LENGTH_SHORT).show();
                        }
                        if (result == Constant.ACCOUNT_RESULT_ERR_USER_PWD) {
                            Toast.makeText(uiConntext, "密码错误", Toast.LENGTH_SHORT).show();
                        }
                        if (result == Constant.ACCOUNT_RESULT_ERR_SERVER_UNAVAILABLE) {
                            Toast.makeText(uiConntext, "没有打开对讲服务", Toast.LENGTH_SHORT).show();
                        }
                        if (result == Constant.ACCOUNT_RESULT_ERR_NETWORK) {
                            Toast.makeText(uiConntext, "无网络", Toast.LENGTH_SHORT).show();
                        }
                        if (result == Constant.ACCOUNT_RESULT_OTHER_LOGIN) {
                            Toast.makeText(uiConntext, "用户错误", Toast.LENGTH_SHORT).show();
                        }
                        Log.i("login","delayInitApi----"+4);
                    }

                });
                //setupToneGen();
                //获取当前用户
                Contact me = accountApi.whoAmI();
                if(me != null) {
                    userBoundPhone = !me.phone.equals("none");//用户电话
                    isVisitor = me.visitor;
                    userNick = new String(me.name);
                    // userAccount = registerUser.getUserAccount();
                    Log.d("main", "Poc sdk for uid: " + me.id
                            + " is running, self state:" + me.state
                            + ", link in.");
                    uiHandler.obtainMessage(MsgCode.MC_SDKISRUNNING, me.id,
                            me.state).sendToTarget();
//                    Intent intent = new Intent(Login.this,MainActivity.class);
//                    startActivity(intent);
                   accountApi.logout(selfId);
                } else {
                    uiHandler.sendEmptyMessage(MsgCode.ASKFORSTARTSDK);
                }
            }
            else { //当账号没登录成功时
                Log.i("login","delayInitApi----"+5);
                if(uiHandler != null) {
                    Log.d("main",
                            "start SDK and waiting another 300ms.");
                    uiHandler.postDelayed(delayInitApi, 300);//重新初始化
                }
            }
        }
    };

    //点击登录按钮处理事件
    public void subMit(View view){
        String straccount = account.getText().toString();//账号
        String passwd = userPassWord.getText().toString();//密码
        if(straccount == "" || straccount == null){
            straccount = account.getHint().toString();
        }
        if(passwd == "" || passwd == null){
            passwd = userPassWord.getHint().toString();
        }

        Log.i("login","nowuid:"+straccount);
        outputInfo.clear();
        Log.i("login","nowuid:"+straccount);
        //添加信息对象
        if(passwd.length()>10){
            outputInfo.put(RegisterUser.KeyPassword,
                    passwd);
        } else {
            outputInfo.put(RegisterUser.KeyPassword,
                    API.md5(passwd));
        }

        outputInfo.put(RegisterUser.KeyAccount, straccount);
        //发送信息
        if (!ISDEBUG) {
            uiHandler.obtainMessage(MsgCode.ASKFORLOGIN, 1,
                    0, outputInfo)
                    .sendToTarget();
        } else {
            Intent i = new Intent(Login.this,MainActivity.class);
            startActivity(i);
            finish();
        }

    }
    //退出
    public void quit(View view){
        Log.i("login","ASKFORLOGIN selfid"+selfId);
        if (selfId > 0) {
            Log.i("login","selfId:"+selfId);
            accountApi.logout(selfId);
            selfId = 0;
        }
        if (uiHandler != null) {
            uiHandler.sendEmptyMessage(MsgCode.ASKFOREXIT);
        } else {
            Login.this.finish();
        }
    }
    //
    public void contact(View view){
        AccountApi me = API.getAccountApi();
        ChannelApi channel = API.getChannelApi();
        channel.channelListGet(me.whoAmI().id);

        channel.setOnChannelListener(new MyOnChannelListener() {
            @Override
            public void onChannelListGet(int i, Channel channel, List<Channel> list) {
                super.onChannelListGet(i, channel, list);

            }
        });
        Log.i("login","chanel inti"+(me.whoAmI().id));
        //channel.channelListGet(0);
        //channel.
    }
//注册
    public void register(View view) {
        Intent i = new Intent(Login.this,Register.class);
        startActivity(i);
    }

    private void initChannes() {
        AccountApi me = API.getAccountApi();
        channelApi = API.getChannelApi();
        channelApi.channelListGet(me.whoAmI().id);

        channelApi.setOnChannelListener(new MyOnChannelListener() {
            @Override
            public void onChannelListGet(int i, Channel channel, List<Channel> list) {
                super.onChannelListGet(i, channel, list);
                channelList = list;
                for(Channel channel2 : list){
                    Log.i("login","------------"+channel2.toString());
                    Log.i("login",channel2.name+channel2.cid);
                    CompactID cid =  channel2.cid;
                    Log.i("login","id"+cid.getId()+" type:"+cid.getType());
                }
            }
        });
        Log.i("login","chanel inti"+(me.whoAmI().id));
    }
    public static List<Channel> getChannelList(){
        return channelList;
    }

}
