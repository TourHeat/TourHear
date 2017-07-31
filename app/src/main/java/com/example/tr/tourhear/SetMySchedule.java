package com.example.tr.tourhear;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lilei.springactionmenu.ActionMenu;

public class SetMySchedule extends AppCompatActivity {
    private ActionMenu actionMenu;
    private ActionMenu actionMenu2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_my_schedule);

        actionMenu = (ActionMenu) findViewById(R.id.actionMenuTop);
        // actionMenu.me
        actionMenu.addView(R.mipmap.menu_carmer, getItemColor(R.color.menuNormalInfo), getItemColor(R.color.menuPressInfo));
        actionMenu.addView(R.mipmap.menu_photo, getItemColor(R.color.menuNormalRed), getItemColor(R.color.menuPressRed));
        actionMenu.addView(R.mipmap.mikefeng);

        actionMenu2 = (ActionMenu) findViewById(R.id.actionMenuTop2);
        // actionMenu.me
        actionMenu2.addView(R.mipmap.menu_carmer, getItemColor(R.color.menuNormalInfo), getItemColor(R.color.menuPressInfo));
        actionMenu2.addView(R.mipmap.menu_photo, getItemColor(R.color.menuNormalRed), getItemColor(R.color.menuPressRed));
        actionMenu2.addView(R.mipmap.mikefeng);
    }

    public void back(View view) {
        finish();
    }
    //获取颜色
    private int getItemColor(int colorID) {
        return getResources().getColor(colorID);
    }
}
