package com.project.tangaofeng.actionbar_demo.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.project.tangaofeng.actionbar_demo.R;

public class AboutActivity extends AppCompatActivity {

    private Button btnBack;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //绑定控件
        btnBack = (Button) findViewById(R.id.btnBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutActivity.this.finish();
            }
        });
        tvTitle.setText("关于看呀");
    }
}
