package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Eric Cen on 2018/8/12.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodEatFrequency {

    @JsonProperty("openId")
    private String openId;

    @JsonProperty("foodId")
    private String foodId;

    @JsonProperty("frequency")
    private String frequency;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
