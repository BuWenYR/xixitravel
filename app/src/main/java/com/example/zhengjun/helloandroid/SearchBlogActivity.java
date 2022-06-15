package com.example.zhengjun.helloandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.zhengjun.helloandroid.adapter.ImageAdapter;
import com.example.zhengjun.helloandroid.base.BaseActivity;
import com.example.zhengjun.helloandroid.http.model.HttpData;
import com.example.zhengjun.helloandroid.http.model.UrlContant;
import com.example.zhengjun.helloandroid.http.request.SearchBlogApi;
import com.example.zhengjun.helloandroid.http.response.BlogBean;
import com.example.zhengjun.helloandroid.view.NoScrollGridView;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.List;

//搜索页面
public class SearchBlogActivity extends BaseActivity {
    private RelativeLayout mRlSearch;
    private ImageButton mImageBtnLeft;
    private EditText mEdtSearch;
    private LinearLayout mLlResult;
    private RecyclerView mRvSearchResult;
    private RelativeLayout mRlNoData;
    private BaseQuickAdapter mBlogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_blog);
        initView();
    }

    private void initView() {
        mRlSearch = (RelativeLayout) findViewById(R.id.rlSearch);
        mImageBtnLeft = (ImageButton) findViewById(R.id.image_btn_left);
        mImageBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEdtSearch = (EditText) findViewById(R.id.edtSearch);
        mLlResult = (LinearLayout) findViewById(R.id.llResult);
        mRvSearchResult = (RecyclerView) findViewById(R.id.rvSearchResult);
        mRvSearchResult.setLayoutManager(new LinearLayoutManager(this));
        mBlogAdapter = new BaseQuickAdapter<BlogBean, BaseViewHolder>(R.layout.item_home_blog) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, BlogBean blogBean) {
                baseViewHolder.setVisible(R.id.image_container, true);
                DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
                float density = dm.density;
                int allWidth = (int) (110 * blogBean
                        .getImages().size() * density);
                int itemWidth = (int) (100 * density);
                NoScrollGridView image = baseViewHolder.getView(R.id.image_grid_view);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        allWidth, LinearLayout.LayoutParams.FILL_PARENT);
                image.setLayoutParams(params);
                image.setColumnWidth(itemWidth);
                image.setHorizontalSpacing(10);
                image.setStretchMode(GridView.NO_STRETCH);

                image.setNumColumns(blogBean.getImages().size());
                image.setAdapter(new ImageAdapter(getContext(), blogBean.getImages()));
                image.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position1, long id) {
                        Intent intent = new Intent(getContext(), ImageActivity.class);
                        intent.putExtra("image_path", blogBean.getImages().get(position1).getImagePath());
                        startActivity(intent);
                    }
                });
                baseViewHolder.setText(R.id.comment_detail, blogBean.getContent());
                baseViewHolder.setText(R.id.collect_num, blogBean.getCollectUsers().size() + "");
                baseViewHolder.setText(R.id.fav_num, blogBean.getFavouriteUsers().size() + "");
                baseViewHolder.setText(R.id.comment_user_name, blogBean.getUser().getUsername());
                baseViewHolder.setText(R.id.publish_time, new SimpleDateFormat("yyyy-MM-dd hh:mm").format(blogBean.getDate()));
                Glide.with(getContext()).load(UrlContant.IMAGE_URL + blogBean.getUser().getUserInfo().getAvatar())
                        .error(R.drawable.logo_travel).placeholder(R.drawable.logo_travel)
                        .into((ImageView) baseViewHolder.getView(R.id.comments_user_photo));
            }


        };
        mRvSearchResult.setAdapter(mBlogAdapter);
        mBlogAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BlogBean blogBean = (BlogBean) adapter.getData().get(position);
                Intent intent = new Intent(SearchBlogActivity.this, BlogDetailActivity.class);
                intent.putExtra("blog_id", blogBean.getId() + "");
                startActivity(intent);
            }
        });
        mRlNoData = (RelativeLayout) findViewById(R.id.rlNoData);
        //监听软件盘上面的搜索按钮，点击执行搜索
        mEdtSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                String key = mEdtSearch.getText().toString().trim();
                if (TextUtils.isEmpty(key)) {
                    Toast.makeText(this, "请输入关键字", Toast.LENGTH_SHORT).show();
                    return true;
                }
                //关闭软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                getData(key);

                return true;
            }
            return false;
        });
        findViewById(R.id.image_btn_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //获取发布列表
    public void getData(String key) {
        EasyHttp.get(this)
                .api(new SearchBlogApi().setKeyword(key))
                .request(new HttpCallback<HttpData<List<BlogBean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<BlogBean>> data) {
                        //结束刷新状态
                        //重新设置数据
                        if (data.getValue().size() > 0) {
                            mLlResult.setVisibility(View.VISIBLE);
                            mRlNoData.setVisibility(View.GONE);
                            mBlogAdapter.setNewInstance(data.getValue());
                        } else {
                            mRlNoData.setVisibility(View.VISIBLE);
                            mLlResult.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
}