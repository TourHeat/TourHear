package com.example.tr.tourhear.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tr.tourhear.R;
import com.example.tr.tourhear.view.CircleImageView;

import java.util.List;

import static com.example.tr.tourhear.R.layout.chatting_item_msg_photo_right;
import static com.example.tr.tourhear.R.layout.chatting_item_msg_text_left;
import static com.example.tr.tourhear.R.layout.chatting_item_msg_text_right;

/**
 * Created by ZhangYan on 2017/7/16.
 */

public class ChatMsgViewAdapter extends BaseAdapter {

    public static interface IMsgViewType {
        int IMVT_COM_MSG = 0;// 收到对方的消息
        int IMVT_TO_MSG = 1;// 自己发送出去的消息
    }

    private static final int ITEMCOUNT = 2;// 消息类型的总数
    private List<ChatMsgEntity> coll;// 消息对象数组
    private LayoutInflater mInflater;

    public ChatMsgViewAdapter(Context context, List<ChatMsgEntity> coll) {
        this.coll = coll;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return coll.size();
    }

    public Object getItem(int position) {
        return coll.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * 得到Item的类型，是对方发过来的消息，还是自己发送出去的
     */
    public int getItemViewType(int position) {
        ChatMsgEntity entity = coll.get(position);

        if (entity.isComMeg()) {//收到的消息
            return IMsgViewType.IMVT_COM_MSG;
        } else {//自己发送的消息
            return IMsgViewType.IMVT_TO_MSG;
        }
    }

    /**
     * Item类型的总数
     */
    public int getViewTypeCount() {
        return ITEMCOUNT;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ChatMsgEntity entity = coll.get(position);
        boolean isComMsg = entity.isComMeg();
        int type = entity.getMsgType();
        ViewHolder viewHolder = null;
        int laylefts[] = {chatting_item_msg_text_left,chatting_item_msg_text_left,chatting_item_msg_text_left};
        int layrights[] = {chatting_item_msg_text_right,chatting_item_msg_photo_right,chatting_item_msg_text_right};
        if (convertView == null) {
            if (isComMsg) {
                convertView = mInflater.inflate(
                        laylefts[entity.getMsgType()], null);
            } else {
                convertView = mInflater.inflate(
                        layrights[entity.getMsgType()], null);
            }

            viewHolder = new ViewHolder();
            viewHolder.tvSendTime = (TextView) convertView
                    .findViewById(R.id.tv_sendtime);
            viewHolder.tvUserName = (TextView) convertView
                    .findViewById(R.id.tv_username);
            viewHolder.tvContent = (TextView) convertView
                    .findViewById(R.id.tv_chatcontent);
            viewHolder.isComMsg = isComMsg;
//设置头像
            viewHolder.headportrait = (CircleImageView) convertView.findViewById(R.id.iv_userhead);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvSendTime.setText(entity.getDate());
        viewHolder.tvUserName.setText(entity.getName());
        viewHolder.tvContent.setText(entity.getMessage());
        try {
            //getCmemId
            //int hh = Constants.headPortaits[HomeFragment.getCmemId(entity.getName())%5];
            //设置头像
            int hh = Constants.headPortaits[Integer.parseInt(entity.getName())%5];
            viewHolder.headportrait.setImageDrawable(convertView.getResources().getDrawable(hh));
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder {
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;
        public CircleImageView headportrait;//头像
        public boolean isComMsg = true;
    }

}
