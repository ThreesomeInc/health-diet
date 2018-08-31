package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Created by Eric Cen on 2018/8/26.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ThreeDayFoodLogAnalysis {

    @JsonProperty
    private String logTypeInfo;

    @JsonProperty
    private Map<String, Double> elementEvgs;

    @JsonProperty
    private List<String> dieticianAdvice;

    public ThreeDayFoodLogAnalysis() {
    }

    public ThreeDayFoodLogAnalysis(String logTypeInfo) {
        this.logTypeInfo = logTypeInfo;
    }

    public String getLogTypeInfo() {
        return logTypeInfo;
    }

    public void setLogTypeInfo(String logTypeInfo) {
        this.logTypeInfo = logTypeInfo;
    }

    public Map<String, Double> getElementEvgs() {
        return elementEvgs;
    }

    public void setElementEvgs(Map<String, Double> elementEvgs) {
        this.elementEvgs = elementEvgs;
    }

    public List<String> getDieticianAdvice() {
        return dieticianAdvice;
    }

    public void setDieticianAdvice(List<String> dieticianAdvice) {
        this.dieticianAdvice = dieticianAdvice;
    }

    @Override
    public String toString() {
        return "ThreeDayFoodLogAnalysis{" +
                "logTypeInfo='" + logTypeInfo + '\'' +
                ", elementEvgs=" + elementEvgs +
                ", dieticianAdvice=" + dieticianAdvice +
                '}';
    }
}
