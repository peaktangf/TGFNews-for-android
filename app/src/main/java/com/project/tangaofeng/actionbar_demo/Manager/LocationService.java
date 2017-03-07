package com.project.tangaofeng.actionbar_demo.Manager;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by tangaofeng on 16/7/14.
 */
public class LocationService {

    //初始化LocationClient类
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener;
    private LocationCallBack locationCallBack;
    private static LocationService locationService;
    //定义一个回调接口
    public interface LocationCallBack {
        public void locationSuccess(BDLocation location);//成功回调
        public void locationFail(String errMsg);//失败回调
    }


    public static LocationService shareLocation(Context context) {
        if (locationService == null) {
            locationService = new LocationService(context);
        }
        return locationService;
    }

    public LocationService(Context context) {
        mLocationClient = new LocationClient(context);//声明LocationClient类
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);//注册监听函数
        initLocation();//初始化
    }

    //开始定位
    public void startLocation(LocationCallBack locationCallBack) {
        this.locationCallBack = locationCallBack;
        mLocationClient.start();
    }

    //配置定位SDK参数
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    //实现BDLocationListener接口
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            mLocationClient.stop();//结束定位
            if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation) {
                if (locationCallBack != null) {
                    locationCallBack.locationSuccess(location);
                }
            }else {
                if (locationCallBack != null) {
                    locationCallBack.locationFail("定位失败，请稍后再试！");
                }
            }
        }
    }
}
