package com.bing.lan.newsreader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.bing.lan.newsreader.R;
import com.bing.lan.newsreader.base.BaseActivity;
import com.bing.lan.newsreader.cons.Constants;
import com.bing.lan.newsreader.utils.WebUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AdWebActivity extends BaseActivity {

    @Bind(R.id.wv_ad_webview)
    WebView mWvAdWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_web);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        //获取splashactivity穿过来的广告链接
        Intent intent = getIntent();
        String ad_link = intent.getStringExtra(Constants.AD_URL);
        //通过webview加载链接
        WebUtil.createWebViewClient(mWvAdWebview, ad_link);
    }
}
