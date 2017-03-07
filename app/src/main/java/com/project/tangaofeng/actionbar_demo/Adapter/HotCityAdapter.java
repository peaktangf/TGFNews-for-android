package com.project.tangaofeng.actionbar_demo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.project.tangaofeng.actionbar_demo.R;

import java.util.ArrayList;

/**
 * Created by tangaofeng on 16/7/9.
 */
public class HotCityAdapter extends RecyclerView.Adapter{

    private float recWidth;
    Context ctx;
    private ItemClickListener itemClickListener;
    private String[] hotCitys;
    private ArrayList<String> collectCitys;

    public HotCityAdapter(Context ctx, float recWidth, String[] hotCitys,ArrayList<String> collectCitys, ItemClickListener itemClickListener) {
        this.ctx = ctx;
        this.recWidth = recWidth;
        this.hotCitys = hotCitys;
        this.collectCitys = collectCitys;
        this.itemClickListener = itemClickListener;
    }

    //定义点击的回调接口
    public interface ItemClickListener {
        public void OnClick(int postion);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_city_cell,parent,false)) {};
        Button btnCity = (Button) viewHolder.itemView.findViewById(R.id.btnCity);
        btnCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果不为空就执行回调
                if (itemClickListener != null) {
                    itemClickListener.OnClick((int)view.getTag());
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Button btnCity = (Button) holder.itemView.findViewById(R.id.btnCity);
        //设置tag值
        btnCity.setTag(position);
        btnCity.setText(hotCitys[position]);

        if (collectCitys.contains(hotCitys[position]) || position == 0) {
            btnCity.setTextColor(ctx.getResources().getColor(R.color.colorNavbar));
            btnCity.setBackgroundResource(R.mipmap.btn_collect_yes);
        }else  {
            btnCity.setTextColor(ctx.getResources().getColor(R.color.colorNoSelect));
            btnCity.setBackgroundResource(R.mipmap.btn_collect_no);
        }

        //设置itemView的宽度和高度（可以用来做自适应）
        RecyclerView.LayoutParams lp1 = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        lp1.width = (int) (recWidth / 3);
        holder.itemView.setLayoutParams(lp1);
    }

    @Override
    public int getItemCount() {
        return hotCitys.length;
    }
}
