package com.blackchicktech.healthdiet.controller;

import com.blackchicktech.healthdiet.domain.MealsRecommendationResponse;
import com.blackchicktech.healthdiet.service.MealsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 三餐推荐API
 */
@RestController
@RequestMapping("/meals")
public class MealsController {

    private final static Logger LOGGER = LoggerFactory.getLogger(MealsController.class);

    @Autowired
    private MealsService mealsService;


    @RequestMapping(value="/recommendation", method= RequestMethod.GET, produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public MealsRecommendationResponse getRecommendedMeals(@RequestParam String openId){
        LOGGER.info("Getting recommended meals for user of with openId: {}", openId);
        return mealsService.getRecommendedMeals(openId);
    }


}
