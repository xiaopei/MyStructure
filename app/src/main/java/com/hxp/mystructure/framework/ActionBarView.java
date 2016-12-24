package com.hxp.mystructure.framework;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxp.mystructure.R;


/**
 * Created by hxp on 2016/12/23.
 */
public class ActionBarView extends RelativeLayout implements IActionBarView, View.OnClickListener {

    private LinearLayout mActions;
    private TextView mTitile;
    private ImageView mBack;
    private IActionBarCallback mListener;
    private SparseArray<View> mActionsArray = new SparseArray<View>();

    public ImageView getmBack() {
        return mBack;
    }

    public void setmBack(ImageView mBack) {
        this.mBack = mBack;
    }

    public ActionBarView(Context context) {
        super(context);

        init(context);
    }

    public ActionBarView(Context context, AttributeSet attr) {
        super(context, attr);
        init(context);
    }

    public ActionBarView(Context context, AttributeSet attr, int defStyleAttr) {
        super(context, attr, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.actionbar, this);
        mActions = (LinearLayout) findViewById(R.id.actionbar_action_container);
        mTitile = (TextView) findViewById(R.id.actionbar_title);
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        mBack.setOnClickListener(this);
    }

    public void setCallback(IActionBarCallback callback){
        this.mListener = callback;
    }

    @Override
    public void setTitle(String title) {
        mTitile.setText(title);
    }

    @Override
    public void setTitle(int res) {
        mTitile.setText(res);
    }

    @Override
    public void setShowBack(boolean show) {
        mBack.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setBackResource(int res) {
        mBack.setImageResource(res);
    }

    @Override
    public void addAction(int action, String text) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        TextView button = (TextView) inflater.inflate(R.layout.actionbar_action, null);
        button.setText(text);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        button.setLayoutParams(params);
        addAction(action, button);
    }

    @Override
    public void addAction(int id, View view) {
        View old = mActionsArray.get(id);
        if (old != null) {
            mActions.removeView(old);
        }
        mActions.addView(view);
        view.setTag(id);
        view.setOnClickListener(this);
        mActionsArray.put(id, view);
    }
    public void removeAction(int id, View view){
        if(view != null){
            mActions.removeView(view);
        }
        mActionsArray.remove(id);
    }

//    public void addAction(int id, int res) {
//        LayoutInflater inflater = LayoutInflater.from(getContext());
//        View view = inflater.inflate(R.layout.actionbar_image_action, null);
//        ImageView imageView = (ImageView)view.findViewById(R.id.btn);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        view.setLayoutParams(params);
//        imageView.setImageResource(res);
//        addAction(id, view);
//    }

    public void showAction(int id, boolean shown) {
        View view = mActionsArray.get(id);
        if (view != null) {
            view.setVisibility(shown ? View.VISIBLE : View.GONE);
        }
    }

    public void setActionEnable(int id, boolean enable) {
        View view = mActionsArray.get(id);
        view.setEnabled(enable);
    }


    @Override
    public void onClick(View v) {
        switch ( v.getId()){
            case R.id.actionbar_back:
                if(mListener != null)
                    mListener.onBackClick();
                break;
            default:
                break;
        }
        Object o = v.getTag();
        if (o != null && o instanceof Integer) {
            int id = (Integer) o;
            callAction(id);
        }
    }

    private void callAction(int id) {
        if (mListener != null) {
            mListener.onActionClick(id);
        }
    }
}
