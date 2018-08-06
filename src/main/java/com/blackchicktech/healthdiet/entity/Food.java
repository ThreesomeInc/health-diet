package com.blackchicktech.healthdiet.entity;

public class Food implements Entity {

    private String foodId; //保留字段做key

    private String foodName;

    private String picUrl;

    private String description; //预留字段用于说明

    private String type; //类型

    private String subType; //子类型

    private String unit; //一份单位 例如卡路里

    private String value; //默认一份所包含的数值

    public Food(String foodId, String foodName, String picUrl, String description, String type, String subType, String unit, String value) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.picUrl = picUrl;
        this.description = description;
        this.type = type;
        this.subType = subType;
        this.unit = unit;
        this.value = value;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
