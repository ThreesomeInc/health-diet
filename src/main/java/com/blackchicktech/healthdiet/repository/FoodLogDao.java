package com.blackchicktech.healthdiet.repository;

import com.blackchicktech.healthdiet.entity.FoodLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class FoodLogDao {

    private static final Logger logger = LoggerFactory.getLogger(FoodLogDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<FoodLog> getCurrentMonthFoodLog(String openId, Date date) {
        logger.info("Query current month food log openId={}, date={}", openId, date);
        List<FoodLog> foodLogList = jdbcTemplate.query("SELECT * FROM food_log_tbl WHERE openId =? AND " +
                        "log_date BETWEEN DATE_ADD(?,interval -day(?)+1 day) and last_day(?)",
                (resultSet, i) -> new FoodLog(
                      resultSet.getString("open_id"),
                      resultSet.getDate("log_date"),
                      resultSet.getBoolean("is_logged")
                ), openId, date, date, date);
        logger.info("Finished query food log total {} records", foodLogList.size());
        return foodLogList;
    }
}
