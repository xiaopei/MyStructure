package com.hxp.mystructure.app;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;


import com.hxp.mystructure.BuildConfig;
import com.hxp.mystructure.util.FileLogger;
import com.hxp.mystructure.util.UIUtils;

import java.util.List;


/**
 * Created by hxp on 2016/12/23.
 */
public class App extends ApplicationWithCrashHandler {
    public static final String TAG = "com.hxp.mystructure";
    public static final int DOWNLOAD_IMAGE = 1;
    //获取到主线程的handler
    private static Handler mMainHandler = null;
    //获取到主线程的looper
    private static Looper mMainLooper = null;
    //获取到主线程
    private static Thread mMainThread = null;
    //获取到主线程的id
    private static int mMainTheadId = -1;
    private static App INSTANCE;
//    private static RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
//        NIMClient.init(this, null, null);

        /** 只在主进程里面调用一次，防止多次初始化 */
        if (isInMainProcess()) {
            mMainLooper = getMainLooper();
            mMainHandler = new Handler(mMainLooper);
            mMainThread = Thread.currentThread();
            mMainTheadId = Process.myTid();//主線程id
//            RestClient.init(false, ApiUrls.HOST);
//            FrescoHelper.init(this, true);
            initHandler();
//            SoundUtil.initMedia();
            initMiPush();
//            JPushInterface.init(this);     		// 初始化 JPush
//            StethoTool.install(APP.this);
            initZhuGe();
//            mRefWatcher = LeakCanary.install(this);
            FileLogger.init();                  // debug模式才会起作用
            Log.i(TAG, "onCreate is called and is in main process: " + isInMainProcess());
        }
    }

    // 在其他进程，mRefWatcher是null
    @Nullable
//    public static RefWatcher getRefWatcher() {
//        return mRefWatcher;
//    }

    public static App getApp(){
        return INSTANCE;
    }

    private void initMiPush() {
        if (isInMainProcess()) {
//            MiPushClient.registerPush(this, BuildConfig.MI_PUSH_APP_ID, BuildConfig.MI_PUSH_APP_KEY);
            Log.d("Push", "mi push registered");
        }

        initMiPushLogcat();
//        if (Utils.isProdFlavor()) {                         // 生产环境，禁掉push log
//            Logger.disablePushFileLog(this);
//        }
    }

    // 检测是否在主进程中，只有在主进程中才需要注册MiPush
    // MiPush服务是在:PushService中运行，会多次初始化Application，因此需要控制只注册一次。
    private boolean isInMainProcess() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfoList = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfoList) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    // 打开小米Push调试，可以使用Logger.disablePushFileLog(context)关掉
    private void initMiPushLogcat() {
//        LoggerInterface newLogger = new LoggerInterface() {
//            @Override
//            public void setTag(String tag) {
//                // ignore
//            }
//
//            @Override
//            public void log(String content, Throwable t) {
//                Log.d(TAG, content, t);
//            }
//
//            @Override
//            public void log(String content) {
//                Log.d(TAG, content);
//            }
//        };
//        Logger.setLogger(this, newLogger);
    }

    @Override
    public String getLableName() {
        return "yihua";
    }

    private void initZhuGe() {
        if (BuildConfig.DEBUG) {
//            ZhugeSDK.getInstance().openDebug();
//            ZhugeSDK.getInstance().openLog();
        }
    }

    public void initHandler() {
        mMainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DOWNLOAD_IMAGE:
                        UIUtils.showToast(String.format("保存成功，在本地文件夹/sdcard/%s/查看",LABLE));
                        break;
                }
            }
        };
    }


    public static Handler getMainThreadHandler() {
        if (mMainHandler == null)
            mMainHandler = new Handler(getMainThreadLooper());
        return mMainHandler;
    }

    public static Looper getMainThreadLooper() {
        if (mMainLooper == null)
            mMainLooper = Looper.getMainLooper();
        return mMainLooper;
    }

    public static Thread getMainThread() {
        if (mMainThread == null)
            mMainThread = Thread.currentThread();
        return mMainThread;
    }

    public static int getMainThreadId() {
        if (mMainTheadId == -1)
            mMainTheadId = Process.myTid();
        return mMainTheadId;
    }
}
