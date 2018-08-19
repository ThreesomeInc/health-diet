package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.entity.Recipe;
import com.blackchicktech.healthdiet.repository.RecipeDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//食谱
@Service
public class RecipeService {

	@Autowired
	private RecipeDaoImpl recipeDao;

	public List<String> getCategoryList() {
		return recipeDao.getAllCategory();
	}

	public List<String> getMealTimeList() {
		return recipeDao.getAllMealTime();
	}

	public List<Recipe> getRecipeListByCategroy(String category) {
		return recipeDao.getRecipeByCategory(category);
	}

	public List<Recipe> getRecipeListByMealTime(String mealTime) {
		return recipeDao.getRecipeByMealTime(mealTime);
	}

	public Recipe getRecipeById(String recipeId) {
		return recipeDao.getRecipeById(recipeId);
	}

	public List<Recipe> getByName(String name) {
		return recipeDao.getRecipeByName(name);
	}
}
