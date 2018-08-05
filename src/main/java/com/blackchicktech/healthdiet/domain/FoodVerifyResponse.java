package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodVerifyResponse {

    @JsonProperty("valid")
    private Const.FoodAdviceLevel level;

    @JsonProperty("msg")
    private String warningMsg; //如果不能吃，原因

}
