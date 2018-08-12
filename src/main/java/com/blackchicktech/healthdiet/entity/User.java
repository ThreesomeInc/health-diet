package com.blackchicktech.healthdiet.entity;

/**
 * Created by Eric Cen on 2018/8/12.
 */
public class User {

    private String openId;

    private String gender;

    private String birthDay;

    private int height;

    private float weight;

    private String sportRate;

    private int nephroticPeriod;

    private String treatmentMethod;

    private String otherDiseases;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getSportRate() {
        return sportRate;
    }

    public void setSportRate(String sportRate) {
        this.sportRate = sportRate;
    }

    public int getNephroticPeriod() {
        return nephroticPeriod;
    }

    public void setNephroticPeriod(int nephroticPeriod) {
        this.nephroticPeriod = nephroticPeriod;
    }

    public String getTreatmentMethod() {
        return treatmentMethod;
    }

    public void setTreatmentMethod(String treatmentMethod) {
        this.treatmentMethod = treatmentMethod;
    }

    public String getOtherDiseases() {
        return otherDiseases;
    }

    public void setOtherDiseases(String otherDiseases) {
        this.otherDiseases = otherDiseases;
    }
}
