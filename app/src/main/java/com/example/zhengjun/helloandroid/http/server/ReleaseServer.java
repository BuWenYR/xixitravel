package com.example.zhengjun.helloandroid.http.server;

import com.example.zhengjun.helloandroid.http.model.UrlContant;
import com.hjq.http.config.IRequestServer;
import com.hjq.http.config.IRequestType;
import com.hjq.http.model.BodyType;

/**
 * desc   : 正式环境
 */
public class ReleaseServer implements IRequestServer, IRequestType {

    @Override
    public String getHost() {
        return "http://121.199.40.253:98/";
    }

    @Override
    public String getPath() {
        return "";
    }

    @Override
    public BodyType getType() {
        return BodyType.FORM;
    }
}