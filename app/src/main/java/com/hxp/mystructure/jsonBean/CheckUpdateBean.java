package com.hxp.mystructure.jsonBean;

/**
 * Created by hxp on 2016/12/23.
 */
public class CheckUpdateBean extends BaseBean {

    private Integer versionCode;
    private String url;
    private String updateInfo;
    private String versionName;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public String getUrl() {
        return url;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }
}
