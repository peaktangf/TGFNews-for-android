package com.project.tangaofeng.actionbar_demo.Model;

import android.media.Image;

/**
 * Created by tangaofeng on 16/6/15.
 */
public class NewsModel {

    private int image;
    private String title;
    private String content;

    public NewsModel(int image, String title, String content) {
        this.image = image;
        this.title = title;
        this.content = content;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
