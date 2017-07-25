package com.example.tr.tourhear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.tr.tourhear.view.CircleImageView;

import java.util.List;
import java.util.Map;

/**
 * Created by ZhangYan on 2017/7/16.
 */

public class ListViewAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;


    public ListViewAdapter(Context context, List<Map<String, Object>> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * 组件集合，对应list.xml中的控件
     *
     * @author Administrator
     */
    public final class Zujian {
        public CircleImageView image;
        public TextView name;
        public TextView mess;
        public TextView date;
        public Button msg;//信息
    }

    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * 获得某一位置的数据
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    /**
     * 获得唯一标识
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Zujian zujian = null;
        if (convertView == null) {
            zujian = new Zujian();
            //获得组件，实例化组件
            convertView = layoutInflater.inflate(R.layout.listview, null);
            zujian.image = (CircleImageView) convertView.findViewById(R.id.image);
            zujian.name = (TextView) convertView.findViewById(R.id.name);
            zujian.mess = (TextView) convertView.findViewById(R.id.mess);
            zujian.date = (TextView) convertView.findViewById(R.id.date);
            zujian.msg = (Button) convertView.findViewById(R.id.msg);
            convertView.setTag(zujian);
        } else {
            zujian = (Zujian) convertView.getTag();
        }
        //绑定数据
        zujian.image.setImageDrawable(context.getResources().getDrawable((Integer) data.get(position).get("image")));
        zujian.name.setText((String) data.get(position).get("name"));
        zujian.mess.setText((String) data.get(position).get("mess"));
        zujian.date.setText((String) data.get(position).get("date"));
        //信息数
        int ms = (int)(Math.random()*100+1);
        zujian.msg.setText(""+ms);
        if (position ==0) {//设置车队样式
            zujian.msg.setBackground(context.getResources().getDrawable(R.drawable.corner_rec_bg_orange_press));
        }
        return convertView;
    }

}