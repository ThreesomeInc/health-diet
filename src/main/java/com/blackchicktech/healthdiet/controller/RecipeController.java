package com.blackchicktech.healthdiet.controller;

import com.blackchicktech.healthdiet.domain.PreferenceResponse;
import com.blackchicktech.healthdiet.domain.RecipeDetailResponse;
import com.blackchicktech.healthdiet.domain.RecipeListItem;
import com.blackchicktech.healthdiet.domain.RecipeListResponse;
import com.blackchicktech.healthdiet.domain.RecipeTypeListResponse;
import com.blackchicktech.healthdiet.domain.RecipeTypeResponse;
import com.blackchicktech.healthdiet.entity.Preference;
import com.blackchicktech.healthdiet.entity.Recipe;
import com.blackchicktech.healthdiet.service.PreferenceService;
import com.blackchicktech.healthdiet.service.RecipeService;
import com.blackchicktech.healthdiet.service.UserService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通过restful更新食谱相关接口
 */
@RestController
@RequestMapping("/recipe")
public class RecipeController {

	private static Logger LOGGER = LoggerFactory.getLogger(RecipeController.class);

	@Autowired
	private UserService userService; //reversed

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private PreferenceService preferenceService;

	/**
	 * 获取食谱分类
	 *
	 * @return
	 */
	@ApiOperation(value = "Get type of recipe",
			notes = "获取菜谱类别",
			response = RecipeTypeResponse.class)
	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public RecipeTypeResponse getAllRecipeTypes() {
		List<Map<String, String>> recipeTypes = Lists.newArrayList(
				ImmutableMap.of("key", "mealtime", "value", "早午晚"),
				ImmutableMap.of("key", "category", "value", "素荤")
		);
		return new RecipeTypeResponse(recipeTypes);
	}


	@ApiOperation(value = "Get recipe by keyword",
			notes = "根据关键字获取菜谱",
			response = RecipeListResponse.class)
	@GetMapping(value = "/search/{key}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public RecipeListResponse search(
			@ApiParam(example = "牛肉") @PathVariable("key") String key) {
		return new RecipeListResponse(recipeService.getByName(key).stream().map(RecipeListItem::new).collect(Collectors.toList()));
	}

	@ApiOperation(value = "Get recipe by keyword",
			notes = "根据关键字获取菜谱",
			response = RecipeListResponse.class)
	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public RecipeListResponse searchByName(@ApiParam(example = "牛肉") @RequestParam String name) {
		return search(name);
	}

	@ApiOperation(value = "Get recipe list by type",
			notes = "根据分类获取菜谱list",
			response = RecipeTypeListResponse.class)
	@RequestMapping(value = "/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public RecipeTypeListResponse getAllByType(
			@ApiParam(example = "category") @PathVariable("type") String type) {
		switch (type) {
			case "mealtime": {
				return new RecipeTypeListResponse(recipeService.getMealTimeList());
			}
			case "category": {
				return new RecipeTypeListResponse(recipeService.getCategoryList());
			}
			default:
				throw new RuntimeException("not support yet");
		}
	}

	@ApiOperation(value = "Get recipe list according to mealtime",
			notes = "根据早午晚餐时段获取食谱list",
			response = RecipeListResponse.class)
	@RequestMapping(value = "/mealtime/{mealtimeName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public RecipeListResponse getRecipeListByMealTime(
			@ApiParam(example = "早餐") @PathVariable String mealtimeName) {
		List<Recipe> recipeList = recipeService.getRecipeListByMealTime(mealtimeName);
		if (recipeList.isEmpty()) {
			LOGGER.info("Can not find recipe by mealtime={0}", mealtimeName);
			return new RecipeListResponse(Collections.emptyList());
		}
		return new RecipeListResponse(recipeList.stream().map(RecipeListItem::new).collect(Collectors.toList()));
	}

	@ApiOperation(value = "Get recipe list according to mealtime",
			notes = "根据早午晚餐时段获取食谱list",
			response = RecipeListResponse.class)
	@RequestMapping(value = "/mealtime", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public RecipeListResponse getRecipeListByMealTimeName(@ApiParam(example = "早餐") @RequestParam String mealtimeName) {
		return getRecipeListByMealTime(mealtimeName);
	}

	@ApiOperation(value = "Get recipe list according to category",
			notes = "根据荤素分类获取食谱list",
			response = RecipeListResponse.class)
	@RequestMapping(value = "/category/{categoryName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public RecipeListResponse getRecipeListByCategory(
			@ApiParam(example = "素食") @PathVariable String categoryName) {
		List<Recipe> recipeList = recipeService.getRecipeListByCategroy(categoryName);
		if (recipeList.isEmpty()) {
			LOGGER.info("Can not find recipe by categoryName={0}", categoryName);
			return new RecipeListResponse(Collections.emptyList());
		}
		return new RecipeListResponse(recipeList.stream().map(RecipeListItem::new).collect(Collectors.toList()));
	}

	@ApiOperation(value = "Get recipe list according to category",
			notes = "根据荤素分类获取食谱list",
			response = RecipeListResponse.class)
	@RequestMapping(value = "/category", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public RecipeListResponse getRecipeListByCategoryName(@ApiParam(example = "素食") @RequestParam String categoryName) {
		return getRecipeListByCategory(categoryName);
	}

	@ApiOperation(value = "Get recipe detail by ID",
			notes = "根据菜谱ID（eg.1)获取食谱具体内容",
			response = RecipeDetailResponse.class)
	@RequestMapping(value = "/detail/{recipeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public RecipeDetailResponse getRecipeDetailById(
			@ApiParam(example = "1") @PathVariable String recipeId,
			@ApiParam(example = "oXLZ35Pe0eCs-m084xLMdTnyq7c8") @RequestParam String openId) {
		Recipe recipe = recipeService.getRecipeById(recipeId);
		String dieticianAdvice = recipeService.deduceDieticianAdvice(recipe, openId);
		if (recipe == null) {
			LOGGER.info("Can not find recipe by id={0}", recipeId);
			return new RecipeDetailResponse(null, null, Collections.emptyList(), null);
		}
		PreferenceResponse preference = preferenceService.listPreference(openId, recipeId, "recipe");
		return new RecipeDetailResponse(recipe, dieticianAdvice,
				recipeService.getMappedMainIngredients(recipe.getMainIngredients()), preference);
	}

	@ApiOperation(value = "Update receipe preference",
			notes = "更新菜谱偏好 (1-不喜欢吃/2-马马虎虎/3-超爱吃的)",
			response = PreferenceResponse.class)
	@PostMapping(value = "/preference", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public PreferenceResponse pushPreference(
			@ApiParam(example = "1") @RequestParam String recipeId,
			@ApiParam(example = "oXLZ35Pe0eCs-m084xLMdTnyq7c8") @RequestParam String userId,
			@ApiParam(example = "1") @RequestParam int preference) {
		return preferenceService.save(new Preference(userId, recipeId, preference, "recipe"));
	}


	@ApiOperation(value = "Get receipe preference",
			notes = "获取菜谱偏好 (1-不喜欢吃/2-马马虎虎/3-超爱吃的)",
			response = PreferenceResponse.class)
	@GetMapping(value = "/preference", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public PreferenceResponse getPreference(
			@ApiParam(example = "1") @RequestParam String recipeId,
			@ApiParam(example = "oXLZ35Pe0eCs-m084xLMdTnyq7c8") @RequestParam String userId) {
		return preferenceService.listPreference(userId, recipeId, "recipe");
	}
}
