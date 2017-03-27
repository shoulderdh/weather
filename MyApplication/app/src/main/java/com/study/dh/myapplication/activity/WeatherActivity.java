package com.study.dh.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.study.dh.myapplication.R;
import com.study.dh.myapplication.gson.WeatherInfo;
import com.study.dh.myapplication.httputils.HttpUtil;
import com.study.dh.myapplication.httputils.UrlManage;
import com.study.dh.myapplication.httputils.Utility;
import com.study.dh.myapplication.service.AutoUpdateWeatherService;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends BaseActivity {
    private ScrollView weatherLayout;

    private Button navButton;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private ImageView  bing_bic_img;

    public DrawerLayout  drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        // 初始化各控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);

        bing_bic_img= (ImageView) findViewById(R.id.bing_bic_img);
//        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
//        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            // 有缓存时直接解析天气数据
            WeatherInfo weather = Utility.handleWeatherResponse(weatherString);
        //    mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            // 无缓存时去服务器查询天气
        String    mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                requestWeather(mWeatherId);
//            }
//        });
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bing_bic_img);
        } else {
            loadBingPic();
        }

    }

    private void loadBingPic() {
        HttpUtil.sendOkHttpResponse(UrlManage.requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bing_bic_img);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void requestWeather( String mWeatherId) {
        HttpUtil.sendOkHttpResponse(UrlManage.weatherUrl+mWeatherId+UrlManage.hefengKey, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final WeatherInfo weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.getHeWeather5().get(0).getStatus())) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                        //    mWeatherId  = weather.getHeWeather5().get(0).getBasic().getId();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                     //   swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    //    swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });

    }

    private void showWeatherInfo(WeatherInfo weather) {
        if (weather != null && "ok".equals(weather.getHeWeather5().get(0).getStatus())) {

            String cityName = weather.getHeWeather5().get(0).getBasic().getCity();
            String updateTime = weather.getHeWeather5().get(0).getBasic().getUpdate().getLoc().split(" ")[1];
            String degree = weather.getHeWeather5().get(0).getNow().getTmp() + "℃";
            String weatherInfo = weather.getHeWeather5().get(0).getNow().getFl();
            titleCity.setText(cityName);
            titleUpdateTime.setText(updateTime);
            degreeText.setText(degree);
            weatherInfoText.setText(weatherInfo);
            forecastLayout.removeAllViews();
            List<WeatherInfo.HeWeather5Bean.DailyForecastBean>  forecast= weather.getHeWeather5().get(0).getDaily_forecast();
            if (forecast.size()>0){
                for(int i=0;i<forecast.size();i++){
                    View view = LayoutInflater.from(this).inflate(R.layout.forcast_item, forecastLayout, false);
                    TextView dateText = (TextView) view.findViewById(R.id.date_text);
                    TextView infoText = (TextView) view.findViewById(R.id.info_text);
                    TextView maxText = (TextView) view.findViewById(R.id.max_text);
                    TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.get(i).getDate());
            infoText.setText(forecast.get(i).getCond().getTxt_d());
            maxText.setText(forecast.get(i).getTmp().getMax());
            minText.setText(forecast.get(i).getTmp().getMin());
            forecastLayout.addView(view);
                }
            }


            if (weather.getHeWeather5().get(0).getAqi() != null) {
                aqiText.setText(weather.getHeWeather5().get(0).getAqi().getCity().getQlty());
                pm25Text.setText(weather.getHeWeather5().get(0).getAqi().getCity().getPm25());
            }
            String comfort = "舒适度：" + weather.getHeWeather5().get(0).getSuggestion().getComf().getTxt();
            String carWash = "洗车指数：" + weather.getHeWeather5().get(0).getSuggestion().getCw().getTxt();
            String sport = "运行建议：" + weather.getHeWeather5().get(0).getSuggestion().getSport().getTxt();
            comfortText.setText(comfort);
            carWashText.setText(carWash);
            sportText.setText(sport);
            weatherLayout.setVisibility(View.VISIBLE);
                Intent intent = new Intent(this, AutoUpdateWeatherService.class);
              startService(intent);
            Log.i("serviceweather","success");

        }else {
            Log.i("serviceweather","failure");
        }


    }
}
