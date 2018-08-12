package com.blackchicktech.healthdiet.entity;

//数据库中存储的东东
public class FoodTbl {

    private String foodCode;

    private String subCode;

    private String subName;

    private String foodId;

    private String foodName;

    private String water;

    private String energy;

    private String protein;

    private String fat;

    private String cho; //碳水化物

    private String cholesterol; //固醇

    private String p; //磷

    private String k; //钾

    private String na; //钠

    private String unit; //单位

    public String getFoodCode() {
        return foodCode;
    }

    public void setFoodCode(String foodCode) {
        this.foodCode = foodCode;
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

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getCho() {
        return cho;
    }

    public void setCho(String cho) {
        this.cho = cho;
    }

    public String getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(String cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getNa() {
        return na;
    }

    public void setNa(String na) {
        this.na = na;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "FoodTbl{" +
                "foodCode='" + foodCode + '\'' +
                ", subCode='" + subCode + '\'' +
                ", subName='" + subName + '\'' +
                ", foodId='" + foodId + '\'' +
                ", foodName='" + foodName + '\'' +
                ", water='" + water + '\'' +
                ", energy='" + energy + '\'' +
                ", protein='" + protein + '\'' +
                ", fat='" + fat + '\'' +
                ", cho='" + cho + '\'' +
                ", cholesterol='" + cholesterol + '\'' +
                ", p='" + p + '\'' +
                ", k='" + k + '\'' +
                ", na='" + na + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}
