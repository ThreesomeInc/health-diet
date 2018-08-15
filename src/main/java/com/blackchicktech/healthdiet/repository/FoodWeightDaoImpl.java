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


    public FoodWeight getFoodWeightByFoodId(String foodId){
        List<FoodWeight> foodWeightList = jdbcTemplate.query("SELECT * from food_weight_tbl where food_id = ?",
                                                                rowMapper, foodId);
        return foodWeightList.stream().findFirst().orElse(null);
    }

    public List<FoodWeight> getFoodWeightByProteinWeightAndSubCode(int proteinWeight, String subCode){
        List<FoodWeight> foodWeights = jdbcTemplate.query("SELECT * from food_weight_tbl where " +
                                                            " protein_weight = ? and sub_code = ? limit 3" ,
                                                            rowMapper, proteinWeight, subCode);
        return foodWeights;
    }

    public List<FoodWeight> getFoodWeightByMultiWeightFieldsAndSubCode(List<String> multiWeightFields, String subCode){
        StringBuffer sqlSegment = new StringBuffer();
        for(int i = 0; i < multiWeightFields.size(); i++){
            sqlSegment.append(multiWeightFields.get(i)).append(" = 1 and ");
        }
        String sql = "SELECT * from food_weight_tbl where protein_weight = 1 and " +
                sqlSegment + " sub_code = " + subCode + " limit 3";
        List<FoodWeight> foodWeights = jdbcTemplate.query(sql, rowMapper);
        return foodWeights;
    }
}
