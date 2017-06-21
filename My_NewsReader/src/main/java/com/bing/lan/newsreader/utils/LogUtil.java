package com.bing.lan.newsreader.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogUtil {

    public static final int LOG_VERBOSE = 1;
    public static final int LOG_INFO = 2;
    public static final int LOG_DEBUG = 3;
    public static final int LOG_WARN = 4;
    public static final int LOG_ERROR = 5;
    public static final int LOG_NONE = 6;

    /* date formatter */
    private static SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA);

    /* global log level and default value is LOG_ERROR */
    private static int GLOBAL_LOG_LEVEL = LOG_ERROR;

    public static void setTagPrefix(String tagPrefix) {
        TAG_PREFIX = tagPrefix;
    }

    /* default prefix of tag */
    private static String TAG_PREFIX = "bingtag-->";
    private static boolean isOpen = true;
    /* current class log level */
    private int mLogLevel;
    /* current class log tag */
    private String tag;

    private LogUtil(Class clz, int level) {
        tag = TAG_PREFIX + clz.getSimpleName();
        mLogLevel = level;
    }

    /**
     * set global log level
     */
    public static void setGlobalLogLevel(int level) {
        GLOBAL_LOG_LEVEL = level;
    }

    /**
     * log tag equal clz name and log level equal logLevel
     */
    public static LogUtil getLogUtil(Class clz, int logLevel) {
        return new LogUtil(clz, logLevel);
    }

    /**
     * log tag equal clz name and log level equal GLOBAL_LOG_LEVEL
     */
    public static LogUtil getLogUtil(Class clz) {
        return getLogUtil(clz, GLOBAL_LOG_LEVEL);
    }

    private String getPriorityLetter(int level) {
        switch (level) {
            case LOG_VERBOSE:
                return "Verbose";
            case LOG_INFO:
                return "Info";
            case LOG_DEBUG:
                return "Debug";
            case LOG_WARN:
                return "Warning";
            case LOG_ERROR:
                return "Error";
            // case LOG_NONE:
            //     return "Not_Log";
        }
        return null;
    }

    private String getFormat(String msg, int level) {
        return String.format("%s %s: %s\n", formatter.format(new Date()), getPriorityLetter(level), msg);
    }

    /**
     * 默认打印错误级别,禁用可用级别 7,加一个方法
     *
     * @param level
     * @return
     */
    private boolean ensureLevel(int level) {
        //日志总开关
        if (!isOpen) {
            return false;
        }
        //确定打印级别,true,则打印
        return (GLOBAL_LOG_LEVEL <= level || mLogLevel < level);
    }

    private void verbose(String tag, String msg, boolean format) {
        if (!ensureLevel(LOG_VERBOSE))
            return;
        if (format)
            msg = getFormat(msg, LOG_VERBOSE);
        Log.v(tag, msg);
    }

    private void info(String tag, String msg, boolean format) {
        if (!ensureLevel(LOG_INFO))
            return;
        if (format)
            msg = getFormat(msg, LOG_INFO);
        Log.i(tag, msg);
    }

    private void debug(String tag, String msg, boolean format) {
        if (!ensureLevel(LOG_DEBUG))
            return;
        if (format)
            msg = getFormat(msg, LOG_DEBUG);
        Log.d(tag, msg);
    }

    private void warn(String tag, String msg, boolean format) {
        if (!ensureLevel(LOG_WARN))
            return;
        if (format)
            msg = getFormat(msg, LOG_WARN);
        Log.w(tag, msg);
    }

    private void error(String tag, String msg, boolean format) {
        if (!ensureLevel(LOG_ERROR))
            return;
        if (format)
            msg = getFormat(msg, LOG_ERROR);
        Log.e(tag, msg);
    }

    ////////////log verbose/////////////
    public void v(String msg) {
        v(tag, msg);
    }

    public void v(String tag, String msg) {
        verbose(tag, msg, false);
    }

    public void vfmat(String msg) {
        vfmat(tag, msg);
    }

    public void vfmat(String tag, String msg) {
        verbose(tag, msg, true);
    }

    ////////////log info/////////////
    public void i(String msg) {
        i(tag, msg);
    }

    public void i(String tag, String msg) {
        info(tag, msg, false);
    }

    public void ifmat(String msg) {
        ifmat(tag, msg);
    }

    public void ifmat(String tag, String msg) {
        info(tag, msg, true);
    }

    ////////////log debug/////////////
    public void d(String msg) {
        d(tag, msg);
    }

    public void d(String tag, String msg) {
        debug(tag, msg, false);
    }

    public void dfmat(String msg) {
        dfmat(tag, msg);
    }

    public void dfmat(String tag, String msg) {
        debug(tag, msg, true);
    }

    ////////////log warn/////////////
    public void w(String msg) {
        w(tag, msg);
    }

    public void w(String tag, String msg) {
        warn(tag, msg, false);
    }

    public void wfmat(String msg) {
        wfmat(tag, msg);
    }

    public void wfmat(String tag, String msg) {
        warn(tag, msg, true);
    }

    ////////////log error/////////////
    public void e(String msg) {
        e(tag, msg);
    }

    public void e(String tag, String msg) {
        error(tag, msg, false);
    }

    public void efmat(String msg) {
        efmat(tag, msg);
    }

    public void efmat(String tag, String msg) {
        error(tag, msg, true);
    }
}
