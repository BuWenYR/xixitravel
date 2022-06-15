package com.example.zhengjun.helloandroid;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.zhengjun.helloandroid.base.BaseActivity;
import com.example.zhengjun.helloandroid.http.model.HttpData;
import com.example.zhengjun.helloandroid.http.model.UrlContant;
import com.example.zhengjun.helloandroid.http.request.AddBlogApi;
import com.example.zhengjun.helloandroid.http.request.UploadImageApi;
import com.example.zhengjun.helloandroid.utils.StringUtils;
import com.example.zhengjun.helloandroid.view.album.AlbumViewPager;
import com.example.zhengjun.helloandroid.view.album.FilterImageView;
import com.example.zhengjun.helloandroid.view.album.MatrixImageView;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 发布日志
 */
public class PublishBlogActivity extends BaseActivity implements
        OnClickListener, MatrixImageView.OnSingleTapListener {
    public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;

    private ImageView mBack;// 返回键
    private TextView mSend;// 发送
    private EditText mContent;// 动态内容编辑框
    private EditText mTitle;
    private InputMethodManager imm;// 软键盘管理
    private TextView textRemain;// 字数提示
    private TextView picRemain;// 图片数量提示
    private ImageView add;// 添加图片按钮
    private LinearLayout picContainer;// 图片容器
    HorizontalScrollView scrollView;// 滚动的图片容器
    View editContainer;// 动态编辑部分
    View pagerContainer;// 图片显示部分
    private ProgressBar uploadProgressBar;

    // 显示大图的viewpager 集成到了Actvity中 下面是和viewpager相关的控件
    AlbumViewPager viewpager;// 大图显示pager
    ImageView mBackView;// 返回/关闭大图
    TextView mCountView;// 大图数量提示
    View mHeaderBar;// 大图顶部栏
    ImageView delete;// 删除按钮
    private String art_id;
    int size;// 小图大小
    int padding;// 小图间距
    private int comments_sort;

    private TextView title;
    private List<String> filePaths = new ArrayList<>();       // 图片文件路径
    private List<String> networkPaths = new ArrayList<>();    // 图片网络路径


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_dynamic);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        initViews();
        initData();
    }

    /**
     * @Description： 初始化Views
     */
    private void initViews() {
        // TODO Auto-generated method stub
        title = (TextView) findViewById(R.id.publish_title);
        mTitle = findViewById(R.id.post_title);
        title.setText(getIntent().getExtras().getString("publish_title"));
        comments_sort = getIntent().getExtras().getInt("publish_sort");
        mBack = (ImageView) findViewById(R.id.post_back);
        mSend = (TextView) findViewById(R.id.post_send);
        uploadProgressBar = (ProgressBar) findViewById(R.id.upload_progress);
        mContent = (EditText) findViewById(R.id.post_content);
        textRemain = (TextView) findViewById(R.id.post_text_remain);
        picRemain = (TextView) findViewById(R.id.post_pic_remain);
        add = (ImageView) findViewById(R.id.post_add_pic);
        picContainer = (LinearLayout) findViewById(R.id.post_pic_container);
        scrollView = (HorizontalScrollView)
                findViewById(R.id.post_scrollview);
        art_id = getIntent().getExtras().getString("art_id");
        viewpager = (AlbumViewPager) findViewById(R.id.albumviewpager);
        mBackView = (ImageView) findViewById(R.id.header_bar_photo_back);
        mCountView = (TextView) findViewById(R.id.header_bar_photo_count);
        mHeaderBar = findViewById(R.id.album_item_header_bar);
        delete = (ImageView) findViewById(R.id.header_bar_photo_delete);
        editContainer = findViewById(R.id.post_edit_container);
        pagerContainer = findViewById(R.id.pagerview);
        delete.setVisibility(View.VISIBLE);

        viewpager.setOnPageChangeListener(pageChangeListener);
        viewpager.setOnSingleTapListener(this);
        mBackView.setOnClickListener(this);
        mCountView.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mSend.setOnClickListener(this);
        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        //监听内容输入框中的文字变化
        mContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable content) {
                textRemain.setText(content.toString().length() + "/140");
            }
        });
    }

    private void initData() {
        size = (int) getResources().getDimension(R.dimen.size_80);
        padding = (int) getResources().getDimension(R.dimen.padding_10);
    }

    @Override
    public void onBackPressed() {
        if (pagerContainer.getVisibility() != View.VISIBLE) {
//			 showSaveDialog();
        } else {
            hideViewPager();
        }
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.post_back:
                finish();
                break;
            case R.id.header_bar_photo_back:
            case R.id.header_bar_photo_count:
                hideViewPager();
                break;
            case R.id.header_bar_photo_delete:
                final int index = viewpager.getCurrentItem();
                Builder builder = new Builder(
                        PublishBlogActivity.this);
                builder.setMessage("要删除这张照片吗?");
                builder.setTitle("提示");
                builder.setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                filePaths.remove(index);
                                if (filePaths.size() == 6) {
                                    add.setVisibility(View.GONE);
                                } else {
                                    add.setVisibility(View.VISIBLE);
                                }
                                if (filePaths.size() == 0) {
                                    hideViewPager();
                                }
                                picContainer.removeView(picContainer.getChildAt(index));

                                picRemain.setText(filePaths.size() + "/6");
                                mCountView.setText((viewpager.getCurrentItem() + 1)
                                        + "/" + filePaths.size());
                                viewpager.getAdapter().notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
                break;
            case R.id.post_send:
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                String content = mContent.getText().toString();
                if (StringUtils.isEmpty(content) && filePaths.isEmpty()) {
                    Toast.makeText(this, "请添写动态内容或至少添加一张图片", Toast.LENGTH_SHORT)
                            .show();
                    return;
                } else {
                    // 设置为不可点击，防止重复提交
                    view.setEnabled(false);
                    publish_blog();

                }
                break;
            case R.id.post_add_pic:
                Matisse.from(this)
                        .choose(MimeType.ofImage(), false)
                        .countable(true)
                        .maxSelectable(6 - filePaths.size())
                        .thumbnailScale(0.8f)
                        .theme(R.style.Matisse_Zhihu)
                        .imageEngine(new GlideEngine())
                        .setOnSelectedListener((uriList, pathList) -> {
                            int length = pathList.size();
                            if (pathList.size() > 0 && filePaths.contains(pathList.get(length - 1))) {
                                Log.e("selected", pathList.get(length - 1));
                                pathList.remove(length - 1);
                                uriList.remove(length - 1);
                            }
                        })
                        .showSingleMediaType(true)
                        .forResult(REQUEST_CODE_GETIMAGE_BYCROP);
                break;
            default:
                if (view instanceof FilterImageView) {
                    for (int i = 0; i < picContainer.getChildCount(); i++) {
                        if (view == picContainer.getChildAt(i)) {
                            showViewPager(i);
                        }
                    }
                }
                break;
        }
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (viewpager.getAdapter() != null) {
                String text = (position + 1) + "/"
                        + viewpager.getAdapter().getCount();
                mCountView.setText(text);
            } else {
                mCountView.setText("0/0");
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }
    };

    // 显示大图pager
    private void showViewPager(int index) {
        pagerContainer.setVisibility(View.VISIBLE);
        editContainer.setVisibility(View.GONE);
        viewpager.setAdapter(viewpager.new LocalViewPagerAdapter(filePaths));
        viewpager.setCurrentItem(index);
        mCountView.setText((index + 1) + "/" + filePaths.size());
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation((float) 0.9, 1,
                (float) 0.9, 1, pagerContainer.getWidth() / 2,
                pagerContainer.getHeight() / 2);
        scaleAnimation.setDuration(200);
        set.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation((float) 0.1, 1);
        alphaAnimation.setDuration(200);
        set.addAnimation(alphaAnimation);
        pagerContainer.startAnimation(set);
    }

    // 关闭大图显示
    private void hideViewPager() {
        pagerContainer.setVisibility(View.GONE);
        editContainer.setVisibility(View.VISIBLE);
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, (float) 0.9, 1,
                (float) 0.9, pagerContainer.getWidth() / 2,
                pagerContainer.getHeight() / 2);
        scaleAnimation.setDuration(200);
        set.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(200);
        set.addAnimation(alphaAnimation);
        pagerContainer.startAnimation(set);
    }

    @Override
    public void onSingleTap() {
        hideViewPager();
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
                    // 延迟滑动至最右边
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                        }
                    }, 50L);
                }
                break;
            default:
                break;
        }
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
                        LayoutParams params = new LayoutParams(size, size);
                        params.rightMargin = padding;
                        FilterImageView imageView = new FilterImageView(PublishBlogActivity.this);

                        imageView.setLayoutParams(params);
                        imageView.setScaleType(ScaleType.CENTER_CROP);
                        Glide.with(PublishBlogActivity.this).load(UrlContant.IMAGE_URL + value).into(imageView);

                        imageView.setOnClickListener(PublishBlogActivity.this);

                        filePaths.add(value);
                        if (filePaths.size() == 6) {
                            add.setVisibility(View.GONE);
                        } else {
                            add.setVisibility(View.VISIBLE);
                        }
                        picContainer.addView(imageView, picContainer.getChildCount() - 1);
                        picRemain.setText(filePaths.size() + "/6");
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        Toast.makeText(PublishBlogActivity.this, "上传图片失败", Toast.LENGTH_SHORT).show();

                    }
                });
    }


    public void publish_blog() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < filePaths.size(); i++) {
            stringBuilder.append(filePaths.get(i));

            stringBuilder.append(",");

        }

        EasyHttp.post(this)
                .api(new AddBlogApi()
                        .setContent(mContent.getText().toString())
                        .setTitle(mTitle.getText().toString())
                        .setImages(stringBuilder.toString().substring(0, stringBuilder.length() - 1))
                )
                .request(new HttpCallback<HttpData<String>>(this) {


                    @Override
                    public void onSucceed(HttpData<String> data) {
                        Toast.makeText(PublishBlogActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);

                    }
                });
    }


}
