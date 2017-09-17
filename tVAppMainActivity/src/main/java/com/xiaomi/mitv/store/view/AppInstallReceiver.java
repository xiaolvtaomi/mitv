package com.xiaomi.mitv.store.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.VolleyHelper;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.httpurl.HttpUrl;
import com.tv.ui.metro.loader.BaseMyGsonLoader;
import com.tv.ui.metro.loader.TabsGsonLoader;
import com.tv.ui.metro.model.ActiveInfoRespBean;
import com.tv.ui.metro.view.BoardLayout;

/**
 * Created by Lenovo on 2017/6/5.
 */
public class AppInstallReceiver extends BroadcastReceiver {
	String packageName;

		@Override
		public void onReceive(Context context, Intent intent) {
			PackageManager manager = context.getPackageManager();
			if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
				packageName = intent.getDataString().substring(8);
//				String packageName = intent.getData().getSchemeSpecificPart();
			    Toast.makeText(context, "安装成功"+packageName, Toast.LENGTH_LONG).show();
			}
		}

}
