package com.tv.ui.metro.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;



/**
 * 加载图片的异步任务工具类。
 * */
public class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

	private Context context;
	private ImageCallback callback;
	private String path;
	
	public ImageAsyncTask(Context context, ImageCallback callback) {
		super();
		this.context = context;
		this.callback = callback;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		path = params[0];
		return HttpUtils.getImageContent(params[0]);
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		if (result!=null) {
			callback.sendImage(result, path);
		}else {
			//如果获取不到数据，怎么办呢？
			
		}
		
	}

	public interface ImageCallback{
		public void sendImage(Bitmap bm, String path);
	}
}
