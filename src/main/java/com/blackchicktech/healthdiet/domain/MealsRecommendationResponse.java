package com.blackchicktech.healthdiet.domain;


import com.blackchicktech.healthdiet.entity.Recipe;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MealsRecommendationResponse {

    @JsonProperty
    private List<Map<Recipe, Float>> breakfast;

    @JsonProperty
    private List<Map<Recipe, Float>> lunch;

    @JsonProperty
    private List<Map<Recipe, Float>> dinner;

    @JsonProperty
    private List<Map<Recipe, Float>> addition;

    public List<Map<Recipe, Float>> getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(List<Map<Recipe, Float>> breakfast) {
        this.breakfast = breakfast;
    }

    public List<Map<Recipe, Float>> getLunch() {
        return lunch;
    }

    public void setLunch(List<Map<Recipe, Float>> lunch) {
        this.lunch = lunch;
    }

    public List<Map<Recipe, Float>> getDinner() {
        return dinner;
    }

    public void setDinner(List<Map<Recipe, Float>> dinner) {
        this.dinner = dinner;
    }

    public List<Map<Recipe, Float>> getAddition() {
        return addition;
    }

    public void setAddition(List<Map<Recipe, Float>> addition) {
        this.addition = addition;
    }

    @Override
    public String toString() {
        return "MealsRecommendationResponse{" +
                "breakfast=" + breakfast +
                ", lunch=" + lunch +
                ", dinner=" + dinner +
                ", addition=" + addition +
                '}';
    }
}
