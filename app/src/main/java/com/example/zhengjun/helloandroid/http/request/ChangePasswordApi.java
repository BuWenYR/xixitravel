package com.example.zhengjun.helloandroid.http.request;


import com.hjq.http.config.IRequestApi;


//修改密码请求
public class ChangePasswordApi implements IRequestApi {
   @Override
   public String getApi() {
       return "user/changePassword";
   }

   String oldPassword;
   String newPassword;

   public ChangePasswordApi setOldPassword(String changePassword) {
       this.oldPassword = changePassword;
       return this;
   }

   public ChangePasswordApi setNewPassword(String newPassword) {
       this.newPassword = newPassword;
       return this;
   }
}