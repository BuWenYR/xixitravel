package com.example.zhengjun.helloandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhengjun.helloandroid.base.BaseActivity;
import com.example.zhengjun.helloandroid.http.model.HttpData;
import com.example.zhengjun.helloandroid.http.request.LoginApi;
import com.example.zhengjun.helloandroid.http.response.LoginBean;
import com.example.zhengjun.helloandroid.utils.SPUtils;
import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;


public class LoginActivity extends BaseActivity {
    private EditText userName, passWord;
    private TextView userReg;
    private Button login;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();//初始化组件
        ViewClick();//注册组件点击事件
    }

    private void init() {
        userName = (EditText) findViewById(R.id.et_userName);
        passWord = (EditText) findViewById(R.id.et_password);
        userReg = (TextView) findViewById(R.id.link_signup);
        login = (Button) findViewById(R.id.btn_login);

    }

    private void login() {
        final String username = userName.getText().toString();
        final String password = passWord.getText().toString();

        if (username.isEmpty()) {
            Toast.makeText(getApplicationContext(), "帐号不能为空", Toast.LENGTH_LONG).show();
            return;
        } else if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        EasyHttp.post(this)
                .api(new LoginApi()
                        .setUsername(username)
                        .setPassword(password))
                .request(new HttpCallback<HttpData<LoginBean>>(this) {


                    @Override
                    public void onSucceed(HttpData<LoginBean> data) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        //保存用户信息到本地
                        SPUtils.setObject("user", data.getValue(), LoginActivity.this);
                        //保存登录状态为已登录
                        SPUtils.put(LoginActivity.this, "login", true);
                        EasyConfig.getInstance().addHeader("authorization",data.getValue().getToken());
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void goReg() {
        userReg.setTextColor(Color.rgb(0, 0, 0));
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void ViewClick() {
        //注册组件点击事件
        userReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goReg();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            System.exit(0);
            return;
        } else {
            Toast.makeText(getBaseContext(), "再按一次返回退出程序", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }
}
