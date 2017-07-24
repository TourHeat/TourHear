package com.example.tr.tourhear;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
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
import com.algebra.sdk.entity.Contact;
import com.example.tr.tourhear.entity.ChaneelMems;
import com.example.tr.tourhear.myimplements.MyOnMediaListener;
import com.example.tr.tourhear.myimplements.MyOnSessionListener;
import com.example.tr.tourhear.utils.ChatMsgEntity;
import com.example.tr.tourhear.utils.ChatMsgViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ZhangYan on 2017/7/16.
 */

public class ChatActivity extends Activity implements OnClickListener {

    private Button mBtnSend;// 发送btn
    private Button mBtnBack;// 返回btn
    private LinearLayout mBtnmore;// 聊天设置btn
    private EditText mEditTextContent;
    private ListView mListView;
    private ChatMsgViewAdapter mAdapter;// 消息视图的Adapter
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();// 消息对象数组
    private boolean voiceable = true;//设置能否发声
    private ImageView iconVoice;
    private TextView btnSpeak; //按住发声
    private LinearLayout bottom;
    private SessionApi sessionapi;//会话操作
//操作频道
    private List<Channel> cs = new ArrayList<Channel>();
    private  Channel channel = null;
    private DeviceApi deviceApi;//设备操作类
    private com.example.tr.tourhear.tl_demo.TalkHistory talkHistory = null;//历史
//媒体操作类
    private AudioManager mAudioManager = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initView();// 初始化view
        initData();// 初始化数据
        mListView.setSelection(mAdapter.getCount() - 1);

    }

    /**
     * 初始化view
     */
    public void initView() {
        mAudioManager = ((AudioManager) getSystemService(Context.AUDIO_SERVICE));//获取媒体
        talkHistory = new com.example.tr.tourhear.tl_demo.TalkHistory(API.getAccountApi().whoAmI().id);

        mListView = (ListView) findViewById(R.id.listview);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mBtnBack = (Button) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);
        mBtnmore = (LinearLayout) findViewById(R.id.btn_more);
        mBtnmore.setOnClickListener(this);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
        //底部
        bottom = (LinearLayout) findViewById(R.id.bottom);
        //对讲图标
        iconVoice = (ImageView) findViewById(R.id.icon_voice);
        btnSpeak = (TextView) findViewById(R.id.btn_speak);
        //  btnSpeak.setOnFocusChangeListener(this);
        btnSpeak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    iconVoice.setBackground(getResources().getDrawable(R.drawable.tab_message_press));
                    bottom.setBackgroundColor(getResources().getColor(R.color.infosColor));
                    sessionapi.talkRequest(API.getAccountApi().whoAmI().id,channel.cid.getType(),channel.cid.getId());
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    iconVoice.setBackground(getResources().getDrawable(R.drawable.tab_message));
                    bottom.setBackgroundColor(getResources().getColor(R.color.white));
                }
                return true;
            }
        });
        //频道名称
        cs = HomeFragment.getChannelList();
        //获取频道名称
        Intent intent = getIntent();
        String strChannelName = intent.getStringExtra("channelName");
        TextView cn =  (TextView) findViewById(R.id.channel_name);
        if(strChannelName != null && strChannelName !=""){//设置频道名称
            cn.setText(strChannelName);
        }
        for(Channel c : cs){
            Log.i("login","获取频道------------"+c.name);
            if(c.name.equals(strChannelName)){
                channel  = c;
                Log.i("login","频道OK------------"+channel.name);
            }
        }
       initSession();
        //初始化设备

    }

    private void initSession() {
        sessionapi  = API.getSessionApi();
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

                sessionapi.talkRequest(API.getAccountApi().whoAmI().id,channel.cid.getType(),channel.cid.getId());
               //获取频道成员
                ChaneelMems cm = HomeFragment.getCmem(channel.cid.getId());
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
        sessionapi.setOnMediaListener(new MyOnMediaListener(){

        });

       // sessionapi.startDialog(API.getAccountApi().whoAmI().id,channel.cid.getType(),channel.cid.getId());

    }

    private String[] msgArray = new String[]{"你好", "你好", "挖掘机学校哪家强", "中国山东找蓝翔",
            "棒棒哒", "你也棒棒哒", "山东蓝翔", "高级汽修学院",
            "人生若只如初见", "何事秋风悲画扇", "等闲识却故人心", "却道故人心易变"};

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
                entity.setName("白井黑子");
                entity.setMsgType(false);// 自己发送的消息
            }
            entity.setMessage(msgArray[i]);
            mDataArrays.add(entity);
        }

        mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:// 发送按钮点击事件
                send();
                break;
            case R.id.btn_more:// 发送按钮点击事件
                Intent intent=new Intent(ChatActivity.this,ChatSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_back:// 返回按钮点击事件
                //关闭会话
                sessionapi.sessionBye(API.getAccountApi().whoAmI().id,channel.cid.getType(),channel.cid.getId());
                finish();// 结束,实际开发中，可以返回主界面
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
        ImageView v = (ImageView) view.findViewById(R.id.voice);
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
        startActivity(intent);
    }

    public void startSpeak(View view) {
        //iconVoice
    }


}
