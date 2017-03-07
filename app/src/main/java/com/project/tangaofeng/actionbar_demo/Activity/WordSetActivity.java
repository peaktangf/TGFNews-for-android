package com.project.tangaofeng.actionbar_demo.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.project.tangaofeng.actionbar_demo.Adapter.WordSetAdapter;
import com.project.tangaofeng.actionbar_demo.App;
import com.project.tangaofeng.actionbar_demo.Model.UserInfoModel;
import com.project.tangaofeng.actionbar_demo.R;

public class WordSetActivity extends BaseActivity {

    private Button btnBack;
    private TextView tvTitle;
    private ListView listView;
    private WordSetAdapter wordSetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_set);

        //绑定控件
        btnBack = (Button) findViewById(R.id.btnBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        listView = (ListView) findViewById(R.id.listView);

        //初始化数据
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WordSetActivity.this.finish();
            }
        });
        tvTitle.setText("字号设置");
        if (getUserInfoModel().newsFont != null) {
            int position = App.getInstance().newsFonts.indexOf(getUserInfoModel().newsFont);
            wordSetAdapter = new WordSetAdapter(position);
        }else  {
            wordSetAdapter = new WordSetAdapter(0);
        }
        listView.setAdapter(wordSetAdapter);

        //listView的item添加点击时间
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserInfoModel model = getUserInfoModel();
                model.newsFont = App.getInstance().newsFonts.get(i);
                sharedPreferencesUtils.setObject("user",model);
                wordSetAdapter = new WordSetAdapter(i);
                listView.setAdapter(wordSetAdapter);
            }
        });
    }
}
