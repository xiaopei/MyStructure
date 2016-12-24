package com.hxp.mystructure.jsonBean;

import java.io.Serializable;

/**
 * Created by hxp on 2016/12/23.
 */
public class BaseBean implements Serializable {

    /**
     * code : 0
     * apiErrorMessage :
     */
    private int code;
    private String apiErrorMessage;

    public String getApiErrorMessage() {
        return apiErrorMessage;
    }

    public void setApiErrorMessage(String apiErrorMessage) {
        this.apiErrorMessage = apiErrorMessage;
    }

    public boolean isSuccess() {
        return code == 1;
    }

    public void setErrString(String errString) {
        this.apiErrorMessage = errString;
    }

    public int getRetCode() {
        return code;
    }

    public String getErrString() {
        return apiErrorMessage;
    }

    public void setCode(int code){
        this.code = code;
    }

}
