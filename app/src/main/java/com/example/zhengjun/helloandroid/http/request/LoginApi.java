package com.example.zhengjun.helloandroid.http.request;


import com.hjq.http.config.IRequestApi;


//用户登录请求
public class LoginApi implements IRequestApi {

    @Override
    public String getApi() {
        return "user/login";
    }

    private String username;
    private String password;

    public LoginApi setUsername(String username) {
        this.username = username;
        return this;
    }

    public LoginApi setPassword(String password) {
        this.password = password;
        return this;
    }
}