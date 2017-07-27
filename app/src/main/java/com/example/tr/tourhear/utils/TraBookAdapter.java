package com.example.tr.tourhear.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tr.tourhear.R;
import com.example.tr.tourhear.view.CircleImageView;

import java.util.List;
import java.util.Map;

/**
 * Created by ZhangYan on 2017/7/16.
 */

public class TraBookAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;

    public TraBookAdapter(Context context, List<Map<String, Object>> data) {
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
        public TextView time;
        public ImageView picture;
        public TextView tittle;
        public ImageView image_collect;
        public TextView text_collect;
        public ImageView image_like;
        public TextView text_like;
        public ImageView image_comment;
        public TextView text_comment;
        public ImageView image_forward;
        public TextView text_forward;
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
            convertView = layoutInflater.inflate(R.layout.item_tra_book, null);
            zujian.image = (CircleImageView) convertView.findViewById(R.id.image);
            zujian.name = (TextView) convertView.findViewById(R.id.name);
            zujian.time = (TextView) convertView.findViewById(R.id.time);
            zujian.picture = (ImageView) convertView.findViewById(R.id.picture);
            zujian.tittle = (TextView) convertView.findViewById(R.id.tittle);
            zujian.image_collect = (ImageView) convertView.findViewById(R.id.image_collect);
            zujian.text_collect = (TextView) convertView.findViewById(R.id.text_collect);
            zujian.image_like = (ImageView) convertView.findViewById(R.id.image_like);
            zujian.text_like = (TextView) convertView.findViewById(R.id.text_like);
            zujian.image_comment = (ImageView) convertView.findViewById(R.id.image_comment);
            zujian.text_comment = (TextView) convertView.findViewById(R.id.text_comment);
            zujian.image_forward = (ImageView) convertView.findViewById(R.id.image_forward);
            zujian.text_forward = (TextView) convertView.findViewById(R.id.text_forward);
            convertView.setTag(zujian);
        } else {
            zujian = (Zujian) convertView.getTag();
        }
        //绑定数据
     //   zujian.image.setBackgroundResource((Integer) data.get(position).get("image"));
        zujian.image.setImageDrawable(context.getResources().getDrawable((Integer) data.get(position).get("image")));
        zujian.name.setText((String) data.get(position).get("name"));
        zujian.time.setText((String) data.get(position).get("time"));
        zujian.picture.setBackgroundResource((Integer) data.get(position).get("picture"));
        zujian.tittle.setText((String) data.get(position).get("tittle"));
        zujian.image_collect.setBackgroundResource((Integer) data.get(position).get("image_collect"));
        zujian.text_collect.setText((String) data.get(position).get("text_collect"));
        zujian.image_like.setBackgroundResource((Integer) data.get(position).get("image_like"));
        zujian.text_like.setText((String) data.get(position).get("text_like"));
        zujian.image_comment.setBackgroundResource((Integer) data.get(position).get("image_comment"));
        zujian.text_comment.setText((String) data.get(position).get("text_comment"));
        zujian.image_forward.setBackgroundResource((Integer) data.get(position).get("image_forward"));
        zujian.text_forward.setText((String) data.get(position).get("text_forward"));
        return convertView;
    }

}