package com.blackchicktech.healthdiet.service;


import com.blackchicktech.healthdiet.domain.MealsRecommendationResponse;
import com.blackchicktech.healthdiet.entity.User;
import com.blackchicktech.healthdiet.repository.MealsDaoImpl;
import com.blackchicktech.healthdiet.repository.UserDaoImpl;
import com.blackchicktech.healthdiet.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MealsService {

    @Autowired
    private MealsDaoImpl mealsDao;

    @Autowired
    private UserDaoImpl userDao;

    public MealsRecommendationResponse getRecommendedMeals(String openId){
        User user = userDao.getUserByOpenId(openId);
        float standardWeight = calStandardWeight(user);
        int nephroticPeriod = Integer.valueOf(user.getNephroticPeriod());
        MealsRecommendationResponse recommendedMeals = new MealsRecommendationResponse();
        recommendedMeals.setBreakfast(deduceRecommendedBreakfast(standardWeight, nephroticPeriod));
        recommendedMeals.setLunch(deduceRecommendedLunch(standardWeight, nephroticPeriod));
        recommendedMeals.setDinner(deduceRecommendedDinner(standardWeight, nephroticPeriod));
        recommendedMeals.setAdditionMeal(deduceRecommendedAdditionalMeal(standardWeight, nephroticPeriod));
        return recommendedMeals;
    }

    private List<Map<String, Float>> deduceRecommendedBreakfast(float standardWeight, int nephroticPeriod){
        List<Map<String, Float>> recommendedBreakfast = new ArrayList<>();
        return recommendedBreakfast;

    }

    private List<Map<String, Float>> deduceRecommendedLunch(float standardWeight, int nephroticPeriod){
        List<Map<String, Float>> recommendedLunch = new ArrayList<>();
        return recommendedLunch;

    }

    private List<Map<String, Float>> deduceRecommendedDinner(float standardWeight, int nephroticPeriod){
        List<Map<String, Float>> recommendedDinner = new ArrayList<>();
        return recommendedDinner;

    }

    private List<Map<String, Float>> deduceRecommendedAdditionalMeal(float standardWeight, int nephroticPeriod){
        List<Map<String, Float>> recommendedAdditionalMeal = new ArrayList<>();
        return recommendedAdditionalMeal;
    }



    public float calStandardWeight(User user) {
        String gender = user.getGender();
        String height = getHeightOrWeight(user.getHeight());
        if ("male".equals(gender) || Constants.GENDER.getOrDefault("male", "").equals(gender)) {
            return (float) ((Integer.parseInt(height) - 100) * 0.9);
        } else if ("female".equals(gender) || Constants.GENDER.getOrDefault("female", "").equals(gender)) {
            return (float) ((Integer.parseInt(height) - 100) * 0.9 - 2.5);
        } else {
            throw new IllegalArgumentException("Gender can only be male or female");
        }
    }

    private String getHeightOrWeight(String value) {
        Matcher result = Pattern.compile("(\\d+(.\\d))?").matcher(value);
        return result.find() ? result.group() : "0";
    }
}
