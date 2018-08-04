package com.blackchicktech.healthdiet.controller;

import com.blackchicktech.healthdiet.domain.DietHistoryResponse;
import com.blackchicktech.healthdiet.domain.DietRecordRequest;
import com.blackchicktech.healthdiet.domain.DietRecordResponse;
import com.blackchicktech.healthdiet.service.DietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diet")
public class DietController {

    @Autowired
    private DietService dietService;

    //记录每日膳食
    @PostMapping(MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public DietRecordResponse addDiet(@RequestBody DietRecordRequest request) {
        return new DietRecordResponse();
    }

    //修改每日膳食
    @PutMapping(MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public DietRecordResponse updateDiet(@RequestBody DietRecordRequest request) {
        return new DietRecordResponse();
    }

    //获取每日膳食
    @GetMapping(MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public DietHistoryResponse getDietHistory(@RequestParam String period) { //period=month, period=day eg: period='2018-06', period='2018-06-01'
        return new DietHistoryResponse();
    }
}
