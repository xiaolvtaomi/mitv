package com.tv.ui.metro.model;

import com.tv.ui.metro.utils.GsonUtils;

/**
 * Created by Lenovo on 2017/6/5.
 */
public class Updatainfo {
    private String appName;
    private String packageName;
    private String versionCode;
    private String versionName;
    private String apkUrl;
    private String changeLog;
    private String updateTips;
    private boolean forceUpgrade;

    @Override
    public String toString() {
        return "Updatainfo{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", versionName='" + versionName + '\'' +
                ", apkUrl='" + apkUrl + '\'' +
                ", changeLog='" + changeLog + '\'' +
                ", updateTips='" + updateTips + '\'' +
                ", forceUpgrade=" + forceUpgrade +
                '}';
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }

    public String getUpdateTips() {
        return updateTips;
    }

    public void setUpdateTips(String updateTips) {
        this.updateTips = updateTips;
    }

    public boolean isForceUpgrade() {
        return forceUpgrade;
    }

    public void setForceUpgrade(boolean forceUpgrade) {
        this.forceUpgrade = forceUpgrade;
    }

    public Updatainfo(String appName, String packageName, String versionCode, String versionName, String apkUrl, String changeLog, String updateTips, boolean forceUpgrade) {
        this.appName = appName;
        this.packageName = packageName;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.apkUrl = apkUrl;
        this.changeLog = changeLog;
        this.updateTips = updateTips;
        this.forceUpgrade = forceUpgrade;
    }
    public static Updatainfo parseData(String json) {
        return  GsonUtils.toBean(json, Updatainfo.class);

    }
}
