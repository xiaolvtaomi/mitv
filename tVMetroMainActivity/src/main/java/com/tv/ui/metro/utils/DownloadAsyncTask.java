package com.tv.ui.metro.utils;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadAsyncTask extends AsyncTask<String, Integer, Boolean> {

	private ProgressBar bar;
	private DownloadCallback callback;
	private float size;
	private String filePath;
	public DownloadAsyncTask(ProgressBar bar, DownloadCallback callback, float size, String filePath) {
		super();
		this.bar = bar;
		this.callback = callback;
		this.size = size*1024*1024;
		this.filePath = filePath;
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		bar.setVisibility(View.VISIBLE);
	}
	@Override
	protected Boolean doInBackground(String... params) {
		BufferedInputStream bis = null;
		try {
			URL url = new URL(params[0]);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			bis = new BufferedInputStream(connection.getInputStream());
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
			byte buffer[] = new  byte[1024*8];
			int len = 0;
			long total_length = 0;
			
			while ((len = bis.read(buffer))!=-1) {
				total_length += len;
				bos.write(buffer, 0, len);
				bos.flush();
//				baos.write(buffer, 0, len);
				int progress = ((int)((total_length/size)*100));
				publishProgress(progress);
			}
//			byte[]data = baos.toByteArray();
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (bis!=null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		bar.setVisibility(View.INVISIBLE);
		if (result) {
			callback.sendContent(result);
		}
	}
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		Log.i("tag","-progress---"+values[0]);
		bar.setProgress(values[0]);
	}
	public interface DownloadCallback{
		public void sendContent(boolean isSuccess);
//		public void sendContent(byte[]data);
	}
}
