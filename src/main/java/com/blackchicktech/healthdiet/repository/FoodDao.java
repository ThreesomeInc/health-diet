package com.blackchicktech.healthdiet.repository;

import com.blackchicktech.healthdiet.entity.Food;

public interface FoodDao {

    public Food getFoodByCode(String foodCode);
}
