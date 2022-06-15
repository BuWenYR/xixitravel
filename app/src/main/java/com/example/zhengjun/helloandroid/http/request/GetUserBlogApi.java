package com.example.zhengjun.helloandroid.http.request;


import com.hjq.http.config.IRequestApi;


//根据用户id查询发布的日志
public class GetUserBlogApi implements IRequestApi {
    @Override
    public String getApi() {
        return "foodLog/myFoodLog";
    }

    String userId;

    public GetUserBlogApi setUserId(String userId) {
        this.userId = userId;
        return this;
    }
}