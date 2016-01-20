package com.example.zhl.weatherdemo.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.zhl.weatherdemo.R;
import com.example.zhl.weatherdemo.util.HandlerUtils;

/**
 * Created by zhl on 2016/1/19.
 */
public class FirstActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);

        HandlerUtils.postDelayed(this,2000);
    }

    @Override
    public void run() {
        if (getFlag()){
            Intent intent = new Intent(FirstActivity.this,WeatherInfoActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(FirstActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private Boolean getFlag(){
        SharedPreferences sf = this.getSharedPreferences("flag",MODE_PRIVATE);
        return sf.getBoolean("isSelect",false);
    }
}
