package com.project.tangaofeng.actionbar_demo.Model;

import android.widget.ImageButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangaofeng on 16/6/16.
 */

//public class

public class NewsCellModel implements Serializable{

    public String tname;
    //新闻发布时间
    public String ptime;
    //标题
    public String title;
    //多维数组
    public ArrayList<ImgextraModel> imgextra;
    public String photosetID;
    public String skipType;
    //新闻id
    public String docid;
    //图片连接
    public String imgsrc;
    //描述
    public String digest;
    public String subtitle;
    //大图样式
    public String imgType;
    //ads
    public List<AdsModel> ads;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o.getClass() != NewsCellModel.class) {
            return false;
        }
        NewsCellModel model = (NewsCellModel) o;
        if (model.title.equals(this.title)) {
            return true;
        }else {
            return false;
        }
    }
}
