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
	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public RecipeTypeResponse getAllRecipeTypes() {
		List<Map<String, String>> recipeTypes = Lists.newArrayList(
				ImmutableMap.of("key", "mealtime", "value", "早午晚"),
				ImmutableMap.of("key", "category", "value", "素荤")
		);
		return new RecipeTypeResponse(recipeTypes);
	}

	/**
	 * 根据关键字获取菜谱
	 * @param key
	 * @return
	 */
	@GetMapping(value = "/search/{key}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public RecipeListResponse search(@PathVariable("key") String key) {
		return new RecipeListResponse(recipeService.getByName(key).stream().map(RecipeListItem::new).collect(Collectors.toList()));
	}

	/**
	 * 根据分类获取菜谱list
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public RecipeTypeListResponse getAllByType(@PathVariable("type") String type) {
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

	/**
	 * 根据早午晚餐时段获取食谱list
	 * @param mealtimeName
	 * @return
	 */
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

	/**
	 * 根据荤素分类获取食谱list
	 * @param categoryName
	 * @return
	 */
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

	/**
	 * 根据菜谱ID（eg.1)获取食谱具体内容
	 * @param recipeId
	 * @param openId
	 * @return
	 */
	@RequestMapping(value = "/detail/{recipeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public RecipeDetailResponse getRecipeDetailById(@PathVariable String recipeId, @RequestParam String openId) {
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

	/**
	 * 更新菜谱偏好 (1-不喜欢吃/2-马马虎虎/3-超爱吃的)
	 * @param recipeId
	 * @param userId
	 * @param preference
	 * @return
	 */
	@PostMapping(value = "/preference", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public PreferenceResponse pushPreference(@RequestParam String recipeId,
											 @RequestParam String userId,
											 @RequestParam int preference) {
		return preferenceService.save(new Preference(userId, recipeId, preference, "recipe"));
	}

	/**
	 * 获取菜谱偏好 (1-不喜欢吃/2-马马虎虎/3-超爱吃的)
	 * @param recipeId
	 * @param userId
	 * @return
	 */
	@GetMapping(value = "/preference", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public PreferenceResponse getPreference(@RequestParam String recipeId,
											@RequestParam String userId) {
		return preferenceService.listPreference(userId, recipeId, "recipe");
	}
}
