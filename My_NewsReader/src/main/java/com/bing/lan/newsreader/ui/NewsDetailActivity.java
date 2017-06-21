package com.bing.lan.newsreader.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bing.lan.newsreader.R;
import com.bing.lan.newsreader.base.BaseActivity;
import com.bing.lan.newsreader.bean.HotNewsDetailBean;
import com.bing.lan.newsreader.cons.Constants;
import com.bing.lan.newsreader.utils.HttpUtil;
import com.bing.lan.newsreader.utils.InputUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsDetailActivity extends BaseActivity {

    public static final String DOC_ID = "doc_id";
    //getClass().getSimpleName()
    private static int count;
    @Bind(R.id.ib_news_title_back)
    ImageButton mIbNewsTitleBack;
    @Bind(R.id.ib_news_title_setting)
    ImageButton mIbNewsTitleSetting;
    @Bind(R.id.wv_news_detail_webView)
    WebView mWvNewsDetailWebView;
    @Bind(R.id.et_news_detail_reply)
    EditText mEtNewsDetailReply;
    @Bind(R.id.tv_news_detail_reply)
    TextView mTvNewsDetailReply;
    @Bind(R.id.iv_news_detail_share)
    ImageView mIvNewsDetailShare;
    @Bind(R.id.tv_news_detail_send_reply)
    TextView mTvNewsDetailSendReply;
    private String javascrptClass = "demo";
    private Drawable mDrawableLeft;
    private Drawable mDrawableBottom;
    private boolean mIsEditTextFoucs = false;
    private ArrayList<HotNewsDetailBean.ImgBean> mImgList;
    private String mDocId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    private void initListener() {

        mWvNewsDetailWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //请求焦点
                mWvNewsDetailWebView.requestFocus();
                InputUtil.closeSoftInput(NewsDetailActivity.this);
                return false;
            }
        });
        mEtNewsDetailReply.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                mIsEditTextFoucs = hasFocus;

                //如果有焦点
                if (hasFocus) {
                    mEtNewsDetailReply.setCompoundDrawables(null, null, null, mDrawableBottom);
                    //就隐藏左边的图片,取消hint,隐藏右边的两个控件,展示发送控件出来
                    mEtNewsDetailReply.setHint("");
                    mTvNewsDetailReply.setVisibility(View.GONE);
                    mIvNewsDetailShare.setVisibility(View.GONE);
                    mTvNewsDetailSendReply.setVisibility(View.VISIBLE);
                } else {
                    mEtNewsDetailReply.setCompoundDrawables(mDrawableLeft, null, null, mDrawableBottom);
                    //没焦点
                    //就展示左边的图片,展示hint,展示右边的两个控件,隐藏发送控件出来
                    mEtNewsDetailReply.setHint("写跟帖");
                    mTvNewsDetailReply.setVisibility(View.VISIBLE);
                    mIvNewsDetailShare.setVisibility(View.VISIBLE);
                    mTvNewsDetailSendReply.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initView() {
        mWvNewsDetailWebView.getSettings().setJavaScriptEnabled(true);
        mWvNewsDetailWebView.addJavascriptInterface(NewsDetailActivity.this, "news");

        //左边
        mDrawableLeft = getResources().getDrawable(R.drawable.icon_edit_icon);
        //使用Drawable之前,需要给其设置setBounds方法,
        mDrawableLeft.setBounds(0, 0, mDrawableLeft.getIntrinsicWidth(), mDrawableLeft.getIntrinsicHeight());
        //下边的
        mDrawableBottom = getResources().getDrawable(R.drawable.bg_edit_text);
        mDrawableBottom.setBounds(0, 0, mDrawableBottom.getIntrinsicWidth(), mDrawableBottom.getIntrinsicHeight());
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mDocId = intent.getStringExtra(DOC_ID);
            requestData(mDocId);
        }
    }

    private void requestData(final String docId) {
        String url = Constants.getNewsDetailUrl(docId);

        HttpUtil.getInstance(getApplicationContext()).requestGETStringResult(url, new HttpUtil.HttpStringCallBack() {
            @Override
            public void onSuccessResponse(String result) {

                HotNewsDetailBean hotNewsDetailBean = HotNewsDetailBean.objectFromData(result, docId);
                setData2WebView(hotNewsDetailBean);
            }

            @Override
            public void onFail(Exception e) {
                log.e("onFail: " + e.getLocalizedMessage());
            }
        });
    }

    private void setData2WebView(HotNewsDetailBean hotNewsDetailBean) {

        String body = handleHtml(hotNewsDetailBean);
        mWvNewsDetailWebView.loadDataWithBaseURL(null, body, null, "utf-8", null);
    }

    private String handleHtml(HotNewsDetailBean hotNewsDetailBean) {
        if (hotNewsDetailBean != null) {

            String body = hotNewsDetailBean.getBody();

            //直接加载会样式很丑,而且没标题
            // String body = hotNewsDetailBean.getBody();
            //将body中的<!--img#0-->这样的标签进行替换
            mImgList = (ArrayList<HotNewsDetailBean.ImgBean>) hotNewsDetailBean.getImg();
            int size = mImgList.size();
            for (int i = 0; i < size; i++) {
                HotNewsDetailBean.ImgBean articleImageBean = mImgList.get(i);
                //<img src="ic_launcher.png"/>
                body = body.replace(articleImageBean.getRef(), "<img  onClick=\"show(" + i + ")\" src = \"" + articleImageBean.getSrc() + "\"/>");
                //
            }

            //设置标题的一些处理
            String titleHTML = "<p style = \"margin:25px 0px 0px 0px\"><span style='font-size:22px;'><strong>" + hotNewsDetailBean.getTitle() + "</strong></span></p>";// 标题
            titleHTML = titleHTML + "<p><span style='color:#999999;font-size:14px;'>" + hotNewsDetailBean.getSource() + "&nbsp&nbsp" + hotNewsDetailBean.getPtime() + "</span></p>";//来源与时间
            //加条虚线
            titleHTML = titleHTML + "<div style=\"border-top:1px dotted #999999;margin:20px 0px\"></div>";
            //设置正文的字体和行间距
            body = "<div style='line-height:180%;'><span style='font-size:15px;'>" + body + "</span></div>";
            body = titleHTML + body;
            String script = "<script>function show(i){window.news.javaShowPic(i);}</script>";
            body = "<html><head><style>img{width:100%;}</style>" + script + "</head><body>" + body + "</body></html>";

            // try {
            //     // log.d("handleHtml(): " + body);
            //     // byte[] bytes = body.getBytes();
            //     File file = new File(getFilesDir(), "news" + count++ + ".html");
            //     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            //     writer.write(body);
            //     writer.close();
            // } catch (Exception e) {
            //     e.printStackTrace();
            //     log.e("handleHtml: 写出文件出现问题 " + e.getLocalizedMessage());
            // }

            return body;
        }
        return "解析出错了";
    }

    //注意加注解,注意public
    @JavascriptInterface
    public void javaShowPic(int i) {

        Intent intent = new Intent(getApplicationContext(), ShowPicActivity.class);
        intent.putExtra(ShowPicActivity.PIC_INDEX, i);
        intent.putExtra(ShowPicActivity.PIC_LIST, mImgList);//注意序列化

        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mIsEditTextFoucs) {
            //请求焦点
            mWvNewsDetailWebView.requestFocus();
            InputUtil.closeSoftInput(NewsDetailActivity.this);
            return;
        }
        super.onBackPressed();
        //知道EditText焦点是否有拿到
    }

    @OnClick({R.id.ib_news_title_back, R.id.ib_news_title_setting, R.id.tv_news_detail_reply, R.id.iv_news_detail_share, R.id.tv_news_detail_send_reply})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_news_title_back:
                onBackPressed();
                break;
            case R.id.ib_news_title_setting:
                break;
            case R.id.tv_news_detail_reply:
                startCommentActivity();
                break;
            case R.id.iv_news_detail_share:
                break;
            case R.id.tv_news_detail_send_reply:
                break;
        }
    }

    private void startCommentActivity() {

        Intent intent = new Intent();

        intent.setClass(getApplicationContext(), CommentActivity.class);
        intent.putExtra(DOC_ID, mDocId);

        startActivity(intent);
    }
}
