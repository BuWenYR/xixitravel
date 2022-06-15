package com.example.zhengjun.helloandroid.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.zhengjun.helloandroid.ChangeMineInfoActivity;
import com.example.zhengjun.helloandroid.R;
import com.example.zhengjun.helloandroid.base.BaseFragment;
import com.example.zhengjun.helloandroid.http.model.HttpData;
import com.example.zhengjun.helloandroid.http.model.UrlContant;
import com.example.zhengjun.helloandroid.http.request.ChangeUserImageApi;
import com.example.zhengjun.helloandroid.http.request.GetUserInfoApi;
import com.example.zhengjun.helloandroid.http.request.UploadImageApi;
import com.example.zhengjun.helloandroid.http.response.LoginBean;
import com.example.zhengjun.helloandroid.utils.Bimp;
import com.example.zhengjun.helloandroid.utils.PublicUtils;
import com.example.zhengjun.helloandroid.utils.SPUtils;
import com.example.zhengjun.helloandroid.utils.web.FileManagerUtils;
import com.example.zhengjun.helloandroid.utils.web.Helper;
import com.example.zhengjun.helloandroid.view.CircleImageView;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;


//个人中心
public class MineFragment extends BaseFragment {
	private TextView title;
	private ImageView left_button;
	private ImageView right_img;
	private CircleImageView user_photo;
	private TextView user_name;
	private TextView user_sign;
	private String tempPicPath = "";
	private String sex = "";
	private String nickname = "";
	private String sign = "";
	private boolean isLogin = false;
	public static ViewPager viewPager;
	private View inflate;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.fragment_mine, container, false);
		init();
		initViewPager();
		return inflate;
	}

	@Override
	public void onResume() {
		super.onResume();
		getData();
	}

	public void init() {
		title = (TextView) inflate.findViewById(R.id.title);
		title.setText("个人中心");
		title.setTextColor(getResources().getColor(R.color.mine_title_color));
		left_button = (ImageView) inflate.findViewById(R.id.canlendar);
		left_button.setVisibility(View.INVISIBLE);
		right_img = (ImageView) inflate.findViewById(R.id.setting);
		right_img.setImageResource(R.drawable.edit);
		right_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(getActivity(), ChangeMineInfoActivity.class);
				intent2.putExtra("sex", sex);
				intent2.putExtra("nickname", nickname);
				intent2.putExtra("sign", sign);
				getActivity().startActivity(intent2);
			}
		});
		user_photo = (CircleImageView) inflate.findViewById(R.id.user_photo_mine);
		user_photo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				change_photo();
			}
		});
		user_name = (TextView) inflate.findViewById(R.id.user_name);
		user_sign = (TextView) inflate.findViewById(R.id.user_sign);

		if (!PublicUtils.isNetworkAvailable(getActivity())) {
			LoginBean user = SPUtils.getObject("user", LoginBean.class, getContext());

			if (user != null && user.getUserInfo() != null) {
				LoginBean.UserInfoBean userInfo = user.getUserInfo();
				nickname = userInfo.getNickName();
				sign = userInfo.getSign();
				try {
					sign = URLDecoder.decode(sign, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				user_name.setText(nickname);
				user_sign.setText(sign);
			}
		}
	}

	public void getData() {
		//获取用户信息
		EasyHttp.get(this)
				.api(new GetUserInfoApi()
				)
				.request(new HttpCallback<HttpData<LoginBean.UserInfoBean>>(this) {
					@Override
					public void onSucceed(HttpData<LoginBean.UserInfoBean> data) {
						LoginBean.UserInfoBean value = data.getValue();
						user_name.setText(value.getNickName());
						user_sign.setText(value.getSign());
						Glide.with(getContext()).load(UrlContant.IMAGE_URL + value.getAvatar()).placeholder(R.drawable.logo_travel).error(R.drawable.logo_travel).into(user_photo);
					}

					@Override
					public void onFail(Exception e) {
						super.onFail(e);
						Toast.makeText(getContext(), "查询信息失败", Toast.LENGTH_SHORT).show();
					}
				});
	}

	public void change_photo() {
		final PopupWindow pop = new PopupWindow(getActivity());
		View view = getActivity().getLayoutInflater().inflate(R.layout.item_popupwindows, null);
		pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		bt3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
			}
		});
		bt1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String strImgPath = FileManagerUtils.getAppPath("reactor") + "/";
				String fileName = System.currentTimeMillis() + ".jpg";// 照片命名
				tempPicPath = strImgPath + fileName;
				File photoFile = new File(strImgPath, fileName);
				Uri uri = FileProvider.getUriForFile(getActivity(),
						"com.example.zhengjun.helloandroid.fileprovider", photoFile);
				Log.e("YanZi", "takePicture, uri.toString() = " + uri.toString() + " uri.getPath()"
						+ uri.getPath());
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent, 0);
				List<ResolveInfo> cameraActivities = getActivity()
						.getPackageManager().queryIntentActivities(intent,
								PackageManager.MATCH_DEFAULT_ONLY);
				for (ResolveInfo activity : cameraActivities) {
					getActivity().grantUriPermission(activity.activityInfo.packageName,
							uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
				}
				pop.dismiss();
			}
		});
		bt2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_PICK,
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, 1);
				pop.dismiss();
			}
		});
		pop.showAtLocation(inflate.findViewById(R.id.mine_fragment), Gravity.CENTER, 0, 0);
	}

	public String getPath(Uri uri) {
		if (uri == null) {
			return null;
		}
		String[] projection = {MediaStore.Images.Media.DATA};
		Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null,
				null);
		if (cursor != null) {
			//媒体文件
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getColumnCount(); i++) {// 取得图片 uri 的列名和此列的详细信息
				// Log.i("YanZi", i + "-" + cursor.getColumnName(i) + "-" + cursor.getString(i));
			}
			return cursor.getString(column_index);
		} else {
			//普通文件
			return uri.getPath();
		}
	}

	//修改用户信息
	public void sendData(String value) {
		EasyHttp.post(this)
				.api(new ChangeUserImageApi()
						.setAvatar(value))
				.request(new HttpCallback<HttpData<LoginBean>>(this) {

					@Override
					public void onSucceed(HttpData<LoginBean> data) {
						Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
						Glide.with(getContext()).load(UrlContant.IMAGE_URL + value).into(user_photo);
					}

					@Override
					public void onFail(Exception e) {
						super.onFail(e);
						Toast.makeText(getContext(), "修改失败", Toast.LENGTH_SHORT).show();
					}
				});
	}

	public void uploadImage(File file) {
		EasyHttp.post(this)
				.api(new UploadImageApi()
						.setFile(file)
				)
				.request(new HttpCallback<HttpData<String>>(this) {
					@Override
					public void onSucceed(HttpData<String> data) {
						String value = data.getValue();
						sendData(value);
					}

					@Override
					public void onFail(Exception e) {
						super.onFail(e);
						Toast.makeText(getContext(), "上传图片失败", Toast.LENGTH_SHORT).show();

					}
				});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("requestcode", "" + requestCode);
		Log.e("resultCode", "" + resultCode);
		if (resultCode == Activity.RESULT_OK && (requestCode == 0 || requestCode == 1)) {
			if (requestCode == 1) { // 选择图片返回
				tempPicPath = getPath(data.getData());
			}
			Bitmap bitmap = null;
			try {
				bitmap =
						Helper.getInstance().rotaingImageView(Helper.getInstance().readPictureDegree(tempPicPath),
								Bimp.revitionImageSize(tempPicPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (bitmap == null) {
				return;
			}
			tempPicPath = Helper.getInstance().saveMyBitmap(bitmap);
			uploadImage(new File(tempPicPath));
		}
	}

	public void initViewPager() {
		viewPager = (ViewPager) inflate.findViewById(R.id.fragment_mine_viewPager);
		Indicator indicator = (Indicator) inflate.findViewById(R.id.fragment_mine_indicator);
		indicator.setScrollBar(new ColorBar(getContext(),
				getResources().getColor(R.color.pink), 5));
		Resources res = getResources();
		int selectColor = res.getColor(R.color.pink);
		int unSelectColor = res.getColor(R.color.tab_top_text_1);
		indicator.setOnTransitionListener(new OnTransitionTextListener()
				.setColor(selectColor, unSelectColor).setSize(17, 17));
		viewPager.setOffscreenPageLimit(4);
		IndicatorViewPager indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
		// 注意这里 的 FragmentManager 是 getChildFragmentManager(); 因为是在 Fragment 里面
		// 而在 activity 里面用 FragmentManager 是 getSupportFragmentManager()
		indicatorViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
	}

	private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
		public MyAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public View getViewForTab(int position, View convertView,
								  ViewGroup container) {
			LayoutInflater inflate = LayoutInflater.from(getContext());
			switch (position) {
				case 0:
					if (convertView == null) {
						convertView = inflate.inflate(R.layout.tab_top, container, false);
					}
					TextView blog = (TextView) convertView;
					blog.setText("日志");
					break;
				case 1:
					if (convertView == null) {
						convertView = inflate.inflate(R.layout.tab_top, container, false);
					}
					TextView comments = (TextView) convertView;
					comments.setText("点赞");
					break;
				case 2:
					if (convertView == null) {
						convertView = inflate.inflate(R.layout.tab_top, container, false);
					}
					TextView collections = (TextView) convertView;
					collections.setText("收藏");
					break;
				default:
					break;
			}
			return convertView;
		}

		@Override
		public Fragment getFragmentForPage(int position) {
			Fragment fragment = null;
			switch (position) {
				case 0:
					BlogFragment blogFragment = new BlogFragment();
					fragment = blogFragment;
					break;
				case 1:
					FavoriteFragment commentsFragment = new FavoriteFragment();
					fragment = commentsFragment;
					break;
				case 2:
					CollectionFragment collectionFragment = new CollectionFragment();
					fragment = collectionFragment;
					break;
				default:
					break;
			}
			return fragment;
		}
	}
}
