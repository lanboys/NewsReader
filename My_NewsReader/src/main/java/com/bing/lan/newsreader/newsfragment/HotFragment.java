package com.bing.lan.newsreader.newsfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bing.lan.newsreader.R;
import com.bing.lan.newsreader.base.MyBaseAdapter;
import com.bing.lan.newsreader.base.NewsBaseFragment;
import com.bing.lan.newsreader.bean.HotNewsBean;
import com.bing.lan.newsreader.cons.Constants;
import com.bing.lan.newsreader.fragment.NewsFragment;
import com.bing.lan.newsreader.ui.NewsDetailActivity;
import com.bing.lan.newsreader.utils.AppUtil;
import com.bing.lan.newsreader.utils.HttpUtil;
import com.bing.lan.newsreader.utils.ImageUtil;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotFragment extends NewsBaseFragment {

    private static int count = 0;

    @Bind(R.id.lv_hot_news)
    ListView mLvHotNews;
    @Bind(R.id.ptr_hot_news_frame)
    PtrClassicFrameLayout mPtrClassicFrameLayout;
    // @Bind(R.id.ll_load_data_container)
    // LinearLayout mLoadDataContainer;
    // @Bind(R.id.load_more_list_view_container)
    // LoadMoreListViewContainer mLoadMoreListViewContainer;
    private NewsAdapter mNewsAdapter;
    private String mResult;
    private View mHeaderBanner;
    private Banner mBannerView;
    private int mLoadMoreCount = 0;//加载更多的次数
    private long mCurrentThreadTimeMillis;

    public HotFragment(Handler handler, String tabTitle) {
        mHandler = handler;
        mTabTitle = tabTitle;
    }

    public HotFragment() {

    }

    // @Override
    // public void onResume() {
    //     log.d("onStart(): " + mTabTitle + (mNewsAdapter.getDatas() == null));
    //
    //     if (mNewsAdapter.getDatas() == null) {
    //         //打开相邻的Fragment会有问题
    //         sendMessageToMainFragment(NewsFragment.SHOW_LOAD_DATA);
    //         log.d("onResume(): ---------------" + mTabTitle);
    //     }
    //
    //     super.onResume();
    // }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected int getContentViewResLayout() {
        return R.layout.fragment_hot;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this, mRootView);
        // mLvHotNews= (ListView) getActivity().findViewById(R.id.lv_hot_news);

        //给listview添加轮播图
        addBanner2ListView();
    }

    private void addBanner2ListView() {
        //设置轮播图
        mHeaderBanner = mInflater.inflate(R.layout.lv_header_banner, null);
        mLvHotNews.addHeaderView(mHeaderBanner);//有时候出现空指针异常 为什么??主线程还未执行到创建 listview
        mBannerView = (Banner) mHeaderBanner.findViewById(R.id.banner_lv_header);
        //设置轮播图高度为手机屏幕的1/3高
        int screenHeight = AppUtil.getScreenH(getContext());
        AbsListView.LayoutParams layoutParams =
                new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight / 3);
        mBannerView.setLayoutParams(layoutParams);
        // log.d((mLvHotNews == null) + "----setData2ListView---mLvHotNews----" + count++);

        // 设置加载更多
        View footView = mInflater.inflate(R.layout.lv_foot_load_more, null);
        mLvHotNews.addFooterView(footView);
    }

    @Override
    protected void initData() {

        //给listView添加适配器(要在添加头之后再设置适配器)
        mNewsAdapter = new NewsAdapter(getContext());
        mLvHotNews.setAdapter(mNewsAdapter);
        //请求数据前进行显示
        sendMessageToMainFragment(NewsFragment.SHOW_LOAD_DATA);

        requestData(false);
    }

    @Override
    protected void initListener() {

        mLvHotNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HotNewsBean.NewsDetailBean newsDetailBean = (HotNewsBean.NewsDetailBean) parent.getAdapter().getItem(position);
                String docid = newsDetailBean.getDocid();
                Intent intent = new Intent(getContext(), NewsDetailActivity.class);
                intent.putExtra(NewsDetailActivity.DOC_ID, docid);

                startActivity(intent);
            }
        });

        mPtrClassicFrameLayout.setLoadingMinTime(4000);
        mPtrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                //下拉请求新数据
                requestData(false);

                // long d = SystemClock.currentThreadTimeMillis() - mCurrentThreadTimeMillis;
                // log.d(d + "");
                // mCurrentThreadTimeMillis = SystemClock.currentThreadTimeMillis();
            }
        });

        // mLoadMoreListViewContainer.setAutoLoadMore(true);//设置是否自动加载更多
        // mLoadMoreListViewContainer.useDefaultHeader();
        // //添加加载更多的事件监听
        // mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
        //     @Override
        //     public void onLoadMore(LoadMoreContainer loadMoreContainer) {
        //         log.d("onLoadMore(): 加载更多");
        //         requestData(true);
        //     }
        // });

        //设置滑动监听
        mLvHotNews.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //当滚动停止时 如果展示了ListView此时的展示出来的最后一条数据正好是最后一个item
                //进行加载更多操作
                if (scrollState == SCROLL_STATE_IDLE) {
                    int lastVisiblePosition = view.getLastVisiblePosition();
                    int lastItemPosition = view.getAdapter().getCount() - 1;
                    if (lastVisiblePosition == lastItemPosition) {
                        //请求数据
                        requestData(true);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void setRefreshFinish() {
        if (mPtrClassicFrameLayout != null && mPtrClassicFrameLayout.isRefreshing()) {
            //让它刷新完成
            mPtrClassicFrameLayout.refreshComplete();
        }
    }

    private void setLoadMoreFinish(boolean isLoadSuccess) {

        // if (mLoadMoreListViewContainer != null) {
        //
        //     if (isLoadSuccess) {
        //         //让它加载完成
        //         mLoadMoreListViewContainer.loadMoreFinish(false, true);
        //         // TODO: 2017/1/4 参数什么意思
        //     } else {
        //         //加载失败的情节
        //         mLoadMoreListViewContainer.loadMoreFinish(true, false);
        //     }
        // }
    }

    private void requestData(final boolean isLoadMore) {

        String url;
        if (!isLoadMore) {
            //初始化,下拉刷新
            url = Constants.getHotUrl(0, 9);
        } else {
            mLoadMoreCount++;
            //加载更多
            url = Constants.getHotUrl(mLoadMoreCount * 10, 9 + mLoadMoreCount * 10);
        }

        mHttpUtil.requestGETStringResult(url, new HttpUtil.HttpStringCallBack() {
            @Override
            public void onSuccessResponse(String result) {
                //完成数据刷新
                setRefreshFinish();
                //完成数据加载
                setLoadMoreFinish(true);
                //如果请求的数据相同,将不再刷新数据
                // log.d("mResult: " + result.equals(mResult));
                //
                // if (result != null && result.equals(mResult)) {
                //     return;
                // }
                // mResult = result;

                HotNewsBean hotNewsBean = HotNewsBean.objectFromData(result);
                //给ListView设置数据
                setData2ListView(hotNewsBean, isLoadMore);
            }

            @Override
            public void onFail(Exception e) {
                log.e("onFail: " + e.getLocalizedMessage());
                //完成数据刷新
                setRefreshFinish();
                //完成数据加载
                setLoadMoreFinish(false);
                // TODO: 2017/1/4 加载失败更改界面数据
            }
        });
    }

    private void setData2ListView(HotNewsBean hotNewsBean, boolean isLoadMore) {
        //获取ListView数据
        List<HotNewsBean.NewsDetailBean> newsDetailBeen = hotNewsBean.getT1348647909107();
        //发送隐藏的消息
        sendMessageToMainFragment(NewsFragment.HIDE_LOAD_DATA);

        //区分是否为加载更多
        if (!isLoadMore) {
            //下拉刷新   初始化数据
            HotNewsBean.NewsDetailBean bannerNewsDetailBean = newsDetailBeen.remove(0);
            //给轮播图添加数据
            setData2Banner(bannerNewsDetailBean);
            //给ListView设置数据
            mNewsAdapter.setDatas(newsDetailBeen);
        } else {
            //给ListView添加数据
            mNewsAdapter.insert(newsDetailBeen);
        }
    }

    private void setData2Banner(HotNewsBean.NewsDetailBean bannerNewsDetailBean) {

        List<HotNewsBean.NewsDetailBean.BannerBean> ads = bannerNewsDetailBean.getAds();
        List<String> imgUrls = new ArrayList<>();
        for (HotNewsBean.NewsDetailBean.BannerBean ad : ads) {
            String url = ad.getImgsrc();
            imgUrls.add(url);
        }

        mBannerView.setImages(imgUrls)
                .setImageLoader(new GlideImageLoader())//是否需要优化
                // .setOnBannerClickListener( this)
                .start();
    }

    //自定义图片加载器
    class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {

            // RequestUtil.loadImageByRequest(context, imageView, (String) path);
            ImageUtil.getInstance().display((String) path, imageView);
        }
    }

    class NewsAdapter extends MyBaseAdapter<HotNewsBean.NewsDetailBean> {

        public NewsAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {

                convertView = mInflater.inflate(R.layout.lv_item_hot_news, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            initViewHolderDatas(holder, position);
            return convertView;
        }

        private void initViewHolderDatas(ViewHolder holder, int position) {

            HotNewsBean.NewsDetailBean newsDetailBean = mDatas.get(position);

            String specialID = newsDetailBean.getSpecialID();
            holder.mTvTop.setVisibility(TextUtils.isEmpty(specialID) ? View.GONE : View.VISIBLE);
            if (TextUtils.isEmpty(specialID)) {
                //如果专题ID是空的,就展示跟帖的文本
                holder.mTvReply.setVisibility(View.VISIBLE);
                //注意点:如果ReplyCount写的int类型,注意转换,否则报错
                String replyCount = newsDetailBean.getReplyCount();
                if (Integer.valueOf(replyCount) == 0) {
                    //如果0跟帖就不展示了
                    holder.mTvReply.setVisibility(View.GONE);
                } else {
                    holder.mTvReply.setText(replyCount + "跟帖");
                }
            } else {
                //如果专题ID不为空的,就不展示跟帖的文本
                holder.mTvReply.setVisibility(View.GONE);
            }
            holder.mTvItemTitle.setText(newsDetailBean.getTitle());
            holder.mTvItemSource.setText(newsDetailBean.getSource());

            // RequestUtil.loadImageByRequest(mContext, holder.mIvItemHot, newsDetailBean.getImgsrc());
            ImageUtil.getInstance().display(newsDetailBean.getImgsrc(), holder.mIvItemHot);
        }

        class ViewHolder {

            @Bind(R.id.iv_item_pic)
            ImageView mIvItemHot;
            @Bind(R.id.tv_item_title)
            TextView mTvItemTitle;
            @Bind(R.id.tv_item_source)
            TextView mTvItemSource;
            @Bind(R.id.tv_reply)
            TextView mTvReply;
            @Bind(R.id.tv_top)
            TextView mTvTop;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
