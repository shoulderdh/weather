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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.study.dh.myapplication.activity.WeatherActivity;
import com.study.dh.myapplication.gson.WeatherInfo;
import com.study.dh.myapplication.utils.HttpUtil;
import com.study.dh.myapplication.utils.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.study.dh.myapplication.R.id.bing_bic_img;

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
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpResponse(requestBingPic, new Callback() {
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
            String weatherUrl = "https://free-api.heweather.com/v5/weather?city=" + mWeatherId + "&key=b27ff682edbe49e7a8100b1c9657e619";
            HttpUtil.sendOkHttpResponse(weatherUrl, new Callback() {
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
