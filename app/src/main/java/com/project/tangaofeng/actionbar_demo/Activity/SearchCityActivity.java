package com.project.tangaofeng.actionbar_demo.Activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.tangaofeng.actionbar_demo.Adapter.HotCityAdapter;
import com.project.tangaofeng.actionbar_demo.Adapter.SearchListAdapter;
import com.project.tangaofeng.actionbar_demo.App;
import com.project.tangaofeng.actionbar_demo.Manager.LocationService;
import com.project.tangaofeng.actionbar_demo.Manager.NetWorking;
import com.project.tangaofeng.actionbar_demo.Manager.Util;
import com.project.tangaofeng.actionbar_demo.Model.UserInfoModel;
import com.project.tangaofeng.actionbar_demo.Model.Weather.SearchCityModel;
import com.project.tangaofeng.actionbar_demo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchCityActivity extends BaseActivity {

    private LinearLayout layoutInter;
    private TextView tvBottom;
    private TextView tvTop;
    private LinearLayout layoutSearch;
    private Button btnBack;
    private RecyclerView recHotCity;
    private ListView listViewSearch;
    private HotCityAdapter hotCityAdapter;
    private SearchListAdapter searchListAdapter;
    private RelativeLayout layoutHotBg;
    private EditText editSearch;
    private Button btnSearch;
    private ArrayList<SearchCityModel> searchCitys;
    private boolean isEdit = false;//是否正在输入
    private String[] hotCitys = {"定位","北京市","天津市","上海市","重庆市","沈阳市","大连市","长春市","哈尔滨市","郑州市","武汉市","长沙市","广州市","深圳市","南京市"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);

        //绑定控件以及初始化
        btnBack = (Button) findViewById(R.id.btnBack);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        editSearch = (EditText) findViewById(R.id.editSearch);
        recHotCity = (RecyclerView) findViewById(R.id.recHotCity);
        layoutInter = (LinearLayout) findViewById(R.id.layoutInter);
        tvBottom = (TextView) findViewById(R.id.tvBottom);
        tvTop = (TextView) findViewById(R.id.tvTop);
        layoutSearch = (LinearLayout) findViewById(R.id.layoutSearch);
        listViewSearch = (ListView) findViewById(R.id.listViewSearch);
        layoutHotBg = (RelativeLayout) findViewById(R.id.layoutHotBg);
        searchCitys = new ArrayList<SearchCityModel>();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchCityActivity.this.finish();
            }
        });

        //添加获得焦点和失去焦点的监听事件
        editSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    //获得焦点调用
                    translateAnimation(false);//执行动画
                    btnSearch.setText("取消");
                    isEdit = true;
                }else  {
                    //失去焦点调用
                    translateAnimation(true);//执行动画
                    btnSearch.setText("搜索");
                    isEdit = false;
                }
            }
        });

        //添加文本输入改变的监听事件
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                httpRequestCity(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit) {
                    //让editText失去焦点
                    editSearch.clearFocus();
                }else {
                    //让editText获得焦点
                    editSearch.requestFocus();//获取焦点 光标出现
                    //强制弹出键盘
                    InputMethodManager imm = (InputMethodManager) editSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
                }
            }
        });

        //设置recHotCity的布局样式（LinearLayoutManager线性布局,GridLayoutManager表格布局）
        recHotCity.setLayoutManager(new GridLayoutManager(this,3));//表格布局默认是垂直方向的，所以第二个参数默认是表示多少列，当设置为水平方向布局时，第二个参数就表示多少行
        recHotCity.post(new Runnable() {
            @Override
            public void run() {
                //初始化适配器
                hotCityAdapter = new HotCityAdapter(SearchCityActivity.this, recHotCity.getWidth(),hotCitys,getUserInfoModel().weatherCitys,new HotCityAdapter.ItemClickListener() {
                    @Override
                    public void OnClick(int postion) {
                        if (postion == 0) {
                            LocationService locationService = LocationService.shareLocation(getApplication());
                            locationService.startLocation(new LocationService.LocationCallBack() {
                                @Override
                                public void locationSuccess(BDLocation location) {
                                    if (location.getCity() != getUserInfoModel().locationCity) {
                                        UserInfoModel model = getUserInfoModel();
                                        model.locationCity = location.getCity();
                                        model.weatherCitys.set(0,location.getCity());
                                        sharedPreferencesUtils.setObject("user",model);
                                        //返回上一级
                                        setResult(1);
                                    }
                                }

                                @Override
                                public void locationFail(String errMsg) {
                                    Util.showToast(getApplication(),"定位失败");
                                }
                            });
                        }
                        else if (!getUserInfoModel().weatherCitys.contains(hotCitys[postion])) {
                            UserInfoModel model = getUserInfoModel();
                            model.weatherCitys.add(hotCitys[postion]);
                            sharedPreferencesUtils.setObject("user",model);
                            //返回上一级
                            setResult(1);
                        }
                        finish();
                    }
                });
                //设置适配器
                recHotCity.setAdapter(hotCityAdapter);
            }
        });

        //给listView的item添加点击事件
        listViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String selectCity = searchCitys.get(i).name_cn;
                if (!getUserInfoModel().weatherCitys.contains(selectCity)) {
                    UserInfoModel model = getUserInfoModel();
                    model.weatherCitys.add(selectCity);
                    sharedPreferencesUtils.setObject("user",model);
                    //返回上一级
                    setResult(1);
                }
                finish();
            }
        });
    }

    //移动动画
    private void translateAnimation(boolean isBottom) {

        if (isBottom) {
            btnBack.setAlpha(1);
            tvBottom.setAlpha(1);
            tvTop.setAlpha(1);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(layoutInter, "translationY", -370f, 0f);
            //给动画添加监听时间，结束时去做相应的事情
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    listViewSearch.setVisibility(View.INVISIBLE);
                    layoutHotBg.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            objectAnimator.setDuration(400).start();
            ObjectAnimator.ofFloat(layoutSearch,"translationY", 60f, 0f).setDuration(400).start();
        }else  {
            btnBack.setAlpha(0);
            tvBottom.setAlpha(0);
            tvTop.setAlpha(0);
            listViewSearch.setVisibility(View.VISIBLE);
            layoutHotBg.setVisibility(View.INVISIBLE);
            ObjectAnimator.ofFloat(layoutSearch,"translationY", 0f, 60f).setDuration(400).start();
            ObjectAnimator.ofFloat(layoutInter, "translationY", 0f, -370f).setDuration(400).start();
        }
    }

    private void refreshListHight() {
        int totalHeight = 0;
        for (int i = 0, len = searchListAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = searchListAdapter.getView(i, null, listViewSearch);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listViewSearch.getLayoutParams();
        params.height = totalHeight+ (listViewSearch.getDividerHeight() * (searchListAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listViewSearch.setLayoutParams(params);
    }

    private void httpRequestCity(final String cityName) {
        //http://apis.baidu.com/apistore/weatherservice/citylist?cityname=
        new Thread(){
            @Override
            public void run() {
                super.run();

                //清空searchCitys
                searchCitys.clear();
                NetWorking netWorking = new NetWorking();
                try {
                    String data = netWorking.requestNetWorking("http://apis.baidu.com/apistore/weatherservice/citylist?cityname="+cityName);
                    JSONObject jsonObject = new JSONObject(data);
                    String retData = jsonObject.getString("retData");

                    Gson gson = new Gson();
                    ArrayList<SearchCityModel> list = gson.fromJson(retData,new TypeToken<ArrayList<SearchCityModel>>(){}.getType());
                    searchCitys.addAll(list);
                    //通知刷新
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
                searchListAdapter = new SearchListAdapter(searchCitys);
                listViewSearch.setAdapter(searchListAdapter);
                //刷新高度
                refreshListHight();
            }
        }
    };

}
