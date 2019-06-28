package com.yangming.myproject.chat;

import java.io.Serializable;

public class MessageInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 201602231553L;
    private String id = "";
    private String time = "";
    private String title = "";
    private String content = "";
    private String week = "";
    private boolean read = true;
    private int state = 1;// 1:未读；2：已读
    private String picUrl = "";
    private String webUrl = "";
    private String level = "";
    private int fromRobot = 1; // 1:机器人 2：用户 3：时间
    private String date = "";

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFromRobot() {
        return fromRobot;
    }

    public void setFromRobot(int fromRobot) {
        this.fromRobot = fromRobot;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the picUrl
     */
    public String getPicUrl() {
        return picUrl;
    }

    /**
     * @param picUrl the picUrl to set
     */
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    /**
     * @return the webUrl
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * @param webUrl the webUrl to set
     */
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

}
