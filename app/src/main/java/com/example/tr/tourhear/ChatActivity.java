package com.example.tr.tourhear;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
    private EditText mEditTextContent;
    private ListView mListView;
    private ChatMsgViewAdapter mAdapter;// 消息视图的Adapter
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();// 消息对象数组

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
        mListView = (ListView) findViewById(R.id.listview);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mBtnBack = (Button) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
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
            case R.id.btn_back:// 返回按钮点击事件
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
}
