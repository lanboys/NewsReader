package com.bing.lan.newsreader.utils;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * @author 蓝兵
 * @time 2017/1/2  9:03
 */
public class FileUtil {

    /**
     * 获取缓存目录
     *
     * @param context
     * @return
     */
    public static String getDiskCacheDir(Context context) {

        String cachePath = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getAbsolutePath();
        } else {
            cachePath = context.getCacheDir().getAbsolutePath();
        }
        return cachePath;
    }

    public static void saveString2Disk(Context context, String str, File file) throws FileNotFoundException {
        LogUtil log = LogUtil.getLogUtil(str.getClass(), 1);
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write(str);
            writer.close();
            // TODO: 2017/1/6 没有正确关闭流
        } catch (Exception e) {
            e.printStackTrace();
            log.e("saveString2Disk: 写出文件出现问题 " + e.getLocalizedMessage());
        }
    }
}
