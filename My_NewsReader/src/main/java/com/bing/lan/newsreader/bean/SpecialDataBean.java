package com.bing.lan.newsreader.bean;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by 520 on 2017/1/5.
 */

public class SpecialDataBean {

    private String skipcontent;
    private String sid;
    private String shownav;
    private String tag;
    private String photoset;
    private String pushTime;
    private String digest;
    private Object webviews;
    private String type;
    private String sname;
    private String ec;
    private String lmodify;
    private String imgsrc;
    private int del;
    private String ptime;
    private String sdocid;
    private String banner;
    private List<?> topicslatest;
    private List<?> topicspatch;
    private List<?> headpics;
    private List<?> topicsplus;
    private List<TopicsBean> topics;

    public static SpecialDataBean objectFromData(String str, String specialId) {

        try {

            JSONObject jsonObject = new JSONObject(str);
            JSONObject detailJsonObj = jsonObject.optJSONObject(specialId);
            str = detailJsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new Gson().fromJson(str, SpecialDataBean.class);
    }

    public String getSkipcontent() {
        return skipcontent;
    }

    public void setSkipcontent(String skipcontent) {
        this.skipcontent = skipcontent;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getShownav() {
        return shownav;
    }

    public void setShownav(String shownav) {
        this.shownav = shownav;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPhotoset() {
        return photoset;
    }

    public void setPhotoset(String photoset) {
        this.photoset = photoset;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public Object getWebviews() {
        return webviews;
    }

    public void setWebviews(Object webviews) {
        this.webviews = webviews;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public String getLmodify() {
        return lmodify;
    }

    public void setLmodify(String lmodify) {
        this.lmodify = lmodify;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public int getDel() {
        return del;
    }

    public void setDel(int del) {
        this.del = del;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getSdocid() {
        return sdocid;
    }

    public void setSdocid(String sdocid) {
        this.sdocid = sdocid;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public List<?> getTopicslatest() {
        return topicslatest;
    }

    public void setTopicslatest(List<?> topicslatest) {
        this.topicslatest = topicslatest;
    }

    public List<?> getTopicspatch() {
        return topicspatch;
    }

    public void setTopicspatch(List<?> topicspatch) {
        this.topicspatch = topicspatch;
    }

    public List<?> getHeadpics() {
        return headpics;
    }

    public void setHeadpics(List<?> headpics) {
        this.headpics = headpics;
    }

    public List<?> getTopicsplus() {
        return topicsplus;
    }

    public void setTopicsplus(List<?> topicsplus) {
        this.topicsplus = topicsplus;
    }

    public List<TopicsBean> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicsBean> topics) {
        this.topics = topics;
    }

    public static class TopicsBean {

        private int index;
        private String tname;
        private String shortname;
        private String type;
        private List<DocsBean> docs;

        public static TopicsBean objectFromData(String str) {

            return new Gson().fromJson(str, TopicsBean.class);
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public String getShortname() {
            return shortname;
        }

        public void setShortname(String shortname) {
            this.shortname = shortname;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<DocsBean> getDocs() {
            return docs;
        }

        public void setDocs(List<DocsBean> docs) {
            this.docs = docs;
        }

        public static class DocsBean {

            private String postid;
            private int votecount;
            private int replyCount;
            private String tag;
            private String ltitle;
            private String digest;
            private String url;
            private String ipadcomment;
            private String docid;
            private String title;
            private String source;
            private String lmodify;
            private String imgsrc;
            private String ptime;
            private int imgsum;
            private String skipID;
            private String skipType;
            private String specialID;

            public static DocsBean objectFromData(String str) {

                return new Gson().fromJson(str, DocsBean.class);
            }

            public String getPostid() {
                return postid;
            }

            public void setPostid(String postid) {
                this.postid = postid;
            }

            public int getVotecount() {
                return votecount;
            }

            public void setVotecount(int votecount) {
                this.votecount = votecount;
            }

            public int getReplyCount() {
                return replyCount;
            }

            public void setReplyCount(int replyCount) {
                this.replyCount = replyCount;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public String getLtitle() {
                return ltitle;
            }

            public void setLtitle(String ltitle) {
                this.ltitle = ltitle;
            }

            public String getDigest() {
                return digest;
            }

            public void setDigest(String digest) {
                this.digest = digest;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getIpadcomment() {
                return ipadcomment;
            }

            public void setIpadcomment(String ipadcomment) {
                this.ipadcomment = ipadcomment;
            }

            public String getDocid() {
                return docid;
            }

            public void setDocid(String docid) {
                this.docid = docid;
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

            public String getLmodify() {
                return lmodify;
            }

            public void setLmodify(String lmodify) {
                this.lmodify = lmodify;
            }

            public String getImgsrc() {
                return imgsrc;
            }

            public void setImgsrc(String imgsrc) {
                this.imgsrc = imgsrc;
            }

            public String getPtime() {
                return ptime;
            }

            public void setPtime(String ptime) {
                this.ptime = ptime;
            }

            public int getImgsum() {
                return imgsum;
            }

            public void setImgsum(int imgsum) {
                this.imgsum = imgsum;
            }

            public String getSkipID() {
                return skipID;
            }

            public void setSkipID(String skipID) {
                this.skipID = skipID;
            }

            public String getSkipType() {
                return skipType;
            }

            public void setSkipType(String skipType) {
                this.skipType = skipType;
            }

            public String getSpecialID() {
                return specialID;
            }

            public void setSpecialID(String specialID) {
                this.specialID = specialID;
            }
        }
    }
}
