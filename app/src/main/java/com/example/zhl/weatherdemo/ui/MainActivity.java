package com.example.zhl.weatherdemo.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.zhl.weatherdemo.R;
import com.example.zhl.weatherdemo.db.MySqliteDb;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private MySqliteDb mySqliteDb;
    private List<String> dataList = new ArrayList<String>();

    private  int LEVEL_CURRENT;
    private static final int CURRENT_PROVINCE = 0;
    private static final int CURRENT_DISTRICT = 1;
    private static final int CURRENT_AREA = 2;

    private String selectDistrict;
    private String selectProvince;
    private String selectArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mListView = (ListView) findViewById(R.id.list_view);
        mySqliteDb = MySqliteDb.getInstance(this);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        queryProvince();
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (LEVEL_CURRENT == CURRENT_PROVINCE){
            selectProvince = dataList.get(position);
            queryDistrict();
        }else if (LEVEL_CURRENT == CURRENT_DISTRICT){
            selectDistrict = dataList.get(position);
            queryArea();
        }else if (LEVEL_CURRENT == CURRENT_AREA){
            selectArea = dataList.get(position);
            saveFlag();
            Intent intent = new Intent(MainActivity.this,WeatherInfoActivity.class);
            intent.putExtra("select_area_name",selectArea);
            startActivity(intent);
            finish();
        }
    }

    public void saveFlag(){
        SharedPreferences sf = this.getSharedPreferences("flag",MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putBoolean("isSelect",true);
        editor.commit();
    }



    //查询所有省份信息
    private void queryProvince(){
        List<String> provinceArray = mySqliteDb.loadProvince();
        if (provinceArray.size() > 0){
            dataList.clear();
            for (String p : provinceArray){
                dataList.add(p);
            }
        }
        adapter.notifyDataSetChanged();
        getSupportActionBar().setTitle("中国");
        LEVEL_CURRENT = CURRENT_PROVINCE;
    }

    //查询指定省份所有的城市信息
    private void queryDistrict(){
        List<String> districtArray = mySqliteDb.loadDistrict(selectProvince);
        if (districtArray.size() > 0){
            dataList.clear();
            for (String d : districtArray){
                dataList.add(d);
            }
        }
        adapter.notifyDataSetChanged();
        getSupportActionBar().setTitle(selectProvince);
        LEVEL_CURRENT = CURRENT_DISTRICT;
    }

    //查询指定城市下所有的县信息
    private void queryArea(){
        List<String> areaArray = mySqliteDb.loadArea(selectDistrict);
        if (areaArray.size() > 0){
            dataList.clear();
            for (String a : areaArray){
                dataList.add(a);
            }
        }
        adapter.notifyDataSetChanged();
        getSupportActionBar().setTitle(selectDistrict);
        LEVEL_CURRENT = CURRENT_AREA;
    }

    @Override
    public void onBackPressed() {
        if (LEVEL_CURRENT == CURRENT_PROVINCE){
            finish();
        }else if (LEVEL_CURRENT == CURRENT_DISTRICT){
            queryProvince();
        }else if (LEVEL_CURRENT == CURRENT_AREA){
            queryDistrict();

        }

        //super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this,SettingActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_about){
            Intent intent = new Intent(MainActivity.this,AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
