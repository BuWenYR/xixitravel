package com.example.zhengjun.helloandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhengjun.helloandroid.base.BaseActivity;
import com.example.zhengjun.helloandroid.http.model.HttpData;
import com.example.zhengjun.helloandroid.http.model.UrlContant;
import com.example.zhengjun.helloandroid.http.request.ChangeUserInfoApi;
import com.example.zhengjun.helloandroid.http.request.GetUserInfoApi;
import com.example.zhengjun.helloandroid.http.request.UploadImageApi;
import com.example.zhengjun.helloandroid.http.response.LoginBean;
import com.example.zhengjun.helloandroid.popwindow.MyDialog;
import com.example.zhengjun.helloandroid.popwindow.SelectSexPopwindow;
import com.example.zhengjun.helloandroid.utils.SPUtils;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.List;

public class ChangeMineInfoActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout set_nickname;
    private RelativeLayout set_sex;
    private RelativeLayout set_signature;
    private TextView nickname;
    private TextView sex;
    private TextView signature;
    private SelectSexPopwindow selectsex;
    private ImageView back;
    private Button exit;
    private LoginBean.UserInfoBean userInfoBean;
    private ImageView ivMeIcon;
    public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_mineinfo);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    public void init() {
        exit = (Button) findViewById(R.id.exit);
        exit.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.change_info_back);
        back.setOnClickListener(this);
        nickname = (TextView) findViewById(R.id.nickname);
        sex = (TextView) findViewById(R.id.sex);
        signature = (TextView) findViewById(R.id.signature);
        set_nickname = (RelativeLayout) findViewById(R.id.set_nickname);
        set_sex = (RelativeLayout) findViewById(R.id.set_sex);
        set_signature = (RelativeLayout) findViewById(R.id.set_signature);
        set_nickname.setOnClickListener(this);
        set_sex.setOnClickListener(this);
        set_signature.setOnClickListener(this);
        ivMeIcon = findViewById(R.id.iv_me_icon);
        findViewById(R.id.user_rel_accountPW).setOnClickListener(this);
        findViewById(R.id.fl_person_data_head).setOnClickListener(this);
        selectsex = new SelectSexPopwindow(ChangeMineInfoActivity.this, new
                SelectSexPopwindow.PopwindowListener() {
                    @Override
                    public void back(String result) {
                        // TODO Auto-generated method stub
                        sex.setText(result);
                        if (result.equals("保密"))
                            userInfoBean.setSex(0);
                        else if (result.equals("男"))
                            userInfoBean.setSex(1);
                        else if (result.equals("女"))
                            userInfoBean.setSex(2);

                        sendData();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.set_nickname:
                MyDialog myDialog = new MyDialog(this, "编辑昵称", new
                        MyDialog.OnCustomDialogListener() {
                            @Override
                            public void back(String name) {
                                // TODO Auto-generated method stub
                                if (name.length() > 0 && !name.equals("null") && name.length() < 6) {
                                    nickname.setText(name);
                                    userInfoBean.setNickName(name);
                                    sendData();
                                } else {
                                    Toast.makeText(getApplicationContext(), "昵称过长,修改失败",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                myDialog.show();
                break;
            case R.id.set_sex:

                selectsex.showAtLocation(ChangeMineInfoActivity.this.findViewById(R.id.main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.user_rel_accountPW:
                startActivity(new Intent(ChangeMineInfoActivity.this, UserModifyPasswordActivity.class));
                break;
            case R.id.fl_person_data_head:
                Matisse.from(this)
                        .choose(MimeType.ofImage(), false)
                        .countable(true)
                        .maxSelectable(1)
                        .thumbnailScale(0.8f)
                        .theme(R.style.Matisse_Zhihu)
                        .imageEngine(new GlideEngine())
                        .setOnSelectedListener((uriList, pathList) -> {
                            int length = pathList.size();

                        })
                        .showSingleMediaType(true)
                        .forResult(REQUEST_CODE_GETIMAGE_BYCROP);
                break;
            case R.id.set_signature:
                MyDialog myDialog1 = new MyDialog(this, "编辑签名", new
                        MyDialog.OnCustomDialogListener() {
                            @Override
                            public void back(String sign) {
                                // TODO Auto-generated method stub
                                if (sign.length() > 0 && !sign.equals("null") && sign.length() < 6) {
                                    signature.setText(sign);
                                    userInfoBean.setNickName(sign);
                                    sendData();
                                } else {
                                    Toast.makeText(getApplicationContext(), "昵称过长,修改失败",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                myDialog1.show();
                break;

            case R.id.change_info_back:
                finish();
                break;
            case R.id.exit://退出登录
                //设置登录状态为false
                SPUtils.put(ChangeMineInfoActivity.this, "login", false);
                //清空本地保存用户信息
                SPUtils.remove(ChangeMineInfoActivity.this, "user");
                Intent intent = new Intent(ChangeMineInfoActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_GETIMAGE_BYCROP:
                if (resultCode == RESULT_OK) {
                    List<String> files = Matisse.obtainPathResult(data);
                    for (int i = 0; i < files.size(); i++) {

                        uploadFile(new File(files.get(i)));
                    }
                    break;
                }
        }
    }


    //修改用户信息
    public void sendData() {
        EasyHttp.post(this)
                .api(new ChangeUserInfoApi()
                        .setSex(userInfoBean.getSex())
                        .setAvatar(userInfoBean.getAvatar())
                        .setNickname(userInfoBean.getNickName())
                        .setSign(userInfoBean.getSign()))
                .request(new HttpCallback<HttpData<LoginBean>>(this) {

                    @Override
                    public void onSucceed(HttpData<LoginBean> data) {
                        Toast.makeText(ChangeMineInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        getUserInfo();
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        Toast.makeText(ChangeMineInfoActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void uploadFile(File file) {
        EasyHttp.post(this)
                .api(new UploadImageApi()
                        .setFile(file)
                )
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> data) {
                        String value = data.getValue();
                        Glide.with(ChangeMineInfoActivity.this).load(UrlContant.IMAGE_URL + value).into(ivMeIcon);
                        userInfoBean.setAvatar(value);
                        sendData();
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        Toast.makeText(ChangeMineInfoActivity.this, "上传图片失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //获取用户信息
    public void getUserInfo() {
        EasyHttp.get(this)
                .api(new GetUserInfoApi()
                )
                .request(new HttpCallback<HttpData<LoginBean.UserInfoBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<LoginBean.UserInfoBean> data) {
                        LoginBean.UserInfoBean value = data.getValue();
                        userInfoBean = value;
                        if (value.getSex() == 0) {
                            sex.setText("保密");
                        } else if (value.getSex() == 1) {
                            sex.setText("男");
                        } else if (value.getSex() == 2) {
                            sex.setText("女");
                        }
                        nickname.setText(value.getNickName());
                        signature.setText(value.getSign());
                        Glide.with(ChangeMineInfoActivity.this).load(UrlContant.IMAGE_URL + value.getAvatar()).into(ivMeIcon);
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        Toast.makeText(ChangeMineInfoActivity.this, "查询信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
