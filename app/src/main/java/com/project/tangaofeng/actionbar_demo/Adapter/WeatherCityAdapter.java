package com.project.tangaofeng.actionbar_demo.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.tangaofeng.actionbar_demo.R;

import java.util.ArrayList;

/**
 * Created by tangaofeng on 16/7/6.
 */
public class WeatherCityAdapter extends BaseAdapter{

    private ArrayList<String> citys;
    private String locationCity;

    public WeatherCityAdapter(ArrayList<String> citys,String locationCity) {
        this.citys = citys;
        this.locationCity = locationCity;
    }

    @Override
    public int getCount() {
        return citys.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_city_cell,null);
        ImageView imgLocation = (ImageView) relativeLayout.findViewById(R.id.imgLocation);
        TextView tvCity = (TextView) relativeLayout.findViewById(R.id.tvCity);

        tvCity.setText(citys.get(i));
        if (citys.get(i).equals(locationCity)) {
            imgLocation.setImageResource(R.mipmap.img_location);
        }else  {
            imgLocation.setImageResource(0);
        }

        return relativeLayout;
    }
}
