package com.hxp.mystructure.framework;

import android.view.View;

/**
 * 应用actionbar的接口
 * Created by hxp on 2016/12/23.
 */
public interface IActionBarView {
    public void setTitle(String title);
    public void setTitle(int res);
    public void setShowBack(boolean show);
    public void setBackResource(int res);
    public void addAction(int action, String text);
    public void addAction(int action, View view);
    public void setCallback(IActionBarCallback callback);
    public void showAction(int id, boolean shown);
}
