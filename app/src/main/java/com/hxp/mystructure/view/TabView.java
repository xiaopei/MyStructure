package com.hxp.mystructure.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxp.mystructure.R;


/**
 * Created by HXP on 2016/12/23.
 */
public class TabView extends RelativeLayout {

    private ImageView mIvIcon;
    private TextView mTvTabName;
    private TextView mMessageTip;

    public TabView(Context context) {
        super(context);
        init();
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.main_tab_layout, this);
        mTvTabName = (TextView) findViewById(R.id.tv_tab_name);
        mIvIcon = (ImageView) findViewById(R.id.iv_icon);
        mMessageTip = (TextView) findViewById(R.id.tv_message_tip);
    }

    public void resetTabInfo(String tabName, @DrawableRes int iconRes) {
        mTvTabName.setTextColor(getContext().getResources().getColor(R.color.color_3_dark_grey));
        mTvTabName.setText(tabName);
        mIvIcon.setImageResource(iconRes);
    }

    public void resetTabInfo(int tabNameRes, @DrawableRes int iconRes) {
        mTvTabName.setTextColor(getContext().getResources().getColor(R.color.color_3_dark_grey));
        mTvTabName.setText(getContext().getString(tabNameRes));
        mIvIcon.setImageResource(iconRes);
    }

    public void setTabSelect(@DrawableRes int iconRes) {
        mIvIcon.setImageResource(iconRes);
        mTvTabName.setTextColor(getContext().getResources().getColor(R.color.color_4_green));
    }

    public void setMessageTipState(boolean isShow,long count){
        if(isShow){
            mMessageTip.setVisibility(View.VISIBLE);
            mMessageTip.setText(String.valueOf(count));
        } else {
            mMessageTip.setVisibility(View.GONE);
        }
    }
}
