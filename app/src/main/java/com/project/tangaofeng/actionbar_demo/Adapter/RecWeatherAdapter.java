package com.project.tangaofeng.actionbar_demo.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.tangaofeng.actionbar_demo.Manager.DateHelper;
import com.project.tangaofeng.actionbar_demo.Manager.Util;
import com.project.tangaofeng.actionbar_demo.Model.Weather.ForecaseWeatherModel;
import com.project.tangaofeng.actionbar_demo.R;

import java.util.List;

/**
 * Created by tangaofeng on 16/6/29.
 */
public class RecWeatherAdapter extends RecyclerView.Adapter {

    private float recWidth;
    private float recHight;
    private List<ForecaseWeatherModel> forecasts;
    public RecWeatherAdapter(float recWidth,float recHight,List<ForecaseWeatherModel> forecasts) {
        this.recWidth = recWidth;
        this.recHight = recHight;
        this.forecasts = forecasts;
    }

    //自定义的ViewHolder
    class WeatherHolder extends RecyclerView.ViewHolder {

        private TextView tvWeek;
        private TextView tvDate;
        private TextView tvDesc;
        private TextView tvHigh;
        private TextView tvLow;
        private TextView tvWind;
        private ImageView imgMark;

        public WeatherHolder(View itemView) {
            super(itemView);
            tvWeek = (TextView) itemView.findViewById(R.id.tvWeek);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
            tvHigh = (TextView) itemView.findViewById(R.id.tvHigh);
            tvLow = (TextView) itemView.findViewById(R.id.tvLow);
            tvWind = (TextView) itemView.findViewById(R.id.tvWind);
            imgMark = (ImageView) itemView.findViewById(R.id.imgMark);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建viewholder
        //这么添加布局，item中的marchparent将会失效,就不能改变其大小 (LayoutInflater.from(parent.getContext()).inflate(R.layout.renqi_item, null));
        WeatherHolder weatherHolder = new WeatherHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_cell,parent,false));
        return weatherHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //设置viewholder
        WeatherHolder weatherHolder = (WeatherHolder) holder;

        ForecaseWeatherModel model = forecasts.get(position);
        weatherHolder.tvWeek.setText(model.week);
        String[] dates = model.date.split("-");
        weatherHolder.tvDate.setText(dates[1] + "-" + dates[2]);
        weatherHolder.tvDesc.setText(model.type);
        weatherHolder.tvHigh.setText(model.hightemp);
        weatherHolder.tvLow.setText(model.lowtemp);
        weatherHolder.tvWind.setText(model.fengli);
        weatherHolder.imgMark.setImageResource(Util.weatherImageWithKey(model.type));
        //设置itemView的宽度和高度（可以用来做自适应）
        RecyclerView.LayoutParams lp1 = (RecyclerView.LayoutParams) weatherHolder.itemView.getLayoutParams();
        lp1.width = (int) (recWidth / 4);
        lp1.height = (int) recHight;
        weatherHolder.itemView.setLayoutParams(lp1);
    }

    @Override
    public int getItemCount() {
        //返回viewholder的个数
        return 4;
    }
}
