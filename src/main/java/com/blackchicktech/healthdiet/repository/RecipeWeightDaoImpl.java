package com.blackchicktech.healthdiet.repository;

import com.blackchicktech.healthdiet.entity.RecipeWeight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Eric Cen on 2018/8/23.
 */
@Repository
public class RecipeWeightDaoImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeWeightDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper rowMapper = new BeanPropertyRowMapper(RecipeWeight.class);

    public RecipeWeight getRecipeWeightByRecipeId(String recipeId){
        List<RecipeWeight> recipeWeightList = jdbcTemplate.query("SELECT * FROM RECIPE_WEIGHT_TBL WHERE recipe_id = ?",rowMapper, recipeId);
        return recipeWeightList.stream().findFirst().orElse(null);
    }

    public List<RecipeWeight> getRecipeWeightByMultiWeightFieldsAndMaterial(List<String> multiWeightFields, String material){
        LOGGER.debug("Getting RecipeWeight by multi weight fields:" + multiWeightFields);
        StringBuffer sqlSegment = new StringBuffer();
        for(int i = 0; i < multiWeightFields.size(); i++){
            sqlSegment.append(multiWeightFields.get(i)).append(" < 3 and ");
        }
        List<RecipeWeight> result;
        String sqlWithMaterial = "SELECT * from recipe_weight_tbl where protein_weight < 3 and " +
                sqlSegment + " material likes %" + material + "% order by rand() limit 3";
        LOGGER.debug("SQLWithFoodCodeAndSubCode: " + sqlWithMaterial);
        result = jdbcTemplate.query(sqlWithMaterial, rowMapper);
        return result;
    }

    public List<RecipeWeight> getRecipeWeightByProteinWeightAndMaterial(int proteinWeight, String material){
        List<RecipeWeight> result;
        result = jdbcTemplate.query("SELECT * from recipe_weight_tbl where " +
                        " protein_weight < ? and material likes %?% order by rand() limit 3" ,
                rowMapper, proteinWeight, material);

        return result;
    }
}
