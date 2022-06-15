package com.example.zhengjun.helloandroid.http.request;


import com.hjq.http.config.IRequestApi;


//用户登录请求
public class ChangeUserImageApi implements IRequestApi {
    @Override
    public String getApi() {
        return "user/update";
    }

    String avatar;//头像在服务端的资源。

    public ChangeUserImageApi setAvatar(String avatar) {
        this.avatar = avatar;return this;
    }
}