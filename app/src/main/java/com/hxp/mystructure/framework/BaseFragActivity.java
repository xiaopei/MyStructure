package com.hxp.mystructure.framework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.hxp.mystructure.R;
import com.hxp.mystructure.util.ILog;

/**
 * Created by HXP on 2016/12/22.
 */
public class BaseFragActivity extends BaseFragmentActivity {

    public static final String FRAGMENT_NAME = "fragment_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        parseIntent();
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (!handleBack()) {
            finish();
        }
    }

    public interface OnFragmentBackPressed {
        boolean callOnBackPressed();
    }

    private void parseIntent() {
        Bundle extras = getIntent().getExtras();
        launchFragmentInternal(extras);
    }

    private boolean handleBack() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment instanceof OnFragmentBackPressed) {
            if (((OnFragmentBackPressed) fragment).callOnBackPressed()) {
                return true;
            }
        }
        return false;
    }

    private void launchFragmentInternal(Bundle args) {
        String fragName = args.getString(FRAGMENT_NAME);
        args.remove(FRAGMENT_NAME);
        Fragment fragment = Fragment.instantiate(this, fragName);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.fragment_container, fragment).commit();
    }

    /**
     * use the method to launch a fragment that can save and recover state. the
     * save action is triggered in onSaveInstanceState, and the recovery state
     * is passed by savedInstanceState in onCreate.
     */
    public static void launchFragment(Context context, String fragName, Bundle args) {
        if (args == null) {
            args = new Bundle();
        }
        args.putString(BaseFragActivity.FRAGMENT_NAME, fragName);
        Intent i = new Intent(context, BaseFragActivity.class);
        i.putExtras(args);
        context.startActivity(i);
    }

    public static void launchFragmentForResult(Object activityOrFragment, String fragName, Bundle args, int requestCode) {
        if (args == null) {
            args = new Bundle();
        }
        args.putString(BaseFragActivity.FRAGMENT_NAME, fragName);
        if (activityOrFragment instanceof Activity) {
            Activity activity = (Activity) activityOrFragment;
            Intent i = new Intent(activity, BaseFragActivity.class);
            i.putExtras(args);
            activity.startActivityForResult(i, requestCode);
        } else if (activityOrFragment instanceof Fragment) {
            Fragment fragment = (Fragment) activityOrFragment;
            if (fragment.getActivity() == null) {
                return;
            }
            Intent i = new Intent(fragment.getActivity(), BaseFragActivity.class);
            i.putExtras(args);
            ILog.d("cjhtest", "startActivityForResult:" + requestCode);
            fragment.startActivityForResult(i, requestCode);
        } else {
            throw new RuntimeException("Object type error!should be fragment(v4) or activity");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (isShouldHideInput(v, ev)) {
//
//                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//            return super.dispatchTouchEvent(ev);
//        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
