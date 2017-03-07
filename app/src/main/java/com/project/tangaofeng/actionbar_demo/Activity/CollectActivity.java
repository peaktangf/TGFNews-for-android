package com.project.tangaofeng.actionbar_demo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.tangaofeng.actionbar_demo.Adapter.CollectionAdapter;
import com.project.tangaofeng.actionbar_demo.Adapter.HomeMenuAdapter;
import com.project.tangaofeng.actionbar_demo.App;
import com.project.tangaofeng.actionbar_demo.Model.NewsCellModel;
import com.project.tangaofeng.actionbar_demo.Model.UserInfoModel;
import com.project.tangaofeng.actionbar_demo.R;
import com.project.tangaofeng.actionbar_demo.interf.ICallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CollectActivity extends BaseActivity {

    private Button btnBack;
    private TextView tvTitle;
    private Button btn1;
    private Button btn2;
    private ListView listView;
    private int curSelectBtn;
    private CollectionAdapter imgAdapter;
    private CollectionAdapter textAdapter;
    public ICallBack iCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        //绑定控件
        btnBack = (Button) findViewById(R.id.btnBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        listView = (ListView) findViewById(R.id.listView);

        //初始化
        curSelectBtn = 0;
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectActivity.this.finish();
            }
        });
        tvTitle.setText("本地收藏");

        imgAdapter = new CollectionAdapter(getUserInfoModel().photoCollects,true);
        textAdapter = new CollectionAdapter(getUserInfoModel().newsCollects,false);
        listView.setAdapter(textAdapter);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curSelectBtn == 1) {
                    btn1.setTextSize(15);
                    btn1.setTextColor(getResources().getColor(R.color.colorNavbar));
                    btn2.setTextSize(13);
                    btn2.setTextColor(getResources().getColor(R.color.colorNewsBodyText));
                    curSelectBtn = 0;
                    listView.setAdapter(textAdapter);
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curSelectBtn == 0) {
                    btn2.setTextSize(15);
                    btn2.setTextColor(getResources().getColor(R.color.colorNavbar));
                    btn1.setTextSize(13);
                    btn1.setTextColor(getResources().getColor(R.color.colorNewsBodyText));
                    curSelectBtn = 1;
                    listView.setAdapter(imgAdapter);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (curSelectBtn == 0) {
                    NewsCellModel model = getUserInfoModel().newsCollects.get(i);
                    Intent intent = new Intent();
                    //跳NewsDetails
                    intent.setClass(CollectActivity.this, NewsDetailsActivity.class);
                    intent.putExtra("newsModel",new Gson().toJson(model));
                    App.getInstance().activity = CollectActivity.this;
                    startActivity(intent);
                }else  {
                    NewsCellModel model = getUserInfoModel().photoCollects.get(i);
                    Intent intent = new Intent();
                    //跳PhotoDetials
                    intent.setClass(CollectActivity.this, PhotoDetailsActivity.class);
                    intent.putExtra("photoModel",new Gson().toJson(model));
                    App.getInstance().activity = CollectActivity.this;
                    startActivity(intent);
                }
            }
        });

        //回调
        iCallBack = new ICallBack() {
            @Override
            public void OnCallBack() {
                if (curSelectBtn == 0) {
                    textAdapter = new CollectionAdapter(getUserInfoModel().newsCollects,false);
                    listView.setAdapter(textAdapter);
                }else  {
                    imgAdapter = new CollectionAdapter(getUserInfoModel().photoCollects,true);
                    listView.setAdapter(imgAdapter);
                }
            }
        };
    }
}
