package com.example.zhengjun.helloandroid.http.request;


import com.hjq.http.config.IRequestApi;


//根据id查询日志详情
public class GetBlogDetailApi implements IRequestApi {
    @Override
    public String getApi() {
        return "foodLog/findOne";
    }

    String id;

    public GetBlogDetailApi setId(String id) {
        this.id = id;
        return this;
    }
}