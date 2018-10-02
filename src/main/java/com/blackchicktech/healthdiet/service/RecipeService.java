package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.domain.FoodListItem;
import com.blackchicktech.healthdiet.domain.MainIngredient;
import com.blackchicktech.healthdiet.domain.RecipeListItem;
import com.blackchicktech.healthdiet.entity.Recipe;
import com.blackchicktech.healthdiet.entity.RecipeWeight;
import com.blackchicktech.healthdiet.entity.User;
import com.blackchicktech.healthdiet.repository.FoodDaoImpl;
import com.blackchicktech.healthdiet.repository.RecipeDaoImpl;
import com.blackchicktech.healthdiet.repository.RecipeWeightDaoImpl;
import com.blackchicktech.healthdiet.repository.UserDaoImpl;
import com.blackchicktech.healthdiet.util.Constants;
import com.blackchicktech.healthdiet.util.IngredientHelper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//食谱
@Service
public class RecipeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RecipeService.class);

	@Autowired
	private RecipeDaoImpl recipeDao;

	@Autowired
	private FoodDaoImpl foodDao;

	@Autowired
	private PreferenceService preferenceService;

	@Autowired
	private UserDaoImpl userDao;

	@Autowired
	private RecipeWeightDaoImpl recipeWeightDao;


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
		if (StringUtils.isBlank(name)) {
			return Lists.newArrayList();
		}
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

	public String deduceDieticianAdvice(Recipe recipe, String openId) {
		User user = userDao.getUserByOpenId(openId);
		//TODO: user empty validation
		if (user == null) {
			return "";
		}
		RecipeWeight recipeWeight = recipeWeightDao.getRecipeWeightByRecipeId(recipe.getRecipeId());

		int nephroticPeriod = Integer.valueOf(user.getNephroticPeriod());
		String otherDiseases = user.getOtherDiseases();

		int proteinWeight = recipeWeight.getProteinWeight();

		String material = recipe.getMaterial().split("|")[0];

		StringBuilder dieticianAdvice = new StringBuilder();
		StringBuilder otherDiseasesConbinations = new StringBuilder();
		List<String> otherDiseaseFoodWeightFields = new ArrayList<>();

		if (otherDiseases != null && !StringUtils.isEmpty(otherDiseases)) {
			String[] otherDiseasesArray = otherDiseases.split(",");
			LOGGER.info("Other Diseases: \n");
			Arrays.stream(otherDiseasesArray).forEach(LOGGER::info);
			List<String> otherDiseasesList = Arrays.asList(otherDiseasesArray);
			List<String> lowWeight = new ArrayList<>();
			List<String> mediumWeight = new ArrayList<>();
			List<String> highWeight = new ArrayList<>();
			deduceWeight(lowWeight, mediumWeight, highWeight, recipeWeight, otherDiseasesList);
			for (int i = 0; i < otherDiseasesArray.length; i++) {
				otherDiseaseFoodWeightFields.add(Constants.WEIGHT_FILED_DISEASE_MAP.get(otherDiseasesArray[i]));
				if (i != otherDiseasesArray.length - 1) {
					otherDiseasesConbinations.append(Constants.OTHER_DISEASE_ELEMENTS.get(otherDiseasesArray[i]))
							.append(",");
				} else {
					otherDiseasesConbinations.append(Constants.OTHER_DISEASE_ELEMENTS.get(otherDiseasesArray[i]));
				}

			}
			dieticianAdvice.append(String.format(Constants.DIETICIAN_ADVICE_TEMPLATE,
					nephroticPeriod, otherDiseasesConbinations));
			dieticianAdvice.append("该食谱");
			if (!lowWeight.isEmpty()) {
				for (int i = 0; i < lowWeight.size(); i++) {
					if (i != lowWeight.size() - 1) {
						dieticianAdvice.append(lowWeight.get(i)).append("、");
					} else {
						dieticianAdvice.append(lowWeight.get(i)).append("含量低, ");
					}
				}
			}
			if (!mediumWeight.isEmpty()) {
				for (int i = 0; i < mediumWeight.size(); i++) {
					if (i != mediumWeight.size() - 1) {
						dieticianAdvice.append(mediumWeight.get(i)).append("、");
					} else {
						dieticianAdvice.append(mediumWeight.get(i)).append("含量适中, ");
					}
				}
			}
			if (!highWeight.isEmpty()) {
				if (!lowWeight.isEmpty() || !mediumWeight.isEmpty()) {
					dieticianAdvice.append("但");
				}
				for (int i = 0; i < highWeight.size(); i++) {
					if (i != highWeight.size() - 1) {
						dieticianAdvice.append(highWeight.get(i)).append("、");
					} else {
						dieticianAdvice.append(highWeight.get(i)).append("含量偏高, ");
					}
				}
			}

			int maxWeight = getMaxWeight(recipeWeight, otherDiseasesList);

			if (maxWeight == 1) {
				dieticianAdvice.append("可经常食用。");
			} else if (maxWeight == 2) {
				dieticianAdvice.append("可适量食用。");
			} else {
				dieticianAdvice.append("不适宜您食用。 \n");
				String recommendRecipes = deduceRecipeForMultiDisease(otherDiseaseFoodWeightFields, material);
				if (recommendRecipes != null) {
					dieticianAdvice.append("以下食谱更适合您: ").append(recommendRecipes);
				}
			}

		} else {
			dieticianAdvice.append(String.format(Constants.DIETICIAN_ADVICE_WITHOUT_NEOPATHY_TEMPLATE, nephroticPeriod));
			if (proteinWeight == 1) {
				dieticianAdvice.append("该食谱蛋白含量低，可经常食用。");
			} else if (proteinWeight == 2) {
				dieticianAdvice.append("该食谱蛋白含量适中，可适量食用。");
			} else {
				dieticianAdvice.append("该食谱蛋白含量偏高，不适宜您食用。\n");
				String recommendFood = deduceRecommendRecipe(material);
				if (recommendFood != null) {
					dieticianAdvice.append("以下食谱更适合您: ").append(recommendFood);
				}
			}
		}
		return dieticianAdvice.toString();


	}

	private void deduceWeight(List<String> lowWeight, List<String> mediumWeight,
							  List<String> highWeight, RecipeWeight recipeWeight, List<String> otherDiseaseList) {

		int proteinWeight = recipeWeight.getProteinWeight();
		deduceWeight(lowWeight, mediumWeight, highWeight, proteinWeight, "蛋白质");
		if (otherDiseaseList.contains("hyperuricacidemia")) {
			int purineWeight = recipeWeight.getPurineWeight();
			deduceWeight(lowWeight, mediumWeight, highWeight, purineWeight, "嘌呤");
		}
		if (otherDiseaseList.contains("cholesterol")) {
			int cholesterolWeight = recipeWeight.getCholesterolWeight();
			deduceWeight(lowWeight, mediumWeight, highWeight, cholesterolWeight, "胆固醇");
		}
		if (otherDiseaseList.contains("hypertension")) {
			int naWeight = recipeWeight.getNaWeight();
			deduceWeight(lowWeight, mediumWeight, highWeight, naWeight, "钠");
		}
		if (otherDiseaseList.contains("triglyceride")) {
			int fatWeight = recipeWeight.getFatWeight();
			deduceWeight(lowWeight, mediumWeight, highWeight, fatWeight, "脂肪");
		}
		if (otherDiseaseList.contains("hyperglycemia")) {
			int choWeight = recipeWeight.getChoWeight();
			deduceWeight(lowWeight, mediumWeight, highWeight, choWeight, "碳水化合物");
		}
	}

	private void deduceWeight(List<String> lowWeight, List<String> mediumWeight,
							  List<String> highWeight, int foodWeight, String element) {
		if (foodWeight == 1) {
			lowWeight.add(element);
		} else if (foodWeight == 2) {
			mediumWeight.add(element);
		} else {
			highWeight.add(element);
		}
	}

	private int getMaxWeight(RecipeWeight recipeWeight, List<String> otherDiseases) {
		List<Integer> weights = new ArrayList<>();
		weights.add(recipeWeight.getProteinWeight());
		if (otherDiseases.contains("hyperuricacidemia")) {
			weights.add(recipeWeight.getPurineWeight());
		}
		if (otherDiseases.contains("cholesterol")) {
			weights.add(recipeWeight.getCholesterolWeight());
		}
		if (otherDiseases.contains("hypertension")) {
			weights.add(recipeWeight.getNaWeight());
		}
		if (otherDiseases.contains("triglyceride")) {
			weights.add(recipeWeight.getFatWeight());
		}

		if (otherDiseases.contains("hyperglycemia")) {
			weights.add(recipeWeight.getChoWeight());
		}

		return Collections.max(weights);
	}

	private String deduceRecipeForMultiDisease(List<String> multiWeightFields, String material) {
		List<RecipeWeight> recipeWeights = recipeWeightDao.getRecipeWeightByMultiWeightFieldsAndMaterial(multiWeightFields, material);

		if (recipeWeights == null || recipeWeights.size() == 0) {
			return null;
		}

		StringBuffer recipes = new StringBuffer();

		for (int i = 0; i < recipeWeights.size(); i++) {
			String recipeId = recipeWeights.get(i).getRecipeId();
			Recipe recipe = recipeDao.getRecipeById(recipeId);
			if (i != recipeWeights.size() - 1) {
				recipes.append(recipe.getRecipeName()).append("、");
			} else {
				recipes.append(recipe.getRecipeName()).append("。");
			}
		}
		return recipes.toString();
	}

	private String deduceRecommendRecipe(String material) {
		List<RecipeWeight> recipeWeights = recipeWeightDao.getRecipeWeightByProteinWeightAndMaterial(3, material);
		if (recipeWeights == null && recipeWeights.size() == 0) {
			return null;
		}

		List<Recipe> recipes = new ArrayList<>();
		for (RecipeWeight recipeWeight : recipeWeights) {
			Recipe recipe = recipeDao.getRecipeById(recipeWeight.getRecipeId());
			recipes.add(recipe);
		}
		StringBuffer dieticianAdvice = new StringBuffer();
		for (int i = 0; i < recipes.size(); i++) {
			dieticianAdvice.append(recipes.get(i).getRecipeName());
			if (i != recipes.size() - 1) {
				dieticianAdvice.append("、");
			} else {
				dieticianAdvice.append("。");
			}
		}
		return dieticianAdvice.toString();
	}
}
