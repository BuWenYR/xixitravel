package com.example.zhengjun.helloandroid.http.request;


import com.hjq.http.config.IRequestApi;

//根据用户id查询发布的日志
public class GetAllBlogApi implements IRequestApi {

    @Override
    public String getApi() {
        return "foodLog/all";
    }

    String userId;

    public GetAllBlogApi setUserId(String userId) {
        this.userId = userId;
        return this;
    }
}