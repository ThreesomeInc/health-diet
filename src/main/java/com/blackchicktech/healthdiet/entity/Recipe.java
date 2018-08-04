package com.blackchicktech.healthdiet.entity;

import java.util.List;

public class Recipe implements Entity {

    private String recipeId;

    private String recipeName;

    private String recipePicUrl;

    private String detail; //食谱内容描述

    private List<Food> foodList; //所包含的食物内容
}
