package com.bing.lan.newsreader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bing.lan.newsreader.R;
import com.bing.lan.newsreader.bean.HotNewsDetailBean;
import com.bing.lan.newsreader.utils.ImageUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

public class ShowPicActivity extends AppCompatActivity {

    public static final String PIC_INDEX = "pic_index";
    public static final String PIC_LIST = "pic_list";
    @Bind(R.id.tv_pic_index)
    TextView mTvIndex;
    @Bind(R.id.tv_pic_total)
    TextView mTvTotal;
    @Bind(R.id.viewPager_show_pic)
    ViewPager mViewPagerShowPic;

    private List<HotNewsDetailBean.ImgBean> mImgList;
    private int mIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic);
        ButterKnife.bind(this);

        initView();
        initData();
        initListener();
    }

    private void initListener() {
        mViewPagerShowPic.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTvIndex.setText(String.valueOf(position + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {

        Intent intent = getIntent();
        mImgList = (List<HotNewsDetailBean.ImgBean>) intent.getSerializableExtra(PIC_LIST);
        mIndex = intent.getIntExtra(PIC_INDEX, 0);

        BigPicAdapter bigPicAdapter = new BigPicAdapter(mImgList);
        mViewPagerShowPic.setAdapter(bigPicAdapter);
        //初始化文本内容
        mTvIndex.setText(String.valueOf(mIndex + 1));
        mTvTotal.setText("/" + mImgList.size());

        mViewPagerShowPic.setCurrentItem(mIndex);
    }

    private void initView() {
    }

    class BigPicAdapter extends PagerAdapter {

        private List<HotNewsDetailBean.ImgBean> mImgList;

        public BigPicAdapter(List<HotNewsDetailBean.ImgBean> imgList) {
            this.mImgList = imgList;
        }

        @Override
        public int getCount() {
            return mImgList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            //加载图片
            ImageUtil.getInstance().display(mImgList.get(position).getSrc(), photoView);
            container.addView(photoView);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
