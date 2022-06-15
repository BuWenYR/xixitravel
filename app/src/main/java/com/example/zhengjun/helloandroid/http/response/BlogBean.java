package com.example.zhengjun.helloandroid.http.response;

import android.util.Log;

import java.io.Serializable;
import java.util.List;

public class BlogBean implements Serializable {

    private Integer id;
    private UserBean user;
    private Long date;
    private String title;
    private String content;
    private List<ImagesBean> images;
    private List<UserBean> collectUsers;
    private List<UserBean> favouriteUsers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public List<UserBean> getCollectUsers() {
        return collectUsers;
    }

    public void setCollectUsers(List<UserBean> collectUsers) {
        this.collectUsers = collectUsers;
    }

    public List<UserBean> getFavouriteUsers() {
        return favouriteUsers;
    }

    public void setFavouriteUsers(List<UserBean> favouriteUsers) {
        this.favouriteUsers = favouriteUsers;
    }

    public static class UserBean implements Serializable {
        private Integer id;
        private String username;
        private String role;
        private UserInfoBean userInfo;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }

        public static class UserInfoBean {
            private Integer id;
            private Integer sex;
            private String nickName;
            private Object sign;
            private String avatar;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Integer getSex() {
                return sex;
            }

            public void setSex(Integer sex) {
                this.sex = sex;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public Object getSign() {
                return sign;
            }

            public void setSign(Object sign) {
                this.sign = sign;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }
    }

    public static class ImagesBean implements Serializable {
        private Integer id;
        private String imagePath;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }
    }
}
