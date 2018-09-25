package com.blackchicktech.healthdiet.service;


import com.blackchicktech.healthdiet.domain.MealsRecommendationResponse;
import com.blackchicktech.healthdiet.domain.RecommendRecipeInfo;
import com.blackchicktech.healthdiet.entity.FoodRecommended;
import com.blackchicktech.healthdiet.entity.FoodUnit;
import com.blackchicktech.healthdiet.entity.Recipe;
import com.blackchicktech.healthdiet.entity.User;
import com.blackchicktech.healthdiet.repository.FoodDaoImpl;
import com.blackchicktech.healthdiet.repository.MealsDaoImpl;
import com.blackchicktech.healthdiet.repository.RecipeDaoImpl;
import com.blackchicktech.healthdiet.repository.UserDaoImpl;
import com.blackchicktech.healthdiet.util.Constants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MealsService {

    private static Logger LOGGER = LoggerFactory.getLogger(MealsService.class);

    @Autowired
    private MealsDaoImpl mealsDao;

    @Autowired
    private UserDaoImpl userDao;

    @Autowired
    private RecipeDaoImpl recipeDao;

    @Autowired
    private FoodDaoImpl foodDao;

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

    private List<RecommendRecipeInfo> deduceRecommendedBreakfast(FoodRecommended foodRecommended){
        List<RecommendRecipeInfo> breakfast = new ArrayList<>();
        List<String> breakfastElements = candidateFoodElements(foodRecommended, "BR");
        for(String element : breakfastElements){
            LOGGER.info("Trying to get breakfast food for element: {}", element);
            Set<String> ckds = Constants.CKD_FOOD_CATAGARIES.get(element);
            for(String ckd : ckds){
                Recipe recipe = recipeDao.getRecipeByCkdCategory(ckd);
                if(recipe != null){
                    String material = recipe.getMaterial();
                    String recipeId = recipe.getRecipeId();
                    String recipeName = recipe.getRecipeName();
                    FoodUnit food = foodDao.getFoodUnitByAlias(material);
                    if(food != null){
                        float foodProtein = food.getProtein();
                        int foodEdible = food.getEdible();
                        double protein = deduceCandidateFoodFieldValue(foodRecommended, element, "BP");
                        RecommendRecipeInfo recommendRecipeInfo = new RecommendRecipeInfo();
                        recommendRecipeInfo.setRecipeName(recipeName);
                        recommendRecipeInfo.setRecipeId(recipeId);
                        Map<String, Integer> materialMap = new HashMap<>();
                        materialMap.put(material, BigDecimal.valueOf(protein/foodProtein/foodEdible*10000)
                                .setScale(0, BigDecimal.ROUND_FLOOR)
                                .intValue());
                        recommendRecipeInfo.setMaterials(materialMap);
                        recommendRecipeInfo.setProtein(protein);
                        breakfast.add(recommendRecipeInfo);
                    }

                }

            }
        }
        return breakfast;

    }

    private List<RecommendRecipeInfo> deduceRecommendedLunch(FoodRecommended foodRecommended){
        List<RecommendRecipeInfo> lunch = new ArrayList<>();
        List<String> breakfastElements = candidateFoodElements(foodRecommended, "LR");
        for(String element : breakfastElements){
            LOGGER.info("Trying to get lunch food for element: {}", element);
            Set<String> ckds = Constants.CKD_FOOD_CATAGARIES.get(element);
            boolean hasETypeYet = false;
            for(String ckd : ckds){
                if(hasETypeYet && ckd.contains("E")){
                    continue;
                }
                if(ckd.contains("E")){
                    hasETypeYet = true;
                }
                Recipe recipe = recipeDao.getRecipeByCkdCategory(ckd);
                if(recipe != null){
                    String material = recipe.getMaterial();
                    String recipeId = recipe.getRecipeId();
                    String recipeName = recipe.getRecipeName();
                    String meal_time = recipe.getMealTime();
                    if(!"早餐".equals(meal_time)){
                        FoodUnit food = foodDao.getFoodUnitByAlias(material);
                        if(food != null){
                            float foodProtein = food.getProtein();
                            int foodEdible = food.getEdible();
                            double protein = deduceCandidateFoodFieldValue(foodRecommended, element, "LP");
                            RecommendRecipeInfo recommendRecipeInfo = new RecommendRecipeInfo();
                            recommendRecipeInfo.setRecipeName(recipeName);
                            recommendRecipeInfo.setRecipeId(recipeId);
                            Map<String, Integer> materialMap = new HashMap<>();
                            materialMap.put(material, BigDecimal.valueOf(protein/foodProtein/foodEdible*10000)
                                    .setScale(0, BigDecimal.ROUND_FLOOR)
                                    .intValue());
                            recommendRecipeInfo.setMaterials(materialMap);
                            recommendRecipeInfo.setProtein(protein);
                            lunch.add(recommendRecipeInfo);
                        }
                    }


                }

            }
        }
        return lunch;

    }

    private List<RecommendRecipeInfo> deduceRecommendedDinner(FoodRecommended foodRecommended){
        List<RecommendRecipeInfo> recommendedDinner = new ArrayList<>();
        List<String> breakfastElements = candidateFoodElements(foodRecommended, "DR");
        for(String element : breakfastElements){
            LOGGER.info("Trying to get dinner food for element: {}", element);
            Set<String> ckds = Constants.CKD_FOOD_CATAGARIES.get(element);
            boolean hasETypeYet = false;
            for(String ckd : ckds){
                if(hasETypeYet && ckd.contains("E")){
                    continue;
                }
                if(ckd.contains("E")){
                    hasETypeYet = true;
                }
                Recipe recipe = recipeDao.getRecipeByCkdCategory(ckd);
                if(recipe != null){
                    if(recipe != null){
                        String material = recipe.getMaterial();
                        String recipeId = recipe.getRecipeId();
                        String recipeName = recipe.getRecipeName();
                        String meal_time = recipe.getMealTime();
                        if(!"早餐".equals(meal_time)){
                            FoodUnit food = foodDao.getFoodUnitByAlias(material);
                            if(food != null){
                                float foodProtein = food.getProtein();
                                int foodEdible = food.getEdible();
                                double protein = deduceCandidateFoodFieldValue(foodRecommended, element, "DP");
                                RecommendRecipeInfo recommendRecipeInfo = new RecommendRecipeInfo();
                                recommendRecipeInfo.setRecipeName(recipeName);
                                recommendRecipeInfo.setRecipeId(recipeId);
                                Map<String, Integer> materialMap = new HashMap<>();
                                materialMap.put(material, BigDecimal.valueOf(protein/foodProtein/foodEdible*10000)
                                        .setScale(0, BigDecimal.ROUND_FLOOR)
                                        .intValue());
                                recommendRecipeInfo.setMaterials(materialMap);
                                recommendRecipeInfo.setProtein(protein);
                                recommendedDinner.add(recommendRecipeInfo);
                            }
                        }
                    }

                }
            }
        }

            return recommendedDinner;

        }



    private List<RecommendRecipeInfo> deduceRecommendedAdditionalMeal(FoodRecommended foodRecommended){
        List<RecommendRecipeInfo> recommendAdditionalMeal = new ArrayList<>();
        List<String> breakfastElements = candidateFoodElements(foodRecommended, "AR");
        for(String element : breakfastElements) {
            LOGGER.info("Trying to get additional meal food for element: {}", element);
            Set<String> ckds = Constants.CKD_FOOD_CATAGARIES.get(element);
            for (String ckd : ckds) {
                Recipe recipe = recipeDao.getRecipeByCkdCategory(ckd);
                if (recipe != null) {
                    if (recipe != null) {
                        String material = recipe.getMaterial();
                        String recipeId = recipe.getRecipeId();
                        String recipeName = recipe.getRecipeName();
                        String meal_time = recipe.getMealTime();
                        if (!"早餐".equals(meal_time)) {
                            FoodUnit food = foodDao.getFoodUnitByAlias(material);
                            if (food != null) {
                                float foodProtein = food.getProtein();
                                int foodEdible = food.getEdible();
                                double protein = deduceCandidateFoodFieldValue(foodRecommended, element, "AP");
                                RecommendRecipeInfo recommendRecipeInfo = new RecommendRecipeInfo();
                                recommendRecipeInfo.setRecipeName(recipeName);
                                recommendRecipeInfo.setRecipeId(recipeId);
                                Map<String, Integer> materialMap = new HashMap<>();
                                materialMap.put(material, BigDecimal.valueOf(protein / foodProtein / foodEdible * 10000)
                                        .setScale(0, BigDecimal.ROUND_FLOOR)
                                        .intValue());
                                recommendRecipeInfo.setMaterials(materialMap);
                                recommendRecipeInfo.setProtein(protein);
                                recommendAdditionalMeal.add(recommendRecipeInfo);
                            }
                        }
                    }

                }
            }

        }
        return recommendAdditionalMeal;
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

    public List<String> candidateFoodElements(FoodRecommended foodRecommended, String suffix){
        List<String> candidateFoodElements = new ArrayList<>();
        Method[] methods = FoodRecommended.class.getDeclaredMethods();
        for(Method method : methods){
            String methodName = method.getName();
            if(methodName.startsWith("get") && methodName.endsWith(suffix)){
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

    public double deduceCandidateFoodFieldValue(FoodRecommended foodRecommended, String element, String suffix){

        Method[] methods = FoodRecommended.class.getDeclaredMethods();
        for(Method method : methods){
            String methodName = method.getName();
            if(methodName.startsWith("get") && methodName.endsWith(suffix) && StringUtils.containsIgnoreCase(methodName,element)){
                try {
                    return (Double)method.invoke(foodRecommended);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

}
