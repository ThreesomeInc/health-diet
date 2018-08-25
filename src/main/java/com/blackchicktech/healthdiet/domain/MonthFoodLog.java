package com.blackchicktech.healthdiet.domain;

import com.blackchicktech.healthdiet.entity.FoodLog;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

//每月历史记录
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthFoodLog {

    @JsonProperty("logDate")
    private Date date;

    @JsonProperty("isLogged")
    private boolean logged;

    public MonthFoodLog(FoodLog foodLog) {
        this.date = foodLog.getDate();
        this.logged = foodLog.isRecorded();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    @Override
    public String toString() {
        return "FoodLog{" +
                "date=" + date +
                ", logged=" + logged +
                '}';
    }
}
