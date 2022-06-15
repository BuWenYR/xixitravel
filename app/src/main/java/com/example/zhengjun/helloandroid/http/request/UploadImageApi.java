package com.example.zhengjun.helloandroid.http.request;


import com.hjq.http.config.IRequestApi;

import java.io.File;


//上传图片
public class UploadImageApi implements IRequestApi {

    @Override
    public String getApi() {
        return "fileUpload";
    }
    private File file;

    public UploadImageApi setFile(File file) {
        this.file = file;
        return this;
    }
}