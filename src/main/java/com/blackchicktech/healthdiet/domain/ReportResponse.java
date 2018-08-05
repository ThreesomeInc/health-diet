package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportResponse extends BasicResponse{


    @JsonProperty("bmi")
    private String bmi;

    @JsonProperty("standardWeight")
    private String standardWeight;

    @JsonProperty("calorie")
    private String calorie;

    @JsonProperty("protein")
    private String protein;


    @JsonProperty("suggestNutrition")
    private List<SuggestNutrition> suggestNutrition = new ArrayList<>();

    @JsonProperty("advice")
    private String advice;


    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getStandardWeight() {
        return standardWeight;
    }

    public void setStandardWeight(String standardWeight) {
        this.standardWeight = standardWeight;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }


    public List<SuggestNutrition> getSuggestNutrition() {
        return suggestNutrition;
    }

    public void setSuggestNutrition(List<SuggestNutrition> suggestNutrition) {
        this.suggestNutrition = suggestNutrition;
    }

    public void addSuggestNutrition(SuggestNutrition suggestNutrition) {
        this.suggestNutrition.add(suggestNutrition);
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}
