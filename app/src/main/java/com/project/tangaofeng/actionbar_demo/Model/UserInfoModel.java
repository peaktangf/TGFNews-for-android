package com.project.tangaofeng.actionbar_demo.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangaofeng on 16/7/5.
 */
public class UserInfoModel implements Serializable{

    public boolean isFirst;//是否第一次进入程序
    public String newsFont;//当前新闻字体大小
    public String locationCity;//当前定位到的城市
    public boolean isNight;//是否是夜间模式
    public ArrayList<String> weatherCitys;//天气城市集合
    public ArrayList<NewsCellModel> photoCollects;//收藏的图片新闻
    public ArrayList<NewsCellModel> newsCollects;//收藏的文字新闻呢

    public UserInfoModel() {
        photoCollects = new ArrayList<NewsCellModel>();
        newsCollects = new ArrayList<NewsCellModel>();
        weatherCitys = new ArrayList<String>();
    }
}
