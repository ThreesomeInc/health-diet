package com.blackchicktech.healthdiet.entity;

import java.util.Date;

public class FoodLog {

    private String openId;

    private Date date;

    private boolean isRecorded;

    public FoodLog(String openId, Date date, boolean isRecorded) {
        this.openId = openId;
        this.date = date;
        this.isRecorded = isRecorded;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isRecorded() {
        return isRecorded;
    }

    public void setRecorded(boolean recorded) {
        isRecorded = recorded;
    }

    @Override
    public String toString() {
        return "MonthFoodLog{" +
                "openId='" + openId + '\'' +
                ", date=" + date +
                ", isRecorded=" + isRecorded +
                '}';
    }
}
