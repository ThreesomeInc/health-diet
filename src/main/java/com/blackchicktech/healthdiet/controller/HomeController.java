package com.blackchicktech.healthdiet.controller;

import com.blackchicktech.healthdiet.domain.Nutrition;
import com.blackchicktech.healthdiet.domain.ReportRequest;
import com.blackchicktech.healthdiet.domain.ReportResponse;
import com.blackchicktech.healthdiet.domain.SuggestNutrition;
import com.blackchicktech.healthdiet.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    public ReportService reportService;

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
