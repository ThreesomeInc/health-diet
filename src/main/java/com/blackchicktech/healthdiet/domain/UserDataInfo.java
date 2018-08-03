package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDataInfo {

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("birth")
    private String birthDay;

    @JsonProperty("height")
    private String height;

    @JsonProperty("weight")
    private String weight;

    @JsonProperty("sportRate")
    private String sportRate;

    @JsonProperty("nephroticPeriod")
    private String nephroticPeriod;

    @JsonProperty("treatmentMethod")
    private String treatmentMethod;

    @JsonProperty("otherDisease")
    List<String> otherDisease;

    @JsonProperty("irritability")
    List<String> irritability;

}
