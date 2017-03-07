package com.project.tangaofeng.actionbar_demo;


import android.app.Activity;

import com.project.tangaofeng.actionbar_demo.Activity.CollectActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luke on 2015/12/17.
 */

public class App extends BaseApplication {

    private static App INSTANCE;

    public CollectActivity activity;
    public ArrayList<String> newsFonts;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        INSTANCE = this;
        newsFonts = new ArrayList<String>();
        String[] strs = {"小","中","大","超大","巨大","巨无霸"};
        for (int i = 0;i < strs.length; i ++) {
            newsFonts.add(strs[i]);
        }
        initImageLoader(this);
    }

    public static App getInstance() {
        return INSTANCE;
    }

}
