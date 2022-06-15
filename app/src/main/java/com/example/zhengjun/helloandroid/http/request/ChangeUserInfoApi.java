package com.example.zhengjun.helloandroid.http.request;


import com.hjq.http.config.IRequestApi;


//用户登录请求
public class ChangeUserInfoApi implements IRequestApi {
    @Override
    public String getApi() {
        return "user/update";
    }

    String nickname;//昵称。
    String sign;//签名。
    int sex;//性别，0 未知 1 男 2女
    String avatar;//头像在服务端的资源。

    public ChangeUserInfoApi setNickname(String nickname) {
        this.nickname = nickname;return this;
    }

    public ChangeUserInfoApi setSign(String sign) {
        this.sign = sign;return this;
    }

    public ChangeUserInfoApi setSex(int sex) {
        this.sex = sex;return this;
    }

    public ChangeUserInfoApi setAvatar(String avatar) {
        this.avatar = avatar;return this;
    }
}