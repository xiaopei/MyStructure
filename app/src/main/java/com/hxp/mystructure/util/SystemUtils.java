package com.hxp.mystructure.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.PowerManager;
import android.text.TextUtils;

import com.hxp.mystructure.app.ApplicationWithCrashHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by huangyifei on 2015/7/16.
 */
public class SystemUtils {

    public static boolean inMainProcess(Context context) {
        String packageName = context.getPackageName();
        String processName = SystemUtils.getProcessName(context);
        return packageName.equals(processName);
    }

    public static boolean isAppRunningForground() {
        Context context = ApplicationWithCrashHandler.getInstance();
        String packageName = context.getPackageName();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        PowerManager manager = (PowerManager) context.getSystemService(Activity.POWER_SERVICE);
        ActivityManager.RunningTaskInfo info = activityManager.getRunningTasks(1).get(0);
        ComponentName topComponent = info.topActivity;
        if (info.numRunning > 0 && manager.isScreenOn() && topComponent != null && topComponent.getPackageName().equals(packageName)) {
            return true;
        }
        return false;
    }

    public static final String getProcessName(Context context) {
        String processName = null;

        // ActivityManager
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));

        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;

                    break;
                }
            }

            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }

            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static int getVersionCode(Context context) {
        PackageManager pManager = context.getPackageManager();
        try {
            PackageInfo packinfo = pManager.getPackageInfo(context.getPackageName(), 0);
            if (null != packinfo) {
                return packinfo.versionCode;
            } else {
                return -1;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    public static boolean isQQExist(Context context) {
        return checkApkExist(context, "com.tencent.mobileqq");
    }

    public static boolean isWchatExist(Context context) {
        return checkApkExist(context, "com.tencent.mm");
    }

    public static boolean checkApkExist(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getDeviceInfo(Context context) {
        try{
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if( TextUtils.isEmpty(device_id) ){
                device_id = mac;
            }

            if( TextUtils.isEmpty(device_id) ){
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static void synCookies(Context context) {
//        if (context == null) {
//            return;
//        }
//        final String device_model = DeviceUtil.getDeviceModel();
//        final String channel_id =  ChannelUtil.getChannel(APP.getApp());
//        final String device_no = DeviceUtil.getUDID(APP.getApp());
//        final String accessToken = Utils.getToken();
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.setAcceptCookie(true);
//        if(Build.VERSION.SDK_INT >= 21){
//            cookieManager.flush();
//        } else {
//            cookieManager.removeSessionCookie();// 移除
//            CookieSyncManager.createInstance(context);
//            CookieSyncManager.getInstance().sync();
//        }
//        cookieManager.setCookie(ApiUrls.DOMAIN, "device_model="+device_model + ";Max-Age=3600;Domain=" + ApiUrls.DOMAIN + ";Path=/");
//        cookieManager.setCookie(ApiUrls.DOMAIN, "channel_id=" + channel_id + ";Max-Age=3600;Domain=" + ApiUrls.DOMAIN + ";Path=/");
//        cookieManager.setCookie(ApiUrls.DOMAIN, "device_no=" + device_no + ";Max-Age=3600;Domain=" + ApiUrls.DOMAIN + ";Path=/");
//        cookieManager.setCookie(ApiUrls.DOMAIN, "access_token=" + accessToken + ";Max-Age=3600;Domain=" + ApiUrls.DOMAIN + ";Path=/");
//        if(Build.VERSION.SDK_INT >= 21){
//            cookieManager.flush();
//        } else {
//            CookieSyncManager.getInstance().sync();
//        }
    }

    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }

    public static boolean isMIUI() {
        String miuiCode = SystemUtils.getSystemProperty("ro.miui.ui.version.code");
        String miuiVersion =  SystemUtils.getSystemProperty("ro.miui.ui.version.name");
        if(TextUtils.isEmpty(miuiCode) || TextUtils.isEmpty(miuiVersion)) {
            return false;
        }
        return true;
    }

    /**
     * 安装apk文件
     */
    public static void installAPK(Context context, Uri apk) {
        try {
            // 通过Intent安装APK文件
            Intent intents = new Intent();
            intents.setAction("android.intent.action.VIEW");
            intents.addCategory("android.intent.category.DEFAULT");
            intents.setType("application/vnd.android.package-archive");
            intents.setData(apk);
            intents.setDataAndType(apk, "application/vnd.android.package-archive");
            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intents);
            android.os.Process.killProcess(android.os.Process.myPid());
            // 如果不加上这句的话在apk安装完成之后点击单开会崩溃
        } catch (ActivityNotFoundException e) {

        }
    }

}
