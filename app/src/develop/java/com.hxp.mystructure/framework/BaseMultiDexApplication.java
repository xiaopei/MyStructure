package com.hxp.mystructure.framework;

import android.support.multidex.MultiDexApplication;

import com.bugtags.library.Bugtags;

/**
 * Created by hxp on 2016/12/24.
 */
public class BaseMultiDexApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Bugtags.start("d2bf30615d47d3ee1b2035661d652d48", this, Bugtags.BTGInvocationEventBubble);
    }
}
