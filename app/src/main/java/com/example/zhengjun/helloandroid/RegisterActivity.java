package com.example.zhengjun.helloandroid;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zhengjun.helloandroid.base.BaseActivity;
import com.example.zhengjun.helloandroid.http.model.HttpData;
import com.example.zhengjun.helloandroid.http.request.LoginApi;
import com.example.zhengjun.helloandroid.http.request.RegisterApi;
import com.example.zhengjun.helloandroid.http.response.LoginBean;
import com.example.zhengjun.helloandroid.utils.SPUtils;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;


public class RegisterActivity extends BaseActivity {
    private EditText userName, passWord, rePassword;
    private TextView userLogin;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        ViewClick();
    }

    private void ViewClick() {

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin.setTextColor(Color.rgb(0, 0, 0));
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = userName.getText().toString();
                final String password = passWord.getText().toString();
                final String repassword = rePassword.getText().toString();
                if (username.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "??????????????????", Toast.LENGTH_LONG).show();
                    return;
                } else if (password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "??????????????????", Toast.LENGTH_LONG).show();
                    return;
                } else if (!password.equals(repassword)) {
                    Toast.makeText(getApplicationContext(), "???????????????????????????", Toast.LENGTH_LONG).show();
                    return;
                }
                EasyHttp.post(RegisterActivity.this)
                        .api(new RegisterApi()
                                .setUsername(username)
                                .setPassword(password))
                        .request(new HttpCallback<HttpData<LoginBean>>(RegisterActivity.this) {


                            @Override
                            public void onSucceed(HttpData<LoginBean> data) {
                                Toast.makeText(getApplicationContext(), "????????????????????????????????????", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFail(Exception e) {
                                super.onFail(e);
                                Toast.makeText(RegisterActivity.this, "????????????", Toast.LENGTH_SHORT).show();

                            }
                        });


            }
        });
    }

    private void init() {
        userName = (EditText) findViewById(R.id.et_userName);
        passWord = (EditText) findViewById(R.id.et_password);
        rePassword = (EditText) findViewById(R.id.et_repassword);
        userLogin = (TextView) findViewById(R.id.link_signup);
        register = (Button) findViewById(R.id.btn_login);
    }

    //???BACK?????????????????????
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}