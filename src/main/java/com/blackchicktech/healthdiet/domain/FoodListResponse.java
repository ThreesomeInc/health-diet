package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodListResponse extends BasicResponse {

    @JsonProperty("foodList")
    private List<Food> foodList;

    public FoodListResponse(List<Food> foodList) {
        this.foodList = foodList;
    }
}
