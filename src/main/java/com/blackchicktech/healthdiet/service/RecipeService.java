package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.domain.FoodListItem;
import com.blackchicktech.healthdiet.domain.MainIngredient;
import com.blackchicktech.healthdiet.domain.PreferenceResponse;
import com.blackchicktech.healthdiet.domain.RecipeListItem;
import com.blackchicktech.healthdiet.entity.Recipe;
import com.blackchicktech.healthdiet.repository.FoodDao;
import com.blackchicktech.healthdiet.repository.FoodDaoImpl;
import com.blackchicktech.healthdiet.repository.RecipeDaoImpl;
import com.blackchicktech.healthdiet.util.IngredientHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//食谱
@Service
public class RecipeService {

	@Autowired
	private RecipeDaoImpl recipeDao;

	@Autowired
	private FoodDaoImpl foodDao;

	@Autowired
	private PreferenceService preferenceService;

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

	public List<Recipe> getRecommendRecipe(String foodName) {
		return recipeDao.getRecommendRecipe(foodName);
	}

	public List<RecipeListItem> getRecommendRecipeList(String foodName) {
		return recipeDao.getRecommendRecipe(foodName).stream().map(RecipeListItem::new).collect(Collectors.toList());
	}

	public List<Recipe> getByName(String name) {
		return recipeDao.getRecipeByName(name);
	}

	public List<MainIngredient> getMappedMainIngredients(String mainIngredients) {
		List<MainIngredient> list = IngredientHelper.parse(mainIngredients);
		for (MainIngredient item : list) {
			FoodListItem food = foodDao.getFoodByAlias(item.getFoodName());
			if (food != null) {
				item.setFoodId(food.getFoodId());
			}
		}
		return list;
	}
}
