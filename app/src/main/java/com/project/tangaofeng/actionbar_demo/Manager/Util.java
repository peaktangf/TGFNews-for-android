package com.project.tangaofeng.actionbar_demo.Manager;

import android.content.Context;
import android.widget.Toast;

import com.project.tangaofeng.actionbar_demo.R;

/**
 * Created by tangaofeng on 16/7/1.
 */
public class Util {

    //根据key获取对应的图片
    public static int weatherImageWithKey(String key) {

        int image;
        if (key.equals("雷阵雨")) {
            image = R.mipmap.thunder_mini;
        }else if (key.equals("晴")) {
            image = R.mipmap.sun_mini;
        }else if (key.equals("多云")) {
            image = R.mipmap.sun_and_cloud_mini;
        }else if (key.equals("阴")) {
            image = R.mipmap.nosun_mini;
        }else if (key.equals("雨")) {
            image = R.mipmap.rain_mini;
        }else if (key.equals("雪")) {
            image = R.mipmap.snow_heavyx_mini;
        }else {
            image = R.mipmap.sand_float_mini;
        }
        return image;
    }

    //获取字体大小
    public static String fontSizeWithFontMark(String mark) {
        String fontSize;
        if (mark.equals("小")) {
            fontSize = "17";
        }
        else if (mark.equals("中")) {
            fontSize = "19";
        }
        else if (mark.equals("大")) {
            fontSize = "21";
        }
        else if (mark.equals("超大")) {
            fontSize = "23";
        }
        else if (mark.equals("巨大")) {
            fontSize = "25";
        }
        else {
            fontSize = "27";
        }
        return fontSize;
    }

    //通用Toast
    public static void showToast(Context context,String str) {
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }

}
