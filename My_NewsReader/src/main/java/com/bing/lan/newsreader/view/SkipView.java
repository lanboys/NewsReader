package com.bing.lan.newsreader.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.bing.lan.newsreader.R;
import com.bing.lan.newsreader.utils.LogUtil;

/**
 * Created by 520 on 2017/1/1.
 */

public class SkipView extends View {

    LogUtil log = LogUtil.getLogUtil(getClass(), 1);
    float mDegree = 0;
    int mAutoSkipTime = 0;
    int mCurrentTime = 0;
    int mRefreshTime = 100;
    OnSkipListener mListener;
    private RectF mOutRectF;
    private Paint mInnerPaint;
    private Paint mOuterPaint;
    private Paint mTextPaint;
    private RectF mMRectF1;

    private float mTextSize = 40;
    private int mSize = (int) (mTextSize*2+15);
    private int mOutStrokeWidth = 5;
    private String mSkipText = getContext().getString(R.string.skipText);
    private float mSkipTextWidth;
    private float mSkipTextheight;
    private int mInnerRadius;
    private Handler mHandler;

    public SkipView(Context context) {
        super(context);
    }

    public SkipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnSkipListener(OnSkipListener listener) {
        mListener = listener;
    }

    private void init() {

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(Color.BLUE);
        mTextPaint.setStyle(Paint.Style.STROKE);

        mOuterPaint = new Paint();
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setStyle(Paint.Style.STROKE);
        mOuterPaint.setStrokeWidth(mOutStrokeWidth);
        mOuterPaint.setColor(Color.GREEN);

        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(Color.GRAY);


        mOutRectF = new RectF(mOutStrokeWidth / 2, mOutStrokeWidth / 2, mSize - mOutStrokeWidth / 2, mSize - mOutStrokeWidth / 2);
        mInnerRadius = mSize / 2 - mOutStrokeWidth;

        mSkipTextWidth = mTextPaint.measureText(mSkipText);
        log.d(mSkipTextWidth+"");
        mSkipTextheight = mTextPaint.descent() + mTextPaint.ascent();

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                mCurrentTime += mRefreshTime;
                setDegree(mCurrentTime);
                if (mCurrentTime > mAutoSkipTime) {
                    if (mListener != null) {
                        mListener.skip(SkipView.this);
                    }
                    return;
                }
                sendEmptyMessageDelayed(0, mRefreshTime);
            }
        };
    }

    public void startAutoSkip(int autoSkipTime) {
        mAutoSkipTime = autoSkipTime;
        mHandler.sendEmptyMessage(0);
    }

    public void setDegree(int currentTime) {
        mCurrentTime = currentTime;
        float percent = mCurrentTime * 1f / mAutoSkipTime;

        mDegree = 360 * percent;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action) {

            case MotionEvent.ACTION_DOWN:
                setAlpha(0.5f);
                break;
            case MotionEvent.ACTION_UP:

                setAlpha(1f);
                mHandler.removeCallbacksAndMessages(null);

                if (mListener != null) {
                    mListener.skip(this);
                }

                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //绘制动态圆弧
        canvas.drawArc(mOutRectF, -90, mDegree, false, mOuterPaint);

        //绘制内部圆
        canvas.drawCircle(mSize / 2, mSize / 2, mInnerRadius, mInnerPaint);

        // 计算Baseline绘制的起点X轴坐标,计算方式: 画布宽度的一半 - 文字宽度的一半
        int baseX = (int) (canvas.getWidth() / 2 - mTextPaint.measureText(mSkipText) / 2);

        // 计算Baseline绘制的Y坐标,计算方式: 画布高度的一半 - 文字总高度的一半
        int baseY = (int) ((canvas.getHeight() / 2) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2));

        canvas.drawText(mSkipText, baseX, baseY, mTextPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthMode) {

            case MeasureSpec.AT_MOST:

                widthSize = mSize;
                break;
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        switch (heightMode) {

            case MeasureSpec.AT_MOST:
                heightSize = mSize;

                break;
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        setMeasuredDimension(widthSize, heightSize);
        log.i("widthSize: " + widthSize);
    }

    public interface OnSkipListener {

        void skip(View view);
    }
}
