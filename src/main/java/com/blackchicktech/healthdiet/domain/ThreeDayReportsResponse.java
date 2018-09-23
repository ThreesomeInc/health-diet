package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ThreeDayReportsResponse extends BasicResponse {

    @JsonProperty("logDateList")
    private List<Integer> logDateList;

    public ThreeDayReportsResponse(List<Date> logDateList) {
        this.logDateList = logDateList.stream().filter(Objects::nonNull)
                .map(item -> new Date(item.getTime()).toInstant()
                        .atZone(ZoneId.systemDefault()).get(ChronoField.DAY_OF_MONTH))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "ThreeDayReportsResponse{" +
                "logDateList=" + logDateList +
                "} " + super.toString();
    }
}
