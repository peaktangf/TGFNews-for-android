package com.project.tangaofeng.actionbar_demo.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.project.tangaofeng.actionbar_demo.Adapter.WeatherCityAdapter;
import com.project.tangaofeng.actionbar_demo.Model.UserInfoModel;
import com.project.tangaofeng.actionbar_demo.R;

public class WeatherCityActivity extends BaseActivity {

    private Button btnBack;
    private TextView tvTitle;
    private Button btnRight;
    private ListView listView;
    private boolean isBack;
    private WeatherCityAdapter weatherCityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_city);

        //绑定控件
        btnBack = (Button) findViewById(R.id.btnBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        btnRight = (Button) findViewById(R.id.btnRight);
        btnRight.setBackgroundResource(R.mipmap.add);
        listView = (ListView) findViewById(R.id.listView);

        //获取是否是从天气页面跳转过来的
        isBack = getIntent().getBooleanExtra("isBack",false);

        //初始化
        weatherCityAdapter = new WeatherCityAdapter(getUserInfoModel().weatherCitys,getUserInfoModel().locationCity);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeatherCityActivity.this.finish();
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(WeatherCityActivity.this,SearchCityActivity.class);
                startActivityForResult(intent,1);
            }
        });
        tvTitle.setText("天气城市");

        listView.setAdapter(weatherCityAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isBack) {
                    Intent intent = new Intent();
                    intent.putExtra("city",getUserInfoModel().weatherCitys.get(i));
                    setResult(1,intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            weatherCityAdapter = new WeatherCityAdapter(getUserInfoModel().weatherCitys,getUserInfoModel().locationCity);
            listView.setAdapter(weatherCityAdapter);
        }
    }
}
