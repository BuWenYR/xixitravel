package com.example.zhengjun.helloandroid.model;

public class Selected {
    private String id = "";                 //id
    private String title = "";              //标题
    private String intro = "";              //文章简介
    private String image = "";              //标题大图
    private String create_time ="";         //创建时间
    private int collection_number = 0;      //收藏人数
    private String is_collect = "";         //当前用户是否收藏
    public Selected(){     }
    public Selected(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getCollection_number() {
        return collection_number;
    }

    public void setCollection_number(int collection_number) {
        this.collection_number = collection_number;
    }

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }
}
