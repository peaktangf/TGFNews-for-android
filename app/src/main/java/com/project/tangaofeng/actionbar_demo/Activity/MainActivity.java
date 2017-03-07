package com.project.tangaofeng.actionbar_demo.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.project.tangaofeng.actionbar_demo.Adapter.HomeMenuAdapter;
import com.project.tangaofeng.actionbar_demo.Adapter.NewsPagerAdapter;
import com.project.tangaofeng.actionbar_demo.Fragment.NewsFragment;
import com.project.tangaofeng.actionbar_demo.Fragment.WeatherFragment;
import com.project.tangaofeng.actionbar_demo.Manager.DialogManager;
import com.project.tangaofeng.actionbar_demo.Manager.LocationService;
import com.project.tangaofeng.actionbar_demo.Manager.Util;
import com.project.tangaofeng.actionbar_demo.Model.UserInfoModel;
import com.project.tangaofeng.actionbar_demo.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    final String[] newsTypes = {"T1348647853363","T1348649145984","T1348649654285","T1351233117091","T1348648517839","T1348650593803","T1348648650048","T1348649580692"};

    private Button btnLeft;
    private Button btnRight;
    private TextView tvTitle;
    private DrawerLayout drawerLayout;
    private LinearLayout drawer_view;
    private RecyclerView rceMenu;
    private HomeMenuAdapter homeMenuAdapter;
    private ViewPager vPagerNews;
    private NewsPagerAdapter newsPagerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private WeatherFragment weatherFragment;
    private FragmentManager fragmentManager;

    //侧边栏控件
    private Button btnCollect;
    private Button btnWeather;
    private LinearLayout layoutWorld;
    public TextView tvWorldSize;
    private Switch swNight;
    private LinearLayout layoutChach;
    private TextView tvChach;
    private Button btnAbout;

    private boolean isFrist = true;//是否第一次进入界面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //绑定控件
        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnRight = (Button) findViewById(R.id.btnRight);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawer_view = (LinearLayout) findViewById(R.id.drawer_view);
        rceMenu = (RecyclerView) findViewById(R.id.rcvMenu);
        vPagerNews = (ViewPager) findViewById(R.id.vPagerNews);

        btnCollect = (Button) findViewById(R.id.btnCollect);
        btnWeather = (Button) findViewById(R.id.btnWeather);
        btnAbout = (Button) findViewById(R.id.btnAbout);
        layoutWorld = (LinearLayout) findViewById(R.id.layoutWorld);
        tvWorldSize = (TextView) findViewById(R.id.tvWorldSize);
        swNight = (Switch) findViewById(R.id.swichNight);
        layoutChach = (LinearLayout) findViewById(R.id.layoutChach);
        tvChach = (TextView) findViewById(R.id.tvChach);

        //第一次进入app时进行定位操作
        if (!getUserInfoModel().isFirst) {
            UserInfoModel model = getUserInfoModel();
            model.isFirst = true;
            model.newsFont = "小";
            sharedPreferencesUtils.setObject("user",model);

            LocationService locationService = LocationService.shareLocation(getApplication());
            locationService.startLocation(new LocationService.LocationCallBack() {
                @Override
                public void locationSuccess(final BDLocation location) {
                    System.out.println(location.getCity());

                    //定位成功进行提示
                    DialogManager.shareDialog().showDialog(MainActivity.this, "温馨提示", "定位到当前城市为 " + location.getCity() + "是否切换至当前城市", new DialogManager.MyOnClickListener() {
                        @Override
                        public void sureClick() {
                            UserInfoModel model = getUserInfoModel();
                            model.locationCity = location.getCity();
                            model.weatherCitys.add(location.getCity());
                            sharedPreferencesUtils.   setObject("user",model);
                        }

                        @Override
                        public void cancelClick() {
                            UserInfoModel model = getUserInfoModel();
                            model.locationCity = "北京市";
                            model.weatherCitys.add("北京市");
                            sharedPreferencesUtils.setObject("user",model);
                        }
                    });
                }

                @Override
                public void locationFail(String errMsg) {
                    UserInfoModel model = getUserInfoModel();
                    model.locationCity = "北京市";
                    model.weatherCitys.add("北京市");
                    sharedPreferencesUtils.setObject("user",model);
                    Util.showToast(getApplication(),errMsg);
                }
            });
        }

        //设置天气界面
        weatherFragment = new WeatherFragment();
        fragmentManager = getSupportFragmentManager();

        //配置侧边栏
        configureLeft();

        homeMenuAdapter = new HomeMenuAdapter(this, new HomeMenuAdapter.OnClickCallBack() {
            @Override
            public void callBack(int postion) {
                linearLayoutManager.scrollToPositionWithOffset(postion,0);
                //菜单点击的回调事件
                vPagerNews.setCurrentItem(postion);
            }
        });

        //显示侧边栏
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置当前字体大小
                tvWorldSize.setText(getUserInfoModel().newsFont);
                drawerLayout.openDrawer(drawer_view);
            }
        });

        //显示或隐藏天气页面
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isFrist) {
                    fragmentManager.beginTransaction().add(R.id.fragmentWeather,weatherFragment).commit();
                    isFrist = false;
                }
                else  {
                    if (weatherFragment.isHidden()) {
                        fragmentManager.beginTransaction().show(weatherFragment).commit();
                    }else  {
                        fragmentManager.beginTransaction().hide(weatherFragment).commit();
                    }
                }

            }
        });

        //设置菜单项
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rceMenu.setLayoutManager(linearLayoutManager);
        rceMenu.setAdapter(homeMenuAdapter);

        //设置新闻ViewPager页面
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        for(int i = 0;i < newsTypes.length;i ++) {
            NewsFragment newsFragment = new NewsFragment();
            newsFragment.newsType = newsTypes[i];
            fragments.add(newsFragment);
        }
        newsPagerAdapter = new NewsPagerAdapter(getSupportFragmentManager(),fragments);
        vPagerNews.setOffscreenPageLimit(1);
        //添加监听事件
        vPagerNews.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                linearLayoutManager.scrollToPositionWithOffset(position,0);
                homeMenuAdapter.curSelectMenuIndex = position;
                homeMenuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vPagerNews.setAdapter(newsPagerAdapter);

    }

    //设置侧边栏
    private void configureLeft() {

        //拦截侧边栏的手势
        drawer_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        //点击本地收藏
        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,CollectActivity.class);
                startActivityForResult(intent,1);
            }
        });

        //点击天气城市
        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,WeatherCityActivity.class);
                startActivity(intent);
            }
        });

        //设置当前字体大小
        tvWorldSize.setText(getUserInfoModel().newsFont);
        //点击字号设置
        layoutWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,WordSetActivity.class);
                startActivityForResult(intent,1);
            }
        });

        //切换夜间模式

        //清除缓存
        layoutChach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"清除缓存",Toast.LENGTH_SHORT).show();
            }
        });

        //关于看呀
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    //回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //设置当前字体大小
            tvWorldSize.setText(getUserInfoModel().newsFont);
        }
    }
}
