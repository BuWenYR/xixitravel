package com.example.zhengjun.helloandroid.base;

import androidx.fragment.app.Fragment;

import com.hjq.http.listener.OnHttpListener;
import com.wega.library.loadingDialog.LoadingDialog;

import okhttp3.Call;

/**
 * 基础类 处理一些公共方法
 */
public class BaseFragment extends Fragment implements OnHttpListener {
    private LoadingDialog loadingDialog;

    /**
     * 显示加载框
     */
    public void showLodinDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getContext());
            loadingDialog.setCancelable(true);

        }
        loadingDialog.loading();
    }


    /**
     * 隐藏加载框
     */
    public void hideLodingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.cancel();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onEnd(Call call) {
        hideLodingDialog();
    }

    @Override
    public void onSucceed(Object result) {

    }

    @Override
    public void onFail(Exception e) {
        hideLodingDialog();

    }
}
