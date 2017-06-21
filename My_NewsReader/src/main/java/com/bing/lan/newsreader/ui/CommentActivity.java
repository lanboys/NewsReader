package com.bing.lan.newsreader.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bing.lan.newsreader.R;
import com.bing.lan.newsreader.base.BaseActivity;
import com.bing.lan.newsreader.base.MyBaseAdapter;
import com.bing.lan.newsreader.bean.CommentBean;
import com.bing.lan.newsreader.bean.DeviceInfoBean;
import com.bing.lan.newsreader.bean.UserBean;
import com.bing.lan.newsreader.cons.Constants;
import com.bing.lan.newsreader.utils.HttpUtil;
import com.bing.lan.newsreader.utils.ImageUtil;
import com.bing.lan.newsreader.utils.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bing.lan.newsreader.ui.NewsDetailActivity.DOC_ID;

public class CommentActivity extends BaseActivity {

    @Bind(R.id.ib_comment_back)
    ImageButton mIbCommentBack;
    @Bind(R.id.lv_comment_display)
    ListView mLvCommentDisplay;
    @Bind(R.id.et_comment_reply_content)
    EditText mEtCommentReplyContent;
    @Bind(R.id.tv_comment_send_reply)
    TextView mTvCommentSendReply;
    private String mDocid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {

        mDocid = getIntent().getStringExtra(DOC_ID);
        if (mDocid != null) {
            requestData();
        }
    }


    private void requestData() {
        String url = Constants.getNewsReplyUrl(mDocid);
        log.d("requestData(): " + url);

        HttpUtil.getInstance(getApplicationContext()).requestGETStringResult(url, new HttpUtil.HttpStringCallBack() {
            @Override
            public void onSuccessResponse(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    //先手动解析上面的数组commentIds,得到一个个关于评论JsonObject的key(id)
                    JSONArray commentIds = jsonObject.optJSONArray("commentIds");
                    JSONObject comments = jsonObject.optJSONObject("comments");
                    //开始遍历,取出一个个id
                    int length = commentIds.length();
                    //存放所有评论数据的集合
                    ArrayList<CommentBean> commentList = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        String id = commentIds.getString(i);
                        //id可能是楼中楼的形式,需要做处理,只拿最后一个id即可
                        if (id.contains(",")) {
                            int start = id.lastIndexOf(",");
                            id = id.substring(start + 1);
                        }
                        //拿到一个id,就开始在下面的那个大的JsonObject(comments)中,去取出一个个评论JsonObject出来
                        JSONObject comment = comments.optJSONObject(id);
                        //取出来以后,就可以使用GSON了,因为评论JsonObject里面的字段都一样
                        CommentBean commentBean = JsonUtil.parseJson(comment.toString(), CommentBean.class);
                        commentList.add(commentBean);
                        Log.e("xmg", "onSuccessResponse: " + id + " commentBean " + commentBean.getVote());
                    }
                    //应该在循环外面去设置listVIew数据
                    initListViewData(commentList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    private void initListViewData(ArrayList<CommentBean> commentList) {
        CommentAdapter commentAdapter = new CommentAdapter(commentList);
        mLvCommentDisplay.setAdapter(commentAdapter);
    }

    @OnClick({R.id.ib_comment_back, R.id.tv_comment_send_reply})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_comment_back:
                onBackPressed();
                break;
            case R.id.tv_comment_send_reply:
                break;
        }
    }

    class CommentAdapter extends MyBaseAdapter<CommentBean> {

        public static final int TYPE_HOT = 0;
        public static final int TYPE_COMMENT = 1;

        public CommentAdapter(List<CommentBean> datas) {
            super(datas);
            //因为第一条是热门跟帖,所以在第一条位置上插入一个无关紧要的数据
            mDatas.add(0, new CommentBean());
            Log.e("xmg", "CommentAdapter: mDatas.size " + mDatas.size() + " datas.size " + datas.size());
        }

        // 方法用来指定类型有多少种
        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                //热门跟帖类型
                return TYPE_HOT;
            } else {
                //评论类型
                return TYPE_COMMENT;
            }
            //        return super.getItemViewType(position);
        }


        // public int getItemViewType1(int position) {
        //     if (position == 0) {
        //         //热门跟帖类型
        //         return TYPE_HOT;
        //     } else {
        //         //评论类型
        //         return TYPE_COMMENT;
        //     }
        //     //        return super.getItemViewType(position);
        // }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //判断一下当前的Item属于哪种类型,根据类型来搞
            int itemViewType = getItemViewType(position);
            if (itemViewType == TYPE_HOT) {
                //热门跟帖类型
                if (convertView == null) {
                    convertView = View.inflate(parent.getContext(), R.layout.lv_item_hot_title, null);
                    //因为不需要任何设置数据的逻辑,不再各种判断和取holder了
                    log.d("getView(): convertView 1 "+convertView);
                }
                    log.d("getView(): convertView 2 "+convertView);
            } else {
                CommentViewHolder holder;
                //item是评论类型
                if (convertView == null) {
                    convertView = View.inflate(parent.getContext(), R.layout.lv_item_comment, null);
                    log.d("getView(): convertView 3 "+convertView);
                    holder = new CommentViewHolder();
                    //开始找控件
                    holder.ivUserIcon = (ImageView) convertView.findViewById(R.id.iv_item_user_icon);
                    holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_item_user_name);
                    holder.tvUserInfo = (TextView) convertView.findViewById(R.id.tv_item_user_info);
                    holder.ivSupport = (ImageView) convertView.findViewById(R.id.iv_item_user_vote);
                    holder.tvVoteCount = (TextView) convertView.findViewById(R.id.tv_item_vote_count);
                    holder.flSubFloor = (FrameLayout) convertView.findViewById(R.id.fl_sub_floor);
                    holder.tvComment = (TextView) convertView.findViewById(R.id.tv_comment);
                    convertView.setTag(holder);
                } else {
                    holder = (CommentViewHolder) convertView.getTag();
                }
                    log.d("getView(): convertView 4 "+convertView);
                //对holder中的控件进行数据设置
                changUI(holder, mDatas.get(position));
            }
            return convertView;
        }

        private void changUI(CommentViewHolder holder, CommentBean commentBean) {
            //点赞效果偷懒了
            //        holder.ivSupport
            //北京市 IPhone 6s 2小时前
            UserBean user = commentBean.getUser();
            DeviceInfoBean deviceInfo = commentBean.getDeviceInfo();
            holder.tvUserInfo.setText(user.getLocation() + " " + deviceInfo.getDeviceName() + " " + commentBean.getCreateTime());
            holder.tvUserName.setText(user.getNickname());
            if (TextUtils.isEmpty(user.getAvatar())) {
                holder.ivUserIcon.setImageResource(R.drawable.icon_user_default);
            } else {
                ImageUtil.getInstance().display(user.getAvatar(), holder.ivUserIcon, R.drawable.icon_user_default);
            }
            holder.tvVoteCount.setText(String.valueOf(commentBean.getVote()));
            holder.tvComment.setText(commentBean.getContent());
        }

        private class CommentViewHolder {

            ImageView ivUserIcon;
            TextView tvUserName;
            TextView tvUserInfo;
            ImageView ivSupport;
            TextView tvVoteCount;
            FrameLayout flSubFloor;
            TextView tvComment;
        }
    }
}
