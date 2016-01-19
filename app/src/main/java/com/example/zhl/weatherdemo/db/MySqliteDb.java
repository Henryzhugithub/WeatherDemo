package com.example.zhl.weatherdemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhl on 2016/1/15.
 */
public class MySqliteDb {

    public static final String DB_NAME = "area_db";
    public static final int VERSION = 1;

    private static MySqliteDb mySqliteDb;
    private SQLiteDatabase db;

    private MySqliteDb(Context context){
        MyOpenDbHelper helper = new MyOpenDbHelper(context,DB_NAME,null,VERSION);
        db = helper.getWritableDatabase();
    }

    public synchronized static MySqliteDb getInstance(Context context){
        if (mySqliteDb == null){
            mySqliteDb = new MySqliteDb(context);
        }
        return mySqliteDb;
    }

    //根据Area名称查询AreaId
    public String queryAreaId(String area_anme){
        Cursor cursor = db.query("area",new String[]{"area_id"},"area_name = ?",new String[]{area_anme},null,null,null);
        if (cursor.moveToFirst()){
            do {
                String area_id = cursor.getString(cursor.getColumnIndex("area_id"));
                return area_id;
            }while (cursor.moveToNext());
        }
        if (cursor != null){
            cursor.close();
        }
        return null;
    }

    //查询所有的省份信息
    public List<String> loadProvince(){
        Cursor cursor = db.query(true,"area",new String[]{"province_name"},null,null,null,null,null,null,null);
        List<String> array = new ArrayList<String>();
        if (cursor.moveToFirst()){
            do {
                array.add(cursor.getString(cursor.getColumnIndex("province_name")));
            }while (cursor.moveToNext());
        }
        if (cursor != null){
            cursor.close();
        }
        return array;
    }

    //根据选择的省份信息查询城市信息
    public List<String> loadDistrict(String province_name){
        Cursor cursor = db.query(true,"area",new String[]{"district_name"},"province_name = ?",new String[]{province_name},null,null,null,null,null);
        List<String> array = new ArrayList<String>();
        if (cursor.moveToFirst()){
            do {
                array.add(cursor.getString(cursor.getColumnIndex("district_name")));
            }while (cursor.moveToNext());
        }
        if (cursor != null){
            cursor.close();
        }
        return array;
    }

    //根据选择的城市查询所有地区信息
    public List<String> loadArea(String district_name){
        Cursor cursor = db.query(true,"area",new String[]{"area_name"},"district_name = ?",new String[]{district_name},null,null,null,null,null);
        List<String> array = new ArrayList<String>();
        if (cursor.moveToFirst()){
            do {
                array.add(cursor.getString(cursor.getColumnIndex("area_name")));
            }while (cursor.moveToNext());
        }
        if (cursor != null){
            cursor.close();
        }
        return array;
    }

    //保存获取到的基本天气信息到数据库中，如果存在该Area的记录，则删除之，然后将最新数据保存
    public void saveWeatherInfo(String areaId,String areaName,String lastUpdateTime,String weatherDayPhenomenonId,String weatherNightPhenomenonId,
                                String firstHightTemp,String firstLowTemp,String dayWind,String dayWindNum,String secondHightTemp,String secondLowTemp,
                                String weatherSecondDayPhenomenonId,String weatherSecondNightPhenomenonId,String thirdHightTemp,String thirdLowTemp,
                                String weatherThirdDayPhenomenonId,String weatherThirdNightPhenomenonId,String currentDateSave){

        db.delete("weatherinfo","area_id = ?",new String[]{areaId});
        ContentValues values = new ContentValues();
        Cursor cursor = db.rawQuery("select * from weatherinfo where area_id = ?",new String[]{areaId});
        values.put("area_id",areaId);
        values.put("area_name",areaName);
        values.put("Last_update_time",lastUpdateTime);
        values.put("weather_day_phenomenon_id",weatherDayPhenomenonId);
        values.put("weather_night_phenomenon_id",weatherNightPhenomenonId);
        values.put("first_low_temp",firstLowTemp);
        values.put("secondHightTemp",secondHightTemp);
        values.put("secondLowTemp",secondLowTemp);
        values.put("weather_second_day_phenomenon_id",weatherSecondDayPhenomenonId);
        values.put("weather_second_night_phenomenon_id",weatherSecondNightPhenomenonId);
        values.put("thirdHightTemp",thirdHightTemp);
        values.put("thirdLowTemp",thirdLowTemp);
        values.put("weather_third_day_phenomenon_id",weatherThirdDayPhenomenonId);
        values.put("weather_third_night_phenomenon_id",weatherThirdNightPhenomenonId);
        values.put("current_date",currentDateSave);
        if (cursor.getCount() == 0){
            values.put("first_hight_temp",firstHightTemp);
            values.put("dayWind",dayWind);
            values.put("dayWindNum",dayWindNum);
            db.insert("weatherinfo",null,values);
        }else {
            db.update("weatherinfo",values,null,null);
        }

    }

    //保存获取到的指数天气信息到数据库中，如果存在该Area的记录，则更新之
    public void saveWeatherIndexInfo(String areaId,String clothIndex,String carIndex){
/*        Cursor cursor = db.query("weatherinfo",null,"area_id = ?",new String[]{areaId},null,null,null);
        int cursorCountIndex = cursor.getCount();
        Log.d("cursor",cursorCountIndex+"");*/

        ContentValues values = new ContentValues();
        values.put("cloth_index",clothIndex);
        values.put("car_index",carIndex);
        db.update("weatherinfo",values,"area_id = ?",new String[]{areaId});

    }

    //查询出需要显示的天气的记录
    public Cursor queryWeatherInfo(String temp_area_id){
        Cursor cursor = db.query("weatherinfo",null,"area_id = ?",new String[]{temp_area_id},null,null,null,"1");
        return cursor;

/*        int curcount = cursor.getCount();
        Log.d("c",curcount+"");
        String[] data = new String[15];
        if (cursor.moveToFirst()){
            do {
                data[0] = temp_area_id;
                data[1] = cursor.getString(cursor.getColumnIndex("area_name"));
                data[2] = cursor.getString(cursor.getColumnIndex("last_update_time"));
                data[3] = cursor.getString(cursor.getColumnIndex("weather_day_phenomenon_id"));
                data[4] = cursor.getString(cursor.getColumnIndex("first_hight_temp"));
                data[5] = cursor.getString(cursor.getColumnIndex("first_low_temp"));
                data[6] = cursor.getString(cursor.getColumnIndex("dayWind"));
                data[7] = cursor.getString(cursor.getColumnIndex("dayWindNum"));
                data[8] = cursor.getString(cursor.getColumnIndex("secondHightTemp"));
                data[9] = cursor.getString(cursor.getColumnIndex("secondLowTemp"));
                data[10] = cursor.getString(cursor.getColumnIndex("weather_second_day_phenomenon_id"));
                data[11] = cursor.getString(cursor.getColumnIndex("thirdHightTemp"));
                data[12] = cursor.getString(cursor.getColumnIndex("thirdLowTemp"));
                data[13] = cursor.getString(cursor.getColumnIndex("weather_third_day_phenomenon_id"));
                data[14] = cursor.getString(cursor.getColumnIndex("cloth_index"));
            }while (cursor.moveToNext());
        }

        return data;*/
    }

    //查询需要直接显示的天气信息
    public Cursor queryLastWeather(){
        //Cursor cursor = db.query("weatherinfo",null,null,null,null,null,"current_date desc");
        Cursor cursor = db.rawQuery("select * from weatherinfo order by current_date asc",null);
        return cursor;
    }

}
