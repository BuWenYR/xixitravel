package com.example.zhengjun.helloandroid.http.model;

/**
 * desc   : 统一接口数据结构
 */
public class HttpData<T> {


    /**
     * 接口请求返回状态
     */
    private int code;
    /**
     * 数据
     */
    private T value;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}