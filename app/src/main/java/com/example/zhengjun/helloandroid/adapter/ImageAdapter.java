package com.example.zhengjun.helloandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zhengjun.helloandroid.R;
import com.example.zhengjun.helloandroid.http.model.UrlContant;
import com.example.zhengjun.helloandroid.http.response.BlogBean;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	public List<BlogBean.ImagesBean> _listData = null;
	private Context mContext;

	public ImageAdapter(Context context, List<BlogBean.ImagesBean> list) {
		_listData = new ArrayList<BlogBean.ImagesBean>();
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
		return _listData == null ? null : _listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return _listData == null ? 0 : position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_image, null);
			holder.album_img = (ImageView) convertView.findViewById(R.id.imageView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Glide.with(parent.getContext()).load(UrlContant.IMAGE_URL + _listData.get(position).getImagePath())
				.error(R.drawable.iv_load_fail).placeholder(R.drawable.iv_load_fail)
				.into(holder.album_img);
		return convertView;
	}

	public class ViewHolder {
		private ImageView album_img;
	}
}
