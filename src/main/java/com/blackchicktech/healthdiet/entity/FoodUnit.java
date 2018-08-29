package com.blackchicktech.healthdiet.entity;

public class FoodUnit {

    private String foodId;

    private String foodName;

    private String unit; //单位

    private String foodAlias; //别名

    private int edible; //食部

    public FoodUnit(String foodId, String foodName, String unit, String foodAlias, int edible) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.unit = unit;
        this.foodAlias = foodAlias;
        this.edible = edible;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFoodAlias() {
        return foodAlias;
    }

    public void setFoodAlias(String foodAlias) {
        this.foodAlias = foodAlias;
    }

    public int getEdible() {
        return edible;
    }

    public void setEdible(int edible) {
        this.edible = edible;
    }

    @Override
    public String toString() {
        return "FoodUnit{" +
                "foodId='" + foodId + '\'' +
                ", foodName='" + foodName + '\'' +
                ", unit='" + unit + '\'' +
                ", foodAlias='" + foodAlias + '\'' +
                ", edible=" + edible +
                '}';
    }
}
