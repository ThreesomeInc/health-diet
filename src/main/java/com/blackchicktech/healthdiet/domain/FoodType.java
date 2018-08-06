package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodType {

    @JsonProperty("foodTypeCode")
    private String foodTypeCode;

    @JsonProperty("foodTypeName")
    private String foodTypeName;

    @JsonProperty("subCode")
    private String subCode;

    @JsonProperty("subName")
    private String subName;

    public FoodType(String foodTypeCode, String foodTypeName, String subCode, String subName) {
        this.foodTypeCode = foodTypeCode;
        this.foodTypeName = foodTypeName;
        this.subCode = subCode;
        this.subName = subName;
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

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }
}
