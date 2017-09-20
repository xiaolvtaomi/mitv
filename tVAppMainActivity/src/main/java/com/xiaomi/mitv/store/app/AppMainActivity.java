package com.xiaomi.mitv.store.app;


import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.MyApplication;
import com.tv.ui.metro.MainActivity;
import com.tv.ui.metro.custom.CustomDialog;
import com.tv.ui.metro.galleryviewpager.Gallery2;
import com.tv.ui.metro.httpurl.HttpUrl;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.PDSBean;
import com.tv.ui.metro.model.Returned;
import com.tv.ui.metro.model.Updatainfo;
import com.tv.ui.metro.sampleapp.R;
import com.tv.ui.metro.utils.DBUtils;
import com.tv.ui.metro.view.IncludeWebViewActivity;
import com.tv.ui.metro.view.RecommendCardView;
import com.tv.ui.metro.view.RecommendCardViewClickListenerFactory;
import com.tv.ui.metro.view.VitamioActivity;
import com.yealink.lib.common.wrapper.AccountConstant;
import com.yealink.lib.common.wrapper.CallParams;
import com.yealink.lib.common.wrapper.Calllog;
import com.yealink.lib.common.wrapper.SettingsManager;
import com.yealink.lib.common.wrapper.SipProfile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import yealink.MainApplication;
import yealink.sample.TalkingActivity;


public class AppMainActivity extends MainActivity  implements SettingsManager.AccountStateListener{
	private static final String TAG = AppMainActivity.class.getName();
	File file;
	DisplayItem.Target target;
	String model;
    boolean isFromRoot;
	public String calledURL = "";
	int  codes;

	boolean type=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		model= Build.BRAND;
        Bundle bundle = this.getIntent().getExtras();
        //不加bundle非空判断，第一次启动，bundle报空指针
        if (bundle!=null) {
            isFromRoot = bundle.getBoolean("isFromRoot");
        }
		SettingsManager.getInstance().registerAccountListener(AppMainActivity.this);
		RecommendCardViewClickListenerFactory.getInstance().setFactory(new RecommendCardViewClickListenerFactory.ClickCreatorFactory() {
			@Override
			public View.OnClickListener getRecommendCardViewClickListener() {
				return mRecommendCardViewClickListener;
			}
		});
		init();
		if (type==true){
        replace();
		}

	}
	public  void in(){
		final StringRequest request = new StringRequest(Request.Method.GET, calledURL, new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
            String code= Returned.parseData(s).getCode();

			}

		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {

			}
		});
		MyApplication.getHttpQueue().add(request);
	}
	public  void init(){
		final StringRequest request = new StringRequest(Request.Method.GET, HttpUrl.download_url, new Response.Listener<String>() {

			@Override
			public void onResponse(String s) {
				String code= Updatainfo.parseData(s).getVersionCode();
				codes= Integer.parseInt(code);
				if (codes>getVersionCode()) {
					calledURL = HttpUrl.url_version + HttpUrl.getMacAddress() + "&versionCode=" + codes;
					in();
				}

			}


		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {

			}
		});

		MyApplication.getHttpQueue().add(request);
         type=true;
	}
	public  void replace(){
		//自动更新
//		UpdateHelper updateHelper = new UpdateHelper.Builder(getApplication())
//				.checkUrl(HttpUrl.download_url)
//				.isAutoInstall(true) //设置为false需在下载完手动点击安装;默认值为true，下载后自动安装。
//				.build();
//		updateHelper.check();
	}
	// 获取版本号
	public int getVersionCode()
	{
		try {
			// 获取packagemanager的实例
			PackageManager manager = getApplicationContext().getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo info = manager.getPackageInfo(getApplicationContext().getPackageName(), 0);
			int version = info.versionCode;
			return version;
		}  catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	//please override this fun
	protected void createTabsLoader(){
//        mLoader = new TabsGsonLoader(this, albumItem, panelgroupid);
		super.createTabsLoader();
	}

	View.OnClickListener mRecommendCardViewClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if(RecommendCardView.class.isInstance(v)){
				RecommendCardView rcv = (RecommendCardView)v;
				DisplayItem item = rcv.getContentData();
				target = item.target;
				if (null != item) {

					//  ---------wangzhiwei---------begin------跳转
					if (null != target) {

						if (target.type.equals("open_url")&&(!target.jump_uri.endsWith(".mp4"))) {
//							String params = "";
//							try {
//								if (target.param != null && target.param.indexOf("=") > 0) {
//									String[] and_slip = target.param.split("&");
//									for (String and_item : and_slip) {
//										String[] equal_splip = and_item.split("=");
//										if (equal_splip != null && equal_splip.length > 1) {
//											params = params + "&" + equal_splip[0] + "=" + URLEncoder.encode(equal_splip[1], "utf-8");
//										}
//									}
//
//								}
//							} catch (UnsupportedEncodingException e) {
//								e.printStackTrace();
//							}
//
//							Intent intent = new Intent(getApplicationContext(),IncludeWebViewActivity.class);
//							if (params != null && !params.equals("")) {
//								intent.putExtra("site", target.jump_uri + "?mac=" + HttpUrl.getMacAddress() + params);
//							}
//							else {
//
//								intent.putExtra("site", target.jump_uri+(target.jump_uri.contains("?")?"&mac=" + HttpUrl.getMacAddress():"?mac=" + HttpUrl.getMacAddress()));
//							}
//							startActivity(intent);
//
							Intent intent = new Intent(getApplicationContext(),IncludeWebViewActivity.class);
							if (item.type.equals("news")||item.type.equals("news_noface")){
								intent.putExtra("site", target.jump_uri +"?mac=" + HttpUrl.getMacAddress()+"&id="+item.id);
								startActivity(intent);
							}else{
								intent.putExtra("site", target.jump_uri + "?mac=" + HttpUrl.getMacAddress());
								startActivity(intent);
							}
						}
						else if (target.type.equals("open_url")&&(target.jump_uri.endsWith(".mp4"))) {

							Intent intent = new Intent(getApplicationContext(), VitamioActivity.class);
							intent.putExtra("video",target.jump_uri);
							startActivity(intent);
						}else if (target.type.equals("openzhibo")){
							Toast.makeText(AppMainActivity.this, "opengzhibo="+item.id, Toast.LENGTH_SHORT).show();
						}
						//区分华为和中兴盒子
						else if (target.type.equals("open_app")) {
							Toast.makeText(getApplicationContext(), item.name, Toast.LENGTH_LONG).show();

						} else if (target.type.equals("download_app")) {

                            if (target.install_url!=null&&""!=target.install_url) {
                                if (isAppInstalled(target.install_url)) {
                                    doStartApplicationWithPackageName(target.install_url);
                                }
                                else{

									CustomDialog.Builder builder = new CustomDialog.Builder(AppMainActivity.this);
									builder.setMessage("该应用还未下载，是否前往应用商店下载");
									builder.setTitle("应用提示ʾ");
									builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											dialog.dismiss();

										}
									});

									builder.setNegativeButton("确定",
											new android.content.DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int which) {
													dialog.dismiss();
													doStartApplicationWithPackageName("com.huawei.dsm");
												}
											});

									builder.onCreat().show();

                                }
                            }

//							downLoadFile("http://gdown.baidu.com/data/wisegame/b7d7e4efd8199dea/tianyiyuedu_310.apk");
//							if (file!=null){
//								openFile(file);
//							}
							//判断动作类型
							//添加一个标志，区分Activity
						} else if (target.type.equals("open_group")) {
							Intent intent = new Intent(getApplicationContext(), AppMainActivity.class);
							intent.putExtra("panelgroupid",target.jump_uri);
							intent.putExtra("item", item);
                            intent.putExtra("isFromRoot", true) ;
							startActivity(intent);
						}  else if (target.type.equals("open_goodsGroup")) {
							Intent intent = new Intent(getApplicationContext(), IncludeWebViewActivity.class);
							intent.putExtra("site","file:///android_asset/index1.html");
							startActivity(intent);
						}
						else if (target.type.equals("open_goodsInfo")) {
							Intent intent = new Intent(getApplicationContext(), Gallery2.class);
							intent.putExtra("id", target.jump_uri);
							intent.putExtra("activity_Name", "AppMain");
							startActivity(intent);
//							Intent intent = new Intent(getApplicationContext(), LoginStep1Activity.class);
//							startActivity(intent);

						}else if(target.type.equals("open_zhibo")){
                            String code = target.jump_uri ; // 直播的code,需要根据code再查账号密码

							PDSBean bean = DBUtils.getPDSBean(code);
							if (!isNullOrEmpty(bean.getCalledNum())
									&& !isNullOrEmpty(bean.getCallingNum())) {
								if (ad != null && ad.isShowing()) {
									ad.dismiss();
								}
								try {
									ad.show();
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								callId = bean.getCalledNum();
								save(bean.getCallingNum());
								isCall = true;
								MainApplication.code = bean.getCode();
							} else {
								Toast.makeText(AppMainActivity.this,"数据库未录入",Toast.LENGTH_SHORT).show();
							}

                        }


					}
				}
			}
		}
		protected File downLoadFile(final String httpUrl) {
			new Thread(new Runnable() {
				@Override
				public void run() {
//					final String fileName = "updata.apk";
//					File tmpFile = new File("/sdcard/update");
//					if (!tmpFile.exists()) {
//						tmpFile.mkdir();
//					}
//					 file = new File("/sdcard/update/" + fileName);
					InputStream inputStream = null;
					FileOutputStream fileOutputStream = null;
					try {
						URL url = new URL(httpUrl);
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						connection.setRequestMethod("GET");
						connection.setConnectTimeout(5000);
						if (connection.getResponseCode() == 200) {
							inputStream = connection.getInputStream();
							if (inputStream != null) {
								file = getFile(httpUrl);
								fileOutputStream = new FileOutputStream(file);
								byte[] buffer = new byte[1024];
								int length = 0;
								while ((length = inputStream.read(buffer)) != -1) {
									fileOutputStream.write(buffer, 0, length);
								}
								fileOutputStream.close();
								fileOutputStream.flush();
							}
							inputStream.close();
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (inputStream != null) {
								inputStream.close();
								inputStream = null;
							}
							if (fileOutputStream != null) {
								fileOutputStream.close();
								fileOutputStream = null;
							}
						}catch (Exception e){
							e.printStackTrace();
						}
					}
				}
			}).start();
			return file;
		}

		private File getFile(String url) {
			File files = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), getFilePath(url));
			return files;
		}
		private String getFilePath(String url) {
			return url.substring(url.lastIndexOf("/"), url.length());
		}
		//打开APK程序代码
		private void openFile(File file) {
			Log.e("OpenFile", file.getName());
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
			startActivity(intent);
		}
		private void doStartApplicationWithPackageName(String packagename) {

			// 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
			PackageInfo packageinfo = null;
			try {
				packageinfo = getPackageManager().getPackageInfo(packagename, 0);
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
			if (packageinfo == null) {
				return;
			}
			// 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
			Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
			resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			resolveIntent.setPackage(packageinfo.packageName);

			// 通过getPackageManager()的queryIntentActivities方法遍历
			List<ResolveInfo> resolveinfoList = getPackageManager()
					.queryIntentActivities(resolveIntent, 0);

			ResolveInfo resolveinfo = resolveinfoList.iterator().next();
			if (resolveinfo != null) {
				// packagename = 参数packname
				String packageName = resolveinfo.activityInfo.packageName;
				// 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
				String className = resolveinfo.activityInfo.name;
				// LAUNCHER Intent
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				// 设置ComponentName参数1:packagename参数2:MainActivity路径
				ComponentName cn = new ComponentName(packageName, className);
				intent.setComponent(cn);
				startActivity(intent);
			}
		}

	};
	private void doStartApplicationWithPackageNames(String packagename) {

		// 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
		PackageInfo packageinfo = null;
		try {
			packageinfo = getPackageManager().getPackageInfo(packagename, 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		if (packageinfo == null) {
			return;
		}
		// 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(packageinfo.packageName);

		// 通过getPackageManager()的queryIntentActivities方法遍历
		List<ResolveInfo> resolveinfoList = getPackageManager()
				.queryIntentActivities(resolveIntent, 0);

		ResolveInfo resolveinfo = resolveinfoList.iterator().next();
		if (resolveinfo != null) {
			// packagename = 参数packname
			String packageName = resolveinfo.activityInfo.packageName;
			// 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
			String className = resolveinfo.activityInfo.name;
			// LAUNCHER Intent
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			// 设置ComponentName参数1:packagename参数2:MainActivity路径
			ComponentName cn = new ComponentName(packageName, className);
			intent.setComponent(cn);
			startActivity(intent);
		}
	}
	 //返回直接进iptv
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
//		if (     event.getAction() == KeyEvent.ACTION_DOWN
//				&& event.getKeyCode()==KeyEvent.KEYCODE_BACK
//				&& model.equals("HUAWEI")
//                && !isFromRoot){
//			doStartApplicationWithPackageNames("com.huawei.iptv.stb");
//			return  true;
//		}
		if (     event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode()==KeyEvent.KEYCODE_BACK
				&& !model.equals("HUAWEI")
                && !isFromRoot
				&& !model.equals(null)){
			doStartApplicationWithPackageNames("com.itv.android.iptv");
			return  true;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
	}

	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}
    private boolean isAppInstalled(String uri){
        PackageManager pm = getPackageManager();
        boolean installed =false;
        try{
            pm.getPackageInfo(uri,PackageManager.GET_ACTIVITIES);
            installed =true;
        }catch(PackageManager.NameNotFoundException e){
            installed =false;
        }

        return installed;
}

	private void save(String sipId) {
		SipProfile sp = SettingsManager.getInstance().getSipProfile(0);
		sp.userName = sipId;
		sp.registerName = sipId;
		sp.password = sipId;
		sp.server = "42.236.68.190";
		sp.port = 5237;
		sp.isEnableOutbound=false;
		sp.isBFCPEnabled = false;
		sp.isEnabled = true;
		sp.transPort = SipProfile.TRANSPORT_TCP;
		System.out.println("!!!!" + sipId + "!!!" + callId);
		SettingsManager.getInstance().setSipProfile(0, sp);
	}

	private static final int MSG_ACCOUNT_CHANGE = 200;
	private boolean isCall = false;
	private String callId = "";// 被叫电话
	private AlertDialog ad;
	private AlertDialog.Builder builder;
	private LinearLayout ll_status;
	private TextView tv_status;
	protected Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_ACCOUNT_CHANGE:
					setStatus(SettingsManager.getInstance().getSipProfile(0));
					break;
				default:
					break;
			}
		}
	};
	@Override
	public void onAccountStateChanged(int arg0, int arg1, int arg2) {
		handler.sendEmptyMessage(MSG_ACCOUNT_CHANGE);
	}

	private void setStatus(SipProfile sp) {
		if (sp.state == AccountConstant.STATE_UNKNOWN) {
			tv_status.setText("未知");
		} else if (sp.state == AccountConstant.STATE_DISABLED) {
			tv_status.setText("网络繁忙，请稍后在拨……");
		} else if (sp.state == AccountConstant.STATE_REGING) {
			tv_status.setText("正在注册...");
		} else if (sp.state == AccountConstant.STATE_REGED) {
			tv_status.setText("已注册");

			if (isCall) {
				if (ad != null && ad.isShowing()) {
					ad.dismiss();
				}
				isCall = false;
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						CallParams ps = CallParams.create(callId, 0,
								Calllog.PROTOCOL_SIP, false, 0, true, true, null);
						Intent it = new Intent(AppMainActivity.this, TalkingActivity.class);
						it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						it.putExtra(TalkingActivity.EXTRA_CALL_PARAMS, ps);
						startActivity(it);
					}
				},1000);
			}

		} else if (sp.state == AccountConstant.STATE_REG_FAILED) {
			tv_status.setText("注册失败");
		} else if (sp.state == AccountConstant.STATE_UNREGING) {
			tv_status.setText("正在注销...");
		} else if (sp.state == AccountConstant.STATE_UNREGED) {
			tv_status.setText("已注销");
		} else if (sp.state == AccountConstant.STATE_REG_ON_BOOT) {
			tv_status.setText("启动时注册");
		}
	}

	private void setDialog() {
		// TODO Auto-generated method stub
		ll_status = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.ad_threetree_status, null);
		tv_status = (TextView) ll_status.findViewById(R.id.tv_status);
		builder = new AlertDialog.Builder(this, R.style.Base_Theme_AppCompat_Dialog);
		builder.setView(ll_status);
		builder.setCancelable(false);
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				hide();
				isCall = false;
			}
		});
		ad = builder.create();
	}


	public void hide() {
		// TODO Auto-generated method stub
		SipProfile sp = SettingsManager.getInstance().getSipProfile(0);
		sp.isEnabled = false;
		SettingsManager.getInstance().setSipProfile(0, sp);
		isCall = false;
	}
	/**
	 * 判断是否为null或空�?
	 *
	 * @param str
	 *            String
	 * @return true or false
	 */
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

}
