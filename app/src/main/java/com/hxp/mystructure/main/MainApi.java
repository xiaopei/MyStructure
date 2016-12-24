package com.hxp.mystructure.main;

import com.ehyy.yihua.constant.ApiUrls;
import com.ehyy.yihua.jsonBean.CheckUpdateBean;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by HXP on 2016/12/23.
 */

public interface MainApi {

    @GET(ApiUrls.CHECK_UPDATE)
    Call<CheckUpdateBean> checkUpdateInfo();
}
