package com.example.tr.tourhear;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.algebra.sdk.API;
import com.algebra.sdk.AccountApi;
import com.algebra.sdk.ChannelApi;
import com.algebra.sdk.entity.Channel;
import com.algebra.sdk.entity.Contact;
import com.example.tr.tourhear.entity.ChaneelMems;
import com.example.tr.tourhear.myimplements.MyOnChannelListener;
import com.example.tr.tourhear.utils.Constants;
import com.example.tr.tourhear.utils.ListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.tr.tourhear.utils.Constants.ISDEBUG;

/**
 * Created by ZhangYan on 2017/7/16.
 */



public class HomeFragment extends Fragment {

    public static List<ChaneelMems> cmems;//频道成员
    private ListView listView;
    //频道
    private Handler uiHandler = null;
    private Context uiContext;
    private ChannelApi channelApi;
    private Channel channel;
    private static List<Channel> channelList = new ArrayList<Channel>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.page_01, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        if(!ISDEBUG){
            cmems = new ArrayList<ChaneelMems>();
            uiHandler = Login.getUiHandler();
            channelApi = API.getChannelApi();
            initChannes();
            initList();
        } else {
            initList();
        }



//        listView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ChatActivity.class);
//                startActivity(intent);
//            }
//        });
        return view;
    }
    private void initList() {
        List<Map<String, Object>> list = new ArrayList<>();
        if(ISDEBUG) {
            list = getData2();
        }else {
            list = getData();
        }
        listView.setAdapter(new ListViewAdapter(getActivity(), list));
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(getActivity(),ChatActivity.class);
//                startActivity(intent);
//            }
//        });
//        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent=new Intent(getActivity(),ChatActivity.class);
//                Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
//                startActivity(intent);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
    }

    public List<Map<String, Object>> getData() {
        //List<Channel> channels = MainActivity.getChannelList();
        Log.i("login","获取频道：length:"+channelList.size());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //车队
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("image", R.mipmap.ic_launcher);
        map2.put("name","我的车队");
        map2.put("mess", "平安出行,放心游玩");
        map2.put("date", "11:03");

       // Log.i("login","获取频道：length:"+c.name);
        list.add(map2);
        int i = 0;
        for (Channel c:channelList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", Constants.headPortaits[(i++)%5]);
            map.put("name",c.name);
            map.put("mess", "陪我一起,浪迹天涯!");
            map.put("date", "11:03");
            Log.i("login","获取频道：length:"+c.name);
            list.add(map);
        }
//
//        }
        return list;
    }
    public List<Map<String, Object>> getData2() {
        //List<Channel> channels = MainActivity.getChannelList();
        //Log.i("login","获取频道：length:"+channelList.size());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //车队
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("image", R.mipmap.ic_launcher);
        map2.put("name","我的车队");
        map2.put("mess", "平安出行,放心游玩");
        map2.put("date", "11:03");

        // Log.i("login","获取频道：length:"+c.name);
        list.add(map2);

        int image_input[]={R.mipmap.ic_launcher,R.drawable.timg,R.drawable.timg,R.drawable.timg,R.drawable.timg,R.drawable.timg,R.drawable.timg,R.drawable.timg,R.drawable.timg,R.drawable.timg};
        String name_input[]={"九寨小分队","御坂美琴","炮姐1号","炮姐2号","炮姐3号","炮姐4号","炮姐5号","炮姐6号","炮姐7号","炮姐8号"};
        String date_input[]={"20人","2017-7-21","2017-7-20","2017-7-20","2017-7-20","2017-7-19","2017-7-19","2017-7-19","2017-7-18","2017-7-17"};

        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", Constants.headPortaits[i%5]);
            map.put("name", name_input[i]);
            map.put("mess", "陪我一起,浪迹天涯!");
            map.put("date", date_input[i]);
            list.add(map);
        }
        return list;
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
                initList();
                for(Channel c : list){
                    channelApi.channelMemberGet(API.getAccountApi().whoAmI().id ,c.cid.getType(),c.cid.getId());
                }

            }

            @Override
            public void onChannelMemberListGet(int userId, int ctype, int channelId, List<Contact> mems) {
                super.onChannelMemberListGet(userId, ctype, channelId, mems);
                cmems.add(new ChaneelMems(mems,channelId));
            }
        });
        Log.i("login","chanel inti"+(me.whoAmI().id));

    }

    public static List<Channel> getChannelList() {
        return channelList;
    }

    public static List<ChaneelMems> getCmems() {
        return cmems;
    }
    public static ChaneelMems getCmem(int cid) {
        for(ChaneelMems cm : cmems){
            if(cm.getCid() == cid){
                return  cm;
            }
        }
        return null;
    }
    public static int getCmemId(String name) {
        for(ChaneelMems cm : cmems){
            for(Contact c : cm.getCs()){
                if(c.name.equals(name)){
                    return c.id;
                }
            }
        }
        return 0;
    }
}