package com.bing.lan.newsreader.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * @author 蓝兵
 * @time 2017/1/3  14:21
 */
public class HotNewsBean {

    private List<NewsDetailBean> T1348647909107;

    public static HotNewsBean objectFromData(String str) {

        return new Gson().fromJson(str, HotNewsBean.class);
    }

    public List<NewsDetailBean> getT1348647909107() {
        return T1348647909107;
    }

    public void setT1348647909107(List<NewsDetailBean> T1348647909107) {
        this.T1348647909107 = T1348647909107;
    }

    public static class NewsDetailBean {

        public List<BannerBean> getAds() {
            return ads;
        }

        void setAds(List<BannerBean> ads) {
            this.ads = ads;
        }

        List<BannerBean> ads;//轮播图
        String imgsrc;//图片地址
        String title;//标题
        String source;//新闻来源
        String replyCount;//跟帖数
        String docid;//新闻的id,用来标记当前是哪条新闻,方便后面展示详情
        String specialID;//是否为专题,我这里简单处理,认为有specialID,就是置顶帖

        @Override
        public String toString() {
            return "NewsDetailBean{" +
                    "ads=" + ads +
                    ", imgsrc='" + imgsrc + '\'' +
                    ", title='" + title + '\'' +
                    ", source='" + source + '\'' +
                    ", replyCount='" + replyCount + '\'' +
                    ", docid='" + docid + '\'' +
                    ", specialID='" + specialID + '\'' +
                    '}';
        }

        public static NewsDetailBean objectFromData(String str) {

            return new Gson().fromJson(str, NewsDetailBean.class);
        }

        public String getReplyCount() {
            return replyCount;
        }

        void setReplyCount(String replyCount) {
            this.replyCount = replyCount;
        }

        public String getDocid() {
            return docid;
        }

        public void setDocid(String docid) {
            this.docid = docid;
        }

        public String getImgsrc() {
            return imgsrc;
        }

        public void setImgsrc(String imgsrc) {
            this.imgsrc = imgsrc;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getSpecialID() {
            return specialID;
        }

        public void setSpecialID(String specialID) {
            this.specialID = specialID;
        }

        public static class BannerBean {

            private String imgsrc;
            private String title;
            private String url;

            @Override
            public String toString() {
                return "BannerBean{" +
                        "imgsrc='" + imgsrc + '\'' +
                        ", title='" + title + '\'' +
                        ", url='" + url + '\'' +
                        '}';
            }

            public static BannerBean objectFromData(String str) {

                return new Gson().fromJson(str, BannerBean.class);
            }

            public String getImgsrc() {
                return imgsrc;
            }

            public void setImgsrc(String imgsrc) {
                this.imgsrc = imgsrc;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

    }
}
