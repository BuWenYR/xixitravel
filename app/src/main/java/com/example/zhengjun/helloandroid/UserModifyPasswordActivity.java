package com.example.zhengjun.helloandroid;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zhengjun.helloandroid.base.BaseActivity;
import com.example.zhengjun.helloandroid.http.model.HttpData;
import com.example.zhengjun.helloandroid.http.request.ChangePasswordApi;
import com.example.zhengjun.helloandroid.http.response.LoginBean;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;


public class UserModifyPasswordActivity extends BaseActivity {
    EditText edOPW, edNPW, edNPW2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_modify_password);

        edOPW = findViewById(R.id.user_ed_oldPW);
        edNPW = findViewById(R.id.user_ed_newPW);
        edNPW2 = findViewById(R.id.user_ed_newPW2);
        findViewById(R.id.change_info_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.user_btn_modifyPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edOPW.getText().toString())) {
                    Toast.makeText(UserModifyPasswordActivity.this, "请输入老密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edNPW.getText().toString())) {
                    Toast.makeText(UserModifyPasswordActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!edNPW.getText().toString().equals(edNPW2.getText().toString())) {
                    Toast.makeText(UserModifyPasswordActivity.this, "两次新密码输入不一样", Toast.LENGTH_SHORT).show();
                    return;
                }
                EasyHttp.post(UserModifyPasswordActivity.this)
                        .api(new ChangePasswordApi()
                                .setNewPassword(edNPW.getText().toString())
                                .setOldPassword(edOPW.getText().toString())
                        )
                        .request(new HttpCallback<HttpData<LoginBean.UserInfoBean>>(UserModifyPasswordActivity.this) {
                            @Override
                            public void onSucceed(HttpData<LoginBean.UserInfoBean> data) {
                                Toast.makeText(UserModifyPasswordActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFail(Exception e) {
                                super.onFail(e);
                                Toast.makeText(UserModifyPasswordActivity.this, "密码修改失败", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}