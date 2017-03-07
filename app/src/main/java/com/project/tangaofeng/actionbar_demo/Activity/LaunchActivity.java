package com.project.tangaofeng.actionbar_demo.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.project.tangaofeng.actionbar_demo.R;

import java.util.Timer;
import java.util.TimerTask;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        final Timer timer = new Timer(true);
        //开启一个定时器
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //结束定时器
                timer.cancel();
                Intent intent = new Intent();
                intent.setClass(LaunchActivity.this,MainActivity.class);
                startActivity(intent);
                //不加这一句，按退回键就会回到欢迎页，不合理
                finish();
            }
        }, 1500);
    }
}
