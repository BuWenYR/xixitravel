package com.example.zhengjun.helloandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhengjun.helloandroid.adapter.ImageAdapter;
import com.example.zhengjun.helloandroid.base.BaseActivity;
import com.example.zhengjun.helloandroid.http.model.HttpData;
import com.example.zhengjun.helloandroid.http.model.UrlContant;
import com.example.zhengjun.helloandroid.http.request.CollectionBlogApi;
import com.example.zhengjun.helloandroid.http.request.FavBlogApi;
import com.example.zhengjun.helloandroid.http.request.GetBlogDetailApi;
import com.example.zhengjun.helloandroid.http.response.BlogBean;
import com.example.zhengjun.helloandroid.http.response.LoginBean;
import com.example.zhengjun.helloandroid.utils.SPUtils;
import com.example.zhengjun.helloandroid.view.CircleImageView;
import com.example.zhengjun.helloandroid.view.NoScrollGridView;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;


import java.text.SimpleDateFormat;
import java.util.Date;

public class BlogDetailActivity extends BaseActivity implements OnClickListener {
    private CircleImageView blog_user_photo;
    private TextView blog_content;
    private ImageView ivCollection;
    private ImageView ivFav;

    private TextView tvCollectNum;
    private TextView tvFavNum;

    private TextView publish_time;
    private TextView blog_user_name;
    private LinearLayout blog_praise_container;
    private String id;
    private String nickname;
    private ImageView blogDetailBack;
    private ImageView blog_share;
    private String intro = "";
    private int is_collect = 0;
    private int is_fav = 0;
    private NoScrollGridView image;
    private HorizontalScrollView imge_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        init();
        getData(id);
    }

    public void init() {
        blog_share = (ImageView) findViewById(R.id.blog_share);
        ivFav = findViewById(R.id.iv_detail_fav);
        blog_share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        blogDetailBack = (ImageView) findViewById(R.id.blog_detail_back);
        blogDetailBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        id = getIntent().getExtras().getString("blog_id");
        blog_content = (TextView) findViewById(R.id.blog_detail);
        tvFavNum = findViewById(R.id.tv_detail_fav_num);
        blog_user_photo = (CircleImageView) findViewById(R.id.blog_user_image);
        publish_time = (TextView) findViewById(R.id.blog_publish_time);
        blog_user_name = (TextView) findViewById(R.id.blog_user_name);
        ivCollection = (ImageView) findViewById(R.id.blog_praise_label);
        blog_praise_container = (LinearLayout) findViewById(R.id.blog_praise_linearlayout);
        blog_praise_container.setOnClickListener(this);
        tvCollectNum = (TextView) findViewById(R.id.blog_prise_info);
        image = (NoScrollGridView) findViewById(R.id.image_grid_view);
        imge_container = (HorizontalScrollView) findViewById(R.id.image_container);
        ivCollection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCollectionStatus();
            }
        });
        ivFav.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFavStatus();
            }
        });
    }

    public void getData(final String id) {
        EasyHttp.post(this)
                .api(new GetBlogDetailApi()
                        .setId(id))
                .request(new HttpCallback<HttpData<BlogBean>>(this) {

                    @Override
                    public void onSucceed(HttpData<BlogBean> data) {
                        BlogBean value = data.getValue();
                        try {
                            nickname = value.getUser().getUsername();
                            blog_user_name.setText(nickname);
                            imge_container.setVisibility(View.VISIBLE);
                            DisplayMetrics dm = getResources().getDisplayMetrics();
                            // getWindowManager().getDefaultDisplay().getMetrics(dm);
                            float density = dm.density;
                            int allWidth = (int) (110 * value.getImages()
                                    .size() * density);
                            int itemWidth = (int) (100 * density);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    allWidth, LinearLayout.LayoutParams.FILL_PARENT);
                            image.setLayoutParams(params);
                            image.setColumnWidth(itemWidth);
                            image.setHorizontalSpacing(10);
                            image.setStretchMode(GridView.NO_STRETCH);

                            image.setNumColumns(value.getImages().size());
                            image.setAdapter(new ImageAdapter(BlogDetailActivity.this, value.getImages()));
                            image.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position1, long id) {
                                    // TODO Auto-generated method stub
                                    Intent intent = new Intent(BlogDetailActivity.this, ImageActivity.class);
                                    intent.putExtra("image_path", value.getImages().get(position1).getImagePath());
                                    startActivity(intent);
                                }
                            });
                            Glide.with(BlogDetailActivity.this).load(UrlContant.IMAGE_URL + value.getUser().getUserInfo().getAvatar())
                                    .placeholder(R.drawable.logo_travel).error(R.drawable.logo_travel)
                                    .into(blog_user_photo);
                            blog_content.setText(value.getContent());
                            SimpleDateFormat format = new SimpleDateFormat(
                                    "yy.MM.dd hh:mm");
                            publish_time.setText(format.format(new Date(value.getDate())));
                            tvCollectNum.setText(value.getCollectUsers().size() + "收藏了日志");
                            tvFavNum.setText(value.getFavouriteUsers().size() + "觉得很赞");

                            is_collect = 0;
                            is_fav = 0;
                            for (int i = 0; i < value.getFavouriteUsers().size(); i++) {
                                LoginBean user = SPUtils.getObject("user", LoginBean.class, BlogDetailActivity.this);
                                if (value.getFavouriteUsers().get(i).getId() == user.getUserInfo().getId()) {
                                    is_fav++;
                                }
                            }
                            for (int i = 0; i < value.getCollectUsers().size(); i++) {
                                LoginBean user = SPUtils.getObject("user", LoginBean.class, BlogDetailActivity.this);
                                if (value.getCollectUsers().get(i).getId() == user.getUserInfo().getId()) {
                                    is_collect++;
                                }
                            }
                            if (is_fav > 0) {
                                ivFav.setImageResource(R.drawable.prise_active_detail);
                            } else {
                                ivFav.setImageResource(R.drawable.prise_detail);
                            }
                            if (is_collect > 0) {
                                ivCollection.setImageResource(R.drawable.collecting_active);
                            } else {
                                ivCollection.setImageResource(R.drawable.collecting_normal);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);

                    }
                });


    }

    //改变收藏状态
    public void changeCollectionStatus() {
        EasyHttp.post(this)
                .api(new CollectionBlogApi().setFoodLogId(id))
                .request(new HttpCallback<HttpData<Object>>(this) {
                    @Override
                    public void onSucceed(HttpData<Object> data) {
                        Toast.makeText(BlogDetailActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                        getData(id);
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        Toast.makeText(BlogDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();

                    }
                });
    }


    //改变点赞状态
    public void changeFavStatus() {
        EasyHttp.post(this)
                .api(new FavBlogApi().setFoodLogId(id))
                .request(new HttpCallback<HttpData<Object>>(this) {
                    @Override
                    public void onSucceed(HttpData<Object> data) {
                        Toast.makeText(BlogDetailActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                        getData(id);

                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        Toast.makeText(BlogDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

}
