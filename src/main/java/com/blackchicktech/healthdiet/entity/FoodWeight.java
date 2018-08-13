package com.blackchicktech.healthdiet.entity;

//食物赋值表
public class FoodWeight {

    private String foodId;

    private String foodCode;

    private String subCode;

    private String proteinWeight;

    private String fatWeight;

    private String choWeight;

    private String kWeight;

    private String naWeight;

    private String purineWeight;

    private String cholesterolWeight;

    private String pWeight;

    public String getPurineWeight() {
        return purineWeight;
    }

    public void setPurineWeight(String purineWeight) {
        this.purineWeight = purineWeight;
    }

    public String getCholesterolWeight() {
        return cholesterolWeight;
    }

    public void setCholesterolWeight(String cholesterolWeight) {
        this.cholesterolWeight = cholesterolWeight;
    }

    public String getpWeight() {
        return pWeight;
    }

    public void setpWeight(String pWeight) {
        this.pWeight = pWeight;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

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

    public String getProteinWeight() {
        return proteinWeight;
    }

    public void setProteinWeight(String proteinWeight) {
        this.proteinWeight = proteinWeight;
    }

    public String getFatWeight() {
        return fatWeight;
    }

    public void setFatWeight(String fatWeight) {
        this.fatWeight = fatWeight;
    }

    public String getChoWeight() {
        return choWeight;
    }

    public void setChoWeight(String choWeight) {
        this.choWeight = choWeight;
    }

    public String getkWeight() {
        return kWeight;
    }

    public void setkWeight(String kWeight) {
        this.kWeight = kWeight;
    }

    public String getNaWeight() {
        return naWeight;
    }

    public void setNaWeight(String naWeight) {
        this.naWeight = naWeight;
    }

    @Override
    public String toString() {
        return "FoodWeight{" +
                "foodId='" + foodId + '\'' +
                ", foodCode='" + foodCode + '\'' +
                ", subCode='" + subCode + '\'' +
                ", proteinWeight='" + proteinWeight + '\'' +
                ", fatWeight='" + fatWeight + '\'' +
                ", choWeight='" + choWeight + '\'' +
                ", kWeight='" + kWeight + '\'' +
                ", naWeight='" + naWeight + '\'' +
                ", purineWeight='" + purineWeight + '\'' +
                ", cholesterolWeight='" + cholesterolWeight + '\'' +
                ", pWeight='" + pWeight + '\'' +
                '}';
    }
}
