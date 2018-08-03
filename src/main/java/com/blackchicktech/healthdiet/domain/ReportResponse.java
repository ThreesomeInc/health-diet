package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportResponse extends BasicResponse{

    @JsonProperty("healthEstimation")
    private String healthEstimation;

    @JsonProperty("suggestNutrition")
    private List<SuggestNutrition> suggestNutrition = new ArrayList<>();

    @JsonProperty("advice")
    private String advice;

    @JsonProperty("nutrition")
    private List<Nutrition> nutrition = new ArrayList<>();

    public String getHealthEstimation() {
        return healthEstimation;
    }

    public void setHealthEstimation(String healthEstimation) {
        this.healthEstimation = healthEstimation;
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

    public List<Nutrition> getNutrition() {
        return nutrition;
    }

    public void setNutrition(List<Nutrition> nutrition) {
        this.nutrition = nutrition;
    }

    public void addNutrition(Nutrition nutrition) {
        this.nutrition.add(nutrition);
    }
}
