package com.example.zhengjun.helloandroid.utils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class GetTokenClient {
    private static final String BASE_URL = "http://api.reactorlive.com/auth/token?appid=cb_7jcelkr9yr82b&secret=b4700f27855f4ee1576bae09c14a7682b32bc84c";

    private static OkHttpClient client = new OkHttpClient();

    public static void get(Callback callback, String device) {
        Request request = new Request.Builder()
                .url(BASE_URL)
                .get()
                .addHeader("token", device)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }


}
