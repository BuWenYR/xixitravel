package com.example.zhengjun.helloandroid.http.request;


import com.hjq.http.config.IRequestApi;

//收藏或者取消收藏
public class CollectionBlogApi implements IRequestApi {
    @Override
    public String getApi() {
        return "foodLog/collect";
    }

    String foodLogId;

    public CollectionBlogApi setFoodLogId(String foodLogId) {
        this.foodLogId = foodLogId;
        return this;
    }
}