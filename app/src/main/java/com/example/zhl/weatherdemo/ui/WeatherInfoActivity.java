package com.example.zhl.weatherdemo.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhl.weatherdemo.R;
import com.example.zhl.weatherdemo.db.MySqliteDb;
import com.example.zhl.weatherdemo.util.GetKey;
import com.example.zhl.weatherdemo.util.HttpCallback;
import com.example.zhl.weatherdemo.util.HttpUtil;
import com.example.zhl.weatherdemo.util.Utility;

import org.w3c.dom.Text;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by zhl on 2016/1/15.
 */
public class WeatherInfoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private LinearLayout mWeather;
    private TextView mSyncFlag;
    private ImageView mShare;
    private TextView mCityName;    //城市名称
    private TextView index_info;  //指数信息
    private ImageView weather_phenomenon;  //天气现象
    private TextView low_temp;       //第一天最低温度
    private TextView high_temp;      //第一天最高温度
    private TextView pollution_disc;   //风力、风向
    private ImageView tomorrow_phenomenon_iamge;   //第二天天气现象
    private TextView tomorrow_low_temp;     //第二天最低温度
    private TextView tomorrow_high_temp;    //第二天最高温度
    private ImageView day_after_tomorrow_iamge;  //第三天天气现象
    private TextView day_after_tomorrow_low_temp;     //第二天最低温度
    private TextView day_after_tomorrow_high_temp;    //第二天最高温度

    private String[] weatherInfoData;

    private MySqliteDb mySqliteDb;
    private String select_area_name;
    private String temp_area_id;

    private static final String PRIVATE_KEY = "db4f97_SmartWeatherAPI_6e3a472";
    private String public_key;
    private String public_key_index;

    private ProgressDialog progressDialog;

    private static final int THREAD_NUM = 1;
    private CyclicBarrier cb;

    //第一个查询天气基本信息的子线程,需要优先指数查询线程执行
    Thread firstThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        mWeather = (LinearLayout) findViewById(R.id.weather);
        mSyncFlag = (TextView) findViewById(R.id.sync_flag);
        mShare = (ImageView) findViewById(R.id.share);
        mCityName = (TextView) findViewById(R.id.city_name);
        index_info = (TextView) findViewById(R.id.index_info);
        weather_phenomenon = (ImageView) findViewById(R.id.weather_phenomenon);
        low_temp = (TextView) findViewById(R.id.low_temp);
        high_temp = (TextView)findViewById(R.id.high_temp);
        pollution_disc = (TextView) findViewById(R.id.pollution_disc);
        tomorrow_phenomenon_iamge = (ImageView) findViewById(R.id.tomorrow_iamge);
        tomorrow_low_temp = (TextView) findViewById(R.id.tomorrow_low_temp);
        tomorrow_high_temp = (TextView) findViewById(R.id.tomorrow_high_temp);
        day_after_tomorrow_iamge = (ImageView) findViewById(R.id.day_after_tomorrow_iamge);
        day_after_tomorrow_low_temp = (TextView) findViewById(R.id.day_after_tomorrow_low_temp);
        day_after_tomorrow_high_temp = (TextView) findViewById(R.id.day_after_tomorrow_high_temp);


        mySqliteDb = MySqliteDb.getInstance(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("天气");

        //隐藏天气信息，显示progressDialog
        mWeather.setVisibility(View.GONE);
        showProgressDialog();

        select_area_name = getIntent().getStringExtra("select_area_name");
        if(!TextUtils.isEmpty(select_area_name)){
            //根据select_area_name查询天气JSON信息
            temp_area_id = mySqliteDb.queryAreaId(select_area_name);    //根据传递过来的area名称查询area_id
            queryWeather(temp_area_id);       //根据所选Area的id查询查询当前的天气信息，并保存到数据库中
            //把信息显示到界面上

        }else {
            showWeather();
        }
    }

    private void showWeather(){
        Cursor cursor;
        if (temp_area_id != null){
            cursor = mySqliteDb.queryWeatherInfo(temp_area_id);
        }else {
            cursor = mySqliteDb.queryLastWeather();
        }
        closeProgressDialog();
        if (cursor.moveToLast()){

            String dtime = cursor.getString(cursor.getColumnIndex("current_date"));
            Log.d("dtime",dtime);
            String last_update_time = cursor.getString(cursor.getColumnIndex("last_update_time"));
            if (last_update_time == null || last_update_time.equals("")){
                mSyncFlag.setText("同步天气信息出现异常，请重新刷新");
                return;
            }
            mWeather.setVisibility(View.VISIBLE);
            mSyncFlag.setText(last_update_time.substring(6,8)+"日"+last_update_time.substring(8,10)+"时更新！");


            mCityName.setText(cursor.getString(cursor.getColumnIndex("area_name")));     //  cursor.getString(cursor.getColumnIndex("current_date"))
            index_info.setText(cursor.getString(cursor.getColumnIndex("cloth_index")));
            //根据返回的白天天气现象代码设置图片     // TODO: 2016/1/17
            high_temp.setText(cursor.getString(cursor.getColumnIndex("first_hight_temp"))+"℃");
            low_temp.setText(cursor.getString(cursor.getColumnIndex("first_low_temp"))+"℃");
            pollution_disc.setText(judgeWind(cursor.getString(cursor.getColumnIndex("dayWind")))+","+judgeWindNum(cursor.getString(cursor.getColumnIndex("dayWindNum"))));   //风向、风力
            tomorrow_phenomenon_iamge.setImageResource(R.mipmap.ic_launcher);    // TODO: 2016/1/17  weatherInfoData[10]
            tomorrow_high_temp.setText(cursor.getString(cursor.getColumnIndex("secondHightTemp"))+"℃");
            tomorrow_low_temp.setText(cursor.getString(cursor.getColumnIndex("secondLowTemp"))+"℃");
            day_after_tomorrow_iamge.setImageResource(R.mipmap.ic_launcher);     // TODO: 2016/1/17   weatherInfoData[13]
            day_after_tomorrow_high_temp.setText(cursor.getString(cursor.getColumnIndex("thirdHightTemp"))+"℃");
            day_after_tomorrow_low_temp.setText(cursor.getString(cursor.getColumnIndex("thirdLowTemp"))+"℃");

        }
        if (cursor != null){
            cursor.close();
        }

        Log.d("Thread","showWeather");
    }




    private void queryWeather(final String area_id){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        final String currentDate = df.format(date);
        final String currentDateSave = df2.format(date);
        Log.d("currentDate",currentDate);
        Log.d("currentDate",currentDateSave);
        public_key ="http://open.weather.com.cn/data/?areaid="+area_id+"&type=forecast_v&date="+currentDate+"&appid=e56d996b0823c0ec";
        public_key_index = "http://open.weather.com.cn/data/?areaid="+area_id+"&type=index_v&date="+currentDate+"&appid=e56d996b0823c0ec";

        String key = GetKey.standardURLEncoder(public_key,PRIVATE_KEY);              //经过加密处理后得到的key
        Log.d("key",key);
        final String  address = "http://open.weather.com.cn/data/?areaid="+area_id+"&type=forecast_v&date="+currentDate+"&appid=e56d99&key="+key;

        //解析天气信息
        String key2 = GetKey.standardURLEncoder(public_key_index,PRIVATE_KEY);
        Log.d("key2",key2);
        final String addressIndex = "http://open.weather.com.cn/data/?areaid="+area_id+"&type=index_v&date="+currentDate+"&appid=e56d99&key="+key2;

        HttpUtil.sendHttpRequest(address, new HttpCallback() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(WeatherInfoActivity.this,response,currentDateSave);
                Log.d("Thread","主信息！");

                //解析天气指数信息
                HttpUtil.sendHttpRequest(addressIndex, new HttpCallback() {
                    @Override
                    public void onFinish(String response) {
                        boolean result = Utility.handleWeatherIndexResponse(WeatherInfoActivity.this,area_id,response);
                        if (result){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showWeather();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSyncFlag.setVisibility(View.VISIBLE);
                                mSyncFlag.setText("同步指数信息失败！");
                            }
                        });
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSyncFlag.setVisibility(View.VISIBLE);
                        mSyncFlag.setText("同步3天预报信息失败！");
                    }
                });
            }
        });



    }



    //显示进度条对话框
    private void showProgressDialog(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(WeatherInfoActivity.this);
            progressDialog.setMessage("正在加载……");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    //关闭进度条对话框
    private void closeProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather_info_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.action_location:
                Intent intent = new Intent(WeatherInfoActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.action_refresh:

                break;
            case R.id.action_settings:
                Intent intent2 = new Intent(WeatherInfoActivity.this,SettingActivity.class);
                startActivity(intent2);
                break;
            case R.id.action_about:
                Intent intent3 = new Intent(WeatherInfoActivity.this,AboutActivity.class);
                startActivity(intent3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private String judgeWind(String wind_id){
        String wind = null;
        switch (wind_id){
            case "0":
                wind = "无持续风向";
                break;
            case "1":
                wind = "东北风";
                break;
            case "2":
                wind = "东风";
                break;
            case "3":
                wind = "东南风";
                break;
            case "4":
                wind = "南风";
                break;
            case "5":
                wind = "西南风";
                break;
            case "6":
                wind = "西风";
                break;
            case "7":
                wind = "西北风";
                break;
            case "8":
                wind = "北风";
                break;
            case "9":
                wind = "旋转风";
                break;
        }
        return wind;
    }

    private String judgeWindNum(String windNum_id){
        String windNum = null;
        switch (windNum_id){
            case "0":
                windNum = "微风";
                break;
            case "1":
                windNum = "3-4级";
                break;
            case "2":
                windNum = "4-5级";
                break;
            case "3":
                windNum = "5-6级";
                break;
            case "4":
                windNum = "6-7级";
                break;
            case "5":
                windNum = "7-8级";
                break;
            case "6":
                windNum = "8-9级";
                break;
            case "7":
                windNum = "9-10级";
                break;
            case "8":
                windNum = "10-11级";
                break;
            case "9":
                windNum = "11-12级";
                break;
        }
        return windNum;
    }

}
