package com.study.dh.myapplication.httputils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.study.dh.myapplication.db.City;
import com.study.dh.myapplication.db.County;
import com.study.dh.myapplication.db.Province;
import com.study.dh.myapplication.gson.WeatherInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dh on 2017/3/21.
 */

public class Utility {

    //解析和处理服务器返回的省份数据
    public static boolean  handleProvinceResponse(String response){
           if (!TextUtils.isEmpty(response)){
               try {
                   JSONArray  allProvince=new JSONArray(response);
                   for (int i=0;i<allProvince.length();i++){
                       JSONObject  jsonObject=allProvince.getJSONObject(i);
                       Province  province=new Province();
                       province.setProvinceName(jsonObject.getString("name"));
                       province.setProvinceCode(jsonObject.getInt("id"));
                       province.save();
                   }
                   return true;

               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }

        return false;
    }

    //解析和处理服务器返回的市级数据
    public static boolean  handleCityResponse(String response,int provinceId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray  allProvince=new JSONArray(response);
                for (int i=0;i<allProvince.length();i++){
                    JSONObject  jsonObject=allProvince.getJSONObject(i);
                    City city=new City();
                    city.setCityName(jsonObject.getString("name"));
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    //解析和处理服务器返回的县级数据
    public static boolean  handleCountyResponse(String response,int countyId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray  allCounty=new JSONArray(response);
                for (int i=0;i<allCounty.length();i++){
                    JSONObject  jsonObject=allCounty.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(jsonObject.getString("name"));
                    county.setWeatherId(jsonObject.getString("weather_id"));
                    county.setCityId(countyId);
                    county.save();
                }
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    //解析和处理服务器返回的天气数据
    public static WeatherInfo handleWeatherResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
            //    JSONObject  jsonObject=new JSONObject(response);
            //    JSONArray  jsonArray=jsonObject.getJSONArray("HeWeather5");
            //    String weatherContent=jsonArray.getJSONObject(0).toString();
                Gson  gson=new Gson();
              WeatherInfo  weatherContent=gson.fromJson(response,WeatherInfo.class);
                 //return  new Gson().fromJson(weatherContent,WeatherInfo.class);
                Log.i("weather",weatherContent.getHeWeather5().get(0).getStatus());
                return  weatherContent;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

}
