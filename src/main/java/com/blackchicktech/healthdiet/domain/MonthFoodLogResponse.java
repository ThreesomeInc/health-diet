package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthFoodLogResponse extends BasicResponse {

    @JsonProperty("logFoodList")
    private List<MonthFoodLog> monthFoodLogs;

    @JsonProperty("expectEnergy")
    private String totalEnergy;

    @JsonProperty("expectProtein")
    private String totalProtein;

    public MonthFoodLogResponse(List<MonthFoodLog> monthFoodLogs, String totalEnergy, String totalProtein) {
        this.monthFoodLogs = monthFoodLogs;
        this.totalEnergy = totalEnergy;
        this.totalProtein = totalProtein;
    }

    @Override
    public String toString() {
        return "MonthFoodLogResponse{" +
                "monthFoodLogs=" + monthFoodLogs +
                ", totalEnergy='" + totalEnergy + '\'' +
                ", totalProtein='" + totalProtein + '\'' +
                "} " + super.toString();
    }
}
