package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Eric Cen on 2018/8/11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodDieticianAdvice {

    @JsonProperty("advices")
    private String advices;

}
