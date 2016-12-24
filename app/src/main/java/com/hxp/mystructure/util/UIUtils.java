package com.hxp.mystructure.util;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.hxp.mystructure.app.App;


public class UIUtils {

	/**
	 * 获取上下文
	 */
	public static Context getContext() {
		return App.getApp();
	}

	/**
	 * 获取主线程
	 */
	public static Thread getMainThread() {
		return App.getMainThread();
	}

	/**
	 * 获取主线程id
	 */
	public static long getMainThreadId() {
		return App.getMainThreadId();
	}

	/**
	 * 获取到主线程的looper
	 */
	public static Looper getMainThreadLooper() {
		return App.getMainThreadLooper();
	}

	/**
	 * dip转换px
	 */
	public static int dip2px(int dip) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/**
	 * pxz转换dip
	 */
	public static int px2dip(int px) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/**
	 * 获取主线程的handler
	 */
	public static Handler getHandler() {
		return App.getMainThreadHandler();
	}

	/**
	 * 延时在主线程执行runnable
	 */
	public static boolean postDelayed(Runnable runnable, long delayMillis) {
		return getHandler().postDelayed(runnable, delayMillis);
	}

	/**
	 * 在主线程执行runnable
	 */
	public static boolean post(Runnable runnable) {
		return getHandler().post(runnable);
	}

	/**
	 * 从主线程looper里面移除runnable
	 */
	public static void removeCallbacks(Runnable runnable) {
		getHandler().removeCallbacks(runnable);
	}

	/**
	 * 获取布局
	 */
	public static View inflate(int resId) {
		return LayoutInflater.from(getContext()).inflate(resId, null);
	}

	/**
	 * 获取资源
	 */
	public static Resources getResources() {

		return getContext().getResources();
	}

	/**
	 * 获取文字
	 */
	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	/**
	 * 获取文字数组
	 */
	public static String[] getStringArray(int resId) {
		return getResources().getStringArray(resId);
	}

	/**
	 * 获取dimen
	 */
	public static int getDimens(int resId) {
		return getResources().getDimensionPixelSize(resId);
	}

	/**
	 * 获取drawable
	 */
	public static Drawable getDrawable(int resId) {
		return getResources().getDrawable(resId);
	}

	/**
	 * 获取颜色
	 */
	public static int getColor(int resId) {
		return getResources().getColor(resId);
	}

	/**
	 * 获取颜色选择器
	 */
	public static ColorStateList getColorStateList(int resId) {
		return getResources().getColorStateList(resId);
	}

	// 判断当前的线程是不是在主线程
	public static boolean isRunInMainThread() {
		return android.os.Process.myTid() == getMainThreadId();
	}

	public static void runInMainThread(Runnable runnable) {
		// 在主线程运行
		if (isRunInMainThread()) {
			runnable.run();
		} else {
			post(runnable);
		}
	}

	/**
	 * 获取状态栏高度
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * 对toast的简易封装。线程安全，可以在非UI线程调用。
	 */
	public static void showToast(int resId) {
		showToast(getString(resId));
	}

	/**
	 * 对toast的简易封装。线程安全，可以在非UI线程调用。
	 */
	public static void showToast(final String str) {
		if (isRunInMainThread()) {
			showToast_(str);
		} else {
			post(new Runnable() {
				@Override
				public void run() {
					showToast_(str);
				}
			});
		}
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private static void showToast_(Context context, CharSequence text, int duration) {
		if (context != null) {
			Toast toast = Toast.makeText(context, text, duration);
			try {
				if (19 <= Build.VERSION.SDK_INT) {
					toast.getView().setFitsSystemWindows(false);
				}
				toast.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void showToast_(String str) {
		showToast_(getContext(), str, Toast.LENGTH_SHORT);
	}
}
