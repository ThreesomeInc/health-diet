package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.domain.FoodType;
import com.blackchicktech.healthdiet.entity.Food;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

//食材相关
@Service
public class FoodService {

    private Map<String, Food> cache = new HashMap<>();

    private Set<FoodType> typeCache = new HashSet<>();

    @PostConstruct
    public void reloadCache() {
        //读入缓存 mock用于开发测试
        cache.put("1-1-503,", new Food("1-1-503", "面筋(肉馅)", "someurl", "常见食物", "1", "1", "千卡", "364"));
        cache.put("2-1-107,", new Food("2-1-107", "马铃薯(煮)", "someurl", "常见食物", "2", "1", "千卡", "65"));
        cache.put("3-1-305,", new Food("3-1-305", "豆腐脑", "someurl", "常见食物", "3", "1", "千卡", "15"));
        cache.put("4-8-002,", new Food("4-8-002", "白花菜", "someurl", "常见食物", "4", "8", "千卡", "0"));

        typeCache.add(new FoodType("1", "谷类", "1.pic"));
        typeCache.add(new FoodType("2", "薯类淀粉", "2.pic"));
        typeCache.add(new FoodType("3", "干豆类", "3.pic"));
        typeCache.add(new FoodType("4", "蔬菜类", "4.pic"));
        typeCache.add(new FoodType("5", "菌藻类", "5.pic"));
        typeCache.add(new FoodType("6", "水果类", "6.pic"));
        typeCache.add(new FoodType("7", "坚果种子", "7.pic"));
        typeCache.add(new FoodType("8", "畜肉类", "8.pic"));
        typeCache.add(new FoodType("9", "禽肉类", "9.pic"));
        typeCache.add(new FoodType("10", "乳类", "10.pic"));
        typeCache.add(new FoodType("11", "蛋类", "11.pic"));
        typeCache.add(new FoodType("12", "鱼虾蟹贝类", "12.pic"));
        typeCache.add(new FoodType("13", "婴幼儿食品", "13.pic"));
        typeCache.add(new FoodType("14", "小吃甜饼", "14.pic"));
        typeCache.add(new FoodType("15", "速食食品", "15.pic"));
        typeCache.add(new FoodType("16", "饮料", "16.pic"));
        typeCache.add(new FoodType("17", "魔法制品类", "17.pic"));
        typeCache.add(new FoodType("18", "糖果蜜饯", "18.pic"));
        typeCache.add(new FoodType("19", "油脂类", "19.pic"));
        typeCache.add(new FoodType("20", "调味品类", "20.pic"));
        typeCache.add(new FoodType("21", "其他", "21.pic"));

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
