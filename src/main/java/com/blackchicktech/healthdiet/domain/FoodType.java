package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodType {

    @JsonProperty("foodTypeCode")
    private String foodTypeCode;

    @JsonProperty("foodTypeName")
    private String foodTypeName;

    @JsonProperty("picUrl")
    private String picUrl;

    public FoodType(String foodTypeCode, String foodTypeName, String picUrl) {
        this.foodTypeCode = foodTypeCode;
        this.foodTypeName = foodTypeName;
        this.picUrl = picUrl;
    }

    public String getFoodTypeCode() {
        return foodTypeCode;
    }

    public void setFoodTypeCode(String foodTypeCode) {
        this.foodTypeCode = foodTypeCode;
    }

    public String getFoodTypeName() {
        return foodTypeName;
    }

    public void setFoodTypeName(String foodTypeName) {
        this.foodTypeName = foodTypeName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
