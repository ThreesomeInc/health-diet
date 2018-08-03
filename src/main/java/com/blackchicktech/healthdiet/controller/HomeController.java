package com.blackchicktech.healthdiet.controller;

import com.blackchicktech.healthdiet.domain.Nutrition;
import com.blackchicktech.healthdiet.domain.ReportRequest;
import com.blackchicktech.healthdiet.domain.ReportResponse;
import com.blackchicktech.healthdiet.domain.SuggestNutrition;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
public class HomeController {


    @RequestMapping(value = "/report", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReportResponse getReport(ReportRequest reportRequest) {
        ReportResponse response = new ReportResponse();
        response.setHealthEstimation("Normal");
        response.setAdvice("No Advice");
        SuggestNutrition sn1 = new SuggestNutrition("energy", "1575", "kcal");
        SuggestNutrition sn2 = new SuggestNutrition("protein", "45", "g");
        response.addSuggestNutrition(sn1);
        response.addSuggestNutrition(sn2);

        Nutrition n1 = new Nutrition("milk", "220", "ml");
        Nutrition n2 = new Nutrition("fruit", "176", "g");
        Nutrition n3 = new Nutrition("egg", "1", "ge");
        response.addNutrition(n1);
        response.addNutrition(n2);
        response.addNutrition(n3);
        return response;
    }
}
