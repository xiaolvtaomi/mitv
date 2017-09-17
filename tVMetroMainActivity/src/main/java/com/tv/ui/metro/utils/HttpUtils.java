package com.tv.ui.metro.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 获取http请求的工具类
 * */
public class HttpUtils {
	/**
	 * 通过HttpURLConnection获得字符串数据
	 * @param ：params--->网络请求地址
	 * 
	 * @return  返回请求获得字符串数据
	 * */
	public static String getJsonContent(String params){
		String result = "";
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			URL url = new URL(params);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			is = connection.getInputStream();
			isr = new InputStreamReader(is,"utf-8");
			br = new BufferedReader(isr);
			String line = "";
			while ((line=br.readLine())!=null) {
				result+=line;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (isr!=null) {
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}
	/**
	 * 通过HttpClient获得字符串数据
	 * @param ：params--->网络请求地址
	 * 
	 * @return  返回请求获得字符串数据
	 * */
	public static String getJSONContent2(String params){
		String result = "";
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(params);
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode()==200) {
				HttpEntity entity = response.getEntity();
					
				result = EntityUtils.toString(entity, "utf-8");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 通过HttpURLConnection获得字节数组
	 * @param ：params--->网络请求地址
	 * 
	 * @return  返回请求获得字节数组
	 * */
	public static byte[] getByteContent(String params){
		BufferedInputStream bis = null;
		try {
			URL url = new URL(params);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			 bis = new BufferedInputStream(connection.getInputStream());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte buffer[] = new  byte[1024];
			int len = 0;
			while ((len = bis.read(buffer))!=-1) {
				baos.write(buffer,0,len);
				
			}
			byte data[] = baos.toByteArray();
			return data;
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
		return null;
	}
	/**
	 * 通过HttpClient获得字节数组
	 * @param ：params--->网络请求地址
	 * 
	 * @return  返回请求获得字节数组
	 * */
	public static byte[] getByteContent2(String params){
		byte[] data = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(params);
	    HttpResponse response = null;
	    try {
			response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode()==200) {
				HttpEntity entity = response.getEntity();
				data = EntityUtils.toByteArray(entity);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    		
		return data;
	}
	/**
	 * 通过HttpURLConnection获得位图Bitmap对象
	 * @param ：params--->网络请求地址
	 * 
	 * @return  返回请求获得位图Bitmap对象
	 * */
	public static Bitmap getImageContent(String params){
		//获取网络图片
		URL url;
		Bitmap bm = null;
		try {
			url = new URL(params);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			//能够把输入字节流转化为bitmap对象。
			//bitmap-->位图，可以直接显示在imageview当中
			 bm = BitmapFactory.decodeStream(bis);
			 //关闭流，最好写在finally当中
			 is.close();
			 bis.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bm;
	}
}