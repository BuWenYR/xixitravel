package com.example.zhengjun.helloandroid.http.request;


import com.hjq.http.config.IRequestApi;

//根据关键字搜索
public class SearchBlogApi implements IRequestApi {
    @Override
    public String getApi() {
        return "foodLog/all";
    }

    String keyword;

    public SearchBlogApi setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }
}