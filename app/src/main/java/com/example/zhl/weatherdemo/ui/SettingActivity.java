package com.example.zhl.weatherdemo.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
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
    private RelativeLayout greatLayout;
    private Context mContext;
    private SharedPreferences sf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme();
        setContentView(R.layout.setting_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("设置");
        mContext = this;

        GpsSwitch = (SwitchCompat) findViewById(R.id.switchwidget);
        chooseThemeLayout = (RelativeLayout) findViewById(R.id.choose_theme);
        contributeLayout = (RelativeLayout) findViewById(R.id.contribute);
        greatLayout = (RelativeLayout) findViewById(R.id.great);


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
                FragmentManager manager = getSupportFragmentManager();
                ColorChooserDialog dialog = new ColorChooserDialog();
                dialog.show(manager,"fragment_color_chooser");
            }
        });

        contributeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,ContributeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        greatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id="+mContext.getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                mContext.startActivity(intent);
            }
        });
    }


    private void theme(){
        sf = this.getSharedPreferences("theme",MODE_PRIVATE);
        int themId = sf.getInt("theme_value",1);
        selectTheme(themId);
    }

    private void selectTheme(int themeId){
        switch (themeId){
            case 1:
                setTheme(R.style.AppTheme);
                break;
            case 2:
                setTheme(R.style.AppTheme2);
                break;
        }
    }

}
