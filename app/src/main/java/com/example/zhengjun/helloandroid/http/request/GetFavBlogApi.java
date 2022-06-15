package com.example.zhengjun.helloandroid.http.request;


import com.hjq.http.config.IRequestApi;

//根据用户id查询点赞的日志
public class GetFavBlogApi implements IRequestApi {
    @Override
    public String getApi() {
        return "foodLog/myFavouriteFoodLog";
    }

    String userId;

    public GetFavBlogApi setUserId(String userId) {
        this.userId = userId;
        return this;
    }
}