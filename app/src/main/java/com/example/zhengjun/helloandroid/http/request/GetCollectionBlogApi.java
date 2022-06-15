package com.example.zhengjun.helloandroid.http.request;


import com.hjq.http.config.IRequestApi;

//根据用户id查询收藏的日志
public class GetCollectionBlogApi implements IRequestApi {
    @Override
    public String getApi() {
        return "foodLog/myCollectFoodLog";
    }

    String userId;

    public GetCollectionBlogApi setUserId(String userId) {
        this.userId = userId;
        return this;
    }
}