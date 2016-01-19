package com.example.zhl.weatherdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.zhl.weatherdemo.R;

/**
 * Created by zhl on 2016/1/14.
 */
public class SettingActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RelativeLayout GpsSelectLayout;
    private SwitchCompat GpsSwitch;
    private RelativeLayout chooseThemeLayout;
    private RelativeLayout contributeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("设置");

        GpsSwitch = (SwitchCompat) findViewById(R.id.switchwidget);
        chooseThemeLayout = (RelativeLayout) findViewById(R.id.choose_theme);
        contributeLayout = (RelativeLayout) findViewById(R.id.contribute);

        GpsSelectLayout = (RelativeLayout) findViewById(R.id.gps_select);
        GpsSelectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GpsSwitch.toggle();
            }
        });

        chooseThemeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        contributeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,ContributeActivity.class);
                startActivity(intent);
            }
        });
    }
}
