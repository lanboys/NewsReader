package com.bing.lan.newsreader.ui;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bing.lan.newsreader.R;
import com.bing.lan.newsreader.base.BaseActivity;
import com.bing.lan.newsreader.bean.AdsBean;
import com.bing.lan.newsreader.cons.Constants;
import com.bing.lan.newsreader.service.DownloadAdPicService;
import com.bing.lan.newsreader.utils.SpUtil;
import com.bing.lan.newsreader.view.SkipView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends BaseActivity {

    @Bind(R.id.iv_splash_ad)
    ImageView mIvSplashAd;
    @Bind(R.id.sv_splash_skip)
    SkipView mSvSplashSkip;
    @Bind(R.id.iv_splash_logo)
    ImageView mIvSplashLogo;
    @Bind(R.id.activity_splash)
    LinearLayout mActivitySplash;
    private int mAdTime;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        initData();
        initListener();
        initAnim();
    }

    private void initAnim() {

        final ViewPropertyAnimator animate = mActivitySplash.animate();
        //先隐藏,动画播放时再配合透明动画慢慢显示出来
        mActivitySplash.setVisibility(View.INVISIBLE);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //显示
                mActivitySplash.setVisibility(View.VISIBLE);
                //放大动画
                animate.scaleX(1.1f).scaleY(1.1f).setDuration(mAdTime / 2);
                //透明动画
                ObjectAnimator animator = ObjectAnimator.ofFloat(mActivitySplash, "alpha", 0f, 1f);
                animator.setDuration(mAdTime / 2);
                animator.start();

                // ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mActivitySplash, "scaleX", 0, 1);
                // scaleXAnimator.setDuration(mAdTime / 2);
                // scaleXAnimator.start();
            }
        }, 1000);
    }

    private void initListener() {
        mSvSplashSkip.setOnSkipListener(new SkipView.OnSkipListener() {
            @Override
            public void skip(View view) {
                startHomeAcitvity();
            }
        });
    }

    private void initData() {

        // 获取广告播放时间
        mAdTime = getResources().getInteger(R.integer.ad_time);

        String ads_json = SpUtil.getString(Constants.ADS_JSON);
        long deadline = SpUtil.getLong(Constants.DEAD_LINE);

        if (TextUtils.isEmpty(ads_json) || System.currentTimeMillis() > deadline) {
            requestJsonData();
        } else {
            log.i("initData: 有缓存,且未超过有效期");
        }

        showAdPic();
    }

    /**
     * 展示广告图片
     */
    private void showAdPic() {
        String ads_json = SpUtil.getString(Constants.ADS_JSON);
        if (TextUtils.isEmpty(ads_json)) {
            startHomeAcitvityDelayed();
            return;
        }
        final AdsBean adsBean = AdsBean.objectFromData(ads_json);
        List<AdsBean.AdBean> ads = adsBean.getAds();

        int index = SpUtil.getInt(Constants.ADS_PIC_INDEX);
        final AdsBean.AdBean adBean = ads.get(index);
        String resUrl = adBean.getRes_url().get(0);
        File file = DownloadAdPicService.getAdsPicFile(this, resUrl);

        if (file.exists() && file.length() > 0) {

            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            //跳转按钮启动动画
            mSvSplashSkip.startAutoSkip(mAdTime);
            mSvSplashSkip.setVisibility(View.VISIBLE);

            mIvSplashAd.setImageBitmap(bitmap);
            mIvSplashAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SplashActivity.this, AdWebActivity.class);

                    intent.putExtra(Constants.AD_URL, adBean.getAction_params().getLink_url());
                    startActivity(intent);
                    finish();
                }
            });

            index++;
            index = index % ads.size();

            SpUtil.putInt(Constants.ADS_PIC_INDEX, index);
        } else {
            requestJsonData();
            startHomeAcitvityDelayed();
            log.w("showAdPic: 图片丢失,重新请求数据");
        }
    }

    private void startHomeAcitvityDelayed() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startHomeAcitvity();
            }
        }, mAdTime);
    }

    private void startHomeAcitvity() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.alpha_activity_enter_anim, R.anim.alpha_activity_exit_anim);
        finish();
    }

    /**
     * 请求广告json数据
     */
    private void requestJsonData() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Constants.API.ADS_JSON_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.e("onFailure:Json请求失败: " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                AdsBean adsBean = AdsBean.objectFromData(result);

                if (adsBean != null) {
                    Intent intent = new Intent(SplashActivity.this, DownloadAdPicService.class);

                    intent.putExtra(DownloadAdPicService.DOWNLOAD_ADS_BEAN, adsBean);

                    startService(intent);

                    int next_req = adsBean.getNext_req();
                    long deadline = System.currentTimeMillis() + next_req * 60 * 1000;

                    SpUtil.putLong(Constants.DEAD_LINE, deadline);
                    SpUtil.putString(Constants.ADS_JSON, result);
                }
            }
        });
    }
}

