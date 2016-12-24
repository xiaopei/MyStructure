package com.hxp.mystructure.framework;

import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;

import com.bugtags.library.Bugtags;
import com.hxp.mystructure.BuildConfig;

/**
 * Created by hxp on 2016/12/24.
 */
public class BaseFragmentActivity extends FragmentActivity {
    @Override
    protected void onResume() {
        super.onResume();
        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (BuildConfig.DEBUG) {Bugtags.onPause(this);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Bugtags.onDispatchTouchEvent(this, event);

        return super.dispatchTouchEvent(event);
    }
}
