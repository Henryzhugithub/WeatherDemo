package com.example.zhl.weatherdemo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhl on 2016/1/20.
 */
public class GetAddress {

    private static final String PRIVATE_KEY = "db4f97_SmartWeatherAPI_6e3a472";

    public static String address(String area_id){
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmm");
        Date date = new Date();
        String current_date = sf.format(date);
        String public_key ="http://open.weather.com.cn/data/?areaid="+area_id+"&type=forecast_v&date="+current_date+"&appid=e56d996b0823c0ec";
        String key = GetKey.standardURLEncoder(public_key,PRIVATE_KEY);
        String  address = "http://open.weather.com.cn/data/?areaid="+area_id+"&type=forecast_v&date="+current_date+"&appid=e56d99&key="+key;
        return address;
    }

    public static String addressIndex(String area_id){
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmm");
        Date date = new Date();
        String current_date = sf.format(date);
        String public_key_index = "http://open.weather.com.cn/data/?areaid="+area_id+"&type=index_v&date="+current_date+"&appid=e56d996b0823c0ec";
        String key_index = GetKey.standardURLEncoder(public_key_index,PRIVATE_KEY);
        String addressIndex = "http://open.weather.com.cn/data/?areaid="+area_id+"&type=index_v&date="+current_date+"&appid=e56d99&key="+key_index;
        return addressIndex;
    }

}
