package com.blackchicktech.healthdiet.service;


import com.blackchicktech.healthdiet.domain.MealsRecommendationResponse;
import com.blackchicktech.healthdiet.entity.FoodRecommended;
import com.blackchicktech.healthdiet.entity.User;
import com.blackchicktech.healthdiet.repository.MealsDaoImpl;
import com.blackchicktech.healthdiet.repository.UserDaoImpl;
import com.blackchicktech.healthdiet.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        int standardWeightRange = deduceStandardWeightRange(standardWeight);
        String ckd = nephroticPeriod == 1 || nephroticPeriod == 2? "CKD 1-2期":"CKD 3-5期";
        FoodRecommended foodRecommended = mealsDao.getFoodRecommendedByStdWgtAndCkd(standardWeightRange, ckd);
        recommendedMeals.setBreakfast(deduceRecommendedBreakfast(foodRecommended));
        recommendedMeals.setLunch(deduceRecommendedLunch(foodRecommended));
        recommendedMeals.setDinner(deduceRecommendedDinner(foodRecommended));
        recommendedMeals.setAdditionMeal(deduceRecommendedAdditionalMeal(foodRecommended));
        return recommendedMeals;
    }

    private List<Map<String, Float>> deduceRecommendedBreakfast(FoodRecommended foodRecommended){
        List<Map<String, Float>> recommendedBreakfast = new ArrayList<>();


        return recommendedBreakfast;

    }

    private List<Map<String, Float>> deduceRecommendedLunch(FoodRecommended foodRecommended){
        List<Map<String, Float>> recommendedLunch = new ArrayList<>();
        return recommendedLunch;

    }

    private List<Map<String, Float>> deduceRecommendedDinner(FoodRecommended foodRecommended){
        List<Map<String, Float>> recommendedDinner = new ArrayList<>();
        return recommendedDinner;

    }

    private List<Map<String, Float>> deduceRecommendedAdditionalMeal(FoodRecommended foodRecommended){
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

    public int deduceStandardWeightRange(float standardWeight){
        if(standardWeight <= 40 || standardWeight > 40 && standardWeight < 45){
            return 40;
        } else if(standardWeight >=45 && standardWeight < 50){
            return 45;
        } else if(standardWeight >= 50 && standardWeight < 55){
            return 50;
        } else if(standardWeight >= 55 && standardWeight < 60){
            return 55;
        } else if(standardWeight >= 60 && standardWeight < 65){
            return 60;
        } else if(standardWeight >= 65 && standardWeight < 70){
            return 65;
        } else if(standardWeight >= 70 && standardWeight < 75){
            return 70;
        }
        return 75;

    }

    public List<String> candidateFoodElements(FoodRecommended foodRecommended){
        List<String> candidateFoodElements = new ArrayList<>();
        Method[] methods = FoodRecommended.class.getDeclaredMethods();
        for(Method method : methods){
            String methodName = method.getName();
            if(methodName.startsWith("get") && methodName.endsWith("R")){
                try {
                    double quantity = (Double)method.invoke(foodRecommended);
                    if(quantity > 0){
                        String candidateFoodElement = methodName.substring(3, methodName.length()-2);
                        candidateFoodElements.add(candidateFoodElement);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return candidateFoodElements;
    }
}
