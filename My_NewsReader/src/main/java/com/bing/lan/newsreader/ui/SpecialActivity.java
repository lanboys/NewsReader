package com.bing.lan.newsreader.ui;

import android.content.Intent;
import android.os.Bundle;

import com.bing.lan.newsreader.R;
import com.bing.lan.newsreader.base.BaseActivity;
import com.bing.lan.newsreader.bean.SpecialDataBean;
import com.bing.lan.newsreader.cons.Constants;
import com.bing.lan.newsreader.utils.HttpUtil;

public class SpecialActivity extends BaseActivity {

    public static final String SPECIAL_ID = "special_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        String specialId;
        if (intent != null) {
            specialId = intent.getStringExtra(SPECIAL_ID);
            requestData(specialId);
        }
    }

    private void requestData(final String specialId) {
        String url = Constants.getNewsDetailUrl(specialId);

        HttpUtil.getInstance(getApplicationContext()).requestGETStringResult(url, new HttpUtil.HttpStringCallBack() {
            @Override
            public void onSuccessResponse(String result) {

                SpecialDataBean specialBean = SpecialDataBean.objectFromData(result, specialId);
            }

            @Override
            public void onFail(Exception e) {
                log.e("onFail: " + e.getLocalizedMessage());
            }
        });
    }
}
