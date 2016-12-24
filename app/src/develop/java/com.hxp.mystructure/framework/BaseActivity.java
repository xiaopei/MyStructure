package com.hxp.mystructure.framework;

import android.app.Activity;
import android.view.MotionEvent;

import com.bugtags.library.Bugtags;

/**
 * Created by hxp on 2016/12/24.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onResume() {
        super.onResume();
        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Bugtags.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }
}
