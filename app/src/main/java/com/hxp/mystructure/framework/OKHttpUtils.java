package com.hxp.mystructure.framework;

import com.hxp.mystructure.util.ILog;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hxp on 2016/12/23.
 */
public class OKHttpUtils {
    private static OkHttpClient mOkHttpClient;

    private static void initClient() {
        if(mOkHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS);
            //添加stetho检测
//            Interceptor stethoInterceptor = StethoTool.getNetworkInterceptor();
//            if (stethoInterceptor != null) {                                // debug上是null，release不是null，参考StethoUtil
//                builder.addNetworkInterceptor(stethoInterceptor);
//            }
            mOkHttpClient = builder.build();
        }
    }

    /**
     * 该不会开启异步线程。
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        initClient();
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 开启异步线程访问网络
     *
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback) {
        initClient();
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     *
     * @param request
     */
    public static void enqueue(Request request) {
        initClient();
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response arg0) throws IOException {
            }

            @Override
            public void onFailure(Call call, IOException e) {
            }
        });
    }

    public static Headers getHeaders() {
        return  new Headers.Builder()
//                .add("device_info", DeviceUtil.getDeviceInfo(APP.getApp()))
//                .add("device_model", DeviceUtil.getDeviceModel())
//                .add("User_agent", DeviceUtil.getUserAgent(APP.getApp()))
//                .add("channel_id", ChannelUtil.getChannel(APP.getApp()))
//                .add("device_no", DeviceUtil.getUDID(APP.getApp()))
//                .add("access_token", Utils.getToken())
                .build();
    }

    public static void postRequest(String url, Map<String, String> params, Callback responseCallback) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (params != null) {
            for (String key : params.keySet()) {
                formBodyBuilder.add(key, params.get(key));
            }
        }
        HttpUrl parsed = HttpUrl.parse(url);
        if (parsed == null) {
            responseCallback.onFailure(null, null);
            return;
        }
        okhttp3.Request request = new okhttp3.Request.Builder()
                .post(formBodyBuilder.build())
                .headers(getHeaders())
                .url(url)
                .build();
        ILog.e("urlurlurl",url);
        enqueue(request, responseCallback);
    }

    public static void getRequest(String url, Map<String, String> params, Callback responseCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        url = sb.toString();
        HttpUrl parsed = HttpUrl.parse(url);
        if (parsed == null) {
            responseCallback.onFailure(null, null);
            return;
        }
        ILog.e("urlurlurl",url);
        Request request = new Request.Builder().headers(getHeaders()).url(url).get().build();
        enqueue(request, responseCallback);
    }
}
