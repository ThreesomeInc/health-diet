package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SuggestNutrition {

    @JsonProperty("name")
    private String name;

    @JsonProperty("value")
    private String value;


    public SuggestNutrition(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "SuggestNutrition{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                "}";
    }
}
