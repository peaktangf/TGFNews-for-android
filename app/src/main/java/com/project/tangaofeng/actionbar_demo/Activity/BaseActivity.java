package com.project.tangaofeng.actionbar_demo.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.project.tangaofeng.actionbar_demo.Manager.SharedPreferencesUtils;
import com.project.tangaofeng.actionbar_demo.Model.UserInfoModel;

public class BaseActivity extends AppCompatActivity {

    public UserInfoModel userInfoModel;
    public SharedPreferencesUtils sharedPreferencesUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferencesUtils = new SharedPreferencesUtils(getApplication(),"confing");
    }

    public UserInfoModel getUserInfoModel() {
        userInfoModel = sharedPreferencesUtils.getObject("user",UserInfoModel.class);
        if (userInfoModel == null) {
            userInfoModel = new UserInfoModel();
        }
        return userInfoModel;
    }
}
