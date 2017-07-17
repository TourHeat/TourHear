package com.example.tr.tourhear.utils;

import com.example.tr.tourhear.R;

/**
 * Created by ZhangYan on 2017/7/16.
 */
public class User {
    private String name;
    private String letter;
    private int icon = R.mipmap.ic_launcher;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
