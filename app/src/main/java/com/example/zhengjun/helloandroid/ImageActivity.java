package com.example.zhengjun.helloandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.bumptech.glide.Glide;
import com.example.zhengjun.helloandroid.http.model.UrlContant;
import com.example.zhengjun.helloandroid.view.album.PhotoView;

public class ImageActivity extends Activity {
	private PhotoView image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		image = (PhotoView) findViewById(R.id.image);
		image.enable();
		String imagepath = UrlContant.IMAGE_URL + getIntent().getExtras().getString("image_path");
		Glide.with(this).load(imagepath)
				.error(R.drawable.iv_load_fail).placeholder(R.drawable.iv_load_fail)
				.into(image);
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
