package com.example.zhengjun.helloandroid.utils;

import android.app.Activity;
import android.content.Context;

import com.example.zhengjun.helloandroid.MyApplication;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class BaseClient {
	  private static final String BASE_URL = "http://121.199.40.253:98/";

      private static OkHttpClient client = new OkHttpClient();

    static Context context = MyApplication.getInstance();


    public static void get(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(getAbsoluteUrl(url))
                .get()
                .addHeader("token", context.getSharedPreferences("token",
                        Activity.MODE_PRIVATE).getString("token", "null"))
                .addHeader("appid", "cb_7jcelkr9yr82b")
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback); //异步执行
    }

    public static void post(String url, RequestBody body, Callback callback) {
        Request request = new Request.Builder()
                .url(getAbsoluteUrl(url))
                .post(body)
                .addHeader("token", context.getSharedPreferences("token",
                        Activity.MODE_PRIVATE).getString("token", "null"))
                .addHeader("appid", "cb_7jcelkr9yr82b")
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }



    private static String getAbsoluteUrl(String relativeUrl) {
	      return BASE_URL + relativeUrl;
	  }

    public static void put(String url, RequestBody body, Callback callback){
        Request request = new Request.Builder()
                .url(getAbsoluteUrl(url))
                .put(body)
                .addHeader("token", context.getSharedPreferences("token",
                        Activity.MODE_PRIVATE).getString("token", "null"))
                .addHeader("appid", "cb_7jcelkr9yr82b")
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
    public static void delete(String url, RequestBody body, Callback callback){
        Request request = new Request.Builder()
                .url(getAbsoluteUrl(url))
                .delete(body)
                .addHeader("token", context.getSharedPreferences("token",
                        Activity.MODE_PRIVATE).getString("token", "null"))
                .addHeader("appid", "cb_7jcelkr9yr82b")
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

}
