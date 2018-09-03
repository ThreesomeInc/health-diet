package com.blackchicktech.healthdiet.service;


import com.blackchicktech.healthdiet.domain.MealsRecommendationResponse;
import org.springframework.stereotype.Service;

@Service
public class MealsService {

    public MealsRecommendationResponse getRecommendedMeals(String openId){
        return new MealsRecommendationResponse();
    }
}
