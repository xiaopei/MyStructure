package com.hxp.mystructure.app;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;


import com.hxp.mystructure.framework.BaseMultiDexApplication;
import com.hxp.mystructure.util.CacheFileUtils;
import com.hxp.mystructure.util.FileLogger;
import com.hxp.mystructure.util.ILog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by hxp on 2016/12/23.
 */
public abstract class ApplicationWithCrashHandler extends BaseMultiDexApplication {
    public static String TEMP_FILE_PATH;
    public static String BASE_FILE_PATH;
    public static String LABLE;
    private static ApplicationWithCrashHandler mApplication;

    /**
     * set lableName of Application  ,like  'huatian' or 'pet';
     * must not be null!
     */
    public abstract String getLableName();

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

        LABLE = getLableName().trim();
        BASE_FILE_PATH = CacheFileUtils.getInstance().getExternalSdPath(false) + "/" + LABLE;
        TEMP_FILE_PATH = BASE_FILE_PATH + "/temp/";
        File tmpFile = new File(TEMP_FILE_PATH);
        if (!(tmpFile.exists() && tmpFile.isDirectory())) {
            tmpFile.delete();
            tmpFile.mkdirs();
        }
        Thread.setDefaultUncaughtExceptionHandler(new LauncherUncaughtExceptionHandler());
    }

    public static ApplicationWithCrashHandler getInstance() {
        return mApplication;
    }

    private static class LauncherUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        private final Thread.UncaughtExceptionHandler _defaultHandler;

        private final Map<String, String> _infos = new LinkedHashMap<String, String>();

        private final DateFormat _formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);

        public LauncherUncaughtExceptionHandler() {
            _defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        }

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ILog.e("ID=", "uncaught exception @ thread " + thread.getId() + ", err: " + ex);
            FileLogger.close();
            try {
                collectDeviceInfo(ApplicationWithCrashHandler.getInstance());
                saveCrashInfo2File(ex);
                saveHeapDump2File(ex);
            } catch (Exception e) {
                ILog.e("CATCH=", "error writing crash log");
            }
            _defaultHandler.uncaughtException(thread, ex);
        }

        public void collectDeviceInfo(Context context) {
            _infos.put("PackageName", context.getPackageName());
            try {
                _infos.put("VersionName", context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
                _infos.put("VersionCode",
                        String.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode));

            } catch (PackageManager.NameNotFoundException e1) {
                e1.printStackTrace();
            }

            _infos.put("=", "==============================");

            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    _infos.put(field.getName(), field.get("").toString());
                } catch (Exception e) {
                }
            }
            _infos.put("==", "=============================");
        }

        private String saveCrashInfo2File(Throwable ex) {
            String fileName = null;

            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> entry : _infos.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key + "=" + value + "\n");
            }

            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.close();
            sb.append(writer.toString());

            try {
                fileName = String.format("crash-%s.txt", _formatter.format(new Date()));
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    final String path = CacheFileUtils.getInstance().getExternalSdPath(false) + "/" + LABLE + "_log/crash_info/";
                    File dir = new File(path);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(path + fileName);
                    fos.write(sb.toString().getBytes());
                    fos.close();
                }
            } catch (Exception e) {
                ILog.e("CATCH=", "an error occured while writing file..." + e.getMessage());
            }

            return fileName;
        }

        private void saveHeapDump2File(Throwable throwable) {
            if (isOutOfMemoryError(throwable)) {
                try {
                    String fileName = String.format("crash-%s.hprof", _formatter.format(new Date()));
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        final String path = CacheFileUtils.getInstance().getExternalSdPath(false) + "/" + LABLE + "_log/heap_dump/";
                        File dir = new File(path);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }

                        Debug.dumpHprofData(path + fileName);
                    }
                } catch (Exception ex) {
                    ILog.e("CATCH", "couldn't dump hprof:" + ex.getMessage());
                }
            }
        }
    }

    private static boolean isOutOfMemoryError(Throwable ex) {
        if (OutOfMemoryError.class.equals(ex.getClass())) {
            return true;
        } else {
            Throwable cause = ex.getCause();
            while (null != cause) {
                if (OutOfMemoryError.class.equals(cause.getClass())) {
                    return true;
                }
                cause = cause.getCause();
            }
        }
        return false;
    }
}
