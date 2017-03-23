package com.study.dh.myapplication.utils;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by dh on 2017/3/21.
 */

public class HttpUtil {

      public static void  sendOkHttpResponse(String address,okhttp3.Callback callback){
          OkHttpClient  client=new OkHttpClient();
          Request  request=new Request.Builder().url(address).build();
          client.newCall(request).enqueue(callback);

      }

}
