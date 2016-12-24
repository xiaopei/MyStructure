package com.hxp.mystructure.framework;


import com.hxp.mystructure.jsonBean.BaseBean;

/**
 * 向服务器发送消息,但不对消息成功与否进行处理
 * Created by hxp on 2016/12/23.
 */
public class BaseApiCallBack extends ApiCallBack<BaseBean> {

    public BaseApiCallBack(boolean isSilent) {
        super(isSilent);
    }

    @Override
    protected void onSuccess(BaseBean response) {
        //DO NOTHING
    }
}
