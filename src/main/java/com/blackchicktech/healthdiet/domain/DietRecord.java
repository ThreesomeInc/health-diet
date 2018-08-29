package com.blackchicktech.healthdiet.domain;

import com.blackchicktech.healthdiet.entity.FoodLogDetail;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//就餐记录 log your diet
@JsonIgnoreProperties(ignoreUnknown = true)
public class DietRecord {

    @JsonProperty("mealtime")
    private String mealtime;

    @JsonProperty("foodLogItems")
    private String foodLogItems;

    public DietRecord(FoodLogDetail foodLogDetail) {
        this.mealtime = foodLogDetail.getMealTime();
        this.foodLogItems = foodLogDetail.getContent();
    }

    public String getMealtime() {
        return mealtime;
    }

    public void setMealtime(String mealtime) {
        this.mealtime = mealtime;
    }

    public String getFoodLogItems() {
        return foodLogItems;
    }

    public void setFoodLogItems(String foodLogItems) {
        this.foodLogItems = foodLogItems;
    }
}
