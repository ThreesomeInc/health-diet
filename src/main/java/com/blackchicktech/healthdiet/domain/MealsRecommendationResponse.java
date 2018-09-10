package com.blackchicktech.healthdiet.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MealsRecommendationResponse {

    @JsonProperty
    private Map<String, Float> breakfast;

    @JsonProperty
    private Map<String, Float> lunch;

    @JsonProperty
    private Map<String, Float> dinner;

    @JsonProperty
    private Map<String, Float> additionMeal;

    public Map<String, Float> getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(Map<String, Float> breakfast) {
        this.breakfast = breakfast;
    }

    public Map<String, Float> getLunch() {
        return lunch;
    }

    public void setLunch(Map<String, Float> lunch) {
        this.lunch = lunch;
    }

    public Map<String, Float> getDinner() {
        return dinner;
    }

    public void setDinner(Map<String, Float> dinner) {
        this.dinner = dinner;
    }

    public Map<String, Float> getAdditionMeal() {
        return additionMeal;
    }

    public void setAdditionMeal(Map<String, Float> additionMeal) {
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
