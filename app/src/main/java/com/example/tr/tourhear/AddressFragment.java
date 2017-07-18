package com.example.tr.tourhear;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tr.tourhear.utils.ChineseToEnglish;
import com.example.tr.tourhear.utils.CompareSort;
import com.example.tr.tourhear.utils.SideBarView;
import com.example.tr.tourhear.utils.User;
import com.example.tr.tourhear.utils.UserAdapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ZhangYan on 2017/7/16.
 */

public class AddressFragment extends Fragment implements SideBarView.LetterSelectListener {
    ListView mListview;
    UserAdapter mAdapter;
    TextView mTip;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_02, container, false);
        mListview = (ListView) view.findViewById(R.id.listview);
        SideBarView sideBarView = (SideBarView) view.findViewById(R.id.sidebarview);
        String[] contactsArray = getResources().getStringArray(R.array.data);
        String[] headArray = getResources().getStringArray(R.array.head);
        mTip = (TextView) view.findViewById(R.id.tip);

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
        int temp[]={R.drawable.icon_add,R.drawable.icon_group,R.drawable.icon_tribe};
        //图表的icon存放在数组中循环调用
        for (int i = 0; i < headArray.length; i++) {
            User user = new User();
            user.setName(headArray[i]);
            user.setIcon(temp[i]);
            user.setLetter("@");
            users.add(user);
        }

        //排序
        Collections.sort(users, new CompareSort());

        //设置数据
        mAdapter = new UserAdapter(getActivity());
        mAdapter.setData(users);
        mListview.setAdapter(mAdapter);

        //设置回调
        sideBarView.setOnLetterSelectListen(this);
        return view;
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
        }
    }

}