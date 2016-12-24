package com.hxp.mystructure.framework;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hxp.mystructure.jsonBean.BaseBean;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hxp on 2016/12/23.
 */
public class NetDataCallBack<T extends BaseBean> implements Callback<T> {

    private WeakReference<NetDataListener> listenerWeakReference;
    private int id;
    private Bundle bundle;

    public NetDataCallBack(NetDataListener a, int id, Bundle bundle) {
        this.id = id;
        listenerWeakReference = new WeakReference<>(a);
        this.bundle = bundle;
    }

    protected void onFail(BaseBean error) {
        NetDataListener listener = listenerWeakReference.get();
        if(listener == null) {
            return;
        }
        if(error == null) {
            BaseBean BaseBean = new BaseBean();
            BaseBean.setCode(0);
            BaseBean.setErrString("网络请求失败");
            listener.OnNetDataReturn(id, BaseBean, bundle);
        } else {
            listener.OnNetDataReturn(id, error, bundle);
        }
    }

    protected void onSuccess(T response) {
        NetDataListener listener = listenerWeakReference.get();
        if(listener != null) {
            listener.OnNetDataReturn(id, response, bundle);
        }
    }

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
        t.printStackTrace();
        onFail(null);
    }

    public interface NetDataListener{
        void OnNetDataReturn(int id, @NonNull BaseBean response, Bundle bundle);
    }
}
