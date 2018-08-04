package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DietRecordRequest {

    @JsonProperty("userInfo")
    private UserInfo userInfo;

    @JsonProperty("dietRecord")
    private DietRecord dietRecord;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public DietRecord getDietRecord() {
        return dietRecord;
    }

    public void setDietRecord(DietRecord dietRecord) {
        this.dietRecord = dietRecord;
    }
}
