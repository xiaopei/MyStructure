package com.hxp.mystructure.framework;

import android.support.annotation.CallSuper;

import com.hxp.mystructure.jsonBean.BaseBean;
import com.hxp.mystructure.util.UIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hxp on 2016/12/23.
 */
    public abstract class ApiCallBack<T extends BaseBean> implements Callback<T> {
        private boolean isSilent;

        public ApiCallBack(boolean isSilent) {
            this.isSilent = isSilent;
        }

        protected void onFail(BaseBean error) {
            if (isSilent) return;
            if (error == null) {
                UIUtils.showToast("网络请求失败");
            } else {
                String str = error.getErrString();
                UIUtils.showToast(str);
            }
        }

    // response不会为null
    protected abstract void onSuccess(T response);

    @CallSuper
    @Override
    public void onResponse(Call<T> call, Response<T> response){
        T t = response.body();
        if (t != null && t.isSuccess()) {
            onSuccess(t);
        } else {
            onFail(t);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t){
        onFail(null);
    }

}
