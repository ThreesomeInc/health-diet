package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by Eric Cen on 2018/8/26.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ThreeDayFoodLogAnalysis {

    @JsonProperty
    private String logTypeInfo;

    @JsonProperty
    private Map<String, String> elementEvgs;

    @JsonProperty
    private String dieticianAdvice;

    @JsonProperty
    private final static String reminder = "以上建议是基于您上述输入的食物分量得出的结果。希望您能准确记录您的食物摄入量，以便获得更准确的数值";

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

    public Map<String, String> getElementEvgs() {
        return elementEvgs;
    }

    public void setElementEvgs(Map<String, String> elementEvgs) {
        this.elementEvgs = elementEvgs;
    }

    public String getDieticianAdvice() {
        return dieticianAdvice;
    }

    public void setDieticianAdvice(String dieticianAdvice) {
        this.dieticianAdvice = dieticianAdvice;
    }

    public static String getReminder() {
        return reminder;
    }

    @Override
    public String toString() {
        return "ThreeDayFoodLogAnalysis{" +
                "logTypeInfo='" + logTypeInfo + '\'' +
                ", elementEvgs=" + elementEvgs +
                ", dieticianAdvice='" + dieticianAdvice + '\'' +
                '}';
    }
}
