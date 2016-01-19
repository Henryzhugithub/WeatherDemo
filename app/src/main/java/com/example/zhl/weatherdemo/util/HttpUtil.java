package com.example.zhl.weatherdemo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zhl on 2016/1/15.
 */
public class HttpUtil {

    public static void  sendHttpRequest(final String address,final HttpCallback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    HttpURLConnection connection = null;
                    try {
                        URL url = new URL(address);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setReadTimeout(8000);
                        connection.setConnectTimeout(8000);
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));

                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null){
                            response.append(line);
                        }
                        if (callback != null){
                            //回调onFinish()方法
                            callback.onFinish(response.toString());
                        }
                    } catch (IOException e) {
                        if (callback != null){
                            callback.onError(e);
                        }
                    } finally {
                        if (connection != null){
                            connection.disconnect();
                        }
                    }
                }
            }
        }).start();

    }
}
