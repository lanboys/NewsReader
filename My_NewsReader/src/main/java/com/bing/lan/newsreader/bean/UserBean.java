package com.bing.lan.newsreader.bean;

/**
 * Created by xmg on 2017/1/6.
 */

public class UserBean {


    private String avatar;
    private String id;
    private String location;
    private String nickname;
    private int userId;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "avatar='" + avatar + '\'' +
                ", id='" + id + '\'' +
                ", location='" + location + '\'' +
                ", nickname='" + nickname + '\'' +
                ", userId=" + userId +
                '}';
    }
}
