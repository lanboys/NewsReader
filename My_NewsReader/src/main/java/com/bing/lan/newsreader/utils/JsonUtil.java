package com.bing.lan.newsreader.utils;

import com.google.gson.Gson;

/**
 * Created by xmg on 2016/12/31.
 */

public class JsonUtil {


    public static <T> T parseJson(String ads_json,Class<T> clz){
        Gson gson = new Gson();
        T adsBean = gson.fromJson(ads_json, clz);
        return adsBean;
    }

}
