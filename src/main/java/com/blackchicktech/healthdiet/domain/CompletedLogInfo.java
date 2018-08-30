package com.blackchicktech.healthdiet.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CompletedLogInfo {

    @JsonProperty
    private String openId;

    @JsonProperty
    private String date;

    @JsonProperty
    private boolean checked;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "CompletedLogInfo{" +
                "openId='" + openId + '\'' +
                ", date='" + date + '\'' +
                ", checked=" + checked +
                '}';
    }
}
