package com.example.zhl.weatherdemo.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.zhl.weatherdemo.db.MySqliteDb;
import com.example.zhl.weatherdemo.receiver.AutoUpdateReceiver;
import com.example.zhl.weatherdemo.util.GetAddress;
import com.example.zhl.weatherdemo.util.HttpCallback;
import com.example.zhl.weatherdemo.util.HttpUtil;
import com.example.zhl.weatherdemo.util.Utility;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhl on 2016/1/20.
 */
public class AutoUpdateService extends Service {

    private MySqliteDb mySqliteDb;
    private String area_id;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 1000*60*60;
        Long triggerAtTime = SystemClock.elapsedRealtime()+anHour;
        Intent i = new Intent(this,AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    //更新天气信息
    private void updateWeather(){
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        final String currentDateSave = df2.format(date);
        Log.d("currentDate", currentDateSave);
        mySqliteDb = MySqliteDb.getInstance(this);
        Cursor cursor = mySqliteDb.queryLastWeather();
        if (cursor != null){
            if (cursor.moveToLast()){
                area_id = cursor.getString(cursor.getColumnIndex("area_id"));
            }
            String address = GetAddress.address(area_id);
            final String address_index = GetAddress.addressIndex(area_id);
            HttpUtil.sendHttpRequest(address, new HttpCallback() {
                @Override
                public void onFinish(String response) {
                    Utility.handleWeatherResponse(AutoUpdateService.this, response, currentDateSave);

                    //更新天气指数信息
                    HttpUtil.sendHttpRequest(address_index, new HttpCallback() {
                        @Override
                        public void onFinish(String response) {
                            Utility.handleWeatherIndexResponse(AutoUpdateService.this, area_id, response);
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                            Log.d("AutoUpdateException","AutoUpdateException_http_index");
                        }
                    });


                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    Log.d("AutoUpdateException","AutoUpdateException_http");
                }
            });

        }
        Toast.makeText(AutoUpdateService.this, "需要自动更新的数据库中没有记录！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
