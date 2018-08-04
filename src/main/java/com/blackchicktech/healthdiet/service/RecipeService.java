package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.entity.Food;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

//食谱
@Service
public class RecipeService {

    public void reloadCache() {
        //读入缓存
    }

    public boolean addRecipe() {
        return true;
    }

    public boolean deleteRecipe() {
        return true;
    }

    public List<Food> listRecipe() {  //分页
        return Collections.emptyList();
    }
}
