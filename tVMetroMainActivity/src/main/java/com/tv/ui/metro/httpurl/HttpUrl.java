package com.tv.ui.metro.httpurl;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
/**
 * Created by Lenovo on 2017/1/19.
 */
public class HttpUrl {

  private static final String HOST = "10.254.203.196";
  private static final String PORT = "8081";
//  private static final String HOST = "192.168.10.49";
//  private static final String PORT = "8080";
//  private static final String HOST = "192.168.10.4";
//  private static final String PORT = "8080";
//  private static final String HOST = "192.168.10.88";
//  private static final String PORT = "8080";
    private static final String HTTPPRE = "http://"+HOST+":"+PORT+"/";
    public  static final String download_url=HTTPPRE+"panda/cms/apkVersion" ;
    public  static String  goodsgroup_calledurl = HTTPPRE+"panda/cms/selectGoodsSortByGroupId?" ;
    public  static String  calledurl = HTTPPRE+"panda/cms/json?account=&type=1&mac=" ;
    public  static String  url_activeinfo = HTTPPRE+"panda/cms/propertyJson?account=&type=1&mac=" ;
    public  static String  url_version = HTTPPRE+"panda/cms/updateApkVersion?mac=" ;
    //商品详情界面
    public  static String  goods_details = HTTPPRE+"panda/cms/goodsDetails?id=" ;
    public static String getMacFromWifi(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifiState = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if(wifiState == NetworkInfo.State.CONNECTED){//判断当前是否使用wifi连接
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (!wifiManager.isWifiEnabled()) {  //如果当前wifi不可用
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return wifiInfo.getMacAddress();
        }
        return null;
    }

    public  static String getMacAddress(){
        String strMacAddr = null;
        try {
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip)
                    .getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
//                if (i != 0) {
//                    buffer.append(':');
//                }
                String str = Integer.toHexString(b[i]&0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {

            e.printStackTrace();
        }

        return strMacAddr;
    }
    /**
     * 获取移动设备本地IP
     * @return
     */
    protected static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            //列举
            Enumeration en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {//是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();//得到下一个元素
                Enumeration en_ip = ni.getInetAddresses();//得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = (InetAddress) en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ip;
    }

}
