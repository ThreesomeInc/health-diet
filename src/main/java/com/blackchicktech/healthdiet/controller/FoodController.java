package com.blackchicktech.healthdiet.controller;

import com.blackchicktech.healthdiet.domain.FoodDetailResponse;
import com.blackchicktech.healthdiet.domain.FoodListResponse;
import com.blackchicktech.healthdiet.domain.FoodTypeListResponse;
import com.blackchicktech.healthdiet.domain.PreferenceResponse;
import com.blackchicktech.healthdiet.entity.Preference;
import com.blackchicktech.healthdiet.service.FoodService;
import com.blackchicktech.healthdiet.service.PreferenceService;
import com.blackchicktech.healthdiet.service.UserService;
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

/**
 * 通过restful更新食物相关接口
 */
@RestController
@RequestMapping("/food")
public class FoodController {

	private static Logger LOGGER = LoggerFactory.getLogger(FoodController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private PreferenceService preferenceService;

	@Autowired
	private FoodService foodService;

	/**
	 * HTTP-GET： Collect food list by name or alias
	 * Usage: UI食材库搜索栏输入名称搜索
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public FoodListResponse getFoodListByName(@RequestParam(value = "name", required = false) String name,
											  @RequestParam(value = "alias", required = false) String alias) {
		if (alias != null) {
			return new FoodListResponse(foodService.listFoodByAlias(alias));
		}
		return new FoodListResponse(foodService.listFoodByName(name));
	}

	/**
	 *	HTTP-GET： Collect food type list
	 *  Usage: UI食材库分类搜索时获取有多少个食材分类（如蛋类等）
	 */
	@RequestMapping(value = "/type", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public FoodTypeListResponse getFoodTypeList() {
		return new FoodTypeListResponse(foodService.listFoodType());
	}

	/**
	 * HTTP-GET： Collect food list by type ID（eg.1)
	 * Usage: UI食材库分类搜索根据分类（如蛋类等）获取该类下食物列表
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public FoodListResponse getFoodList(@RequestParam("typeId") String typeId) {
		return new FoodListResponse(foodService.listFood(typeId));
	}

	/**
	 *	HTTP-GET： get food detail by ID （01-1-101）
	 *  Usage: UI食材库分类搜索（如蛋类等）
	 */
	@RequestMapping(value = "/detail/{foodId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public FoodDetailResponse getFoodDetailById(@PathVariable String foodId,
												@RequestParam("openId") String openId) {
		return foodService.getFoodById(foodId, openId);
	}

	/**
	 *	HTTP-POST: update food preference
	 *  Usage: UI输入用户对该食材的偏好(1-不喜欢吃/2-马马虎虎/3-超爱吃的)
	 */
	@PostMapping(value = "/preference", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public PreferenceResponse pushPreference(@RequestParam String foodId,
											 @RequestParam String userId,
											 @RequestParam int preference) {
		LOGGER.info(userId + "update preference for food(" + foodId + "): "+ preference);
		return preferenceService.save(new Preference(userId, foodId, preference, "food"));
	}

	/**
	 *	HTTP-GET: get food preference
	 *  Usage: UI获取用户对该食材的偏好 (1-不喜欢吃/2-马马虎虎/3-超爱吃的)
	 */
	@GetMapping(value = "/preference", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public PreferenceResponse getPreference(@RequestParam String foodId,
											@RequestParam String userId) {
		return preferenceService.listPreference(userId, foodId, "food");
	}
}
