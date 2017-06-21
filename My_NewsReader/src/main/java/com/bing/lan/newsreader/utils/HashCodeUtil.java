package com.bing.lan.newsreader.utils;


public class HashCodeUtil {

    public static String toHashCode(String url) {

        if (url != null) {
            int hashCode = url.hashCode();
            return String.valueOf(hashCode);
        }
        return "";
    }
}
