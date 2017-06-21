package com.bing.lan.newsreader.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bing.lan.newsreader.utils.HttpUtil;
import com.bing.lan.newsreader.utils.LogUtil;

public abstract class BaseFragment extends Fragment {

    protected LogUtil log = LogUtil.getLogUtil(getClass(), 1);
    protected LayoutInflater mInflater;
    protected View mRootView;
    protected HttpUtil mHttpUtil;
    protected String mTabTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // if (mRootView == null) {
        //     mInflater = inflater;
        //     mRootView = mInflater.inflate(getContentViewResLayout(), container, false);
        // }
        // ViewGroup parent = (ViewGroup) mRootView.getParent();
        // if (parent != null) {
        //     parent.removeView(mRootView);
        // }

        mInflater = inflater;
        mRootView = mInflater.inflate(getContentViewResLayout(), container, false);

        initView();
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHttpUtil = HttpUtil.getInstance(getContext());
        initAnim();
        initData();
        initListener();
    }

    protected void initListener() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // ButterKnife.unbind(this);
    }

    protected abstract int getContentViewResLayout();

    // ButterKnife.bind(this, mRootView);
    protected abstract void initView();

    protected void initAnim() {
    }

    protected abstract void initData();
}
