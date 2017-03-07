package com.project.tangaofeng.actionbar_demo.Adapter;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.tangaofeng.actionbar_demo.R;

/**
 * Created by tangaofeng on 16/6/22.
 */
public class HomeMenuAdapter extends RecyclerView.Adapter {

    public int curSelectMenuIndex = 0;
    Context ctx;
    private OnClickCallBack onClickCallBack;
    final String[] menuLists = {"头条","NBA","手机","移动互联","时尚","娱乐","电影","科技"};

    public HomeMenuAdapter(Context ctx,OnClickCallBack onClickCallBack) {
        this.ctx = ctx;
        this.onClickCallBack = onClickCallBack;
    }

    //定义回调接口
    public interface OnClickCallBack {
        public void callBack(int postion);
    }

    //定义监听点击的接口
    private interface MyItemClickListener {
        public void onItemClick(View view,int postion);
    }

    //自定义ViewHolder
    class MenuHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Button btn;
        private MyItemClickListener mListener;

        public MenuHolder(Button itemView,MyItemClickListener listener) {
            super(itemView);
            btn = itemView;
            mListener = listener;
            itemView.setOnClickListener(this);
        }
        public TextView getBtn() {
            return btn;
        }

        @Override
        public void onClick(View view) {
            if(mListener != null){
                mListener.onItemClick(view, (int) view.getTag());
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建ViewHolder
        MenuHolder holder = new MenuHolder(new Button(parent.getContext()),
                new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                curSelectMenuIndex = postion;
                notifyDataSetChanged();
                //回调
                onClickCallBack.callBack(postion);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //设置ViewHolder
        MenuHolder menuHolder = (MenuHolder) holder;
        Button btn = menuHolder.btn;
        btn.setTag(position);
        btn.setWidth(140);
        btn.setPadding(0,0,0,0);
        //去掉背景色
        btn.setBackgroundResource(0);
        btn.setText(menuLists[position]);
        if (btn.getTag() == curSelectMenuIndex) {
            btn.setTextSize(14);
            btn.setTextColor(ctx.getResources().getColor(R.color.colorNavbar));
        }
        else  {
            btn.setTextSize(12);
            btn.setTextColor(ctx.getResources().getColor(R.color.colorNewsBodyText));
        }
    }

    @Override
    public int getItemCount() {
        //返回ViewHolder的个数
        return menuLists.length;
    }
}
