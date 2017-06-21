package com.bing.lan.newsreader.base;

import android.os.Handler;
import android.os.Message;

/**
 * Created by 520 on 2017/1/4.
 */

public abstract class NewsBaseFragment extends BaseFragment {

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    protected Handler mHandler;

    protected void sendMessageToMainFragment(int msgWhat, Object msgObj) {
        if (mHandler != null) {
            Message message = Message.obtain();
            message.what = msgWhat;
            message.obj = msgObj;
            mHandler.sendMessage(message);
        } else {
            log.w("sendMessageToActivity:::Handler为空,发送消息失败!");
        }
    }

    protected void sendMessageToMainFragment(int msgWhat) {
        sendMessageToMainFragment(msgWhat, null);
    }





}
