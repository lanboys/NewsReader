package com.bing.lan.newsreader.utils;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by 520 on 2017/1/1.
 */

public class WebUtil {

    public static void createWebViewClient(WebView webview, String url) {
        webview.loadUrl(url);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
    }
}
