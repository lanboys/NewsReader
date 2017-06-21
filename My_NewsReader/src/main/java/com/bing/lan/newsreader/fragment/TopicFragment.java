package com.bing.lan.newsreader.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.bing.lan.newsreader.R;
import com.bing.lan.newsreader.base.NewsBaseFragment;
import com.bing.lan.newsreader.newsfragment.HotFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopicFragment extends NewsBaseFragment {

    @Bind(R.id.stl_topic_title)
    SmartTabLayout mSmartTabLayout;
    @Bind(R.id.vp_topic_viewpager)
    ViewPager mVpTopicViewpager;

    public TopicFragment() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected int getContentViewResLayout() {
        return R.layout.fragment_topic;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this, mRootView);
    }

    @Override
    protected void initData() {

        final String[] topicTabTitle = getResources().getStringArray(R.array.topic_fragment_tab_title);

        mVpTopicViewpager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new HotFragment(mHandler, topicTabTitle[position]);
            }

            @Override
            public int getCount() {
                return topicTabTitle.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return topicTabTitle[position];
            }
        });

        mSmartTabLayout.setViewPager(mVpTopicViewpager);
    }
}
