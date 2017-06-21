package com.bing.lan.newsreader.app;

import android.app.Application;

import com.bing.lan.newsreader.cons.Constants;
import com.bing.lan.newsreader.utils.ImageUtil;
import com.bing.lan.newsreader.utils.SpUtil;

/**
 * Created by 520 on 2017/1/1.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SpUtil.prepare(getApplicationContext(), Constants.SP_FILE_NAME);

        ImageUtil.prepare(getApplicationContext());











    }
}
