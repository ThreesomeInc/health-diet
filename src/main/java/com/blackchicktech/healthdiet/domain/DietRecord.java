package com.blackchicktech.healthdiet.domain;

import com.blackchicktech.healthdiet.entity.FoodLogDetail;
import com.blackchicktech.healthdiet.util.FoodLogUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("就餐记录 log your diet")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DietRecord {

    @ApiModelProperty(value = "就餐时间",example = "午餐")
    @JsonProperty("mealtime")
    private String mealtime;

    @JsonProperty("foodLogItems")
    private List<FoodLogItem> foodLogItems;

    public DietRecord(FoodLogDetail foodLogDetail) {
        this.mealtime = foodLogDetail.getMealTime();
        this.foodLogItems = FoodLogUtil.readFromJson(foodLogDetail.getContent());
    }

    public String getMealtime() {
        return mealtime;
    }

    public void setMealtime(String mealtime) {
        this.mealtime = mealtime;
    }

    public List<FoodLogItem> getFoodLogItems() {
        return foodLogItems;
    }

    public void setFoodLogItems(List<FoodLogItem> foodLogItems) {
        this.foodLogItems = foodLogItems;
    }
}
