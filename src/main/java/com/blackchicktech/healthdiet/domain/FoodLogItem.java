package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodLogItem {

    @JsonProperty("foodId")
    private String foodId;

    @JsonProperty("unit")
    private double unit;

    @JsonProperty("channel") //市场，超市
    private String channel;

    @JsonProperty("edible") //食部，入库作回显
    private String edible;

    @JsonProperty("foodName") //食材名，入库作回显
    private String foodName;

    public FoodLogItem() {
    }

    public FoodLogItem(String foodId, double unit, String channel) {
        this.foodId = foodId;
        this.unit = unit;
        this.channel = channel;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public double getUnit() {
        return unit;
    }

    public void setUnit(double unit) {
        this.unit = unit;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getEdible() {
        return edible;
    }

    public void setEdible(String edible) {
        this.edible = edible;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    @Override
    public String toString() {
        return "FoodLogItem{" +
                "foodId='" + foodId + '\'' +
                ", unit=" + unit +
                ", channel='" + channel + '\'' +
                '}';
    }
}
