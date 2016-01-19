package com.example.zhl.weatherdemo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.zhl.weatherdemo.db.MySqliteDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhl on 2016/1/15.
 */
public class Utility {

    private static MySqliteDb mySqliteDb;

    //解析服务器返回的天气JSON数据，并将解析结果存储到数据库中
    public static void handleWeatherResponse(Context context,String response,String currentDateSave){
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONObject cityInfo = jsonObject.getJSONObject("c");
            String areaId = cityInfo.getString("c1");   //城市编码
            String areaName = cityInfo.getString("c3");   //城市名称

            JSONObject weatherInfo = jsonObject.getJSONObject("f");
            String lastUpdateTime = weatherInfo.getString("f0");
            JSONArray array = weatherInfo.getJSONArray("f1");

            JSONObject firstDay = (JSONObject) array.opt(0);
            String weatherDayPhenomenonId = firstDay.getString("fa");  //第一天白天天气现象编码
            String weatherNightPhenomenonId = firstDay.getString("fb");  //第一天晚上天气现象编码
            String firstHightTemp = firstDay.getString("fc");     //第一天最高温度
            String firstLowTemp = firstDay.getString("fd");       //第二天最低温度
            String dayWind = firstDay.getString("fe");            //白天风向编号
            String dayWindNum = firstDay.getString("fg");            //白天风力编号

            JSONObject SecondDay = (JSONObject) array.opt(1);
            String secondHightTemp = SecondDay.getString("fc");     //第二天最高温度
            String secondLowTemp = SecondDay.getString("fd");       //第二天最低温度
            String weatherSecondDayPhenomenonId = SecondDay.getString("fa");  //第二天白天天气现象编码
            String weatherSecondNightPhenomenonId = SecondDay.getString("fb");  //第二天晚上天气现象编码


            JSONObject ThirdDay = (JSONObject) array.opt(2);
            String thirdHightTemp = ThirdDay.getString("fc");     //第三天最高温度
            String thirdLowTemp = ThirdDay.getString("fd");        //第三天最低温度
            String weatherThirdDayPhenomenonId = ThirdDay.getString("fa");  //第三天白天天气现象编码
            String weatherThirdNightPhenomenonId = ThirdDay.getString("fb");  //第三天晚上天气现象编码
            Log.d("Thread","主信息！");
            mySqliteDb = MySqliteDb.getInstance(context);
            mySqliteDb.saveWeatherInfo(areaId,areaName,lastUpdateTime,weatherDayPhenomenonId,weatherNightPhenomenonId,firstHightTemp,firstLowTemp,dayWind,
                    dayWindNum,secondHightTemp,secondLowTemp,weatherSecondDayPhenomenonId,weatherSecondNightPhenomenonId,thirdHightTemp,thirdLowTemp,
                    weatherThirdDayPhenomenonId,weatherThirdNightPhenomenonId,currentDateSave
                    );

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //解析服务器返回的天气JSON数据，并将解析结果存储到本地
    public static boolean handleWeatherIndexResponse(Context context,String areaId,String response){
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray array = jsonObject.optJSONArray("i");
            JSONObject jsOne= (JSONObject)array.opt(0);
            String clothIndex = jsOne.getString("i5");    //穿衣指数
            JSONObject jsTwo= (JSONObject)array.opt(1);
            String carIndex = jsTwo.getString("i5");    //洗车指数
            mySqliteDb = MySqliteDb.getInstance(context);
            mySqliteDb.saveWeatherIndexInfo(areaId,clothIndex,carIndex);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

}
