package com.bing.lan.newsreader.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bing.lan.newsreader.R;
import com.bing.lan.newsreader.base.NewsBaseFragment;
import com.bing.lan.newsreader.newsfragment.HotFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends NewsBaseFragment {

    public static final int HIDE_LOAD_DATA = 0;
    public static final int SHOW_LOAD_DATA = 1;
    public static final String CHILD_FRAGMENT_TITLE = "child_fragment_title";
    @Bind(R.id.anim_news_title_live)
    ImageView mAnimNewsTitleLive;
    @Bind(R.id.iv_news_title_live)
    ImageView mIvNewsTitleLive;
    @Bind(R.id.tv_new_title_live)
    TextView mTvNewTitleLive;
    @Bind(R.id.tl_hot_tablayout)
    TabLayout mTlHotTablayout;
    @Bind(R.id.vp_hot_viewpager)
    ViewPager mVpHotViewpager;
    @Bind(R.id.ll_load_data_container)
    LinearLayout mLoadDataContainer;
    @Bind(R.id.iv_translucent_bars)
    ImageView mIvTranslucentBars;
    @Bind(R.id.tv_change_tip)
    TextView mTvChangeTip;
    @Bind(R.id.ibtn_news_arrow)
    ImageButton mIbtnNewsArrow;
    @Bind(R.id.tv_change_done)
    TextView mTvChangeDone;

    private int animTime = 1000;

    private View mInflate;
    private ViewPager mViewPager;
    private SmartTabLayout mSmartTabLayout;

    private ValueAnimator mValueAnimToUp;
    private ValueAnimator mValueAnimToDown;
    private FrameLayout mFl_change_title;
    private TranslateAnimation mTranslateAnimShow;
    private TranslateAnimation mTranslateAnimHide;
    private TextView mTv_change_tip;
    private GridView mGvShowTitle;
    private GridView mGvAddTitle;
    private TextView mTv_change_done;//那个完成按钮

    private ArrayList<Fragment> mFragments;
    private boolean mIsArrowDown = true;//箭头按钮是否向下
    private boolean mIsAnimFinish = true;//认为动画是否已经播放完毕

    public NewsFragment() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case HIDE_LOAD_DATA:
                        hideLoadDataContainer();
                        break;
                    case SHOW_LOAD_DATA:
                        showLoadDataContainer();
                        break;
                }
            }
        };
    }

    private void showLoadDataContainer() {
        //方法一
        // mLoadDataContainer.setVisibility(View.VISIBLE);

        //方法二
        mLoadDataContainer.setAlpha(1f);
    }

    private void hideLoadDataContainer() {

        //方法一
        // int visibility = mLoadDataContainer.getVisibility();
        // if (visibility != View.INVISIBLE) {
        //
        //     AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        //     alphaAnimation.setDuration(2000);
        //     // alphaAnimation.setFillAfter(true);//设置后,显示出来的时候仍然是看不见,没办法调回来,除非用属性动画
        //     mLoadDataContainer.startAnimation(alphaAnimation);
        //
        //     mLoadDataContainer.setVisibility(View.INVISIBLE);
        // }

        //方法二
        float alpha1 = mLoadDataContainer.getAlpha();
        if (alpha1 != 0) {
            ObjectAnimator alpha = ObjectAnimator.ofFloat(mLoadDataContainer, "alpha", 1f, 0f);
            alpha.setDuration(2000);
            alpha.start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    protected int getContentViewResLayout() {
        return R.layout.fragment_news;
    }

    protected void initView() {
        ButterKnife.bind(this, mRootView);
    }

    protected void initAnim() {
        //给直播旁边两个字的设置动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(mTvNewTitleLive, "translationX", 20, 0, 20, 10, 20);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(animTime);
        animator.start();
        //直播----帧动画开启
        final Animatable animatable = (Animatable) mAnimNewsTitleLive.getDrawable();
        animatable.start();
        //延时发送停止动画的消息
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //直播----帧动画开启
                animatable.stop();

                mAnimNewsTitleLive.setVisibility(View.INVISIBLE);//狂点击的时候,空指针bug
                mIvNewsTitleLive.setVisibility(View.VISIBLE);
            }
        }, animTime);

        //初始化箭头动画
        initArrowAnim();
        //初始化隐藏显示动画
        initChangeTitleAnim();
    }

    @Override
    protected void initData() {

        final String[] newsTabTitle = getResources().getStringArray(R.array.news_fragment_tab_title);

        mVpHotViewpager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                HotFragment hotFragment = new HotFragment();
                hotFragment.setHandler(mHandler);
                Bundle bundle = new Bundle();
                bundle.putString(CHILD_FRAGMENT_TITLE, newsTabTitle[position]);
                hotFragment.setArguments(bundle);

                return hotFragment;
            }

            @Override
            public int getCount() {
                return newsTabTitle.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return newsTabTitle[position];
            }
        });

        mTlHotTablayout.setupWithViewPager(mVpHotViewpager);

        //设置图标
        // int tabCount = mTabLayout.getTabCount();
        // for (int i = 0; i < tabCount; i++) {
        //     mTabLayout.getTabAt(i).setIcon(icon[i]);
        // }
    }

    private void initChangeTitleAnim() {
        //这里使用补间动画中的位移
        //展示出来的向下的动画
        mTranslateAnimShow = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, -1,
                TranslateAnimation.RELATIVE_TO_SELF, 0);
        //向上的隐藏动画
        mTranslateAnimHide = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, -1);
        mTranslateAnimShow.setDuration(500);
        mTranslateAnimHide.setDuration(500);
    }

    private void initArrowAnim() {
        ArrowAnimatorUpdateListener updateListener = new ArrowAnimatorUpdateListener();
        ArrowAnimatorListener arrowAnimatorListener = new ArrowAnimatorListener();
        //让箭头向上
        mValueAnimToUp = ValueAnimator.ofFloat(0f, 180f).setDuration(500);
        mValueAnimToUp.addUpdateListener(updateListener);
        mValueAnimToUp.addListener(arrowAnimatorListener);
        //让箭头继续向下
        mValueAnimToDown = ValueAnimator.ofFloat(180f, 0f).setDuration(500);
        //需要让控件真正的动起来,需要给动画添加一个动画更新的监听器,
        // 在监听器中,根据动画当前的值,来真正的设置旋转角度
        mValueAnimToDown.addUpdateListener(updateListener);
        mValueAnimToDown.addListener(arrowAnimatorListener);
    }

    @OnClick(R.id.ibtn_news_arrow)
    public void onClick() {

        if (mIsArrowDown) {
            mValueAnimToUp.start();
        } else {
            mValueAnimToDown.start();
        }

        //点击以后就取反
        mIsArrowDown = !mIsArrowDown;
    }

    private class ArrowAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float animatedValue = (float) animation.getAnimatedValue();
            //根据这个值来修改角度
            mIbtnNewsArrow.setRotation(animatedValue);
        }
    }

    private class ArrowAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {
            mIsAnimFinish = false;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mIsAnimFinish = true;
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
