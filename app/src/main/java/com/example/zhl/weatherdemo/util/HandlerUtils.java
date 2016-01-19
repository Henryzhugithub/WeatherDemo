package com.example.zhl.weatherdemo.util;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by zhl on 2016/1/19.
 */
public class HandlerUtils {

    private static final Handler handler = new Handler(Looper.getMainLooper());

    public static boolean postDelayed(Runnable r,long delayMillis){
        return handler.postDelayed(r,delayMillis);
    }
}
