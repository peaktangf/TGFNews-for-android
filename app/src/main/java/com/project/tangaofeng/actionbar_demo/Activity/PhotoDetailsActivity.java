package com.project.tangaofeng.actionbar_demo.Activity;

import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.project.tangaofeng.actionbar_demo.App;
import com.project.tangaofeng.actionbar_demo.Manager.NetWorking;
import com.project.tangaofeng.actionbar_demo.Manager.Util;
import com.project.tangaofeng.actionbar_demo.Model.NewsCellModel;
import com.project.tangaofeng.actionbar_demo.Model.NewsDetailsModel;
import com.project.tangaofeng.actionbar_demo.Model.PhotoSetDetailsModel;
import com.project.tangaofeng.actionbar_demo.Model.UserInfoModel;
import com.project.tangaofeng.actionbar_demo.R;
import com.project.tangaofeng.actionbar_demo.interf.ICallBack;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoDetailsActivity extends BaseActivity{

    private Button btnBack;
    private ViewPager viewPagerImg;
    private TextView tvTitle;
    private TextView tvCount;
    private TextView tvContent;
    private Button btnCollect;
    private Button btnShare;
    private List<View> views;
    private NewsCellModel newsCellModel;
    private List<PhotoSetDetailsModel> photoSets;
    public ICallBack iCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);

        //初始化数据
        String modelJson = getIntent().getStringExtra("photoModel");
        newsCellModel = new Gson().fromJson(modelJson, NewsCellModel.class);
        views = new ArrayList<View>();
        photoSets = new ArrayList<PhotoSetDetailsModel>();

        //绑定控件
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoDetailsActivity.this.finish();
            }
        });
        viewPagerImg = (ViewPager) findViewById(R.id.viewPagerImg);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvCount = (TextView) findViewById(R.id.tvCount);
        tvContent = (TextView) findViewById(R.id.tvContent);
        btnCollect = (Button) findViewById(R.id.btnCollect);
        //设置收藏按钮的状态，是否被收藏
        if (getUserInfoModel().photoCollects.contains(newsCellModel)) {
            btnCollect.setBackgroundResource(R.mipmap.btn_yet_collect);
        }else  {
            btnCollect.setBackgroundResource(R.mipmap.btn_collect);
        }

        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getUserInfoModel().photoCollects.contains(newsCellModel)) {
                    //取消收藏
                    UserInfoModel userInfoModel = getUserInfoModel();
                    userInfoModel.photoCollects.remove(newsCellModel);
                    sharedPreferencesUtils.setObject("user", userInfoModel);
                    Util.showToast(getApplication(), "已取消收藏");
                    btnCollect.setBackgroundResource(R.mipmap.btn_collect);
                    //回调，让上级页面刷新
                    if (App.getInstance().activity != null) {
                        iCallBack = App.getInstance().activity.iCallBack;
                        if (iCallBack != null) {
                            iCallBack.OnCallBack();
                        }
                    }
                } else {
                    //收藏
                    UserInfoModel userInfoModel = getUserInfoModel();
                    userInfoModel.photoCollects.add(newsCellModel);
                    sharedPreferencesUtils.setObject("user", userInfoModel);
                    Util.showToast(getApplication(), "收藏成功");
                    btnCollect.setBackgroundResource(R.mipmap.btn_yet_collect);
                    //回调，让上级页面刷新
                    if (App.getInstance().activity != null) {
                        iCallBack = App.getInstance().activity.iCallBack;
                        if (iCallBack != null) {
                            iCallBack.OnCallBack();
                        }
                    }
                }
            }
        });
        btnShare = (Button) findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.showToast(getApplication(),"点击了分享");
            }
        });


        //取出关键字
        String one = newsCellModel.photosetID;
        String two = one.substring(4, one.length());
        String three[] = two.split("\\|");

        //获得url
        final String url = String.format("http://c.m.163.com/photo/api/set/%s/%s.json", three[0], three[1]);
        //进行网络请求
        new Thread() {
            @Override
            public void run() {
                super.run();
                NetWorking netWorking = new NetWorking();
                try {
                    String data = netWorking.requestNetWorking(url);
                    JSONObject jsonObject = new JSONObject(data);
                    String dataJson = jsonObject.getString("photos");

                    Gson gson = new Gson();
                    List<PhotoSetDetailsModel> list = gson.fromJson(dataJson, new TypeToken<List<PhotoSetDetailsModel>>() {
                    }.getType());
                    photoSets.addAll(list);
                    handler.sendEmptyMessage(0x123);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                //设置ui
                configureUI();
            }
        }
    };

    //初始化ui
    private void configureUI() {

        PhotoSetDetailsModel model = photoSets.get(0);

        tvTitle.setText(newsCellModel.title);
        tvContent.setText(model.note);
        tvCount.setText("1/"+photoSets.size());

        for (int i = 0;i < photoSets.size();i ++) {
            PhotoSetDetailsModel model1 = photoSets.get(i);

            View v = this.getLayoutInflater().inflate(R.layout.photoset_page,null);
            LinearLayout imgPageView = (LinearLayout) v;
            ImageView imageView = (ImageView) imgPageView.findViewById(R.id.imgPage);
            App.getInstance().setImageViewUrl(imageView,model1.imgurl);
            views.add(v);
        }
        //设置ViewPager适配器
        viewPagerImg.setAdapter(new PagerAdapter() {
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

        });
        //viewpager滑动监听事件
        viewPagerImg.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                PhotoSetDetailsModel model = photoSets.get(position);
                tvContent.setText(model.note);
                tvCount.setText((position + 1)+"/"+photoSets.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
