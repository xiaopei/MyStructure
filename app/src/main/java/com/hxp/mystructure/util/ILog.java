package com.hxp.mystructure.util;

import android.os.Environment;
import android.util.Log;

import com.hxp.mystructure.BuildConfig;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Logging helper class.
 * <p/>
 * to see logs call:<br/>
 * {@code <android-sdk>/platform-tools/adb shell setprop log.tag VERBOSE}
 */
public class ILog {
	public static String tagName = "yihua";
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss", Locale.getDefault());
	public static String TAG = "com.hxp.mystructure";
	public static boolean DEBUG = BuildConfig.DEBUG;

	public static void d(String tag, String msg) {
		if (DEBUG) Log.d(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (DEBUG) Log.e(tag, msg);
	}

	public static void ew(String tag, String msg) {
		if (DEBUG)
			Log.e(tag, msg);
		write(tagName, tag + "|ew = " + sdf.format(new Date()) + " = " + msg + "\n");
	}

	public static void i(String tag, String msg) {
		if (DEBUG) {
			Log.i(tag, msg);
		}
	}

	public static void iw(String tag, String msg) {
		if (DEBUG)
			Log.i(tag, msg);
		write(tagName, tag + "|iw = " + sdf.format(new Date()) + " = " + msg + "\n");
	}

	public static void i(String tag, Object object) {
		if (DEBUG) Log.i(tag, (object == null ? "null" : object) + "");
	}

	public static void v(String msg) {
		if (DEBUG) {
			Log.v(tagName, msg);
		}
	}

	public static void vw(String tag, String msg) {
		if (DEBUG)
			Log.v(tag, msg);
		write(tagName, tag + "|vw = " + sdf.format(new Date()) + " = " + msg + "\n");
	}

	public static void w(String tag, String msg) {
		if (DEBUG) Log.w(tag, msg);
	}

	public static void ww(String tag, String msg) {
		if (DEBUG)
			Log.w(tag, msg);
		write(tagName, tag + "|ww = " + sdf.format(new Date()) + " = " + msg + "\n");
	}

	public static void LogInit(Object obj) {
		if (DEBUG) if (obj != null) Log.d("init:", "" + obj.getClass().getName());
	}

	public static void LogDestroy(Object obj) {
		if (DEBUG) if (obj != null) Log.d("detroy:", "" + obj.getClass().getName());
	}

	public static void println(Object obj) {
		if (DEBUG) if (obj != null) System.out.println(obj);
	}

	public static void write(String name, String msg) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File cacheDirectory = Environment.getExternalStorageDirectory();
			String fileName = name + ".log";
			File logFile = new File(cacheDirectory, fileName);
			try {
				if (!logFile.exists())
					logFile.createNewFile();
				FileOutputStream output = new FileOutputStream(logFile, true);
//				msg += ("\n -------------------------------" + new Date(System.currentTimeMillis()).toString() + "------------------------------- \n");
				// 得到网络资源并写入文件
				InputStream input = new ByteArrayInputStream(msg.getBytes());
				byte b[] = new byte[1024];
				int j = 0;
				while ((j = input.read(b)) != -1) {
					output.write(b, 0, j);
				}
				output.flush();
				output.close();
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setTag(String tag) {
		d("Changing log tag to " + tag);
		TAG = tag;
		DEBUG = Log.isLoggable(TAG, Log.VERBOSE);
	}

	public static void v(Object msg) {
		if (DEBUG)
			Log.v(TAG, buildMessage(String.valueOf(msg)));
	}

	public static void i(Object msg) {
		if (DEBUG)
			Log.i(TAG, buildMessage(String.valueOf(msg)));
	}

	public static void w(Object msg) {
		if (DEBUG)
			Log.w(TAG, buildMessage(String.valueOf(msg)));
	}

	public static void d(Object msg) {
		if (DEBUG)
			Log.d(TAG, buildMessage(String.valueOf(msg)));
	}

	public static void e(Object msg) {
		if (DEBUG)
			Log.e(TAG, buildMessage(String.valueOf(msg)));
	}

	public static void e(Throwable tr, Object msg) {
		if (DEBUG)
			Log.e(TAG, buildMessage(String.valueOf(msg)), tr);
	}

	private static String buildMessage(String msg) {
		StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
		StringBuilder caller = new StringBuilder();
		try {
			for (int i = 2; i < trace.length; i++) {
				Class<?> clazz = trace[i].getClass();
				if (!clazz.equals(ILog.class)) {
					String callingClass = getSimpleClassName(trace[i].getClassName());
					caller.append(callingClass).append('.').append(trace[i].getMethodName()).append(':')
							.append(trace[i].getLineNumber());
					break;
				}
			}
		} catch (Exception e) {
			caller.append(TAG);
		}
		return String.format(Locale.US, "<%s>: %s", caller, msg);
	}

	public static String getSimpleClassName(String path) throws Exception {
		int index = path.lastIndexOf('.');
		path = index == -1 ? path : path.substring(index + 1);
		index = path.lastIndexOf('$');
		path = index == -1 ? path : path.substring(0, index);
		return path;
	}
}
