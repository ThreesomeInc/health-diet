package com.blackchicktech.healthdiet.repository;

import com.blackchicktech.healthdiet.entity.Food;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Created by Eric Cen on 2018/8/12.
 */
@Repository
public class FoodDaoImpl implements FoodDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper rowMapper = new BeanPropertyRowMapper(Food.class);

    @Override
    public Food getFoodByCode(String foodCode) {
        Food food = (Food)jdbcTemplate.query("SELECT * from food_repository where code = " + foodCode,
                rowMapper);
        return food;
    }
}
