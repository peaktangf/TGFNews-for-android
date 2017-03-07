package com.project.tangaofeng.actionbar_demo.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.tangaofeng.actionbar_demo.App;
import com.project.tangaofeng.actionbar_demo.Model.NewsCellModel;
import com.project.tangaofeng.actionbar_demo.R;

import java.util.List;

/**
 * Created by tangaofeng on 16/7/5.
 */
public class CollectionAdapter extends BaseAdapter{

    private List<NewsCellModel> collects;
    private boolean isPhoto;

    //构造方法
    public CollectionAdapter(List<NewsCellModel> collects,boolean isPhoto) {
        this.collects = collects;
        this.isPhoto = isPhoto;
    }

    //重写该方法，指定一共包含多少个选项
    @Override
    public int getCount() {
        return collects.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    //重写该方法，该方法的返回值将作为列表项的ID
    @Override
    public long getItemId(int i) {
        return i;
    }

    //重写该方法，该方法返回的view将作为列表框
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        NewsCellModel newsCellModel = collects.get(i);
        LinearLayout linearLayout;
        if (isPhoto) {
            linearLayout = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.collection_img_item,null);
            ImageView imgMark = (ImageView) linearLayout.findViewById(R.id.imgMark);
            TextView tvTitle = (TextView) linearLayout.findViewById(R.id.tvTitle);
            App.getInstance().setImageViewUrl(imgMark,newsCellModel.imgsrc);
            tvTitle.setText(newsCellModel.title);
        } else  {
            linearLayout = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.collection_text_item,null);
            TextView tvTitle = (TextView) linearLayout.findViewById(R.id.tvTitle);
            tvTitle.setText(newsCellModel.title);
        }

        return linearLayout;
    }
}
