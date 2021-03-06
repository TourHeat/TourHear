package com.example.tr.tourhear.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tr.tourhear.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhangYan on 2017/7/16.
 */
public class PersonSelectAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<User> users;

    public PersonSelectAdapter(Context context) {
        this.mContext = context;
        users = new ArrayList<>();
    }

    public void setData(List<User> data) {
        this.users.clear();
        this.users.addAll(data);
    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_person_select, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvicon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.tvItem = (RelativeLayout) convertView.findViewById(R.id.item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(users.get(position).getName());
        viewHolder.tvicon.setImageResource(users.get(position).getIcon());
        viewHolder.tvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext,users.get(position).getName(),Toast.LENGTH_SHORT).show();
                //选中与取消选中事件
             //   Log.i("test","here");
                ImageView imageView = (ImageView) v.findViewById(R.id.select_it);
                if (imageView.getVisibility() == View.GONE) {
                    imageView.setVisibility(View.VISIBLE);
                } else if (imageView.getVisibility() == View.VISIBLE) {
                    imageView.setVisibility(View.GONE);
                }

            }
        });
        //当前的item的title与上一个item的title不同的时候回显示title(A,B,C......)
        if (position == getFirstLetterPosition(position) && !users.get(position).getLetter().equals("@")) {
            viewHolder.tvTitle.setVisibility(View.VISIBLE);
            viewHolder.tvTitle.setText(users.get(position).getLetter().toUpperCase());
        } else {
            viewHolder.tvTitle.setVisibility(View.GONE);
        }


        return convertView;
    }

    /**
     * 顺序遍历所有元素．找到position对应的title是什么（A,B,C?）然后找这个title下的第一个item对应的position
     *
     * @param position
     * @return
     */
    private int getFirstLetterPosition(int position) {

        String letter = users.get(position).getLetter();
        int cnAscii = ChineseToEnglish.getCnAscii(letter.toUpperCase().charAt(0));
        int size = users.size();
        for (int i = 0; i < size; i++) {
            if (cnAscii == users.get(i).getLetter().charAt(0)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 顺序遍历所有元素．找到letter下的第一个item对应的position
     *
     * @param letter
     * @return
     */
    public int getFirstLetterPosition(String letter) {
        int size = users.size();
        for (int i = 0; i < size; i++) {
            if (letter.charAt(0) == users.get(i).getLetter().charAt(0)) {
                return i;
            }
        }
        return -1;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvTitle;
        ImageView tvicon;
        RelativeLayout tvItem;
    }

}
