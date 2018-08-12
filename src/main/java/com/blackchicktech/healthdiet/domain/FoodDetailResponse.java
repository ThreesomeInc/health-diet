package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by Eric Cen on 2018/8/11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodDetailResponse extends BasicResponse{

    @JsonProperty("name")
    private String name;

    @JsonProperty("label")
    private String label;

    @JsonProperty("preference")
    private String preference;

    @JsonProperty("composition")
    private Map<String, String> composition;

    @JsonProperty("foodDieticianAdvice")
    private FoodDieticianAdvice foodDieticianAdvice;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public Map<String, String> getComposition() {
        return composition;
    }

    public void setComposition(Map<String, String> composition) {
        this.composition = composition;
    }

    public FoodDieticianAdvice getAdvice() {
        return foodDieticianAdvice;
    }

    public void setAdvice(FoodDieticianAdvice advice) {
        this.foodDieticianAdvice = advice;
    }
}
