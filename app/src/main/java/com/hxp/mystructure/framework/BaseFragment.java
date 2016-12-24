package com.hxp.mystructure.framework;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.hxp.mystructure.R;
import com.hxp.mystructure.jsonBean.BaseBean;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hxp on 2016/12/23.
 */
public abstract class BaseFragment extends Fragment implements IActionBarCallback, NetDataCallBack.NetDataListener {

    public static final String SAVED_INFO = "saved_info";

    protected IActionBarView mActionBar;

    protected ActionBarView titleBar;

    /**
     * BaseFragment实现了自定义ActionBar的逻辑，这个方法用于处理ActionBar以外的View的创建逻辑
     */
    protected abstract View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public IActionBarView createActionBar(LayoutInflater inflater) {
        ActionBarView view = new ActionBarView(getActivity());
        view.setCallback(this);
        titleBar = view;
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout ret = (RelativeLayout) inflater.inflate(R.layout.fragment_container, null);
        mActionBar = createActionBar(inflater);
        if (mActionBar != null) {
            int actionHeight = (int) getResources().getDimension(R.dimen.actionbar_height);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.topMargin = actionHeight;
            ret.addView(onCreateContentView(inflater, container, savedInstanceState), params);
            params = new LayoutParams(LayoutParams.MATCH_PARENT, actionHeight);
            ret.addView((View) mActionBar, params);
        } else {
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            ret.addView(onCreateContentView(inflater, container, savedInstanceState), params);
        }

        return ret;
    }

    public void setTitle(String title) {
        if (mActionBar != null)
            mActionBar.setTitle(title);
    }

    public void setTitle(@StringRes int title) {
        if (mActionBar != null)
            mActionBar.setTitle(title);
    }

    public void setShowBack(boolean show) {
        if (mActionBar != null)
            mActionBar.setShowBack(show);
    }

    @Override
    public void onBackClick() {
        Activity activity = getActivity();
        if (activity instanceof BaseFragActivity) {
            ((BaseFragActivity) activity).onBackPressed();
        } else {
            activity.finish();
        }
    }

    @Override
    public void onActionClick(int action) {
    }

    public void hideKeyBoard(boolean force) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText() || force) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (imm != null && getView() != null) {
                        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    }
                }
            }, 200);
        }
    }

    @Override
    public void OnNetDataReturn(int id, BaseBean response, Bundle bundle) {}

    @Override
    public void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = APP.getRefWatcher();
//        if (refWatcher != null) {
//            refWatcher.watch(this);
//        }
    }
}
