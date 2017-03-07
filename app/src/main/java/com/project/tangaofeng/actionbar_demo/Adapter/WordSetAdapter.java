package com.project.tangaofeng.actionbar_demo.Adapter;

import android.graphics.ImageFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.tangaofeng.actionbar_demo.App;
import com.project.tangaofeng.actionbar_demo.R;

/**
 * Created by tangaofeng on 16/7/5.
 */
public class WordSetAdapter extends BaseAdapter{

    private int selectPosition;

    public WordSetAdapter(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    @Override
    public int getCount() {
        return App.getInstance().newsFonts.size();
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
        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wordset_cell,null);
        TextView tvSize = (TextView) relativeLayout.findViewById(R.id.tvSize);
        ImageView imgSelect = (ImageView) relativeLayout.findViewById(R.id.imgSelect);
        tvSize.setText(App.getInstance().newsFonts.get(i));
        if (i == selectPosition) {
            imgSelect.setImageResource(R.mipmap.img_draw_normal);
        }else  {
            imgSelect.setImageResource(0);
        }
        return relativeLayout;
    }
}
