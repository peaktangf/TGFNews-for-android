package com.project.tangaofeng.actionbar_demo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.tangaofeng.actionbar_demo.Activity.MainActivity;
import com.project.tangaofeng.actionbar_demo.Activity.WeatherCityActivity;
import com.project.tangaofeng.actionbar_demo.Adapter.RecWeatherAdapter;
import com.project.tangaofeng.actionbar_demo.Manager.NetWorking;
import com.project.tangaofeng.actionbar_demo.Manager.Util;
import com.project.tangaofeng.actionbar_demo.Model.Weather.WeatherModel;
import com.project.tangaofeng.actionbar_demo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WeatherFragment extends Fragment {

    private TextView tvTemp;
    private TextView tvTempUnit;
    private TextView tvHighAndLow;
    private TextView tvDate;
    private TextView tvPm;
    private ImageView imgMark;
    private TextView tvDesc;
    private TextView tvCity;
    private Button btnSwitchCity;
    private RecyclerView recWeather;
    private RecWeatherAdapter recWeatherAdapter;
    private WeatherModel weatherModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_weather,container,false);
        tvTemp = (TextView) rootView.findViewById(R.id.tvTemp);
        tvTempUnit = (TextView) rootView.findViewById(R.id.tvTempUnit);
        tvHighAndLow = (TextView) rootView.findViewById(R.id.tvHighAndLow);
        tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        tvPm = (TextView) rootView.findViewById(R.id.tvPm);
        imgMark = (ImageView) rootView.findViewById(R.id.imgMark);
        tvDesc = (TextView) rootView.findViewById(R.id.tvDesc);
        tvCity = (TextView) rootView.findViewById(R.id.tvCity);
        btnSwitchCity = (Button) rootView.findViewById(R.id.switchCity);
        btnSwitchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), WeatherCityActivity.class);
                //startActivity(intent);
                intent.putExtra("isBack",true);
                startActivityForResult(intent,1);
            }
        });
        //添加下拉刷新
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorNavbar);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //请求天气数据
                swipeRefreshLayout.setRefreshing(true);
                httpRequestWeather(((MainActivity) getActivity()).getUserInfoModel().locationCity);
            }
        });
        RefreshLoading(((MainActivity) getActivity()).getUserInfoModel().locationCity);

        recWeather = (RecyclerView) rootView.findViewById(R.id.recWeather);
        //拦截RecyclerView的手势，使其不能响应触碰事件
        recWeather.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        //拦截rootView的手势，原理同上
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        return rootView;
    }

    private void RefreshLoading(final String cityName) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //第一次进入时进行刷新请求
                swipeRefreshLayout.setRefreshing(true);
                httpRequestWeather(cityName);
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //设置界面数据
            if (msg.what == 0x123) {
                tvTemp.setText(weatherModel.today.curTemp.split("℃")[0]);
                tvCity.setText(weatherModel.city);
                tvDate.setText(weatherModel.today.week + " " + weatherModel.today.date);
                tvPm.setText("PM "+weatherModel.today.aqi);
                tvHighAndLow.setText(weatherModel.today.hightemp+"/"+weatherModel.today.lowtemp);
                tvDesc.setText(weatherModel.today.type + " " + weatherModel.today.fengli);
                imgMark.setImageResource(Util.weatherImageWithKey(weatherModel.today.type));

                recWeatherAdapter = new RecWeatherAdapter(recWeather.getWidth(),recWeather.getHeight(),weatherModel.forecast);
                //给RecyclerView设置布局方式
                recWeather.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                //设置适配器
                recWeather.setAdapter(recWeatherAdapter);
                //结束刷新
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    private void httpRequestWeather(final String cityName) {

        new Thread() {
            @Override
            public void run() {
                super.run();

                NetWorking netWorking = new NetWorking();
                String utfCityName = null;
                try {
                    String newCityName = cityName.replace("市","");
                    utfCityName = URLEncoder.encode(newCityName, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String url = String.format("http://apis.baidu.com/apistore/weatherservice/recentweathers?cityname=%s",utfCityName);
                //请求数据
                try {
                    String data = netWorking.requestNetWorking(url);
                    JSONObject jsonData = new JSONObject(data);
                    weatherModel = new WeatherModel(jsonData.getJSONObject("retData"));
                    handler.sendEmptyMessage(0x123);

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
        if (resultCode == 1 && requestCode == 1) {
            RefreshLoading(data.getStringExtra("city"));
        }
    }
}
