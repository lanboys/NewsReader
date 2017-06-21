package com.bing.lan.newsreader.cons;

import static com.bing.lan.newsreader.cons.Constants.API.HOT_NEWS_JSON_URL;
import static com.bing.lan.newsreader.cons.Constants.API.NEWS_DETAIL_JSON_URL;
import static com.bing.lan.newsreader.cons.Constants.API.NEWS_REPLY_JSON_URL;

public class Constants {

    public static final String SP_FILE_NAME = "sp_file_name.xml";
    public static final String DEAD_LINE = "dead_line";
    public static final String ADS_JSON = "ads_json";
    public static final String ADS_PIC_INDEX = "ads_pic_index";
    public static final String AD_URL = "AD_URL";

    public static final String getHotUrl(int start, int end) {
        String url = HOT_NEWS_JSON_URL;
        url = url.replace("START", String.valueOf(start))
                .replace("END", String.valueOf(end));
        return url;
    }

    public static final String getNewsDetailUrl(String docId) {
        String url = NEWS_DETAIL_JSON_URL;
        url = url.replace("DOCID", docId);
        return url;
    }

    public static final String getNewsReplyUrl(String docId) {
        String url = NEWS_REPLY_JSON_URL;
        url = url.replace("REPLY", docId);
        return url;
    }

    public class API {

        public static final String ADS_JSON_URL = "http://g1.163.com/madr?app=7A16FBB6&platform=android&category=STARTUP&location=1";
        public static final String HOT_NEWS_JSON_URL = "http://c.m.163.com/nc/article/headline/T1348647909107/START-END.html?from=toutiao&size=10&passport=&devId=bMo6EQztO2ZGFBurrbgcMQ%3D%3D&net=wifi";
        public static final String NEWS_DETAIL_JSON_URL = "http://c.m.163.com/nc/article/DOCID/full.html";

        public static final String NEWS_REPLY_JSON_URL = "http://comment.api.163.com/api/v1/products/" +
                "a2869674571f77b5a0867c3d71db5856/threads/REPLY" +
                "/app/comments/hotList?offset=0&limit=10&showLevelThreshold=10&headLimit=2&tailLimit=2";
    }
}
