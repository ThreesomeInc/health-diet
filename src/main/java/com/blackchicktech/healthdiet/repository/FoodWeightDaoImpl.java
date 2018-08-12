package com.blackchicktech.healthdiet.repository;

import com.blackchicktech.healthdiet.entity.FoodWeight;
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
public class FoodWeightDaoImpl {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper rowMapper = new BeanPropertyRowMapper(FoodWeight.class);


    public FoodWeight getFoodWeightByFoodCode(String foodCode){
        List<FoodWeight> foodWeightList = jdbcTemplate.query("SELECT * from food_weight_tbl where food_code = " + foodCode, rowMapper);
        if(foodWeightList != null){
            return foodWeightList.get(0);
        } else {
            return null;
        }
    }
}
