package com.blackchicktech.healthdiet.domain;

import com.blackchicktech.healthdiet.entity.FoodUnit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodUnitResponse extends BasicResponse {

    @JsonProperty("foodId")
    private String foodId;

    @JsonProperty("foodName")
    private String foodName;

    @JsonProperty("foodAlias")
    private String foodAlias;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("edible")
    private int edible;

    public FoodUnitResponse() {

    }

    public FoodUnitResponse(FoodUnit foodUnit) {
        this.foodId = foodUnit.getFoodId();
        this.foodName = foodUnit.getFoodName();
        this.foodAlias = foodUnit.getFoodAlias();
        this.unit = foodUnit.getUnit();
        this.edible = foodUnit.getEdible();
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

    public String getFoodAlias() {
        return foodAlias;
    }

    public void setFoodAlias(String foodAlias) {
        this.foodAlias = foodAlias;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getEdible() {
        return edible;
    }

    public void setEdible(int edible) {
        this.edible = edible;
    }

    @Override
    public String toString() {
        return "FoodUnitResponse{" +
                "foodId='" + foodId + '\'' +
                ", foodName='" + foodName + '\'' +
                ", foodAlias='" + foodAlias + '\'' +
                ", unit='" + unit + '\'' +
                ", edible=" + edible +
                "} " + super.toString();
    }
}
