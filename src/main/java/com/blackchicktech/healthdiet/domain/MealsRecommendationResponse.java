package com.blackchicktech.healthdiet.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MealsRecommendationResponse {

    @JsonProperty
    private List<Map<String, Float>> breakfast;

    @JsonProperty
    private List<Map<String, Float>> lunch;

    @JsonProperty
    private List<Map<String, Float>> dinner;

    @JsonProperty
    private List<Map<String, Float>> additionMeal;

    public List<Map<String, Float>> getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(List<Map<String, Float>> breakfast) {
        this.breakfast = breakfast;
    }

    public List<Map<String, Float>> getLunch() {
        return lunch;
    }

    public void setLunch(List<Map<String, Float>> lunch) {
        this.lunch = lunch;
    }

    public List<Map<String, Float>> getDinner() {
        return dinner;
    }

    public void setDinner(List<Map<String, Float>> dinner) {
        this.dinner = dinner;
    }

    public List<Map<String, Float>> getAdditionMeal() {
        return additionMeal;
    }

    public void setAdditionMeal(List<Map<String, Float>> additionMeal) {
        this.additionMeal = additionMeal;
    }

    @Override
    public String toString() {
        return "MealsRecommendationResponse{" +
                "breakfast=" + breakfast +
                ", lunch=" + lunch +
                ", dinner=" + dinner +
                ", additionMeal=" + additionMeal +
                '}';
    }
}
