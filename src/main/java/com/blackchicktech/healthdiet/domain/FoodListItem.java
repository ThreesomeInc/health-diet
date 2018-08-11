package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//区别于food这个是食材列表只显示有限的信息
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodListItem {

    @JsonProperty("foodId")
    private String foodId;

    @JsonProperty("foodName")
    private String foodName;

    @JsonProperty("picUrl")
    private String picUrl;

    @JsonProperty("nutrition") //400k/100g 具体显示待定
    private String nutrition;

    public FoodListItem(String foodId, String foodName, String picUrl, String nutrition) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.picUrl = picUrl;
        this.nutrition = nutrition;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getNutrition() {
        return nutrition;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }
}
