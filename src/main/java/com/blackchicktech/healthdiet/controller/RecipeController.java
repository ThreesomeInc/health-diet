package com.blackchicktech.healthdiet.controller;

import com.blackchicktech.healthdiet.domain.*;
import com.blackchicktech.healthdiet.entity.Recipe;
import com.blackchicktech.healthdiet.service.RecipeService;
import com.blackchicktech.healthdiet.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

	private static Logger LOGGER = LoggerFactory.getLogger(RecipeController.class);

	@Autowired
	private UserService userService; //reversed

	@Autowired
	private RecipeService recipeService;

	@RequestMapping(value = "/mealtime", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public RecipeTypeListResponse getAllMealTime() {
		return new RecipeTypeListResponse(recipeService.getMealTimeList());
	}

	@RequestMapping(value = "/mealtime/{mealtimeName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public RecipeListResponse getRecipeListByMealTime(@PathVariable String mealtimeName) {
		List<Recipe> recipeList = recipeService.getRecipeListByMealTime(mealtimeName);
		if (recipeList.isEmpty()) {
			LOGGER.info("Can not find recipe by mealtime={0}", mealtimeName);
			return new RecipeListResponse(Collections.emptyList());
		}
		return new RecipeListResponse(recipeList.stream().map(RecipeListItem::new).collect(Collectors.toList()));
	}

	@RequestMapping(value = "/category", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public RecipeTypeListResponse getAllCategory() {
		return new RecipeTypeListResponse(recipeService.getCategoryList());
	}

	@RequestMapping(value = "/category/{categoryName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public RecipeListResponse getRecipeListByCategory(@PathVariable String categoryName) {
		List<Recipe> recipeList = recipeService.getRecipeListByCategroy(categoryName);
		if (recipeList.isEmpty()) {
			LOGGER.info("Can not find recipe by categoryName={0}", categoryName);
			return new RecipeListResponse(Collections.emptyList());
		}
		return new RecipeListResponse(recipeList.stream().map(RecipeListItem::new).collect(Collectors.toList()));
	}

	@RequestMapping(value = "/detail/{recipeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public RecipeDetailResponse getFoodDetailById(@PathVariable String recipeId) {
		Recipe recipe = recipeService.getRecipeById(recipeId);
		if (recipe == null) {
			LOGGER.info("Can not find recipe by id={0}", recipeId);
			return new RecipeDetailResponse(null);
		}
		return new RecipeDetailResponse(recipe);
	}
}
