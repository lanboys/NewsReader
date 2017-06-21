package com.bing.lan.newsreader.bean;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 520 on 2017/1/1.
 */

public class AdsBean implements Serializable {

    private int next_req;
    private List<AdBean> ads;

    public static AdsBean objectFromData(String str) {

        return new Gson().fromJson(str, AdsBean.class);
    }

    public int getNext_req() {
        return next_req;
    }

    public void setNext_req(int next_req) {
        this.next_req = next_req;
    }

    public List<AdBean> getAds() {
        return ads;
    }

    public void setAds(List<AdBean> ads) {
        this.ads = ads;
    }

    @Override
    public String toString() {
        return "AdsBean{" +
                "ads=" + ads +
                ", next_req=" + next_req +
                '}';
    }

    public static class AdBean implements Serializable {

        private List<String> res_url;
        private ActionParamsBean action_params;

        public static AdBean objectFromData(String str) {

            return new Gson().fromJson(str, AdBean.class);
        }

        public List<String> getRes_url() {
            return res_url;
        }

        public void setRes_url(List<String> res_url) {
            this.res_url = res_url;
        }

        @Override
        public String toString() {
            return "AdBean{" +
                    "res_url=" + res_url +
                    '}';
        }


        public ActionParamsBean getAction_params() {

            return action_params;
        }

        public void setAction_params(ActionParamsBean action_params) {
            this.action_params = action_params;
        }

        public static class ActionParamsBean implements Serializable {

            private String link_url;

            public static ActionParamsBean objectFromData(String str) {

                return new Gson().fromJson(str, ActionParamsBean.class);
            }

            public String getLink_url() {
                return link_url;
            }

            public void setLink_url(String link_url) {
                this.link_url = link_url;
            }

            @Override
            public String toString() {
                return "ActionParamsBean{" +
                        "link_url='" + link_url + '\'' +
                        '}';
            }
        }
    }
}
