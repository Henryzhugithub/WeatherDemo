package com.example.zhl.weatherdemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhl on 2016/1/15.
 */
public class MyOpenDbHelper extends SQLiteOpenHelper {

    public static final String CREATE_TABLE = "create table area("
            + "_id integer primary key autoincrement,"
            +"area_id text,"
            +"area_name text,"
            +"district_name text,"
            +"province_name text)"
            ;

    public static final String CREATE_WEATHERINFO = "create table weatherinfo("
            +"_id integer primary key autoincrement,"
            +"area_id text,"
            +"area_name text,"
            +"last_update_time text,"
            +"weather_day_phenomenon_id text,"
            +"weather_night_phenomenon_id text,"
            +"first_hight_temp text,"
            +"first_low_temp text,"
            +"dayWind text,"
            +"dayWindNum text,"
            +"secondHightTemp text,"
            +"secondLowTemp text,"
            +"weather_second_day_phenomenon_id text,"
            +"weather_second_night_phenomenon_id text,"
            +"thirdHightTemp text,"
            +"thirdLowTemp text,"
            +"weather_third_day_phenomenon_id text,"
            +"weather_third_night_phenomenon_id text,"
            +"cloth_index text,"
            +"car_index text,"
            +"currentdate text)"
            ;

    public MyOpenDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_WEATHERINFO);
        initDate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void initDate(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("area_id","101010100");
        values.put("area_name","北京");
        values.put("district_name","北京");
        values.put("province_name","北京");
        db.insert("area",null,values);
        values.clear();
        values.put("area_id","101010200");
        values.put("area_name","海淀");
        values.put("district_name","北京");
        values.put("province_name","北京");
        db.insert("area",null,values);
        values.clear();
        values.put("area_id","101010300");
        values.put("area_name","朝阳");
        values.put("district_name","北京");
        values.put("province_name","北京");
        db.insert("area",null,values);
        values.clear();
        values.put("area_id","101010400");
        values.put("area_name","顺义");
        values.put("district_name","北京");
        values.put("province_name","北京");
        db.insert("area",null,values);
        values.clear();
        values.put("area_id","101020100");
        values.put("area_name","上海");
        values.put("district_name","上海");
        values.put("province_name","上海");
        db.insert("area",null,values);
        values.clear();
        values.put("area_id","101020200");
        values.put("area_name","闵行");
        values.put("district_name","上海");
        values.put("province_name","上海");
        db.insert("area",null,values);
        values.clear();
        values.put("area_id","101180504");
        values.put("area_name","汝州");
        values.put("district_name","平顶山");
        values.put("province_name","河南");
        db.insert("area",null,values);
        values.clear();
        values.put("area_id","101210101");
        values.put("area_name","杭州");
        values.put("district_name","杭州");
        values.put("province_name","浙江");
        db.insert("area",null,values);
        values.clear();
        values.put("area_id","101210102");
        values.put("area_name","萧山");
        values.put("district_name","杭州");
        values.put("province_name","浙江");
        db.insert("area",null,values);
        values.clear();
        values.put("area_id","101210103");
        values.put("area_name","桐庐");
        values.put("district_name","杭州");
        values.put("province_name","浙江");
        db.insert("area",null,values);
        values.clear();

    }
}
