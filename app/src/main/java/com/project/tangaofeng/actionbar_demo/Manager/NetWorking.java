package com.project.tangaofeng.actionbar_demo.Manager;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by tangaofeng on 16/6/15.
 */


public class NetWorking {

    OkHttpClient client = new OkHttpClient();

    public String requestNetWorking(String url) throws IOException {

        Request request;
        if (url.contains("http://apis.baidu.com")) {
            request = new Request.Builder().url(url).header("apikey","6d814be9002444468b458046bf7bb5ce").build();
        }
        else  {
            request = new Request.Builder().url(url).build();
        }

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
}
