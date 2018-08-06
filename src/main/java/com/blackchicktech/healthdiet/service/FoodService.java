package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.domain.FoodType;
import com.blackchicktech.healthdiet.entity.Food;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//食材相关
@Service
public class FoodService {

    private Map<String, Food> cache = new HashMap<>();

    private Map<String, FoodType> typeCache = new HashMap<>();

    @PostConstruct
    public void reloadCache() {
        //读入缓存 mock用于开发测试
        cache.put("1-1-503,", new Food("1-1-503", "面筋(肉馅)", "someurl", "常见食物", "1", "1", "千卡", "364"));
        cache.put("2-1-107,", new Food("2-1-107", "马铃薯(煮)", "someurl", "常见食物", "2", "1", "千卡", "65"));
        cache.put("3-1-305,", new Food("3-1-305", "豆腐脑", "someurl", "常见食物", "3", "1", "千卡", "15"));
        cache.put("4-8-002,", new Food("4-8-002", "白花菜", "someurl", "常见食物", "4", "8","千卡", "0"));

        typeCache.put("1-1", new FoodType("1", "谷类及制品", "1", "小麦"));
        typeCache.put("2-1", new FoodType("2", "薯类淀粉", "1", "薯类"));
        typeCache.put("2-2", new FoodType("2", "薯类淀粉", "2", "淀粉"));
        typeCache.put("3-1", new FoodType("3", "干豆类及制品", "1", "大豆"));
        typeCache.put("4-8", new FoodType("4", "蔬菜类及制品", "8", "野生蔬菜类"));
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
        return new ArrayList<>(typeCache.values());
    }

    public FoodType getFoodType(String typeCode, String subTypeCode) {
        return typeCache.get(typeCode + "-" + subTypeCode);
    }

    public List<com.blackchicktech.healthdiet.domain.Food> toDomainFoodList() {
        List<com.blackchicktech.healthdiet.domain.Food> result = new ArrayList<>();
        for (Food food : cache.values()) {
            com.blackchicktech.healthdiet.domain.Food domainFood = new com.blackchicktech.healthdiet.domain.Food();
            domainFood.setFoodId(food.getFoodId());
            domainFood.setFoodName(food.getFoodName());
            domainFood.setFoodType(getFoodType(food.getType(), food.getSubType()));
            domainFood.setPicUrl(food.getPicUrl());
            domainFood.setUnit(food.getUnit());
            domainFood.setValue(food.getValue());
            result.add(domainFood);
        }
        return result;
    }
}
