package com.project.tangaofeng.actionbar_demo.Model.Weather;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangaofeng on 16/6/30.
 */
public class WeatherModel {

    public String city;
    public TodayModel today;
    public List<ForecaseWeatherModel> forecast;

    public WeatherModel(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null) {
            return;
        }

        Gson gson = new Gson();

        city = jsonObject.getString("city");
        TodayModel todayModel = gson.fromJson(jsonObject.getString("today"),TodayModel.class);
        today = todayModel;
        forecast = new ArrayList<ForecaseWeatherModel>();

        List<ForecaseWeatherModel> list = gson.fromJson(jsonObject.getString("forecast"),new TypeToken<List<ForecaseWeatherModel>>(){}.getType());
        forecast.addAll(list);
    }

}
