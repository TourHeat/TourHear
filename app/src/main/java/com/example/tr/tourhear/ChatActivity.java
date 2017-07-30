package com.example.tr.tourhear;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.algebra.sdk.API;
import com.algebra.sdk.DeviceApi;
import com.algebra.sdk.SessionApi;
import com.algebra.sdk.entity.Channel;
import com.algebra.sdk.entity.CompactID;
import com.algebra.sdk.entity.Contact;
import com.algebra.sdk.entity.OEMToneGenerator;
import com.algebra.sdk.entity.OEMToneProgressListener;
import com.algebra.sdk.entity.Session;
import com.example.tr.tourhear.entity.ChaneelMems;
import com.example.tr.tourhear.myimplements.MyOnMediaListener;
import com.example.tr.tourhear.myimplements.MyOnSessionListener;
import com.example.tr.tourhear.utils.ChatMsgEntity;
import com.example.tr.tourhear.utils.ChatMsgViewAdapter;
import com.example.tr.tourhear.utils.Constants;
import com.example.tr.tourhear.utils.MyLocation;
import com.example.tr.tourhear.view.CircleImageView;
import com.lilei.springactionmenu.ActionMenu;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.androidbucket.utils.imageprocess.ABShape;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;
import com.wangjie.rapidfloatingactionbutton.listener.OnRapidFloatingButtonSeparateListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.tr.tourhear.utils.Constants.ISDEBUG;

/**
 * Created by ZhangYan on 2017/7/16.
 */
public class ChatActivity extends Activity implements OnClickListener, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener, OnRapidFloatingButtonSeparateListener {

    private Button mBtnSend;// 发送btn

    private EditText mEditTextContent;
    private ListView mListView;
    private ChatMsgViewAdapter mAdapter;// 消息视图的Adapter
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();// 消息对象数组
    private boolean voiceable = true;//设置能否发声
    private ImageView iconVoice;
    private TextView btnSpeak; //按住发声
    private LinearLayout bottom;
    private SessionApi sessionapi;//会话操作
    private CompactID currSession = null;//会话
    private Session session;
//操作频道
    private List<Channel> cs = new ArrayList<Channel>();
    private  Channel channel = null;
    private DeviceApi deviceApi;//设备操作类
    private com.example.tr.tourhear.tl_demo.TalkHistory talkHistory = null;//历史
//媒体操作类
    private AudioManager mAudioManager = null;
    private int CHAT_TYPE = 0;//0,个人聊天;1,
    private ChaneelMems cm;//聊天成员
    //谁正在说话
    private LinearLayout layout_whospeak;
    private TextView layout_whospeak_name;
    private CircleImageView layout_whospeak_headportrait;
    private Handler uiHandler = null;
    private Date speakTime;//对讲时间
    private boolean someOneisSpeak = false;
    private ActionMenu actionMenu;
    private LinearLayout sendOthers;
    //
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaButton;
    private RapidFloatingActionHelper rfabHelper;
    private MyLocation myLocation;//位置信息
    private Bundle bundle = null;
    private boolean startSessionOK = false;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bundle = savedInstanceState;
        if(!ISDEBUG){
            uiHandler = Login.getUiHandler();
        }

        myLocation = new MyLocation(ChatActivity.this);//实例化位置
        initView();// 初始化view
        rfaLayout = (RapidFloatingActionLayout) findViewById(R.id.label_list_sample_rfal);
        rfaButton = (RapidFloatingActionButton) findViewById(R.id.add_menu);
        initMenu();
       // initData();// 初始化数据
     //   mListView.setSelection(mAdapter.getCount() - 1);

    }

    private void initMenu() {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(ChatActivity.this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(ChatActivity.this);


        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("图片")
                .setResId(R.mipmap.menu_photo)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("拍摄")
                //                        .setResId(R.mipmap.ico_test_c)
                .setDrawable(getResources().getDrawable(R.mipmap.menu_carmer))
                .setIconNormalColor(0xff4e342e)
                .setIconPressedColor(0xff3e2723)
                .setLabelColor(Color.WHITE)
                .setLabelSizeSp(14)
                .setLabelBackgroundDrawable(ABShape.generateCornerShapeDrawable(0xaa000000, ABTextUtil.dip2px(ChatActivity.this, 4)))
                .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("位置")
                .setResId(R.mipmap.menu_location)
                .setIconNormalColor(0xff056f00)
                .setIconPressedColor(0xff0d5302)
                .setLabelColor(0xff056f00)
                .setWrapper(2)
        );

        rfaContent
                .setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(ChatActivity.this, 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(ABTextUtil.dip2px(ChatActivity.this, 5))
        ;

        rfabHelper = new RapidFloatingActionHelper(
                this,
                rfaLayout,
                rfaButton,
                rfaContent
        ).build();
        rfaButton.setOnRapidFloatingButtonSeparateListener(this);
    }

    /**
     * 初始化view
     */
    public void initView() {
        if (!ISDEBUG) {
            mAudioManager = ((AudioManager) getSystemService(Context.AUDIO_SERVICE));//获取媒体
            talkHistory = new com.example.tr.tourhear.tl_demo.TalkHistory(API.getAccountApi().whoAmI().id);
        }

        speakTime = new Date(System.currentTimeMillis());
        mListView = (ListView) findViewById(R.id.listview);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);

        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
        sendOthers = (LinearLayout) findViewById(R.id.send_others);
        actionMenu = (ActionMenu) findViewById(R.id.expanded_menu);
       // actionMenu.me
        actionMenu.addView(R.mipmap.menu_carmer, getItemColor(R.color.menuNormalInfo), getItemColor(R.color.menuPressInfo));
        actionMenu.addView(R.mipmap.menu_photo, getItemColor(R.color.menuNormalRed), getItemColor(R.color.menuPressRed));
        actionMenu.addView(R.mipmap.menu_location);

        //底部
        bottom = (LinearLayout) findViewById(R.id.bottom);
        //对讲图标
        iconVoice = (ImageView) findViewById(R.id.icon_voice);
        layout_whospeak = (LinearLayout) findViewById(R.id.whospeaking);//谁正在说话
        layout_whospeak_name = (TextView) layout_whospeak.findViewById(R.id.name);//名称
        layout_whospeak_headportrait = (CircleImageView) findViewById(R.id.speacker_headportrait);//头像

        btnSpeak = (TextView) findViewById(R.id.btn_speak);
        //  btnSpeak.setOnFocusChangeListener(this);
        btnSpeak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    //打开对讲
                    iconVoice.setBackground(getResources().getDrawable(R.drawable.tab_message_press));
                    bottom.setBackgroundColor(getResources().getColor(R.color.infosColor));
                    layout_whospeak_headportrait.setImageDrawable(getSpeakerHeadPortrait(API.getAccountApi().whoAmI().id));
                    layout_whospeak_name.setText("我"+"正在说话...");//我正在说话
                    layout_whospeak.setVisibility(View.VISIBLE);
                    speakTime.setTime(System.currentTimeMillis());
                    if ( currSession!= null && sessionapi != null){
                        sessionapi.talkRequest(API.getAccountApi().whoAmI().id,currSession.getType(),currSession.getId());
                        talkRequest(currSession);
                        Log.i("login","alkRequest:"+"uid: "+API.getAccountApi().whoAmI().id+"type"+currSession.getType()+" id :"+ currSession.getId());
                    }
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    long dur = 0;
                    dur = System.currentTimeMillis() - speakTime.getTime();
//新增发言
                    ChatMsgEntity entity = new ChatMsgEntity();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss");//时间
                    speakTime.setTime(System.currentTimeMillis());
                    entity.setDate(format.format(speakTime).toString());
                    entity.setMessage(dur/1000+"''");
                    entity.setMsgType(false);
                    entity.setMsgType(0);
                    if(!ISDEBUG) {
                        entity.setName(API.getAccountApi().whoAmI().name);
                    } else {
                        entity.setName("我");
                    }

                    sendMsg(entity);
//                    mDataArrays.add(entity);
//                    mAdapter = new ChatMsgViewAdapter(getBaseContext(), mDataArrays);
//                    mListView.setAdapter(mAdapter);
//                    mListView.setSelection(mAdapter.getCount() - 1);
                    layout_whospeak.setVisibility(View.GONE);
                    iconVoice.setBackground(getResources().getDrawable(R.drawable.tab_message));
                    bottom.setBackgroundColor(getResources().getColor(R.color.white));
                    if ( currSession!= null && sessionapi != null){
                        sessionapi.talkRequest(API.getAccountApi().whoAmI().id,currSession.getType(),currSession.getId());
                        talkRelease(currSession);
                        Log.i("login","alkRequest:"+"uid: "+API.getAccountApi().whoAmI().id+"type"+currSession.getType()+" id :"+ currSession.getId());
                    }
                }

                return true;
            }
        });
        if (!ISDEBUG) {
            //频道名称
            cs = HomeFragment.getChannelList();
        }

        //获取频道名称
        Intent intent = getIntent();
        String strChannelName = intent.getStringExtra("channelName");
        CHAT_TYPE = intent.getIntExtra("CHATTYPE", 0);//聊天形式
        LinearLayout btn_tomap = (LinearLayout) findViewById(R.id.btn_tomap);
        if (CHAT_TYPE == Constants.CHAT_TYPE_CHEDUI) {//动态添加车队地图控件与listener
            btn_tomap.setVisibility(View.VISIBLE);
            btn_tomap.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ChatActivity.this,CarGroupMapActivity.class);
                    startActivity(intent);
                }
            });
        }

        TextView cn =  (TextView) findViewById(R.id.channel_name);
        if(strChannelName != null && strChannelName !=""){//设置频道名称
            cn.setText(strChannelName);
        }
        if (!ISDEBUG) {
            for(Channel c : cs){
                Log.i("login","获取频道------------"+c.name);
                if(c.name.equals(strChannelName)){
                    channel  = c;
                    initSession();
                    Log.i("login","频道OK------------"+channel.name);
                }
            }
        }


        //初始化设备

    }
    //获取颜色
    private int getItemColor(int colorID) {
        return getResources().getColor(colorID);
    }
//获取说话者头像
    private Drawable getSpeakerHeadPortrait(int i) {
        //HomeFragment.get
        int h = Constants.headPortaits[Integer.parseInt(getMemberName(i))%5];
        return getResources().getDrawable(h);
//        if (i == 0) {
//            return getResources().getDrawable(R.drawable.timg0);
//        }else {
//            return getResources().getDrawable(R.drawable.timg);
//        }
    }

    private void initSession() {
        deviceApi = API.getDeviceApi();//获取媒体操作
      //  deviceApi.setOemToneGenerator();
        setupToneGen();
        sessionapi  = API.getSessionApi();
        if(sessionapi == null){
            uiHandler.postDelayed(delayInitApi, 300);
        }else {
            sessionapi.setOnMediaListener(new MyOnMediaListener(){
                //媒体回调
                //获取新的对讲记录
                @Override
                public void onSomeoneSpeaking(int i, int i1, int i2, int i3, int i4) {
                    super.onSomeoneSpeaking(i, i1, i2, i3, i4);
                    setSpeack(i);
                    if (!someOneisSpeak) {
                        someOneisSpeak = true;
                        speakTime.setTime(System.currentTimeMillis());
                    }
                    //speakTime.setTime(System.currentTimeMillis());
                }

                @Override
                public void onThatoneSayOver(int i, int i1) {
                    super.onThatoneSayOver(i, i1);
                    someOneisSpeak = false;
                    long dur = 0;
                    dur = System.currentTimeMillis() - speakTime.getTime();
//新增发言
                    ChatMsgEntity entity = new ChatMsgEntity();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss");//时间
                    speakTime.setTime(System.currentTimeMillis());
                    entity.setDate(format.format(speakTime).toString());
                    entity.setMessage(dur/1000+"''");
                    entity.setMsgType(true);
                    entity.setMsgType(0);
                    entity.setName(getMemberName(i));
                    sendMsg(entity);
                    closeSpeack();
                }
            });
        }
        sessionapi.sessionCall(API.getAccountApi().whoAmI().id,channel.cid.getType(),channel.cid.getId());
        Log.i("login","创建会话:"+API.getAccountApi().whoAmI().id+" ::  "+channel.cid.getType()+" ::  "+channel.cid.getId());
        sessionapi.setOnSessionListener(new MyOnSessionListener(){
            @Override
            public void onSessionGet(int selfUserId, int type, int sessionId, int initiator) {
                super.onSessionGet(selfUserId, type, sessionId, initiator);
                Log.i("login","sessionOK----- sessionId"+sessionId);
                Log.i("login","sessionOK----- selfUserId"+selfUserId);
                Log.i("login","sessionOK----- initiator"+initiator);
            }

            @Override
            public void onSessionReleased(int i, int i1, int i2) {
                super.onSessionReleased(i, i1, i2);
            }

            @Override
            public void onSessionEstablished(int selfUserId, int type, int sessionId) {
                super.onSessionEstablished(selfUserId, type, sessionId);
                Log.i("login","onSessionEstablished----- sessionId"+sessionId);
                currSession = new CompactID(type,sessionId);
              //  sessionapi.talkRequest(API.getAccountApi().whoAmI().id,currSession.getType(),currSession.getId());
                //获取频道成员
                 cm = HomeFragment.getCmem(channel.cid.getId());
                int [] ids = new int[cm.getCs().size()];
                int i = 0;
                for(Contact c: cm.getCs()){
                    ids[i++] = c.id;
                }
                sessionapi.startDialog(API.getAccountApi().whoAmI().id,channel.cid.getType(),channel.cid.getId(),ids);
            }

            @Override
            public void onDialogEstablished(int i, int i1, int i2, List<Integer> list) {
                Log.i("login","onDialogEstablished----- sessionId"+i1);
            }
        });

       // sessionapi.startDialog(API.getAccountApi().whoAmI().id,channel.cid.getType(),channel.cid.getId());
    }
    //发送信息
    public void sendMsg ( ChatMsgEntity entity) {
        mDataArrays.add(entity);
        mAdapter = new ChatMsgViewAdapter(getBaseContext(), mDataArrays,bundle);
        mListView.setAdapter(mAdapter);
        mListView.setSelection(mAdapter.getCount() - 1);
    }
    //接受信息

    private void setSpeack(int owner) {
        layout_whospeak_headportrait.setImageDrawable(getSpeakerHeadPortrait(owner));
        String speakerName = getMemberName(owner);
        layout_whospeak_name.setText(speakerName+"正在说话...");//用户正在说话
        layout_whospeak.setVisibility(View.VISIBLE);
    }
    private void closeSpeack(){
        layout_whospeak.setVisibility(View.GONE);
    }

    //获取频道成员名称,owner是uid
    private String getMemberName(int owner) {
        for(Contact c : cm.getCs()){
            if(c.id == owner){
                return  c.name;
            }
        }
        return "";
    }

    private String[] msgArray = new String[]{"13''", "12''", "17''", "55''",
            "25''", "38''", "75''", "85''",
            "14''", "17''", "10''", "11''"};

    private String[] dataArray = new String[]{"2017-07-15 18:00:02",
            "2017-07-15 18:10:22", "2017-07-15 18:11:24",
            "2017-07-15 18:20:23", "2017-07-15 18:30:31",
            "2017-07-15 18:35:37", "2017-07-15 18:40:13",
            "2017-07-15 18:50:26", "2017-07-15 18:52:57",
            "2017-07-15 18:55:11", "2017-07-15 18:56:45",
            "2017-07-15 18:57:33",};
    private final static int COUNT = 12;// 初始化数组总数

    /**
     * 模拟加载消息历史，实际开发可以从数据库中读出
     */
    public void initData() {
        for (int i = 0; i < COUNT; i++) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setDate(dataArray[i]);
            if (i % 2 == 0) {
                entity.setName("御坂美琴");
                entity.setMsgType(true);// 收到的消息
            } else {
                entity.setName(API.getAccountApi().whoAmI().name);
                entity.setMsgType(false);// 自己发送的消息
            }
            entity.setMessage(msgArray[i]);
            mDataArrays.add(entity);
        }
//        ChatMsgEntity entity = new ChatMsgEntity();
//        mDataArrays.add(entity);
        mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:// 发送按钮点击事件
                send();
                break;
        }
    }

    /**
     * 发送消息
     */
    private void send() {
        String contString = mEditTextContent.getText().toString();
        if (contString.length() > 0) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setName("白井黑子");
            entity.setDate(getDate());
            entity.setMessage(contString);
            entity.setMsgType(false);

            mDataArrays.add(entity);
            mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变

            mEditTextContent.setText("");// 清空编辑框数据

            mListView.setSelection(mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
        }
    }

    /**
     * 发送消息时，获取当前事件
     *
     * @return 当前时间
     */
    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(new Date());
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

    public void setting(View view) {
        Intent intent=new Intent(ChatActivity.this,ChatSettingActivity.class);
      //
        if(CHAT_TYPE == Constants.CHAT_TYPE_CHEDUI) {//车队
            intent=new Intent(ChatActivity.this,CarGroupSettingActivity.class);
        } else if(CHAT_TYPE == Constants.CHAT_TYPE_GEREN) {//各人
            intent=new Intent(ChatActivity.this,ChatSettingActivity.class);
        } else  {//群聊
            intent=new Intent(ChatActivity.this,ChatGroupSettingActivity.class);
        }
        startActivity(intent);
    }

    public void startSpeak(View view) {
        //iconVoice
    }




    private class OEMToneGen implements OEMToneGenerator {
        private OEMToneProgressListener toneProgressListener = null;
        private ToneGenerator mToneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 15);

        void setToneProgressListener(OEMToneProgressListener l) {
            toneProgressListener = l;
        }

        @Override
        public void alertTone(final int type) {
            mToneGen.startTone(ToneGenerator.TONE_DTMF_5);
            uiHandler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    mToneGen.stopTone();
                    if (toneProgressListener != null)
                        toneProgressListener.onToneStopped(type);
                }
            }, 100);
        }
    }

    private OEMToneGen oemToneGen = new OEMToneGen();
    private void setupToneGen() {
        deviceApi.setOemToneGenerator(oemToneGen);
        OEMToneProgressListener listener = deviceApi.getToneProgressListener();
        oemToneGen.setToneProgressListener(listener);
    }
    //
    private Runnable delayInitApi = new Runnable() {
        @Override
        public void run() {
            if ((sessionapi = API.getSessionApi()) != null) {
                deviceApi = API.getDeviceApi();
                sessionapi.setOnMediaListener(new MyOnMediaListener(){
                    //媒体回调
                    @Override
                    public void onSomeoneSpeaking(int i, int i1, int i2, int i3, int i4) {
                        super.onSomeoneSpeaking(i, i1, i2, i3, i4);
                        setSpeack(i);
                    }

                    @Override
                    public void onThatoneSayOver(int i, int i1) {
                        super.onThatoneSayOver(i, i1);
                        closeSpeack();
                    }
                });

            } else {
                uiHandler.postDelayed(delayInitApi, 300);
            }
        }
    };
    //获取话权
    private void talkRequest(CompactID session) {
        //isPttPressed = true;
        if (session != null && sessionapi != null)
            sessionapi.talkRequest(API.getAccountApi().whoAmI().id, session.getType(), session.getId());
    }
    //释放
    private void talkRelease(CompactID session) {
       // isPttPressed = false;
        if (session != null && sessionapi != null)
            sessionapi.talkRelease(API.getAccountApi().whoAmI().id, session.getType(), session.getId());
    }
//弹出菜单监听事件
    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        switch (position){
            case 0:
                ChatMsgEntity entity = new ChatMsgEntity();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss");//时间
                speakTime.setTime(System.currentTimeMillis());
                entity.setDate(format.format(speakTime).toString());
                entity.setMessage("西南交大 | 1.2km");
                entity.setMsgType(false);
                entity.setMsgType(1);
                entity.setName(API.getAccountApi().whoAmI().name);
                sendMsg(entity);
                break;
            case 1:
                break;
            case 2:

                break;
        }
        sendOthers.setVisibility(View.VISIBLE);
        rfabHelper.toggleContent();
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        Log.i("login",""+position);
        switch (position){
            case 0:
                ChatMsgEntity entity = new ChatMsgEntity();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss");//时间
                speakTime.setTime(System.currentTimeMillis());
                entity.setDate(format.format(speakTime).toString());
                entity.setMessage("西南交大 | 1.2km");
                entity.setMsgType(false);
                entity.setMsgType(1);
                if(!ISDEBUG){
                    entity.setName(API.getAccountApi().whoAmI().name);
                } else {
                    entity.setName("我");
                }

                sendMsg(entity);
                break;
            case 1:
                break;
            case 2:
                ChatMsgEntity entity2 = new ChatMsgEntity();
                SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss");//时间
                speakTime.setTime(System.currentTimeMillis());
                entity2.setDate(format2.format(speakTime).toString());
                entity2.setMessage("西南交大 | 1.2km");
                entity2.setMsgType(false);
                entity2.setMsgType(2);
                if(!ISDEBUG){
                    entity2.setName(API.getAccountApi().whoAmI().name);
                } else {
                    entity2.setName("我");
                }
                sendMsg(entity2);
                break;
        }
        sendOthers.setVisibility(View.VISIBLE);
        rfabHelper.toggleContent();
    }


    @Override
    public void onRFABClick() {
        Log.i("login","OK22onRFABClick");
        sendOthers.setVisibility(View.INVISIBLE);
    }

    public void back(View view){
        if(currSession != null){
            sessionapi.sessionBye(API.getAccountApi().whoAmI().id,channel.cid.getType(),channel.cid.getId());
        }
        finish();// 结束,实际开发中，可以返回主界面
    }
}

