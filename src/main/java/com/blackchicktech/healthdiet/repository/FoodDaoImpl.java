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

/**
 * Created by Eric Cen on 2018/8/12.
 */
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
						resultSet.getString("energy"))
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
						resultSet.getString("energy"))
				, "%" + foodName + "%");
		logger.info("Finished to query food by foodName={}, totally {} counts", foodName, foodListItems.size());
		return foodListItems;
	}

	public FoodListItem getFoodByAlias(String alias) {
		logger.info("Query food by food alias foodAlias={}", alias);
		List<FoodListItem> foodListItems = jdbcTemplate.query("SELECT * FROM food_tbl WHERE food_alias = ?",
				(resultSet, i) -> new FoodListItem(
						resultSet.getString("food_id"),
						resultSet.getString("food_name"),
						"somePic.pic",
						resultSet.getString("energy"))
				, alias);
		FoodListItem foodListItem = foodListItems.stream().findFirst().orElse(null);
		if (foodListItem == null) {
			logger.info("Can not find food by alias={}", alias);
		}
		return foodListItem;
	}

	public FoodUnit getFoodUnit(String foodId) {
		logger.info("Query food unit by food id foodId={}", foodId);
		List<FoodUnit> foodUnitList = jdbcTemplate.query("SELECT food_id, food_name, food_alias, unit, edible FROM food_tbl WHERE food_id = ?",
				(resultSet, i) -> new FoodUnit(
						resultSet.getString("food_id"),
						resultSet.getString("food_name"),
						resultSet.getString("unit"),
						resultSet.getString("food_alias"),
						resultSet.getInt("edible"))
				, foodId);
		FoodUnit foodUnit = foodUnitList.stream().findFirst().orElse(null);
		if (foodUnit == null) {
			logger.info("Can not find food unit by foodId={}", foodId);
		}
		return foodUnit;
	}

}
