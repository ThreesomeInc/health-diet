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

    public List<FoodWeight> getFoodWeightByProteinWeightAndSubCode(int proteinWeight, String foodCode, String subCode){
        List<FoodWeight> result;
        result = jdbcTemplate.query("SELECT * from food_weight_tbl where " +
                                                            " protein_weight = ? and food_code = ?  and sub_code = ? limit 3" ,
                                                            rowMapper, proteinWeight, foodCode, subCode);
        if(result == null || result.size() == 0){
            result = jdbcTemplate.query("SELECT * from food_weight_tbl where " +
                            " protein_weight = ? and food_code = ? limit 3" ,
                    rowMapper, proteinWeight, foodCode);
        }
        return result;
    }

    public List<FoodWeight> getFoodWeightByMultiWeightFieldsAndSubCode(List<String> multiWeightFields, String foodCode, String subCode){
        StringBuffer sqlSegment = new StringBuffer();
        for(int i = 0; i < multiWeightFields.size(); i++){
            sqlSegment.append(multiWeightFields.get(i)).append(" = 1 and ");
        }
        List<FoodWeight> result;
        String sqlWithFoodCodeAndSubCode = "SELECT * from food_weight_tbl where protein_weight = 1 and " +
                sqlSegment + " food_Code = " + foodCode + " and sub_code = " + subCode + " limit 3";
        result = jdbcTemplate.query(sqlWithFoodCodeAndSubCode, rowMapper);
        if(result == null || result.size() == 0){
            String sqlWithFoodCode = "SELECT * from food_weight_tbl where protein_weight = 1 and " +
                    sqlSegment + " food_Code = " + foodCode + " limit 3";
            result = jdbcTemplate.query(sqlWithFoodCode, rowMapper);
        }
        return result;
    }
}
