package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DietRecordResponse extends BasicResponse {

    @JsonProperty("totalEnergy")
    private String totalEnergy;

    @JsonProperty("totalProtein")
    private String totalProtein;

    @JsonProperty("peRatio")
    private String peRatio;

    @JsonProperty("fat")
    private String fat;

    @JsonProperty("feRatio")
    private String feRatio;

    @JsonProperty("cho")
    private String cho;

    @JsonProperty("ceRatio")
    private String ceRatio;

    @JsonProperty("na")
    private String na;

    @JsonProperty("k")
    private String k;

    @JsonProperty("p")
    private String p;

    @JsonProperty("ca")
    private String ca;

    public DietRecordResponse(AccumulativeEnergy energy) {
        this.totalEnergy = formatDouble(energy.getTotalEnergy());
        this.totalProtein = formatDouble(energy.getTotalProtein());
        this.peRatio = formatDouble(energy.getPeRatio());
        this.fat = formatDouble(energy.getFat());
        this.feRatio = formatDouble(energy.getFeRatio());
        this.cho = formatDouble(energy.getCho());
        this.ceRatio = formatDouble(energy.getCeRatio());
        this.na = formatDouble(energy.getNa());
        this.k = formatDouble(energy.getK());
        this.p = formatDouble(energy.getP());
        this.ca = formatDouble(energy.getCa());
    }

    @JsonIgnore
    private static String formatDouble(double d) {
        return String.format("%.2f",d);
    }
}
