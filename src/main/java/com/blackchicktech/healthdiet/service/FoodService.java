package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.domain.FoodDetailResponse;
import com.blackchicktech.healthdiet.domain.FoodDieticianAdvice;
import com.blackchicktech.healthdiet.domain.FoodType;
import com.blackchicktech.healthdiet.entity.Food;
import com.blackchicktech.healthdiet.entity.User;
import com.blackchicktech.healthdiet.repository.FoodDaoImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private static final Logger logger = LoggerFactory.getLogger(FoodService.class);

    private Map<String, Food> cache = new HashMap<>();

    private List<FoodType> typeCache = new LinkedList<>();

    @PostConstruct
    public void reloadCache() {
        //读入缓存 mock用于开发测试
        cache.put("1-1-503,", new Food("1-1-503", "面筋(肉馅)", "someurl", "常见食物", "1", "1", "千卡", "364"));
        cache.put("2-1-107,", new Food("2-1-107", "马铃薯(煮)", "someurl", "常见食物", "2", "1", "千卡", "65"));
        cache.put("3-1-305,", new Food("3-1-305", "豆腐脑", "someurl", "常见食物", "3", "1", "千卡", "15"));
        cache.put("4-8-002,", new Food("4-8-002", "白花菜", "someurl", "常见食物", "4", "8", "千卡", "0"));

        reloadFoodRanking();
    }

    public boolean addFood() {
        return true;
    }

    public boolean deleteFood() {
        return true;
    }

    public List<Food> listFood() {  //分页
        return new ArrayList<>(cache.values());
    }

    public List<FoodType> listFoodType() {
        return new ArrayList<>(typeCache);
    }

    public FoodType getFoodType(String typeCode, String subTypeCode) {
        return null;
    }

    public List<com.blackchicktech.healthdiet.domain.Food> toDomainFoodList() {
        List<com.blackchicktech.healthdiet.domain.Food> result = new ArrayList<>();
        for (Food food : cache.values()) {
            com.blackchicktech.healthdiet.domain.Food domainFood = new com.blackchicktech.healthdiet.domain.Food();
            domainFood.setFoodId(String.valueOf(food.getId()));
            domainFood.setFoodName(food.getFoodName());
            domainFood.setFoodType(getFoodType(food.getType(), food.getSubType()));
            domainFood.setPicUrl(food.getPicUrl());
            domainFood.setUnit(food.getUnit());
            domainFood.setValue(food.getValue());
            result.add(domainFood);
        }
        return result;
    }

    private void reloadFoodRanking() {
        Reader in = null;
        try {
            in = new InputStreamReader(FoodService.class.getResourceAsStream("/food-type-rank.csv"));
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader("rank","alias","typeId").parse(in);
            for (CSVRecord record : records) {
                String rank = record.get("rank"); //貌似没用
                String name = record.get("alias");
                String typeId = record.get("typeId");
                typeCache.add(new FoodType(typeId, name, typeId+".pic"));
            }
            logger.info("Finished to load food rank");
        } catch (Exception e) {
            logger.warn("Fail to read food rank " + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public FoodDetailResponse getFoodByCode(String foodCode, String openId){
        User user = userService.getUserByOpenId(openId);
        Food food = foodDao.getFoodByCode(foodCode);
        FoodDetailResponse foodDetailResponse = new FoodDetailResponse();
        foodDetailResponse.setName(food.getFood_name());
        foodDetailResponse.setAdvice(deduceDieticianAdvice(food, user));
        foodDetailResponse.setComposition(deduceCompostion(food));
        return foodDetailResponse;

    }

    private FoodDieticianAdvice deduceDieticianAdvice(Food food, User user){
        return null;
    }

    private Map<String, String> deduceCompostion(Food food){
        return null;
    }
}
