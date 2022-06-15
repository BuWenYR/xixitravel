package com.example.zhengjun.helloandroid.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hjq.http.listener.OnHttpListener;
import com.wega.library.loadingDialog.LoadingDialog;

import okhttp3.Call;

/**
 * 基础类 处理一些公共方法
 */
public class BaseActivity extends AppCompatActivity implements OnHttpListener {
    private LoadingDialog loadingDialog;

    /**
     * 显示加载框
     */
    public void showLodinDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.setCancelable(true);
        }
        loadingDialog.loading();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 隐藏加载框
     */
    public void hideLodingDialog() {
        if (loadingDialog!=null&&loadingDialog.isShowing()){
            loadingDialog.cancel();
        }
    }




    @Override
    public void onSucceed(Object result) {
        hideLodingDialog();

    }

    @Override
    public void onFail(Exception e) {
        hideLodingDialog();

    }

    @Override
    public void onStart(Call call) {
        showLodinDialog();
    }

    @Override
    public void onEnd(Call call) {
        hideLodingDialog();
    }
}
