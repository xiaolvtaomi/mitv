package com.tv.ui.metro.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SDCardHelper {
	/** @des 判断sd卡是否装载
	 * 
	 * @return true：已经被装载可以使用
	 * */
	public static boolean isSDCardMounted(){
		return Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED);
	}
	/**@des 获得sdcard的根目录
	 * 
	 * @return sd卡根目录地址
	 * */
	public static String getSDCardBasePath(){
		if (isSDCardMounted()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}else {
			return null;
		}
	}
//	/**
//	 * @return 返回sd卡总共的容量
//	 * */
//	@SuppressLint("NewApi")
//	public static long getSDCardTotalSize(){
//		long size = 0;
//		if (isSDCardMounted()) {
//			StatFs statFs = new StatFs(getSDCardBasePath());
//			if (Build.VERSION.SDK_INT>=18) {
//				size = statFs.getTotalBytes();
//			}else {
//				size = statFs.getBlockCount()*statFs.getBlockSize();
//			}
//			return size/1024/1024;
//		}
//		return 0;
//	}
	
//	/**
//	 * @return 返回sd卡可用的容量
//	 * */
//	@SuppressLint("NewApi")
//	public static long getSDCardAvailableSize(){
//		long size = 0;
//		if (isSDCardMounted()) {
//			StatFs statFs = new StatFs(getSDCardBasePath());
//			if (Build.VERSION.SDK_INT>=18) {
//				size = statFs.getAvailableBytes();
//			}else {
//				size = statFs.getAvailableBlocks()*statFs.getBlockSize();
//			}
//			return size/1024/1024;
//		}
//		return 0;
//	}
//	/**
//	 * @return 返回sd卡空闲的容量
//	 * */
//	@SuppressLint("NewApi")
//	public static long getSDCardFreeSize(){
//		long size = 0;
//		if (isSDCardMounted()) {
//			StatFs statFs = new StatFs(getSDCardBasePath());
//			if (Build.VERSION.SDK_INT>=18) {
//				size = statFs.getFreeBytes();
//			}else {
//				size = statFs.getFreeBlocks()*statFs.getBlockSize();
//			}
//			return size/1024/1024;
//		}
//		return 0;
//	}
	/**
	 * @des 存储文件到sd卡的公共目录下
	 * @return true:表示存储成功
	 * 
	 * @param data :代表存储的字节数组（任何文件都可以转化成字节数组）
	 * @param type ：代表sd卡公共目录的路径分类
	 * @param fileName:文件的名称
	 * */
	public static boolean saveFileToSDCardPublicDir(byte[]data,String type,
			String fileName){
		if (isSDCardMounted()) {
			//获得文件的公共目录
			File file = Environment.getExternalStoragePublicDirectory(type);
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			try {
				 fos = new FileOutputStream(new File(file, fileName));
				 bos = new BufferedOutputStream(fos);
				bos.write(data, 0, data.length);
				bos.flush();
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					fos.close();
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	/**
	 * @des 保存文件到sd卡自定义目录之下
	 * 
	 * @return true -->保存成功
	 * */
	public static boolean saveFileToSDCardCustomDir(byte[]data,String dir,
			String fileName){
		if (isSDCardMounted()) {
			File file = new File(getSDCardBasePath()+ File.separator+dir);
			//判断这个文件夹是否存在，如果不存在就创建
			if (!file.exists()) {
				file.mkdirs();   
			}
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			try {
				 fos = new FileOutputStream(new File(file, fileName));
				 bos = new BufferedOutputStream(fos);
				bos.write(data, 0, data.length);
				bos.flush();
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					fos.close();
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	/**
	 * @des 保存文件到sd卡的私有目录之下
	 * */
	public static boolean saveFileToSDCardPrivateDir(byte[]data, String type,
													 String fileName, Context context){
		if (isSDCardMounted()) {
			BufferedOutputStream bos = null;
			//获取当前应用程序在sd卡当中的私有目录
			File file = context.getExternalFilesDir(type);
			//保存路径：/mnt/sdcard/Android/data/<你的包名>/files
			try {
				bos = new BufferedOutputStream(new FileOutputStream(
						new File(file, fileName)));
				bos.write(data, 0, data.length);
				bos.flush();
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	/**
	 * @des 保存文件到sd卡的缓存目录之下
	 * */
	public static boolean saveFileToSDCardCacheDir(byte[]data,
												   String fileName, Context context){
		if (isSDCardMounted()) {
			BufferedOutputStream bos = null;
			//获取当前应用程序在sd卡当中的私有目录
			File file = context.getExternalCacheDir();
			//保存路径：/mnt/sdcard/Android/data/<你的包名>/cache/....
			try {
				bos = new BufferedOutputStream(new FileOutputStream(
						new File(file, fileName)));
				bos.write(data, 0, data.length);
				bos.flush();
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/**
	 * @des 保存图片到sd卡的缓存目录之下
	 * */
	public static boolean saveBitmapToSDCardCacheDir(Bitmap bm,
													 String fileName, Context context){
		if (isSDCardMounted()) {
			BufferedOutputStream bos = null;
			//获取当前应用程序在sd卡当中的缓存目录
			File file = context.getExternalCacheDir();
			//保存路径：/mnt/sdcard/Android/data/<你的包名>/cache/....
			try {
				bos = new BufferedOutputStream(new FileOutputStream(
						new File(file, fileName)));
				
				if (fileName!=null&&(fileName.contains(".png")||fileName.contains(".PNG"))) {
					/**
					 * format:存储图片的格式
					 * quality:比例
					 * stream:写入的流
					 * */
					bm.compress(Bitmap.CompressFormat.PNG, 90, bos);
				}else {
					bm.compress(Bitmap.CompressFormat.JPEG, 90, bos);
				}
				bos.flush();
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/**
	 * @des 查找指定文件夹中的文件，并且把他读取出来
	 * */
	public static byte[] loadFileFromSDCard(String filePath){
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		File file = new File(filePath);
		if (file.exists()) {
			try {
				bis = new BufferedInputStream(new FileInputStream(file));
				int len = 0;
				byte[]buffer = new byte[1024*8];
				while ((len=bis.read(buffer))!=-1) {
					baos.write(buffer, 0, len);
					baos.flush();
				}
				return baos.toByteArray();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
