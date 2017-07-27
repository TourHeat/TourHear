package com.example.tr.tourhear.utils;

/**
 * Created by ZhangYan on 2017/7/16.
 */

public class ChatMsgEntity {
    private String name;//消息来自
    private String date;//消息日期
    private String message;//消息内容
    private boolean isComMeg = true;// 是否为收到的消息
    private int msgType = 0;// 是否为收到的消息
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public int getMsgType() {
        return this.msgType;
    }
    public void setMsgType(boolean isComMsg) {
        isComMeg = isComMsg;
    }

    public boolean isComMeg() {
        return isComMeg;
    }

    public void setComMeg(boolean comMeg) {
        isComMeg = comMeg;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public ChatMsgEntity() {
    }

    public ChatMsgEntity(String name, String date, String text, boolean isComMsg) {
        super();
        this.name = name;
        this.date = date;
        this.message = text;
        this.isComMeg = isComMsg;
    }
    public ChatMsgEntity(String name, String date, String text, int msgType) {
        super();
        this.name = name;
        this.date = date;
        this.message = text;
        this.msgType = msgType;
    }
}
