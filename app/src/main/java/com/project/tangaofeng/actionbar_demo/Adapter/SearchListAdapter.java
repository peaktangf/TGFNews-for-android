package com.project.tangaofeng.actionbar_demo.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.tangaofeng.actionbar_demo.Model.Weather.SearchCityModel;
import com.project.tangaofeng.actionbar_demo.R;

import java.util.ArrayList;

/**
 * Created by tangaofeng on 16/7/13.
 */
public class SearchListAdapter extends BaseAdapter{

    private ArrayList<SearchCityModel> citys;

    public SearchListAdapter(ArrayList<SearchCityModel> citys) {
        this.citys = citys;
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
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_list_cell,null);
        TextView tvTitle = (TextView) linearLayout.findViewById(R.id.tvTitle);
        tvTitle.setText(String.format("%s,%s,%s",citys.get(i).name_cn,citys.get(i).district_cn,citys.get(i).province_cn));
        return linearLayout;
    }
}
