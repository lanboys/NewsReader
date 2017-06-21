package com.bing.lan.newsreader.bean;

import com.bing.lan.newsreader.utils.LogUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 520 on 2017/1/5.
 */

public class HotNewsDetailBean implements Serializable {

    String title;
    String source;
    String articleTags;
    String ptime;
    String body;
    List<ImgBean> img;
    int replyCount;

    public static HotNewsDetailBean objectFromData(String str, String mDocId) {
        LogUtil log = LogUtil.getLogUtil(str.getClass(), 1);
        HotNewsDetailBean hotNewsDetailBean = null;
        try {

            JSONObject jsonObject = new JSONObject(str);
            JSONObject detailJsonObj = jsonObject.optJSONObject(mDocId);

            str = detailJsonObj.toString();
            hotNewsDetailBean = new Gson().fromJson(str, HotNewsDetailBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
            log.e("objectFromData: 解析出错 " + e.getLocalizedMessage());
        }

        return hotNewsDetailBean;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public String getArticleTags() {
        return articleTags;
    }

    public void setArticleTags(String articleTags) {
        this.articleTags = articleTags;
    }

    public List<ImgBean> getImg() {
        return img;
    }

    public void setImg(List<ImgBean> img) {
        this.img = img;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "HotNewsDetailBean{" +
                "articleTags='" + articleTags + '\'' +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", ptime='" + ptime + '\'' +
                ", body='" + body + '\'' +
                ", img=" + img +
                ", replyCount=" + replyCount +
                '}';
    }

    public static class ImgBean implements Serializable {

        private String ref;
        private String pixel;
        private String alt;
        private String src;

        public static ImgBean objectFromData(String str) {

            return new Gson().fromJson(str, ImgBean.class);
        }

        @Override
        public String toString() {
            return "ImgBean{" +
                    "alt='" + alt + '\'' +
                    ", ref='" + ref + '\'' +
                    ", pixel='" + pixel + '\'' +
                    ", src='" + src + '\'' +
                    '}';
        }

        public String getRef() {
            return ref;
        }

        public void setRef(String ref) {
            this.ref = ref;
        }

        public String getPixel() {
            return pixel;
        }

        public void setPixel(String pixel) {
            this.pixel = pixel;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }
    }

    public static class TopiclistNewsBean implements Serializable {

        private boolean hasCover;
        private String subnum;
        private String alias;
        private String tname;
        private String ename;
        private String tid;
        private String cid;

        public static TopiclistNewsBean objectFromData(String str) {

            return new Gson().fromJson(str, TopiclistNewsBean.class);
        }

        public boolean isHasCover() {
            return hasCover;
        }

        public void setHasCover(boolean hasCover) {
            this.hasCover = hasCover;
        }

        public String getSubnum() {
            return subnum;
        }

        public void setSubnum(String subnum) {
            this.subnum = subnum;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public String getEname() {
            return ename;
        }

        public void setEname(String ename) {
            this.ename = ename;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }
    }
}
