package com.project.tangaofeng.actionbar_demo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.project.tangaofeng.actionbar_demo.Activity.MainActivity;
import com.project.tangaofeng.actionbar_demo.Activity.NewsDetailsActivity;
import com.project.tangaofeng.actionbar_demo.Activity.PhotoDetailsActivity;
import com.project.tangaofeng.actionbar_demo.Adapter.HomePageAdapter;
import com.project.tangaofeng.actionbar_demo.Adapter.NewsRecyAdapter;
import com.project.tangaofeng.actionbar_demo.App;
import com.project.tangaofeng.actionbar_demo.DividerItemDecoration;
import com.project.tangaofeng.actionbar_demo.Manager.NetWorking;
import com.project.tangaofeng.actionbar_demo.Model.AdsModel;
import com.project.tangaofeng.actionbar_demo.Model.NewsCellModel;
import com.project.tangaofeng.actionbar_demo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends Fragment {

    private String pullDown = "PullDown";//下拉
    private String pullUp = "PullUp";//上拉
    public  String newsType = "T1348647853363";//当前新闻类型

    private View rootView;
    private XRecyclerView rceNews;

    private NewsRecyAdapter newsRecyAdapter;
    private HomePageAdapter homePageAdapter;

    private List<View> views;
    private List<AdsModel> adsArray;
    private List<NewsCellModel> newsArray;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_news,container,false);
            rceNews = (XRecyclerView) rootView.findViewById(R.id.rcvNews);

            //初始化数据
            views = new ArrayList<View>();
            adsArray = new ArrayList<AdsModel>();
            newsArray = new ArrayList<NewsCellModel>();

            homePageAdapter = new HomePageAdapter(views);
            newsRecyAdapter = new NewsRecyAdapter(newsArray, homePageAdapter, new NewsRecyAdapter.NewsCellClickListener() {
                @Override
                public void OnItemClick(View view, int postion) {

                    NewsCellModel model = newsArray.get(postion);

                    if ("photoset".equals(model.skipType)) {
                        Intent intent = new Intent();
                        //跳PhotoDetials
                        intent.setClass(getActivity(), PhotoDetailsActivity.class);
                        intent.putExtra("photoModel",new Gson().toJson(model));
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent();
                        //跳NewsDetails
                        intent.setClass(getActivity(), NewsDetailsActivity.class);
                        intent.putExtra("newsModel",new Gson().toJson(model));
                        startActivityForResult(intent,1);
                    }
                }
            });

            //设置新闻列表
            rceNews.setLayoutManager(new LinearLayoutManager(getActivity()));
            //添加分割线
            rceNews.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
            //设置适配器
            rceNews.setAdapter(newsRecyAdapter);

            //设置上拉加载
            rceNews.setLoadingListener(new XRecyclerView.LoadingListener() {
                @Override
                public void onRefresh() {
                    newsArray.clear();
                    httpRequest(pullDown);
                }

                @Override
                public void onLoadMore() {
                    httpRequest(pullUp);
                }
            });

            //第一次进入页面自动进行一次刷新
            rceNews.setRefreshing(true);
        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //要销户之前把跟视图从viewpager中移除，然后在onCreateView中复用
        if (rootView != null){
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }

    //处理通知消息
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x123){

                views.clear();

                NewsCellModel newsCellModel = newsArray.get(0);
                AdsModel adsModel = new AdsModel();
                adsModel.title = newsCellModel.title;
                adsModel.imgsrc = newsCellModel.imgsrc;
                adsModel.tag = newsCellModel.skipType;
                if (newsCellModel.photosetID != null) {
                    adsModel.url = newsCellModel.photosetID;
                }
                else  {
                    adsModel.url = newsCellModel.docid;
                }
                adsArray.add(0,adsModel);

                for (int i = 0;i < adsArray.size();i ++) {

                    AdsModel model = adsArray.get(i);

                    final View v = getActivity().getLayoutInflater().inflate(R.layout.page_view,null);
                    RelativeLayout pageView = (RelativeLayout) v;
                    ImageView imgView = (ImageView) pageView.findViewById(R.id.imgPage);
                    TextView textView = (TextView) pageView.findViewById(R.id.tvPageTitle);

                    App.getInstance().setImageViewUrl(imgView,model.imgsrc);
                    textView.setText(model.title);
                    v.setTag(i);

                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AdsModel adsModel = adsArray.get((Integer) v.getTag());
                            NewsCellModel newsCellModel = new NewsCellModel();
                            newsCellModel.title = adsModel.title;
                            newsCellModel.imgsrc = adsModel.imgsrc;
                            newsCellModel.skipType = adsModel.tag;
                            newsCellModel.photosetID = adsModel.url;
                            newsCellModel.docid = adsModel.url;
                            if ("photoset".equals(newsCellModel.skipType)) {
                                Intent intent = new Intent();
                                //跳PhotoDetials
                                intent.setClass(getActivity(), PhotoDetailsActivity.class);
                                intent.putExtra("photoModel",new Gson().toJson(newsCellModel));
                                startActivity(intent);
                            }
                            else  {
                                Intent intent = new Intent();
                                //跳NewsDetails
                                intent.setClass(getActivity(), NewsDetailsActivity.class);
                                intent.putExtra("newsModel",new Gson().toJson(newsCellModel));
                                startActivityForResult(intent,1);
                            }
                        }
                    });
                    views.add(v);
                }

                //完成刷新
                rceNews.refreshComplete();
                rceNews.loadMoreComplete();
                homePageAdapter.notifyDataSetChanged();
                newsRecyAdapter.notifyDataSetChanged();
            }

            if (msg.what == 0x234) {
                rceNews.loadMoreComplete();
                newsRecyAdapter.notifyDataSetChanged();
            }
        }
    };

    //加载网络数据
    private void httpRequest(final String refreshType){
        new Thread(){
            @Override
            public void run() {
                super.run();
                NetWorking netWorking = new NetWorking();
                try {

                    if (refreshType == pullDown) {

                        String data;
                        if (newsType == "T1348647853363") {
                            data = netWorking.requestNetWorking("http://c.m.163.com/nc/article/headline/"+newsType+"/0-20.html");
                        }
                        else  {
                            data = netWorking.requestNetWorking("http://c.m.163.com/nc/article/list/"+newsType+"/0-20.html");
                        }

                        JSONObject jsonObject = new JSONObject(data);
                        String contentStr = jsonObject.getString(newsType);

                        Gson gson = new Gson();
                        List<NewsCellModel> list = gson.fromJson(contentStr,new TypeToken<List<NewsCellModel>>(){}.getType());
                        newsArray.addAll(list);
                        adsArray.clear();
                        if (newsArray.get(0).ads != null) {
                            adsArray = newsArray.get(0).ads;
                        }
                        //下拉刷新
                        handler.sendEmptyMessage(0x123);
                    }
                    else  {

                        String data;
                        if (newsType == "T1348647853363") {
                            data = netWorking.requestNetWorking("http://c.m.163.com/nc/article/headline/"+newsType+"/"+(newsArray.size() - newsArray.size() % 10)+"-20.html");
                        }
                        else  {
                            data = netWorking.requestNetWorking("http://c.m.163.com/nc/article/list/"+newsType+"/"+(newsArray.size() - newsArray.size() % 10)+"-20.html");
                        }

                        JSONObject jsonObject = new JSONObject(data);
                        String contentStr = jsonObject.getString(newsType);

                        Gson gson = new Gson();
                        List<NewsCellModel> list = gson.fromJson(contentStr,new TypeToken<List<NewsCellModel>>(){}.getType());
                        newsArray.addAll(list);
                        //上拉刷新
                        handler.sendEmptyMessage(0x234);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            MainActivity activity = (MainActivity) getActivity();
            activity.tvWorldSize.setText(activity.getUserInfoModel().newsFont);
        }
    }
}
