package com.hxp.mystructure.framework;

import com.hxp.mystructure.app.App;
import com.hxp.mystructure.util.CacheFileUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hxp on 2016/12/23.
 */
public class RestClient {
    static Retrofit retrofit;

    private RestClient() {
    }

    public static void init(boolean debug, String host) {
        String cacheDir = CacheFileUtils.getDiskCacheDir(App.getApp()) + File.separator + "okhttp";
        Cache cache = new Cache(new File(cacheDir), 30 * 1024 * 1024);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(10, TimeUnit.SECONDS);
//        Interceptor stethoInterceptor = StethoTool.getNetworkInterceptor();
//        if (stethoInterceptor != null) {                                // debug上是null，release不是null，参考StethoUtil
//            builder.addNetworkInterceptor(stethoInterceptor);
//        }
        //处理用户信息的拦截器
//        builder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Interceptor.Chain chain) throws IOException {
//                Request request = chain.request();
//                Request.Builder builder = request.newBuilder()
//                request = builder.build();
//                Response response = chain.proceed(request);
//                if (response.code() == 521) {
//                    String refreshToken = Utils.getRefreshToken();
//                    boolean refreshFail = true;
//                    if (!TextUtils.isEmpty(refreshToken)) {
//                        RequestBody formBody = new FormBody.Builder()
//                                .add("refreshToken", refreshToken)
//                                .build();
//                        Request newRequest = request.newBuilder().url(ApiUrls.HOST + ApiUrls.REFRESH_TOKEN).post(formBody).build();
//                        Response getAuth = chain.proceed(newRequest);
//                        try {
//                            JSONLoginAsDeviceUser login = new Gson().fromJson(getAuth.body().charStream(), JSONLoginAsDeviceUser.class);
//                            if (login != null && login.isSuccess()) {
//                                request = request.newBuilder().removeHeader("access_token").addHeader("access_token", login.getData().getAccessToken()).build();
//                                PrefUtils.putString(PrefKeys.ACCESS_TOKEN, login.getData().getAccessToken());
//                                PrefUtils.putString(PrefKeys.REFRESH_TOKEN, login.getData().getRefreshToken());
//                                response = chain.proceed(request);
//                                refreshFail = false;
//                            }
//                        } catch (JsonSyntaxException e) {
//                        }
//                    }
//                    if (TextUtils.isEmpty(refreshToken) || (refreshFail && Utils.isUserLogin() == false)) {
//                        String deviceNo = DeviceUtil.getUDID(APP.getApp());
//                        RequestBody formBody = new FormBody.Builder()
//                                .add("deviceNo", deviceNo)
//                                .build();
//                        Request newRequest = request.newBuilder().url(ApiUrls.HOST + ApiUrls.LOGIN_AS_DEVICE_USER).post(formBody).build();
//                        Response getAuth = chain.proceed(newRequest);
//                        JSONLoginAsDeviceUser login = new Gson().fromJson(getAuth.body().charStream(), JSONLoginAsDeviceUser.class);
//                        if (login != null && login.isSuccess()) {
//                            PrefUtils.putString(PrefKeys.ACCESS_TOKEN, login.getData().getAccessToken());
//                            PrefUtils.putString(PrefKeys.REFRESH_TOKEN, login.getData().getRefreshToken());
//                            request = request.newBuilder().addHeader("access_token", TextUtils.isEmpty(login.getData().getAccessToken())?"":login.getData().getAccessToken()).build();
//                            response = chain.proceed(request);
//                        }
//                    }
//                }
//                return response;
//            }
//        });
        OkHttpClient client = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(host)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(client)
                .build();
    }

    public static <T> T generateAPI(Class<T> c) {
        return retrofit.create(c);
    }
}
