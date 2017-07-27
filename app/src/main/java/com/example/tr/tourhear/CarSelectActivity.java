package com.example.tr.tourhear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.tr.tourhear.utils.CarSelectAdapter;
import com.example.tr.tourhear.utils.ChineseToEnglish;
import com.example.tr.tourhear.utils.CompareSort;
import com.example.tr.tourhear.utils.User;

import java.util.ArrayList;
import java.util.Collections;

public class CarSelectActivity extends Activity {
    ListView mListview;
    CarSelectAdapter mAdapter;
    LinearLayout mBtnBack;// 返回btn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.car_select);
        Button bt_yes = (Button) findViewById(R.id.btn_yes);
        mListview = (ListView) findViewById(R.id.listview);
        String[] contactsArray = getResources().getStringArray(R.array.group);
        mBtnBack = (LinearLayout) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                ;
            }
        });
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarSelectActivity.this, CarDispatchActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //模拟添加数据到Arraylist
        int length = contactsArray.length;
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            User user = new User();
            user.setName(contactsArray[i]);
            String firstSpell = ChineseToEnglish.getFirstSpell(contactsArray[i]);
            String substring = firstSpell.substring(0, 1).toUpperCase();
            if (substring.matches("[A-Z]")) {
                user.setLetter(substring);
            } else {
                user.setLetter("#");
            }
            users.add(user);
        }


        //排序
        Collections.sort(users, new CompareSort());

        //设置数据
        mAdapter = new CarSelectAdapter(this);
        mAdapter.setData(users);
        mListview.setAdapter(mAdapter);

        //设置回调
    }

}
