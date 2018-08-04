package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDataInfo {

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("birth")
    private String birthDay;

    @JsonProperty("height")
    private String height;

    @JsonProperty("weight")
    private String weight;

    @JsonProperty("sportRate")
    private String sportRate;

    @JsonProperty("nephroticPeriod")
    private String nephroticPeriod;

    @JsonProperty("treatmentMethod")
    private String treatmentMethod;

    @JsonProperty("otherDisease")
    List<String> otherDisease;

    @JsonProperty("irritability")
    List<String> irritability;

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

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSportRate() {
        return sportRate;
    }

    public void setSportRate(String sportRate) {
        this.sportRate = sportRate;
    }

    public String getNephroticPeriod() {
        return nephroticPeriod;
    }

    public void setNephroticPeriod(String nephroticPeriod) {
        this.nephroticPeriod = nephroticPeriod;
    }

    public String getTreatmentMethod() {
        return treatmentMethod;
    }

    public void setTreatmentMethod(String treatmentMethod) {
        this.treatmentMethod = treatmentMethod;
    }

    public List<String> getOtherDisease() {
        return otherDisease;
    }

    public void setOtherDisease(List<String> otherDisease) {
        this.otherDisease = otherDisease;
    }

    public List<String> getIrritability() {
        return irritability;
    }

    public void setIrritability(List<String> irritability) {
        this.irritability = irritability;
    }
}
