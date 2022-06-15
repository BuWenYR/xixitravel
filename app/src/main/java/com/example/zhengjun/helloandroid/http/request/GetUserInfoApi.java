package com.example.zhengjun.helloandroid.http.request;


import com.hjq.http.config.IRequestApi;

/**
 * desc   :用户登录请求
 */
public class GetUserInfoApi implements IRequestApi {

    @Override
    public String getApi() {
        return "user/member";
    }


}