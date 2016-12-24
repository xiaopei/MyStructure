package com.hxp.mystructure;

import android.content.Context;
import android.support.annotation.Nullable;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.Interceptor;

/**
 * Created by hxp on 2016/12/24.
 */
public class StethoTool {
    public static void install(Context appCtx) {
        Stetho.Initializer initializer = Stetho.newInitializerBuilder(appCtx)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(appCtx))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(appCtx))
                .build();
        Stetho.initialize(initializer);
    }

    // 在release模式下会返回null
    @Nullable
    public static Interceptor getNetworkInterceptor() {
        return new StethoInterceptor();
    }
}
