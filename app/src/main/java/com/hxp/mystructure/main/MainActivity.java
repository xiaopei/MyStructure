package com.hxp.mystructure.main;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.hxp.mystructure.R;
import com.hxp.mystructure.constant.PrefKeys;
import com.hxp.mystructure.discover.DiscoverFragment;
import com.hxp.mystructure.framework.BaseFragActivity;
import com.hxp.mystructure.home.HomeFragment;
import com.hxp.mystructure.profile.ProfileFragment;
import com.hxp.mystructure.project.ProjectFragment;
import com.hxp.mystructure.util.SystemUtils;
import com.hxp.mystructure.util.UIUtils;
import com.hxp.mystructure.view.TabView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hxp on 2016/12/23.
 */
public class MainActivity extends BaseFragActivity {
    private Fragment mHomeFragment, mDiscoverFragment, mProjetFragment, mPulishFragment, mMyProfileFragment;
    private static MainActivity mInstance;
    private String nextFragmentTag = "";
    private int tabNum = 0;

    @BindView(R.id.tabView_yihua)
    TabView mHomeTabView;
    @BindView(R.id.tabView_discover)
    TabView mDiscoverTabView;
    @BindView(R.id.tabView_project)
    TabView mProjectTabView;
    @BindView(R.id.tabView_pulish)
    TabView mPulishTabView;
    @BindView(R.id.tabView_my_profile)
    TabView mMyProfileTabView;

    public static void launchActivity(@NonNull Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        //重写此方法，Activity异常销毁后不会保存数据（包括Fragment），避免出现页面重叠问题
        //这么做会出现的问题：无法保存获取焦点的View的状态
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTabs();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SystemUtils.isMIUI()) {              // 为去掉桌面app icon上的未读数角标
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void parseIntent(Intent intent) {
        if (intent == null) {
            return;
        }
//        // 处理客户端唤起
//        if (intent.getData() != null) {
//            Uri data = intent.getData();
//            if (UriActionParser.parseUrl(MainActivity.this, data)) {
//                return;              // 已经跳转，后面无须处理
//            }
//        }

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }

    }

    private void initTabs() {
        resetTabView();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mHomeFragment = new HomeFragment();
        transaction.add(R.id.fragment_container, mDiscoverFragment);
        transaction.commitAllowingStateLoss();
        mDiscoverTabView.setTabSelect(R.mipmap.ic_main_tab_discover_select);
    }

    public static MainActivity getInstance() {
        if (mInstance == null) {
            synchronized (MainActivity.class) {
                if (mInstance == null) {
                    mInstance = new MainActivity();
                }
            }
        }
        return mInstance;
    }

    @OnClick({R.id.tabView_yihua})
    void OnHomeTabClick(View view) {
        setTabSelection(0);
//        if (tabNum == 0 && mYihuaFragment != null) {
//            ((YihuaFragment) mYihuaFragment).backToTop();
//        } else {
//            MobclickAgent.onEvent(this, "gfn_sy_01_01_01_1");
//            ZhugeSDK.getInstance().track(this, "点击关注tab");
            setTabSelection(0);
//        }
    }

    @OnClick({R.id.tabView_discover})
    void OnDiscoverTabClick(View view) {
//        if (tabNum == 1 && mDiscoverFragment != null) {
//            ((DiscoverFragment) mDiscoverFragment).backToTop();
//        } else {
//            MobclickAgent.onEvent(this, "gfn_sy_01_01_02_1");
//            ZhugeSDK.getInstance().track(this, "点击发现tab");
            setTabSelection(2);
//        }
    }

    @OnClick({R.id.tabView_project})
    void OnLiveTabClick(View view) {
//        if (tabNum == 2 && mLiveFragment != null) {
//            ((LiveFragment) mLiveFragment).backToTop();
//        } else {
//            MobclickAgent.onEvent(this, "gfn_sy_01_01_03_1");
//            ZhugeSDK.getInstance().track(this, "点击直播tab");
            setTabSelection(1);
//        }
    }

    @OnClick({R.id.tabView_pulish})
    void OnMessageTabClick(View view) {
//        if (tabNum == 3 && mMessageFragment != null) {
//            ((MessageFragment) mMessageFragment).backToTop();
//        } else {
//            MobclickAgent.onEvent(this, "gfn_sy_01_01_04_1");
//            ZhugeSDK.getInstance().track(this, "点击消息tab");
        UIUtils.showToast("弹出上传界面");
//        }
    }

    @OnClick({R.id.tabView_my_profile})
    void OnProfileTabClick(View view) {
//        if (tabNum == 3 && mMessageFragment != null) {
//            ((MessageFragment) mMessageFragment).backToTop();
//        } else {
//            MobclickAgent.onEvent(this, "gfn_sy_01_01_04_1");
//            ZhugeSDK.getInstance().track(this, "点击消息tab");
        setTabSelection(3);
//        }
    }

    private void resetTabView() {
        mHomeTabView.resetTabInfo(R.string.yihua, R.mipmap.ic_main_tab_home_unselect);
        mDiscoverTabView.resetTabInfo(R.string.discover, R.mipmap.ic_main_tab_discover_unselect);
        mProjectTabView.resetTabInfo(R.string.project, R.mipmap.ic_main_tab_live_unselect);
        mMyProfileTabView.resetTabInfo(R.string.profile, R.mipmap.ic_main_tab_my_profile_unselect);
    }

    public void setTabSelection(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (index) {
            case 0:
                tabNum = 0;
                resetTabView();
                hideFragments(transaction);
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", index);
                    mHomeFragment.setArguments(bundle);
                    transaction.add(R.id.fragment_container, mHomeFragment);
                } else {
                    transaction.show(mHomeFragment);
                }
                mHomeTabView.setTabSelect(R.mipmap.ic_main_tab_home_select);
                break;
            case 1:
                tabNum = 2;
                resetTabView();
                hideFragments(transaction);
                if (mProjetFragment == null) {
                    mProjetFragment = new ProjectFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", index);
                    mProjetFragment.setArguments(bundle);
                    transaction.add(R.id.fragment_container, mProjetFragment);
                } else {
                    transaction.show(mProjetFragment);
                }
                mProjectTabView.setTabSelect(R.mipmap.ic_main_tab_live_select);
                break;
            case 2:
                tabNum = 2;
                resetTabView();
                hideFragments(transaction);
                if (mDiscoverFragment == null) {
                    mDiscoverFragment = new DiscoverFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", index);
                    mDiscoverFragment.setArguments(bundle);
                    transaction.add(R.id.fragment_container, mDiscoverFragment);
                } else {
                    transaction.show(mDiscoverFragment);
                }
                mDiscoverTabView.setTabSelect(R.mipmap.ic_main_tab_discover_select);
                break;
            case 3:
                tabNum = 3;
                resetTabView();
                hideFragments(transaction);
                if (mMyProfileFragment == null) {
                    mMyProfileFragment = new ProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", index);
                    mMyProfileFragment.setArguments(bundle);
                    transaction.add(R.id.fragment_container, mMyProfileFragment);
                } else {
                    transaction.show(mMyProfileFragment);
                }
                mMyProfileTabView.setTabSelect(R.mipmap.ic_main_tab_my_profile_select);
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mDiscoverFragment != null) {
            transaction.hide(mDiscoverFragment);
        }
        if (mProjetFragment != null) {
            transaction.hide(mProjetFragment);
        }
        if (mMyProfileFragment != null) {
            transaction.hide(mMyProfileFragment);
        }
    }

    private void login(String fragmentName) {
//        Intent intent = new Intent(this, LoginActivity.class);
//        intent.putExtra(LoginActivity.NEED_NEW_ACTIVITY, true);
//        intent.putExtra(LoginActivity.NEW_FRAGMENT_NAME, fragmentName);
//        intent.putExtra(LoginActivity.IS_FROM_TAB_PAGE, true);
//        startActivityForResult(intent, REQUEST_FOR_LOGIN_FROM_TAB);
    }

    public int getTabNum() {
        return tabNum;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
