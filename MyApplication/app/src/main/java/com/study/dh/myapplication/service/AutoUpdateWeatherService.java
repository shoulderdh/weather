package com.study.dh.myapplication.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.study.dh.myapplication.gson.WeatherInfo;
import com.study.dh.myapplication.httputils.HttpUtil;
import com.study.dh.myapplication.httputils.UrlManage;
import com.study.dh.myapplication.httputils.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.study.dh.myapplication.httputils.UrlManage.requestBingPic;
import static com.study.dh.myapplication.httputils.UrlManage.weatherUrl;

/**
 * Created by dh on 2017/3/24.
 */

public class AutoUpdateWeatherService  extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager  manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        int hour=8*60*60*1000;
        long triggerAttime= SystemClock.elapsedRealtime()+hour;
        Intent  i=new Intent(this,AutoUpdateWeatherService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAttime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateBingPic() {
        HttpUtil.sendOkHttpResponse(UrlManage.requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateWeatherService.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();

            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateWeather() {
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=preferences.getString("weather",null);
        if (weatherString!=null){
            WeatherInfo  weatherInfo=Utility.handleWeatherResponse(weatherString);
            String mWeatherId=weatherInfo.getHeWeather5().get(0).getBasic().getId();
            HttpUtil.sendOkHttpResponse(UrlManage.weatherUrl+mWeatherId+UrlManage.hefengKey, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText = response.body().string();
                    final WeatherInfo weather = Utility.handleWeatherResponse(responseText);
                    if (weather != null && "ok".equals(weather.getHeWeather5().get(0).getStatus())) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateWeatherService.this).edit();
                        editor.putString("weather", responseText);
                        editor.apply();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();

                }
            });
        }

    }
}
