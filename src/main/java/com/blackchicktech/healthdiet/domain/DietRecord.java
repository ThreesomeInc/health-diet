package com.blackchicktech.healthdiet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//就餐记录 log your diet
@JsonIgnoreProperties(ignoreUnknown = true)
public class DietRecord {

    @JsonProperty("time")
    private String time;

    @JsonProperty("date")
    private String date;

    @JsonProperty("location")
    private String location; //home, outside

    @JsonProperty("diet")
    private Diet diet;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Diet getDiet() {
        return diet;
    }

    public void setDiet(Diet diet) {
        this.diet = diet;
    }
}
