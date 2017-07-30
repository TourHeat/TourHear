package com.example.tr.tourhear;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.example.tr.tourhear.utils.Constants;
import com.example.tr.tourhear.utils.TraBookAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.tr.tourhear.R.mipmap.headportrait_1;
import static com.example.tr.tourhear.R.mipmap.headportrait_2;
import static com.example.tr.tourhear.R.mipmap.headportrait_3;
import static com.example.tr.tourhear.R.mipmap.jingdian_jiuzai1;
import static com.example.tr.tourhear.R.mipmap.jingdian_jiuzai2;
import static com.example.tr.tourhear.R.mipmap.jingdian_kuanzai;

/**
 * Created by ZhangYan on 2017/7/16.
 */

public class SettingFragment extends Fragment implements View.OnClickListener {
    MyListView mylistView;
    private Button btn_mypublish;
    private Button btn_mycolleciton;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        mylistView = (MyListView) view.findViewById(R.id.tra_book_list);
        List<Map<String, Object>> list = getData2();
        mylistView.setAdapter(new TraBookAdapter(getActivity(), list, Constants.LISTVIEW_MYPUBLISH));
        mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //进入帖子
                Intent intent=new Intent(getActivity(),ChatActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initView(View view) {
        btn_mypublish = (Button)view.findViewById(R.id.mine_publish);
        btn_mycolleciton = (Button) view.findViewById(R.id.mine_collection);
        btn_mypublish.setOnClickListener(this);
        btn_mycolleciton.setOnClickListener(this);
    }

    public List<Map<String, Object>> getData() {
        int jingdian[]={jingdian_jiuzai2,jingdian_kuanzai,jingdian_jiuzai1};
        int headport[]={headportrait_2,headportrait_1,headportrait_3};
        String jingdiannames[]={"九寨三日游，平安出行，放心游玩！！","成都周边两日游，一起来畅玩！！","九寨三日游，快来跟我一起浪迹天涯吧!!"};
        String usernames[]={"特立独行的猪","小胖严","张胖胖"};
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 3; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", headport[i]);
            map.put("name", usernames[i]);
            map.put("time", "2分钟前");
            map.put("picture", jingdian[i]);
            map.put("tittle",jingdiannames[i]);
            map.put("image_collect",R.drawable.icon_pulldown);
            map.put("text_collect","5人");
            map.put("image_like",R.drawable.icon_pulldown);
            map.put("text_like","35");
            map.put("image_comment",R.drawable.icon_pulldown);
            map.put("text_comment","233");
            map.put("image_forward",R.drawable.icon_pulldown);
            map.put("text_forward","16");
            list.add(map);
        }
        return list;
    }
    public List<Map<String, Object>> getData2() {
        int jingdian[]={jingdian_jiuzai1,jingdian_kuanzai,jingdian_jiuzai2};
        int headport[]={headportrait_2,headportrait_1,headportrait_3};
        String jingdiannames[]={"九寨三日游，平安出行，放心游玩！！","成都周边两日游，一起来畅玩！！","九寨三日游，快来跟我一起浪迹天涯吧!!"};
        String usernames[]={"特立独行的猪","小胖严","张胖胖"};
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 3; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", headport[i]);
            map.put("name", usernames[i]);
            map.put("time", simpleFormat.format(date).toString());
            date.setTime(date.getTime()-1000*60*60*24*((int)(Math.random()*10)));
            map.put("picture", jingdian[i]);
            map.put("tittle",jingdiannames[i]);
            map.put("image_collect",R.drawable.icon_pulldown);
            map.put("text_collect","5人");
            map.put("image_like",R.drawable.icon_pulldown);
            map.put("text_like","35");
            map.put("image_comment",R.drawable.icon_pulldown);
            map.put("text_comment","233");
            map.put("image_forward",R.drawable.icon_pulldown);
            map.put("text_forward","16");
            list.add(map);
        }
        return list;
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.mine_publish) {
            mylistView.setAdapter(new TraBookAdapter(getActivity(), getData2(), Constants.LISTVIEW_MYPUBLISH));
            btn_mycolleciton.setBackgroundColor(getResources().getColor(R.color.white));
            btn_mypublish.setBackgroundColor(getResources().getColor(R.color.infosColor));
        } else {
            mylistView.setAdapter(new TraBookAdapter(getActivity(), getData(), Constants.LISTVIEW_MYCOLLECTION));
            btn_mycolleciton.setBackgroundColor(getResources().getColor(R.color.infosColor));
            btn_mypublish.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }
}