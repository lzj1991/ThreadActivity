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

	// �첽������AsyncTask��ʵ������ͼƬ�����أ���ʾ��ImageView
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

		// ���еĲ���������UI�߳����棬����̵߳�����,���°汾��Android���׳�NetWorkOnMainThreadException���쳣
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Drawable drawable = getDrawableFromNet(IMAGE_URL);
				mImageView.setImageDrawable(drawable);
			}
		});

		// ��һ���µ��߳��д���UI�̵߳�ImageView�����׳��쳣���̲߳���ȫ
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

		// ͨ��mImageView��post(Runnable)������ִ���첽����������ʵ������ͼƬ���첽����
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

		// ͨ��AsyncTask����ʵ���첽������ʵ������ͼƬ�ļ���
		findViewById(R.id.button4).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new DownloadImageTask().execute(IMAGE_URL);
			}
		});

	}

	// ��ָ����url��������ͼƬ
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
