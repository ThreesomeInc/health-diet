package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SuggestNutrition {

    @JsonProperty("name")
    private String name;

    @JsonProperty("alias")
    private String alias;

    @JsonProperty("value")
    private String value;


    public SuggestNutrition(String name, String alias, String value) {
        this.name = name;
        this.alias = alias;
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


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "SuggestNutrition{" +
                "name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", value='" + value + '\'' +
                "}";
    }
}
