package com.example.zhl.weatherdemo.util;

/**
 * Created by zhl on 2016/1/15.
 */
public interface HttpCallback {

    void onFinish(String response);
    void onError(Exception e);
}
