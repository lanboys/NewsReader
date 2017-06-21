package com.bing.lan.newsreader.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.bing.lan.newsreader.R;
import com.bing.lan.newsreader.base.BaseActivity;
import com.bing.lan.newsreader.fragment.LiveFragment;
import com.bing.lan.newsreader.fragment.MeFragment;
import com.bing.lan.newsreader.fragment.NewsFragment;
import com.bing.lan.newsreader.fragment.TopicFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    @Bind(android.R.id.tabhost)
    FragmentTabHost mTabhost;

    private int[] mTabImageResId = {
            R.drawable.tab_news_selector,
            R.drawable.tab_topic_selector,
            R.drawable.tab_va_selector,
            R.drawable.tab_my_selector
    };
    private Class[] mFragmentArray = {NewsFragment.class, TopicFragment.class, LiveFragment.class, MeFragment.class};
    private String[] mTabTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initDatas();
    }

    private void initDatas() {
        mTabTitles = getResources().getStringArray(R.array.home_tab_title);

        mTabhost.setup(getApplicationContext(), getSupportFragmentManager(), android.R.id.tabcontent);

        for (int i = 0; i < mTabTitles.length; i++) {
            View tabView = getTabView(i);
            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(mTabTitles[i]).setIndicator(tabView);
            mTabhost.addTab(tabSpec, mFragmentArray[i], null);
        }
    }

    private View getTabView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item, null);

        ImageView tabImage = (ImageView) view.findViewById(R.id.iv_home_tab);
        TextView tabTitle = (TextView) view.findViewById(R.id.tv_home_tab);

        tabImage.setImageResource(mTabImageResId[index]);
        tabTitle.setText(mTabTitles[index]);

        return view;
    }
}
