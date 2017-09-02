package com.example.tr.tourhear;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.algebra.sdk.API;
import com.algebra.sdk.AccountApi;
import com.algebra.sdk.ChannelApi;
import com.algebra.sdk.entity.Channel;
import com.algebra.sdk.entity.CompactID;
import com.example.tr.tourhear.myimplements.MyOnChannelListener;
import com.example.tr.tourhear.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.example.tr.tourhear.utils.Constants.ISDEBUG;

public class MainActivity extends FragmentActivity implements OnClickListener {
    // 底部菜单4个Linearlayout
    private LinearLayout ll_home;
    private LinearLayout ll_address;
    private LinearLayout ll_friend;
    private LinearLayout ll_setting;

    // 底部菜单4个ImageView
    private ImageView iv_home;
    private ImageView iv_address;
    private ImageView iv_friend;
    private ImageView iv_setting;

    // 底部菜单4个菜单标题
    private TextView tv_home;
    private TextView tv_address;
    private TextView tv_friend;
    private TextView tv_setting;

    // 4个Fragment
    private Fragment homeFragment;
    private Fragment addressFragment;
    private Fragment friendFragment;
    private Fragment settingFragment;
    private Handler uiHandler = null;
    private Context uiContext;
    private ChannelApi channelApi;
    private Channel channel;
    private static List<Channel> channelList = new ArrayList<Channel>();
    private static String[] channelsname;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // 初始化控件
        initView();
        // 初始化底部按钮事件
        initEvent();
        // 初始化并设置当前Fragment
        initFragment(0);
        if (!ISDEBUG) {
            uiHandler = Login.getUiHandler();
            channelApi = API.getChannelApi();
            initChannes();
        }
        //
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
                for (Channel channel2 : list) {
                    Log.i("login", "频道:::------------" + channel2.toString());
                    Log.i("login", channel2.name + channel2.cid);
                    CompactID cid = channel2.cid;
                    Log.i("login", "id" + cid.getId() + " type:" + cid.getType());
                }
            }
        });
        Log.i("login", "chanel inti" + (me.whoAmI().id));
    }

    public static List<Channel> getChannelList() {
        return channelList;
    }

    private void initFragment(int index) {
        // 由于是引用了V4包下的Fragment，所以这里的管理器要用getSupportFragmentManager获取
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏所有Fragment
        hideFragment(transaction);
        switch (index) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fl_content, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                if (addressFragment == null) {
                    addressFragment = new AddressFragment();
                    transaction.add(R.id.fl_content, addressFragment);
                } else {
                    transaction.show(addressFragment);
                }

                break;
            case 2:
                if (friendFragment == null) {
                    friendFragment = new FriendFragment();
                    transaction.add(R.id.fl_content, friendFragment);
                } else {
                    transaction.show(friendFragment);
                }

                break;
            case 3:
                if (settingFragment == null) {
                    settingFragment = new SettingFragment();
                    transaction.add(R.id.fl_content, settingFragment);
                } else {
                    transaction.show(settingFragment);
                }

                break;

            default:
                break;
        }

        // 提交事务
        transaction.commit();
    }

    //隐藏Fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (addressFragment != null) {
            transaction.hide(addressFragment);
        }
        if (friendFragment != null) {
            transaction.hide(friendFragment);
        }
        if (settingFragment != null) {
            transaction.hide(settingFragment);
        }

    }

    private void initEvent() {
        // 设置按钮监听
        ll_home.setOnClickListener(this);
        ll_address.setOnClickListener(this);
        ll_friend.setOnClickListener(this);
        ll_setting.setOnClickListener(this);

    }

    private void initView() {

        // 底部菜单4个Linearlayout
        this.ll_home = (LinearLayout) findViewById(R.id.ll_home);
        this.ll_address = (LinearLayout) findViewById(R.id.ll_address);
        this.ll_friend = (LinearLayout) findViewById(R.id.ll_friend);
        this.ll_setting = (LinearLayout) findViewById(R.id.ll_setting);

        // 底部菜单4个ImageView
        this.iv_home = (ImageView) findViewById(R.id.iv_home);
        this.iv_address = (ImageView) findViewById(R.id.iv_address);
        this.iv_friend = (ImageView) findViewById(R.id.iv_friend);
        this.iv_setting = (ImageView) findViewById(R.id.iv_setting);

        // 底部菜单4个菜单标题
        this.tv_home = (TextView) findViewById(R.id.tv_home);
        this.tv_address = (TextView) findViewById(R.id.tv_address);
        this.tv_friend = (TextView) findViewById(R.id.tv_friend);
        this.tv_setting = (TextView) findViewById(R.id.tv_setting);

    }

    @Override
    public void onClick(View v) {

        // 在每次点击后将所有的底部按钮(ImageView,TextView)颜色改为灰色，然后根据点击着色
        restartBotton();
        // ImageView和TetxView置为绿色，页面随之跳转
        switch (v.getId()) {
            case R.id.ll_home:
                iv_home.setImageResource(R.drawable.tab_message_press);
                tv_home.setTextColor(0xff1B940A);
                initFragment(0);
                break;
            case R.id.ll_address:
                iv_address.setImageResource(R.drawable.tongxunlu);
                tv_address.setTextColor(0xff1B940A);
                initFragment(1);
                break;
            case R.id.ll_friend:
                iv_friend.setImageResource(R.drawable.tab_find_press);
                tv_friend.setTextColor(0xff1B940A);
                initFragment(2);
                break;
            case R.id.ll_setting:
                iv_setting.setImageResource(R.drawable.tab_me_press);
                tv_setting.setTextColor(0xff1B940A);
                initFragment(3);
                break;

            default:
                break;
        }

    }

    private void restartBotton() {
        // ImageView置为灰色
        iv_home.setImageResource(R.drawable.tab_message);
        iv_address.setImageResource(R.drawable.tab_address);
        iv_friend.setImageResource(R.drawable.tab_find);
        iv_setting.setImageResource(R.drawable.tab_me);
        // TextView置为灰色
        tv_home.setTextColor(0xff50597e);
        tv_address.setTextColor(0xff50597e);
        tv_friend.setTextColor(0xff50597e);
        tv_setting.setTextColor(0xff50597e);
    }

    public void toChatActivity(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        //获取当前点击的频道
        TextView cn = (TextView) view.findViewById(R.id.name);
        intent.putExtra("channelName", cn.getText().toString());
        //群聊，频道
        if (cn.getText().toString().equals("我的车队")) {
            intent.putExtra("CHATTYPE", Constants.CHAT_TYPE_CHEDUI);
        } else if (cn.getText().toString().equals("成都队")) {
            intent.putExtra("CHATTYPE", Constants.CHAT_TYPE_GROUP);
        } else {
             intent.putExtra("CHATTYPE", Constants.CHAT_TYPE_GEREN);
         //   intent.putExtra("CHATTYPE", Constants.CHAT_TYPE_GROUP);
        }
        startActivity(intent);
    }

    public void publishRouteBook(View view) {
        Intent intent = new Intent(MainActivity.this, SetMySchedule.class);
        startActivity(intent);
    }
}