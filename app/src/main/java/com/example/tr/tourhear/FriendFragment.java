package com.example.tr.tourhear;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhangYan on 2017/7/16.
 */

public class FriendFragment extends Fragment {
    MyListView mylistView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_03, container, false);
        mylistView = (MyListView) view.findViewById(R.id.tra_book_list);
        List<Map<String, Object>> list = getData();
        mylistView.setAdapter(new TraBookAdapter(getActivity(), list));
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

    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 3; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", R.drawable.timg);
            map.put("name", "御坂美琴");
            map.put("time", "2分钟前");
            map.put("picture", R.drawable.timg);
            map.put("tittle","西南交通大学抄家三日游，三缺一！！");
            map.put("image_collect",R.drawable.icon_pulldown);
            map.put("text_collect","123");
            map.put("image_like",R.drawable.icon_pulldown);
            map.put("text_like","321");
            map.put("image_comment",R.drawable.icon_pulldown);
            map.put("text_comment","2333");
            map.put("image_forward",R.drawable.icon_pulldown);
            map.put("text_forward","666");
            list.add(map);
        }
        return list;
    }

}