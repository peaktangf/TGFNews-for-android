package com.project.tangaofeng.actionbar_demo.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by tangaofeng on 16/6/22.
 */
public class HomePageAdapter extends PagerAdapter{

    List<View> views;

    public HomePageAdapter(List<View> Views) {
        this.views = Views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //添加页卡
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    //删除页卡
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

}
