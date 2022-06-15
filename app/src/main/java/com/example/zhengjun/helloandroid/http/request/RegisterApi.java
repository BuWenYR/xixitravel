package com.example.zhengjun.helloandroid.http.request;


import com.hjq.http.config.IRequestApi;

//用户登录请求
public class RegisterApi implements IRequestApi {

    @Override
    public String getApi() {
        return "user/register";
    }

    private String username;
    private String password;

    public RegisterApi setUsername(String username) {
        this.username = username;
        return this;
    }

    public RegisterApi setPassword(String password) {
        this.password = password;
        return this;
    }
}