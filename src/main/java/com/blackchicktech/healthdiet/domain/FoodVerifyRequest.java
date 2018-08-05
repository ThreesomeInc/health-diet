package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodVerifyRequest {

    @JsonProperty("userInfo")
    private UserInfo userInfo;

    @JsonProperty("food")
    private Food food;
}
