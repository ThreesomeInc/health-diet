package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.domain.FoodDetailResponse;
import com.blackchicktech.healthdiet.domain.FoodListItem;
import com.blackchicktech.healthdiet.domain.FoodType;
import com.blackchicktech.healthdiet.domain.PreferenceResponse;
import com.blackchicktech.healthdiet.entity.*;
import com.blackchicktech.healthdiet.repository.FoodDaoImpl;
import com.blackchicktech.healthdiet.repository.FoodWeightDaoImpl;
import com.blackchicktech.healthdiet.util.Constants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

//食材相关
@Service
public class FoodService {

    @Autowired
    private FoodDaoImpl foodDao;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodWeightDaoImpl foodWeightDao;

    @Autowired
    private PreferenceService preferenceService;

    @Autowired
    private RecipeService recipeService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FoodService.class);

    private Map<String, Food> cache = new HashMap<>();

    private List<FoodType> typeCache = new LinkedList<>();

    @PostConstruct
    public void reloadCache() {
        //读入缓存 mock用于开发测试
        cache.put("01-1-503,", new Food("01-1-503", "面筋(肉馅)", "someurl", "常见食物", "01", "1", "克", "364"));
        cache.put("02-1-107,", new Food("02-1-107", "马铃薯(煮)", "someurl", "常见食物", "02", "1", "克", "65"));
        cache.put("03-1-305,", new Food("03-1-305", "豆腐脑", "someurl", "常见食物", "03", "1", "克", "15"));
        cache.put("04-8-002,", new Food("04-8-002", "白花菜", "someurl", "常见食物", "04", "8", "克", "0"));

        reloadFoodRanking();
    }


    public List<FoodListItem> listFood(String foodTypeCode) {  //分页
        return foodDao.getFoodByTypeId(foodTypeCode);
    }

    public List<FoodListItem> listFoodByName(String foodName) {
        return foodDao.getFoodByName(foodName);
    }

    public List<FoodType> listFoodType() {
        return new ArrayList<>(typeCache);
    }

    public FoodType getFoodType(String typeCode, String subTypeCode) {
        return null;
    }

    private void reloadFoodRanking() {
        LOGGER.info("Begin to load food rank");
        Reader in = null;
        try {
            in = new InputStreamReader(FoodService.class.getResourceAsStream("/food-type-rank.csv"));
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader("rank", "alias", "typeId")
                    .withSkipHeaderRecord()
                    .parse(in);
            for (CSVRecord record : records) {
                String rank = record.get("rank"); //貌似没用
                String name = record.get("alias");
                String typeId = record.get("typeId");
                typeCache.add(new FoodType(typeId, name, typeId + ".pic"));
            }
            LOGGER.info("Finished to load food rank, total {0} records loaded.", typeCache.size());
        } catch (Exception e) {
            LOGGER.warn("Fail to read food rank " + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public FoodDetailResponse getFoodById(String foodId, String openId) {
        User user = userService.getUserByOpenId(openId);
        FoodTbl food = foodDao.getFoodById(foodId);
        FoodWeight foodWeight = foodWeightDao.getFoodWeightByFoodId(foodId);
        PreferenceResponse preference = preferenceService.listPreference(openId, foodId);

        FoodDetailResponse foodDetailResponse = new FoodDetailResponse();
        foodDetailResponse.setName(food.getFoodName());
        foodDetailResponse.setDieticianAdvice(deduceDieticianAdvice(food, foodWeight, user));
        foodDetailResponse.setComposition(deduceCompostions(food));
        foodDetailResponse.setLabel(deduceLabel(foodWeight));
        foodDetailResponse.setRecipeList(recipeService.getRecommendRecipeList(food.getAlias()));
        if (preference.getPreference() != 0) {
			foodDetailResponse.setFrequency(String.valueOf(preference.getPreference()));
		}
        return foodDetailResponse;

    }

    private List<String> deduceLabel(FoodWeight foodWeight){
        List<String> label = new ArrayList<>();
        LOGGER.info("Deduce Food Label: " + foodWeight);
        int proteinWeight = foodWeight.getProteinWeight();
        int choWeight = foodWeight.getChoWeight();
        int fatWeight = foodWeight.getFatWeight();
        int naWeight = foodWeight.getNaWeight();
        int purineWeight = foodWeight.getPurineWeight();
        int cholesterolWeight = foodWeight.getCholesterolWeight();
        if (proteinWeight == 1) {
            label.add("低蛋白");
        } else if (proteinWeight == 3) {
            label.add("高蛋白");
        }
        if (choWeight == 1) {
            label.add("低糖");
        } else if (choWeight == 3) {
            label.add("高糖");
        }
        if (fatWeight == 1) {
            label.add("低脂肪");
        } else if (fatWeight == 3) {
            label.add("高脂肪");
        }
        if (naWeight == 1) {
            label.add("低钠");
        } else if (naWeight == 3) {
            label.add("高钠");
        }
        if (purineWeight == 1) {
            label.add("低嘌呤");
        } else if (purineWeight == 3) {
            label.add("高嘌呤");
        }
        if (cholesterolWeight == 1) {
            label.add("低胆固醇");
        } else if (cholesterolWeight == 3) {
            label.add("高胆固醇");
        }
        return label;
    }

    private Map<String, String> deduceCompostions(FoodTbl food){
		Map<String, String> compositions = new HashMap<>();
		Optional.ofNullable(food.getWater()).ifPresent(item -> compositions.put("水", item + "克"));
		Optional.of(food.getEnergy()).filter(item -> item > 0).ifPresent(item -> compositions.put("热量", item + "千卡"));
		Optional.of(food.getProtein()).filter(item -> item > 0).ifPresent(item -> compositions.put("蛋白质", item + "克"));
		Optional.ofNullable(food.getFat()).ifPresent(item -> compositions.put("脂肪", item + "克"));
		Optional.ofNullable(food.getCho()).ifPresent(item -> compositions.put("碳水化合物", item + "克"));
		Optional.ofNullable(food.getNa()).ifPresent(item -> compositions.put("钠", item + "毫克"));
		Optional.ofNullable(food.getP()).ifPresent(item -> compositions.put("磷", item + "毫克"));
		Optional.ofNullable(food.getK()).ifPresent(item -> compositions.put("钾", item + "毫克"));
		Optional.ofNullable(food.getCholesterol()).ifPresent(item -> compositions.put("胆固醇", item + "毫克"));
		return compositions;
    }

    private String deduceDieticianAdvice(FoodTbl food, FoodWeight foodWeight, User user) {
        //TODO: user null validation ? return temporarily for front-end testing
        if (user == null) {
            return "";
        }
        int nephroticPeriod = Integer.valueOf(user.getNephroticPeriod());
        String otherDiseases = user.getOtherDiseases();
        String subCode = food.getSubCode();
        String foodCode = food.getFoodCode();

        int proteinWeight = foodWeight.getProteinWeight();
        StringBuilder dieticianAdvice = new StringBuilder();
        StringBuilder otherDiseasesConbinations = new StringBuilder();
        List<String> otherDiseaseFoodWeightFields = new ArrayList<>();

        if (otherDiseases != null && !StringUtils.isEmpty(otherDiseases)) {
            String[] otherDiseasesArray = otherDiseases.split(",");
            LOGGER.info("Other Diseases: \n");
            Arrays.stream(otherDiseasesArray).forEach(LOGGER::info);
            List<String> otherDiseasesList = Arrays.asList(otherDiseasesArray);
            List<String> lowWeight = new ArrayList<>();
            List<String> mediumWeight = new ArrayList<>();
            List<String> highWeight = new ArrayList<>();
            deduceWeight(lowWeight,mediumWeight,highWeight,foodWeight,otherDiseasesList);
            for (int i = 0; i < otherDiseasesArray.length; i++) {
                otherDiseaseFoodWeightFields.add(Constants.WEIGHT_FILED_DISEASE_MAP.get(otherDiseasesArray[i]));
                if (i != otherDiseasesArray.length - 1) {
                    otherDiseasesConbinations.append(Constants.OTHER_DISEASE_ELEMENTS.get(otherDiseasesArray[i]))
                            .append(",");
                } else {
                    otherDiseasesConbinations.append(Constants.OTHER_DISEASE_ELEMENTS.get(otherDiseasesArray[i]));
                }

            }
            dieticianAdvice.append(String.format(Constants.DIETICIAN_ADVICE_TEMPLATE,
                    nephroticPeriod, otherDiseasesConbinations));
            dieticianAdvice.append("该食物");
            if(!lowWeight.isEmpty()){
                for(int i = 0; i < lowWeight.size(); i++){
                    if(i != lowWeight.size() - 1){
                        dieticianAdvice.append(lowWeight.get(i)).append("、");
                    } else {
                        dieticianAdvice.append(lowWeight.get(i)).append("含量低, ");
                    }
                }
            }
            if(!mediumWeight.isEmpty()){
                for(int i = 0; i < mediumWeight.size(); i++){
                    if(i != mediumWeight.size() - 1){
                        dieticianAdvice.append(mediumWeight.get(i)).append("、");
                    } else {
                        dieticianAdvice.append(mediumWeight.get(i)).append("含量适中, ");
                    }
                }
            }
            if(!highWeight.isEmpty()){
                if(!lowWeight.isEmpty() || !mediumWeight.isEmpty()){
                    dieticianAdvice.append("但");
                }
                for(int i = 0; i < highWeight.size(); i++){
                    if(i != highWeight.size() - 1){
                        dieticianAdvice.append(highWeight.get(i)).append("、");
                    } else {
                        dieticianAdvice.append(highWeight.get(i)).append("含量偏高, ");
                    }
                }
            }

            int maxWeight = getMaxWeight(foodWeight, otherDiseasesList);

            if (maxWeight == 1) {
                dieticianAdvice.append("可经常食用。");
            } else if (maxWeight == 2) {
                dieticianAdvice.append("可适量食用。");
            } else {
                dieticianAdvice.append("不适宜您食用。 \n");
                String recommendFoods = deduceFoodForMultiDisease(otherDiseaseFoodWeightFields, foodCode,subCode);
                if(recommendFoods != null){
                    dieticianAdvice.append("以下食材更适合您: ").append(recommendFoods);
                }
            }

        } else {
            dieticianAdvice.append(String.format(Constants.DIETICIAN_ADVICE_WITHOUT_NEOPATHY_TEMPLATE, nephroticPeriod));
            if (proteinWeight == 1) {
                dieticianAdvice.append("该食物蛋白含量低，可经常食用。");
            } else if (proteinWeight == 2) {
                dieticianAdvice.append("该食物蛋白含量适中，可适量食用。");
            } else {
                dieticianAdvice.append("该食物蛋白含量偏高，不适宜您食用。\n");
                String recommendFood  = deduceRecommendFood(foodCode, subCode);
                if(recommendFood != null){
                   dieticianAdvice.append("以下食材更适合您: ").append(recommendFood);
                }
            }
        }
        return dieticianAdvice.toString();
    }

    private String deduceRecommendFood(String foodCode, String subCode){
        List<FoodWeight> foodWeights = foodWeightDao.getFoodWeightByProteinWeightAndSubCode(2, foodCode, subCode);
        if(foodWeights == null && foodWeights.size() == 0){
            return null;
        }

        List<FoodTbl> foodList = new ArrayList<>();
        for (FoodWeight foodWeight : foodWeights) {
            FoodTbl food = foodDao.getFoodById(foodWeight.getFoodId());
            foodList.add(food);
        }
        StringBuffer dieticianAdvice = new StringBuffer();
        for (int i = 0; i < foodList.size(); i++) {
            dieticianAdvice.append(foodList.get(i));
            if (i != foodList.size() - 1) {
                dieticianAdvice.append("、");
            } else {
                dieticianAdvice.append("。");
            }
        }
        return dieticianAdvice.toString();
    }


    private void deduceWeight(List<String> lowWeight, List<String> mediumWeight,
                              List<String> highWeight, FoodWeight foodWeight, List<String> otherDiseaseList){

        int proteinWeight = foodWeight.getProteinWeight();
        deduceWeight(lowWeight,mediumWeight,highWeight,proteinWeight, "蛋白质");
        if(otherDiseaseList.contains("hyperuricacidemia")){
            int purineWeight = foodWeight.getPurineWeight();
            deduceWeight(lowWeight,mediumWeight,highWeight,purineWeight, "嘌呤");
        }
        if(otherDiseaseList.contains("cholesterol")){
            int cholesterolWeight = foodWeight.getCholesterolWeight();
            deduceWeight(lowWeight,mediumWeight,highWeight,cholesterolWeight, "胆固醇");
        }
        if(otherDiseaseList.contains("hypertension")){
            int naWeight = foodWeight.getNaWeight();
            deduceWeight(lowWeight,mediumWeight,highWeight,naWeight, "钠");
        }
        if(otherDiseaseList.contains("triglyceride")){
            int fatWeight = foodWeight.getFatWeight();
            deduceWeight(lowWeight,mediumWeight,highWeight,fatWeight, "脂肪");
        }
        if(otherDiseaseList.contains("hyperglycemia")){
            int choWeight = foodWeight.getChoWeight();
            deduceWeight(lowWeight,mediumWeight,highWeight,choWeight, "碳水化合物");
        }
    }

    private void deduceWeight(List<String> lowWeight, List<String> mediumWeight,
                              List<String> highWeight, int foodWeight, String element){
        if(foodWeight == 1){
            lowWeight.add(element);
        } else if(foodWeight == 2){
            mediumWeight.add(element);
        } else {
            highWeight.add(element);
        }
    }

    private String deduceFoodForMultiDisease(List<String> multiWeightFields, String foodCode, String subCode){
        List<FoodWeight> foodWeights = foodWeightDao.getFoodWeightByMultiWeightFieldsAndSubCode(multiWeightFields, foodCode, subCode);

        if(foodWeights == null || foodWeights.size() == 0){
            return null;
        }

        StringBuffer foods = new StringBuffer();

        for(int i = 0; i < foodWeights.size(); i++){
            String foodId = foodWeights.get(i).getFoodId();
            FoodTbl food = foodDao.getFoodById(foodId);
            if(i != foodWeights.size() - 1){
                foods.append(food.getFoodName()).append("、");
            } else {
                foods.append(food.getFoodName()).append("。");
            }
        }
        return foods.toString();
    }

    private int getMaxWeight(FoodWeight foodWeight, List<String> otherDiseases) {
        List<Integer> weights = new ArrayList<>();
        weights.add(foodWeight.getProteinWeight());
        if (otherDiseases.contains("hyperuricacidemia")) {
            weights.add(foodWeight.getPurineWeight());
        }
        if (otherDiseases.contains("cholesterol")) {
            weights.add(foodWeight.getCholesterolWeight());
        }
        if (otherDiseases.contains("hypertension")) {
            weights.add(foodWeight.getNaWeight());
        }
        if (otherDiseases.contains("triglyceride")) {
            weights.add(foodWeight.getFatWeight());
        }

        if (otherDiseases.contains("hyperglycemia")) {
            weights.add(foodWeight.getChoWeight());
        }

        return Collections.max(weights);
    }
}




