package com.example.zhengjun.helloandroid.http.request;


import com.hjq.http.config.IRequestApi;


//点赞或者取消点赞
public class FavBlogApi implements IRequestApi {
    @Override
    public String getApi() {
        return "foodLog/favourite";
    }

    String foodLogId;

    public FavBlogApi setFoodLogId(String foodLogId) {
        this.foodLogId = foodLogId;
        return this;
    }
}