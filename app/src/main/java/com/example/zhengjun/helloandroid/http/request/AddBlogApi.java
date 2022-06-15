package com.example.zhengjun.helloandroid.http.request;


import com.hjq.http.config.IRequestApi;


//添加日志请求
public class AddBlogApi implements IRequestApi {
    @Override
    public String getApi() {
        return "foodLog/create";
    }

    String title;
    String content;
    String images;

    public AddBlogApi setTitle(String title) {
        this.title = title;
        return this;
    }

    public AddBlogApi setContent(String content) {
        this.content = content;
        return this;
    }

    public AddBlogApi setImages(String images) {
        this.images = images;
        return this;
    }
}