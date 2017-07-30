package com.example.tr.tourhear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tr.tourhear.utils.ChineseToEnglish;
import com.example.tr.tourhear.utils.CompareSort;
import com.example.tr.tourhear.utils.Constants;
import com.example.tr.tourhear.utils.PersonSelectAdapter;
import com.example.tr.tourhear.utils.SideBarView;
import com.example.tr.tourhear.utils.User;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ZhangYan on 2017/7/23.
 */

public class PersonSelectActivity extends Activity implements SideBarView.LetterSelectListener {
    ListView mListview;
    PersonSelectAdapter mAdapter;
    TextView mTip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.person_select);
        LinearLayout bt_back = (LinearLayout) findViewById(R.id.btn_back);
        Button bt_yes = (Button) findViewById(R.id.btn_yes);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonSelectActivity.this, PersonDispatchActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mListview = (ListView) findViewById(R.id.listview);
        SideBarView sideBarView = (SideBarView) findViewById(R.id.sidebarview);
        String[] contactsArray = getResources().getStringArray(R.array.data);
        mTip = (TextView) findViewById(R.id.tip);

        //模拟添加数据到Arraylist
        int length = contactsArray.length;
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            User user = new User();
            user.setName(contactsArray[i]);
            user.setIcon(Constants.headPortaits[i%5]);
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
        mAdapter = new PersonSelectAdapter(PersonSelectActivity.this);
        mAdapter.setData(users);
        mListview.setAdapter(mAdapter);

        //设置回调
        sideBarView.setOnLetterSelectListen(this);
    }

    @Override
    public void onLetterSelected(String letter) {
        setListviewPosition(letter);
        mTip.setText(letter);
        mTip.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLetterChanged(String letter) {
        setListviewPosition(letter);
        mTip.setText(letter);
    }

    @Override
    public void onLetterReleased(String letter) {
        mTip.setVisibility(View.GONE);
    }

    private void setListviewPosition(String letter) {
        int firstLetterPosition = mAdapter.getFirstLetterPosition(letter);
        if (firstLetterPosition != -1) {
            mListview.setSelection(firstLetterPosition);
            /*mscrollview.smoothScrollTo(0,mListview.getHeight()+ (mrelative.getHeight()+20));*/
        }
    }

}
