package com.blackchicktech.healthdiet.repository;

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
public class FoodDaoImpl implements FoodDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper rowMapper = new BeanPropertyRowMapper(FoodTbl.class);

    @Override
    public FoodTbl getFoodById(String foodId) {
        List<FoodTbl> foodList = jdbcTemplate.query("SELECT * from food_tbl where food_id = " + foodId,
                rowMapper);
        if(foodList != null){
            return foodList.get(0);
        }
        return null;
    }
}
