package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.entity.Food;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

//食材相关
@Service
public class FoodService {

    public void reloadCache() {
        //读入缓存
    }

    public boolean addFood() {
        return true;
    }

    public boolean deleteFood() {
        return true;
    }

    public List<Food> listFood() {  //分页
        return Collections.emptyList();
    }
}
