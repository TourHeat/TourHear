package com.example.tr.tourhear.entity;

import com.algebra.sdk.entity.Contact;

import java.util.List;

/**
 * Created by TR on 2017/7/23.
 */

public class ChaneelMems {
    int cid;
    Contact mem;
    List<Contact> cs;
    public int id;
    public String name = null;
    public int state;

    ChaneelMems(){}
    ChaneelMems(int var1, String var2, int var3,int cid){
        this.id = var1;
        this.name = var2;
        this.state = var3;
        this.cid = cid;
    }
    ChaneelMems(Contact mem,int cid){
        this.mem = mem;
        this.cid = cid;
    }
    public ChaneelMems(List<Contact> cs,int cid){
        this.cs = cs;
        this.cid = cid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public Contact getMem() {
        return mem;
    }

    public void setMem(Contact mem) {
        this.mem = mem;
    }

    public List<Contact> getCs() {
        return cs;
    }

    public void setCs(List<Contact> cs) {
        this.cs = cs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
