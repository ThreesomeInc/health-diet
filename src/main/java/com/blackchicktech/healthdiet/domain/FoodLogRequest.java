package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodLogRequest {

    @JsonProperty("openId")
    private String openId;

    @JsonProperty("logDate")
    private Date date;

    @JsonProperty("mealTime")
    private String mealTime;

    @JsonProperty("foodLogItems")
    private List<FoodLogItem> foodLogItemList;

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

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

    public List<FoodLogItem> getFoodLogItemList() {
        return foodLogItemList;
    }

    public void setFoodLogItemList(List<FoodLogItem> foodLogItemList) {
        this.foodLogItemList = foodLogItemList;
    }

    @Override
    public String toString() {
        return "FoodLogRequest{" +
                "openId='" + openId + '\'' +
                ", date=" + date +
                ", mealTime='" + mealTime + '\'' +
                ", foodLogItemList=" + foodLogItemList +
                '}';
    }
}
