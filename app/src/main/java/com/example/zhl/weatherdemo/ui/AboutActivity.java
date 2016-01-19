package com.example.zhl.weatherdemo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.zhl.weatherdemo.R;

/**
 * Created by zhl on 2016/1/14.
 */
public class AboutActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("关于");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
/*        toolbar.setNavigationOnClickListener(new View.OnClickListener() {                 //toolbar返回时有两种方法，第一种是通过toolbar的setNavigationOnClickListener方法点击出发返回事件
            @Override                                                                       //第二种是通过设置该活动的父活动，然后点击toolbar返回父活动
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
