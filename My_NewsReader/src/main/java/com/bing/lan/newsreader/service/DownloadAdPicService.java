package com.bing.lan.newsreader.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.bing.lan.newsreader.bean.AdsBean;
import com.bing.lan.newsreader.utils.FileUtil;
import com.bing.lan.newsreader.utils.HashCodeUtil;
import com.bing.lan.newsreader.utils.LogUtil;
import com.bing.lan.newsreader.utils.RequestUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadAdPicService extends IntentService {

    public static final String DOWNLOAD_ADS_BEAN = "download_ads_bean";
    LogUtil log = LogUtil.getLogUtil(getClass(), 1);

    private OkHttpClient mClient;

    public DownloadAdPicService() {
        super("DownloadAdPicService");
    }

    @NonNull
    public static File getAdsPicFile(Context context, String resUrl) {
        return new File(FileUtil.getDiskCacheDir(context), HashCodeUtil.toHashCode(resUrl) + ".jpg");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            AdsBean adsBean = (AdsBean) intent.getSerializableExtra(DOWNLOAD_ADS_BEAN);

            mClient = new OkHttpClient();

            List<AdsBean.AdBean> ads = adsBean.getAds();
            for (AdsBean.AdBean ad : ads) {

                String resUrl = ad.getRes_url().get(0);

                File mPicFile = getAdsPicFile(DownloadAdPicService.this, resUrl);

                if (mPicFile.exists() && mPicFile.length() > 0) {
                    log.i("onHandleIntent: 图片已经有了,不再去下载");
                    continue;
                }

                downloadAdPic(resUrl);
                // downloadAdPic1(resUrl);
            }
        }
    }

    private void downloadAdPic(final String resUrl) {

        Request request = new Request.Builder()
                .url(resUrl)
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.e("onFailure: 图片下载失败 " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                File picFile = getAdsPicFile(DownloadAdPicService.this, resUrl);

                FileOutputStream fileOutputStream = new FileOutputStream(picFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, fileOutputStream);
                log.i("onResponse: 图片下载成功,并存储到sd卡中");
            }
        });
    }

    public void downloadAdPic1(final String resUrl) {

        RequestUtil.loadImageByRequest(this, resUrl, new RequestUtil.CallBackListner<Bitmap>() {
            @Override
            public void OnFinish(Bitmap bitmap) {

                File picFile = getAdsPicFile(DownloadAdPicService.this, resUrl);

                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(picFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, fileOutputStream);
                log.i("onResponse: 图片下载成功,并存储到sd卡中");
            }

            @Override
            public void OnError(Exception e) {
                log.e("onFailure: 图片下载失败 " + e.getLocalizedMessage());
            }
        });
    }
}

