package com.study.dh.myapplication.application;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

import com.study.dh.myapplication.service.LocationService;

import org.litepal.LitePal;



/**
 * Created by dh on 2017/3/21.
 */

public class MyApplication extends Application {

    private static Context context;

    public LocationService locationService;
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        LitePal.initialize(context);


        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        //SDKInitializer.initialize(getApplicationContext());
    }

    public static Context getContext() {
        return context;
    }

}
