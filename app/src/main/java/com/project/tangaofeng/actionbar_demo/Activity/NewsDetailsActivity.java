package com.project.tangaofeng.actionbar_demo.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.project.tangaofeng.actionbar_demo.App;
import com.project.tangaofeng.actionbar_demo.Manager.NetWorking;
import com.project.tangaofeng.actionbar_demo.Manager.Util;
import com.project.tangaofeng.actionbar_demo.Model.NewsCellModel;
import com.project.tangaofeng.actionbar_demo.Model.NewsDetailsImgModel;
import com.project.tangaofeng.actionbar_demo.Model.NewsDetailsModel;
import com.project.tangaofeng.actionbar_demo.Model.UserInfoModel;
import com.project.tangaofeng.actionbar_demo.R;
import com.project.tangaofeng.actionbar_demo.interf.ICallBack;

import org.json.JSONObject;

public class NewsDetailsActivity extends BaseActivity {

    private WebView webView;
    private Button btnBack;
    private Button btnCollect;
    private Button btnShare;
    private Button btnFont;
    private NewsDetailsModel newsDetailsModel;
    private NewsCellModel newsCellModel;
    public ICallBack iCallBack;
    private OptionsPickerView pvOptions;//选项选择器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        //初始化数据
        String modelJson = getIntent().getStringExtra("newsModel");
        newsCellModel = new Gson().fromJson(modelJson, NewsCellModel.class);
        pvOptions = new OptionsPickerView(this);
        //设置pickerView的数据
        pvOptions.setPicker(App.getInstance().newsFonts);
        //设置是否可循环滚动
        pvOptions.setCyclic(false);
        //设置点击背景dismis
        pvOptions.setCancelable(true);

        //绑定控件
        webView = (WebView) findViewById(R.id.webViewNews);
        //设置webView可以和js交互
        webView.getSettings().setJavaScriptEnabled(true);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsDetailsActivity.this.finish();
            }
        });
        btnCollect = (Button) findViewById(R.id.btnCollect);
        //设置收藏按钮的状态，是否被收藏
        if (getUserInfoModel().newsCollects.contains(newsCellModel)) {
            btnCollect.setBackgroundResource(R.mipmap.btn_yet_collect);
        }else  {
            btnCollect.setBackgroundResource(R.mipmap.btn_collect);
        }

        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getUserInfoModel().newsCollects.contains(newsCellModel)) {
                    //取消收藏
                    UserInfoModel userInfoModel = getUserInfoModel();
                    userInfoModel.newsCollects.remove(newsCellModel);
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
                    userInfoModel.newsCollects.add(newsCellModel);
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
                Toast.makeText(getApplication(),"点击分享",Toast.LENGTH_SHORT).show();
            }
        });
        btnFont = (Button) findViewById(R.id.btnFont);
        btnFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getUserInfoModel().newsFont != null) {
                    pvOptions.setSelectOptions(App.getInstance().newsFonts.indexOf(getUserInfoModel().newsFont));
                }else  {
                    pvOptions.setSelectOptions(0);
                }

                pvOptions.show();
            }
        });
        //点击确认的监听事件
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                UserInfoModel model = getUserInfoModel();
                model.newsFont = App.getInstance().newsFonts.get(options1);
                sharedPreferencesUtils.setObject("user",model);
                //用js改变webVeiw字体大小
                webView.evaluateJavascript(String.format("document.getElementsByTagName('body')[0].style.fontSize = '%spx'",Util.fontSizeWithFontMark(App.getInstance().newsFonts.get(options1))), null);
            }
        });

        //请求数据
        httpRequestNewsDetails();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0x345) {
                //刷新网页
                showInWebView();
            }
        }
    };


    private void httpRequestNewsDetails() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                NetWorking netWorking = new NetWorking();
                try {
                    String data = netWorking.requestNetWorking("http://c.m.163.com/nc/article/"+newsCellModel.docid+"/full.html");

                    JSONObject jsonObject = new JSONObject(data);
                    String docidStr = jsonObject.getString(newsCellModel.docid);

                    Gson gson = new Gson();
                    newsDetailsModel = gson.fromJson(docidStr,NewsDetailsModel.class);

                    //发送通知
                    handler.sendEmptyMessage(0x345);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void showInWebView() {
        String html = new String("");
        html +="<html><head>";
        html += "<style>";
        html += String.format(".title{text-align:left;font-size:24px;font-weight:bold;color:black;} .time{text-align:left;font-size:15px;color:gray;margin-top:7px;margin-bottom:7px;} body{font-size:%spx;color:black;background-color:white;} .img-parent{text-align:center;margin-bottom:10px;}",Util.fontSizeWithFontMark(getUserInfoModel().newsFont));
        html += "</style>";
        html += "</head>";
        html += "<body>";
        html += touchBody();
        html += "</body>";
        html += "</html>";
        //加载网页
        webView.loadData(html, "text/html; charset=UTF-8", null);
    }

    private String touchBody() {

        String body = new String("");
        body += String.format("<div class=\"title\">%s</div>",newsDetailsModel.title);
        body += String.format("<div class=\"time\">%s</div>",newsDetailsModel.ptime);
        if (newsDetailsModel.body != null) {
            body += String.format("<div class=\"body\">%s</div>",newsDetailsModel.body);
        }

        for (int i = 0;i < newsDetailsModel.img.size();i ++) {
            NewsDetailsImgModel imgModel = newsDetailsModel.img.get(i);

            String imgHtml = new String("");
            imgHtml += "<div class=\"img-parent\">";

            String[] pixel = imgModel.pixel.split("\\*");

            float width = Float.parseFloat(pixel[0]);
            float height = Float.parseFloat(pixel[1]);
            //获取屏幕宽度
            Resources resources = this.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            //判断是否超过最大宽度
            float maxWidth = (float) (dm.widthPixels / 3 * 0.96);
            if (width > maxWidth) {
                height = maxWidth / width * height;
                width = maxWidth;
            }

            imgHtml += String.format("<img width=\"%f\" height=\"%f\" src=\"%s\">",width,height,imgModel.src);
            //结束标记
            imgHtml += "</div>";
            //替换标记
            body = body.replace(imgModel.ref,imgHtml);
        }
        return body;
    }
}
