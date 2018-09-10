package com.blackchicktech.healthdiet.repository;

import com.blackchicktech.healthdiet.domain.FoodListItem;
import com.blackchicktech.healthdiet.entity.FoodTbl;
import com.blackchicktech.healthdiet.entity.FoodUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FoodDaoImpl implements FoodDao {

	private static final Logger logger = LoggerFactory.getLogger(FoodDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private RowMapper<FoodTbl> rowMapper = new BeanPropertyRowMapper<>(FoodTbl.class);

	@Override
	public FoodTbl getFoodById(String foodId) {
		logger.info("Query food for foodId={}", foodId);
		List<FoodTbl> foodList = jdbcTemplate.query("SELECT * FROM food_tbl WHERE food_id = ?",
				rowMapper, foodId);
		FoodTbl foodTbl = foodList.stream().findFirst().orElse(null);
		if (foodTbl == null) {
			logger.info("Can not find food by foodId={}", foodId);
		}
		return foodTbl;
	}

	public List<FoodListItem> getFoodByTypeId(String typeId) {
		logger.info("Query food by food type foodCode={}", typeId);
		List<FoodListItem> foodListItems = jdbcTemplate.query("SELECT * from food_tbl where food_code = " + typeId,
				(resultSet, i) -> new FoodListItem(
						resultSet.getString("food_id"),
						resultSet.getString("food_name"),
						"somePic.pic",
						resultSet.getString("energy"),
						resultSet.getString("food_alias"))
		);
		logger.info("Finished to query food by foodCode={}, totally {} counts", typeId, foodListItems.size());
		return foodListItems;
	}

	public List<FoodListItem> getFoodByName(String foodName) {
		logger.info("Query food by food name foodName={}", foodName);
		List<FoodListItem> foodListItems = jdbcTemplate.query("SELECT * FROM food_tbl WHERE food_name LIKE  ?",
				(resultSet, i) -> new FoodListItem(
						resultSet.getString("food_id"),
						resultSet.getString("food_name"),
						"somePic.pic",
						resultSet.getString("energy"),
						resultSet.getString("food_alias"))
				, "%" + foodName + "%");
		logger.info("Finished to query food by foodName={}, totally {} counts", foodName, foodListItems.size());
		return foodListItems;
	}

	public List<FoodListItem> listFoodByAlias(String alias) {
		logger.info("Query food list by food alias alias={}", alias);
		List<FoodListItem> foodListItems = jdbcTemplate.query("SELECT * FROM food_tbl WHERE food_alias LIKE  ?",
				(resultSet, i) -> new FoodListItem(
						resultSet.getString("food_id"),
						resultSet.getString("food_name"),
						"somePic.pic",
						resultSet.getString("energy"),
						resultSet.getString("food_alias"))
				, "%" + alias + "%");
		logger.info("Finished to query food list by alias={}, totally {} counts", alias, foodListItems.size());
		return foodListItems;
	}

	public FoodListItem getFoodByAlias(String alias) {
		logger.info("Query food by food alias foodAlias={}", alias);
		List<FoodListItem> foodListItems = jdbcTemplate.query("SELECT * FROM food_tbl WHERE food_alias = ?",
				(resultSet, i) -> new FoodListItem(
						resultSet.getString("food_id"),
						resultSet.getString("food_name"),
						"somePic.pic",
						resultSet.getString("energy"),
						resultSet.getString("food_alias"))
				, alias);
		FoodListItem foodListItem = foodListItems.stream().findFirst().orElse(null);
		if (foodListItem == null) {
			logger.info("Can not find food by alias={}", alias);
		}
		return foodListItem;
	}

	public FoodUnit getFoodUnit(String foodId) {
		logger.info("Query food unit by food id foodId={}", foodId);
		List<FoodUnit> foodUnitList = jdbcTemplate.query("SELECT food_id, food_name, food_alias, unit, edible, protein FROM food_tbl WHERE food_id = ?",
				(resultSet, i) -> new FoodUnit(
						resultSet.getString("food_id"),
						resultSet.getString("food_name"),
						resultSet.getString("unit"),
						resultSet.getString("food_alias"),
						resultSet.getInt("edible"),
						resultSet.getFloat("protein"))
				, foodId);
		FoodUnit foodUnit = foodUnitList.stream().findFirst().orElse(null);
		if (foodUnit == null) {
			logger.info("Can not find food unit by foodId={}", foodId);
		}
		return foodUnit;
	}

	public FoodUnit getFoodUnitByAlias(String foodAlias) {
		logger.info("Query food unit by foodAlias food_alias={}", foodAlias);
		List<FoodUnit> foodUnitList = jdbcTemplate.query("SELECT food_id, food_name, food_alias, unit, edible, protein FROM food_tbl WHERE food_alias = ?",
				(resultSet, i) -> new FoodUnit(
						resultSet.getString("food_id"),
						resultSet.getString("food_name"),
						resultSet.getString("unit"),
						resultSet.getString("food_alias"),
						resultSet.getInt("edible"),
						resultSet.getFloat("protein"))
				, foodAlias);
		FoodUnit foodUnit = foodUnitList.stream().findFirst().orElse(null);
		if (foodUnit == null) {
			logger.info("Can not find food unit by foodAlias={}", foodAlias);
		}
		return foodUnit;
	}

}
