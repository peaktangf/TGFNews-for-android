package com.project.tangaofeng.actionbar_demo.Adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.tangaofeng.actionbar_demo.App;
import com.project.tangaofeng.actionbar_demo.Model.NewsCellModel;
import com.project.tangaofeng.actionbar_demo.R;

import java.util.List;

/**
 * Created by tangaofeng on 16/6/22.
 */
public class NewsRecyAdapter extends RecyclerView.Adapter{

    public static final int TYPE_HEADER = 0;
    private List<NewsCellModel> newsArray;
    private HomePageAdapter homePageAdapter;
    private NewsCellClickListener mNewsCellClickListener;

    public NewsRecyAdapter(List<NewsCellModel> newsArray,HomePageAdapter homePageAdapter,NewsCellClickListener newsCellClickListener) {
        this.newsArray = newsArray;
        this.homePageAdapter = homePageAdapter;
        this.mNewsCellClickListener = newsCellClickListener;
    }

    //定义监听点击的接口
    public interface NewsCellClickListener {
        public void OnItemClick(View view,int postion);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    //新闻ViewHolder
    class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imgOne;
        private ImageView imgTwo;
        private ImageView imgThree;
        private TextView tvTitle;
        private TextView tvContent;

        public NewsHolder(View itemView) {
            super(itemView);
            imgOne = (ImageView) itemView.findViewById(R.id.imgOne);
            imgTwo = (ImageView) itemView.findViewById(R.id.imgTwo);
            imgThree = (ImageView) itemView.findViewById(R.id.imgThree);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
        }

        @Override
        public void onClick(View view) {
            if (mNewsCellClickListener != null) {
                mNewsCellClickListener.OnItemClick(view, (int) view.getTag());
            }
        }
    }

    //头视图ViewHolder
    class HeadHolder extends RecyclerView.ViewHolder {

        private ViewPager viewPager;

        public HeadHolder(View itemView) {
            super(itemView);
            viewPager = (ViewPager) itemView.findViewById(R.id.viewPager);
        }

        public ViewPager getViewPager() {
            return viewPager;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            HeadHolder headHolder = new HeadHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_header,null));
            return headHolder;
        }

        NewsCellModel newsModel = newsArray.get(viewType);
        if (newsModel.imgType != null) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_bigimg_cell, parent,false);
            NewsHolder newsHolder = new NewsHolder(view);
            //给itemView设置监听事件，监听对象为holder，holder中已经实现了onClick方法
            view.setOnClickListener(newsHolder);
            return newsHolder;
        }else if (newsModel.imgextra != null) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_mulimg_cell, parent,false);
            NewsHolder newsHolder = new NewsHolder(view);
            view.setOnClickListener(newsHolder);
            return newsHolder;
        }else  {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_cell, parent,false);
            NewsHolder newsHolder = new NewsHolder(view);
            view.setOnClickListener(newsHolder);
            return newsHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            HeadHolder headHolder = (HeadHolder) holder;
            headHolder.viewPager.setAdapter(homePageAdapter);
            return;
        }
        NewsHolder newsHolder = (NewsHolder) holder;
        if (newsHolder != null) {
            NewsCellModel newsModel = newsArray.get(position);
            newsHolder.itemView.setTag(position);
            App.getInstance().setImageViewUrl(newsHolder.imgOne,newsModel.imgsrc);
            newsHolder.tvTitle.setText(newsModel.title);
            if (newsModel.imgextra != null && newsModel.imgextra.size() == 2) {
                App.getInstance().setImageViewUrl(newsHolder.imgTwo,newsModel.imgextra.get(0).imgsrc);
                App.getInstance().setImageViewUrl(newsHolder.imgThree,newsModel.imgextra.get(1).imgsrc);
            }
            else {
                newsHolder.tvContent.setText(newsModel.digest);
            }
        }
    }

    @Override
    public int getItemCount() {
        return newsArray.size();
    }

}
