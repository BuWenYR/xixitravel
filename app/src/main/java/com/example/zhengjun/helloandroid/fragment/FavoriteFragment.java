package com.example.zhengjun.helloandroid.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.zhengjun.helloandroid.BlogDetailActivity;
import com.example.zhengjun.helloandroid.ImageActivity;
import com.example.zhengjun.helloandroid.R;
import com.example.zhengjun.helloandroid.adapter.ImageAdapter;
import com.example.zhengjun.helloandroid.base.BaseFragment;
import com.example.zhengjun.helloandroid.http.model.HttpData;
import com.example.zhengjun.helloandroid.http.model.UrlContant;
import com.example.zhengjun.helloandroid.http.request.GetFavBlogApi;
import com.example.zhengjun.helloandroid.http.response.BlogBean;
import com.example.zhengjun.helloandroid.view.NoScrollGridView;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

//点赞日志列表
public class FavoriteFragment extends BaseFragment {
    private RecyclerView bloglist;
    private List<BlogBean> blogListModels;
    private TextView blog_null;
    private View inflate;
    private BaseQuickAdapter<BlogBean, BaseViewHolder> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_collection, container, false);
        init();
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    public void init() {
        blog_null = (TextView) inflate.findViewById(R.id.blog_null);
        blogListModels = new ArrayList<BlogBean>();
        bloglist = (RecyclerView) inflate.findViewById(R.id.fragment_blog_list);
        bloglist.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BaseQuickAdapter<BlogBean, BaseViewHolder>(R.layout.item_home_blog) {
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
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(getContext(), ImageActivity.class);
                        intent.putExtra("image_path", blogBean.getImages().get(position1).getImagePath());
                        getActivity().startActivity(intent);
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
        bloglist.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(getActivity(), BlogDetailActivity.class);
                intent.putExtra("blog_id", blogListModels.get(position).getId() + "");
                startActivity(intent);
            }
        });
    }

    public void getData() {
        EasyHttp.get(this)
                .api(new GetFavBlogApi())
                .request(new HttpCallback<HttpData<List<BlogBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<BlogBean>> data) {
                        blogListModels.clear();
                        blogListModels.addAll(data.getValue());
                        if (blogListModels.size() > 0) {
                            bloglist.setVisibility(View.VISIBLE);
                            blog_null.setVisibility(View.GONE);
                            adapter.setNewInstance(data.getValue());
                        } else {
                            blog_null.setVisibility(View.VISIBLE);
                            bloglist.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
}