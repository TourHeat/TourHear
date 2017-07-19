package com.example.tr.tourhear;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by ZhangYan on 2017/7/16.
 */

public class SettingFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_04, container, false);
        final RelativeLayout personalInformation = (RelativeLayout) view.findViewById(R.id.PersonalInformation);
        personalInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),PersonalActivity.class);
                startActivity(intent);
            }
        });
        final RelativeLayout personalSetting = (RelativeLayout) view.findViewById(R.id.SrelativeLayout6);
        personalSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),PersonalSettingActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}