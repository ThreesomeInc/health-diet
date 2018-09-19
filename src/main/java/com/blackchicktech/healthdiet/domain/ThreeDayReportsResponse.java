package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ThreeDayReportsResponse extends BasicResponse {

    @JsonProperty("logDateList")
    private List<Date> logDateList;

    public ThreeDayReportsResponse(List<Date> logDateList) {
        this.logDateList = logDateList;
    }

    @Override
    public String toString() {
        return "ThreeDayReportsResponse{" +
                "logDateList=" + logDateList +
                "} " + super.toString();
    }
}
