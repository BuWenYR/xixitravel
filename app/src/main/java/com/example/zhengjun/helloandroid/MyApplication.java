package com.example.zhengjun.helloandroid;

import android.app.Application;

import com.example.zhengjun.helloandroid.http.model.RequestHandler;
import com.example.zhengjun.helloandroid.http.response.LoginBean;
import com.example.zhengjun.helloandroid.http.server.ReleaseServer;
import com.example.zhengjun.helloandroid.utils.SPUtils;
import com.hjq.http.EasyConfig;

import okhttp3.OkHttpClient;

public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
        init();
    }

    private void init() {

        // 网络请求框架初始化
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();
        LoginBean user = SPUtils.getObject("user", LoginBean.class, this);
        EasyConfig.with(okHttpClient)
                // 是否打印日志
                .setLogEnabled(true)
                // 设置服务器配置
                .setServer(new ReleaseServer())
                // 设置请求处理策略
                .setHandler(new RequestHandler(this))
                // 设置请求重试次数
                .setRetryCount(1)
                // 添加全局请求参数
                // 启用配置
                .into();
        if (user!=null){
            EasyConfig.getInstance().addHeader("authorization",user.getToken());
        }
    }


}