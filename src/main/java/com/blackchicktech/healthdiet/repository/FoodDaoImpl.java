package com.blackchicktech.healthdiet.repository;

import com.blackchicktech.healthdiet.domain.FoodListItem;
import com.blackchicktech.healthdiet.entity.FoodTbl;
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

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private RowMapper rowMapper = new BeanPropertyRowMapper(FoodTbl.class);

	@Override
	public FoodTbl getFoodById(String foodId) {
		List<FoodTbl> foodList = jdbcTemplate.query("SELECT * FROM food_tbl WHERE food_id = ?",
				rowMapper, foodId);
		return foodList.stream().findFirst().orElse(null);
	}

	public List<FoodListItem> getFoodByTypeId(String typeId) {
		List<FoodListItem> foodListItems = jdbcTemplate.query("SELECT * from food_tbl where food_code = " + typeId,
				(resultSet, i) -> {
					FoodListItem foodListItem = new FoodListItem(
							resultSet.getString("food_id"),
							resultSet.getString("food_name"),
							"somePic.pic",
							resultSet.getString("energy"));
					return foodListItem;
				}
		);
		return foodListItems;
	}

	public List<FoodListItem> getFoodByName(String foodName) {
		List<FoodListItem> foodListItems = jdbcTemplate.query("SELECT * FROM food_tbl WHERE food_name LIKE  ?",
				(resultSet, i) -> {
					FoodListItem foodListItem = new FoodListItem(
							resultSet.getString("food_id"),
							resultSet.getString("food_name"),
							"somePic.pic",
							resultSet.getString("energy"));
					return foodListItem;
				}
				, "%" + foodName + "%");
		return foodListItems;
	}
}
