package com.example.tr.tourhear;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhangYan on 2017/7/16.
 */

public class HomeFragment extends Fragment {
    private ListView listView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.page_01, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        List<Map<String, Object>> list = getData();
        listView.setAdapter(new ListViewAdapter(getActivity(), list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),ChatActivity.class);
                startActivity(intent);
            }
        });
//        listView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ChatActivity.class);
//                startActivity(intent);
//            }
//        });
        return view;
    }

    public List<Map<String, Object>> getData() {
        int image_input[]={R.mipmap.ic_launcher,R.drawable.timg,R.drawable.timg,R.drawable.timg,R.drawable.timg,R.drawable.timg,R.drawable.timg,R.drawable.timg,R.drawable.timg,R.drawable.timg};
        String name_input[]={"西南交大抄家小分队","御坂美琴","炮姐1号","炮姐2号","炮姐3号","炮姐4号","炮姐5号","炮姐6号","炮姐7号","炮姐8号"};
        String date_input[]={"20人","2017-7-21","2017-7-20","2017-7-20","2017-7-20","2017-7-19","2017-7-19","2017-7-19","2017-7-18","2017-7-17"};
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", image_input[i]);
            map.put("name", name_input[i]);
      //      map.put("mess", "喵喵喵~");
            map.put("date", date_input[i]);
            list.add(map);
        }
        return list;
    }

}