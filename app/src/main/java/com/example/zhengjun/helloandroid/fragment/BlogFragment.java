package com.example.zhengjun.helloandroid.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhengjun.helloandroid.BlogDetailActivity;
import com.example.zhengjun.helloandroid.PublishBlogActivity;
import com.example.zhengjun.helloandroid.ImageActivity;
import com.example.zhengjun.helloandroid.R;
import com.example.zhengjun.helloandroid.adapter.ImageAdapter;
import com.example.zhengjun.helloandroid.base.BaseFragment;
import com.example.zhengjun.helloandroid.http.model.HttpData;
import com.example.zhengjun.helloandroid.http.model.UrlContant;
import com.example.zhengjun.helloandroid.http.request.GetUserBlogApi;
import com.example.zhengjun.helloandroid.http.response.BlogBean;
import com.example.zhengjun.helloandroid.utils.ACache;
import com.example.zhengjun.helloandroid.utils.BaseClient;
import com.example.zhengjun.helloandroid.view.CircleImageView;
import com.example.zhengjun.helloandroid.view.NoScrollGridView;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

//日志列表
public class BlogFragment extends BaseFragment {
    private Button new_blog;
    private GridView bloglist;
    private List<BlogBean> blogListModels;
    private BlogListAdapter mBlogAdapter;
    private TextView blog_null;
    private ACache aCache;
    private int i = 0;
    private View inflate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_blog, container, false);
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
        bloglist = (NoScrollGridView) inflate.findViewById(R.id.fragment_blog_list);
        bloglist.setFocusable(false);
        bloglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), BlogDetailActivity.class);
                intent.putExtra("blog_id", blogListModels.get(position).getId()+"");
                startActivity(intent);
            }
        });

        new_blog = (Button) inflate.findViewById(R.id.new_blog);
        new_blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), PublishBlogActivity.class);
                intent.putExtra("publish_title", "我的日志");
                intent.putExtra("publish_sort", 0);
                startActivity(intent);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    bloglist.setVisibility(View.VISIBLE);
                    blog_null.setVisibility(View.GONE);
                    mBlogAdapter = new BlogListAdapter(getActivity(), blogListModels);
                    bloglist.setAdapter(mBlogAdapter);
                    break;
                case 1:
                    int position = msg.arg1;
                    blogListModels.remove(position);
                    if (blogListModels.size() == 0) {
                        bloglist.setVisibility(View.GONE);
                        blog_null.setVisibility(View.VISIBLE);
                    }
                    mBlogAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    public void getData() {
        EasyHttp.post(this)
                .api(new GetUserBlogApi())
                .request(new HttpCallback<HttpData<List<BlogBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<BlogBean>> data) {
                        blogListModels.clear();
                        blogListModels.addAll(data.getValue());
                        if (blogListModels.size() > 0) {
                            bloglist.setVisibility(View.VISIBLE);
                            blog_null.setVisibility(View.GONE);
                            mBlogAdapter = new BlogListAdapter(getActivity(), blogListModels);
                            bloglist.setAdapter(mBlogAdapter);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }

    public class BlogListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        public List<BlogBean> _listData;
        private Context mContext;

        public BlogListAdapter(Context context, List<BlogBean> list) {
            _listData = list;
            this.mContext = context;
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return _listData.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return _listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return Long.valueOf(_listData.get(position).getId());
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_item_blog, null);
                holder.comments_contents = (TextView) convertView.findViewById(R.id.comment_detail);
                holder.publish_time = (TextView) convertView.findViewById(R.id.publish_time);
                holder.user_name = (TextView) convertView.findViewById(R.id.comment_user_name);
                holder.image = (NoScrollGridView) convertView.findViewById(R.id.image_grid_view);
                holder.imge_container = (HorizontalScrollView) convertView.findViewById(R.id.image_container);
                holder.user_photo = (CircleImageView) convertView.findViewById(R.id.comments_user_photo);
                holder.praise_imge = (ImageView) convertView.findViewById(R.id.comments_praise);
                holder.deleteBlog = (LinearLayout) convertView.findViewById(R.id.delete_blog);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.imge_container.setVisibility(View.VISIBLE);
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            // getWindowManager().getDefaultDisplay().getMetrics(dm);
            float density = dm.density;
            int allWidth = (int) (110 * _listData.get(position)
                    .getImages().size() * density);
            int itemWidth = (int) (100 * density);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    allWidth, LinearLayout.LayoutParams.FILL_PARENT);
            holder.image.setLayoutParams(params);
            holder.image.setColumnWidth(itemWidth);
            holder.image.setHorizontalSpacing(10);
            holder.image.setStretchMode(GridView.NO_STRETCH);

            holder.image.setNumColumns(_listData.get(position).getImages().size());
            holder.image.setAdapter(new ImageAdapter(mContext, _listData.get(
                    position).getImages()));
            holder.image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position1, long id) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(mContext, ImageActivity.class);
                    intent.putExtra("image_path", _listData.get(position).getImages().get(position1).getImagePath());
                    mContext.startActivity(intent);
                }
            });
            holder.comments_contents.setText(_listData.get(position).getContent());

            holder.praise_imge.setImageResource(R.drawable.delete_blog);
            holder.deleteBlog.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    deleteBlog(_listData.get(position).getId() + "", position);
                }
            });
            holder.user_name.setText(_listData.get(position).getUser().getUsername());
            Glide.with(parent.getContext()).load(UrlContant.IMAGE_URL + _listData.get(position).getUser().getUserInfo().getAvatar()).into(holder.user_photo);
            holder.publish_time.setText(new SimpleDateFormat("yyyy-MM-dd hh:mm").format(_listData.get(position).getDate()));
            return convertView;
        }

        public class ViewHolder {
            private CircleImageView user_photo;
            private TextView user_name;
            private TextView comments_contents;
            private TextView publish_time;
            private ImageView praise_imge;
            private LinearLayout deleteBlog;
            private NoScrollGridView image;
            private HorizontalScrollView imge_container;
        }

        private void deleteBlog(String id, final int position) {
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            formBodyBuilder.add("id", id);
            BaseClient.delete("Logs", formBodyBuilder.build(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.e("onFailure", e.getMessage());
                    handler.sendEmptyMessage(2);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.e("success", "success");
                    if (response.isSuccessful()) {
                        Message message = new Message();
                        message.what = 1;
                        message.arg1 = position;
                        handler.sendMessage(message);
                    } else {
                        handler.sendEmptyMessage(2);
                    }
                }
            });
        }
    }
}