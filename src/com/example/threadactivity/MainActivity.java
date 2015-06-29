package com.example.threadactivity;

import java.io.IOException;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity {

	ImageView mImageView = null;
	private final String IMAGE_URL = "http://b.hiphotos.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=02b2417e8744ebf8797c6c6db890bc4f/fc1f4134970a304e4ccdd887d1c8a786c8175cdb.jpg";

	// 异步任务类AsyncTask，实现网络图片的下载，显示与ImageView
	private class DownloadImageTask extends AsyncTask<String, Void, Drawable> {

		@Override
		protected Drawable doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return getDrawableFromNet(arg0[0]);
		}

		@Override
		protected void onPostExecute(Drawable result) {
			// TODO Auto-generated method stub
			mImageView.setImageDrawable(result);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mImageView = (ImageView) findViewById(R.id.imageContent);

		// 所有的操作都放在UI线程里面，造成线程的阻塞,较新版本的Android会抛出NetWorkOnMainThreadException的异常
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Drawable drawable = getDrawableFromNet(IMAGE_URL);
				mImageView.setImageDrawable(drawable);
			}
		});

		// 在一个新的线程中处理UI线程的ImageView，会抛出异常。线程不安全
		findViewById(R.id.button2).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Drawable drawable = getDrawableFromNet(IMAGE_URL);
						mImageView.setImageDrawable(drawable);
					}
				}).start();
			}
		});

		// 通过mImageView的post(Runnable)方法来执行异步操作，可以实现网络图片的异步加载
		findViewById(R.id.button3).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						final Drawable drawable = getDrawableFromNet(IMAGE_URL);

						mImageView.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								mImageView.setImageDrawable(drawable);
							}
						});
					}
				}).start();
			}
		});

		// 通过AsyncTask类来实现异步操作，实现网络图片的加载
		findViewById(R.id.button4).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new DownloadImageTask().execute(IMAGE_URL);
			}
		});

	}

	// 从指定的url下载网络图片
	private Drawable getDrawableFromNet(String imageUrl) {

		Drawable drawable = null;

		try {
			drawable = Drawable.createFromStream(
					new URL(imageUrl).openStream(), "image.gif");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("test", e.getMessage());
			e.printStackTrace();
		}

		if (drawable == null) {
			Log.d("test", "image is null");
		} else {
			Log.d("test", "image not null");
		}

		return drawable;

	}

}
