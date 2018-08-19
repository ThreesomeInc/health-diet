package com.blackchicktech.healthdiet.repository;

import com.blackchicktech.healthdiet.entity.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecipeDaoImpl {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private RowMapper<Recipe> rowMapper = new BeanPropertyRowMapper(Recipe.class);

	public Recipe getRecipeById(String recipeId) {
		List<Recipe> foodList = jdbcTemplate.query("SELECT * FROM recipe_tbl WHERE recipe_id = ?",
				rowMapper, recipeId);
		return foodList.stream().findFirst().orElse(null);
	}

	public List<String> getAllMealTime() {
		List<String> list = jdbcTemplate.queryForList("SELECT DISTINCT meal_time FROM recipe_tbl", String.class);
		return list;
	}

	public List<String> getAllCategory() {
		List<String> list = jdbcTemplate.queryForList("SELECT DISTINCT category FROM recipe_tbl", String.class);
		return list;
	}

	public List<Recipe> getRecipeByName(String recipeName) {
		List<Recipe> foodListItems = jdbcTemplate.query("SELECT * FROM recipe_tbl WHERE recipe_name LIKE ?",
				rowMapper, recipeName);
		return foodListItems;
	}

	public List<Recipe> getRecipeByMealTime(String mealTime) {
		List<Recipe> foodListItems = jdbcTemplate.query("SELECT * FROM recipe_tbl WHERE meal_time = ?",
				rowMapper, mealTime);
		return foodListItems;
	}

	public List<Recipe> getRecipeByCategory(String category) {
		List<Recipe> foodListItems = jdbcTemplate.query("SELECT * FROM recipe_tbl WHERE category = ?",
				rowMapper, category);
		return foodListItems;
	}

	public List<Recipe> getRecipeByCookMethod(String cookMethod) {
		List<Recipe> foodListItems = jdbcTemplate.query("SELECT * FROM recipe_tbl WHERE cook_method = ?",
				rowMapper, cookMethod);
		return foodListItems;
	}
}
