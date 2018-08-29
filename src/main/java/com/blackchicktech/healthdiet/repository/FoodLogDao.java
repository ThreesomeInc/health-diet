package com.blackchicktech.healthdiet.repository;

import com.blackchicktech.healthdiet.domain.FoodLogRequest;
import com.blackchicktech.healthdiet.entity.FoodLog;
import com.blackchicktech.healthdiet.entity.FoodLogDetail;
import com.blackchicktech.healthdiet.util.FoodLogUtil;
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
        List<FoodLog> foodLogList = jdbcTemplate.query("SELECT * FROM food_log_tbl WHERE open_id =? AND " +
                        "log_date BETWEEN DATE_ADD(?,interval -day(?)+1 DAY) AND last_day(?)",
                (resultSet, i) -> new FoodLog(
                        resultSet.getString("open_id"),
                        resultSet.getDate("log_date"),
                        resultSet.getBoolean("is_logged"),
                        resultSet.getDouble("totalEnergy"),
                        resultSet.getDouble("totalProtein"),
                        resultSet.getDouble("peRatio"),
                        resultSet.getDouble("fat"),
                        resultSet.getDouble("feRatio"),
                        resultSet.getDouble("cho"),
                        resultSet.getDouble("ceRatio"),
                        resultSet.getDouble("na"),
                        resultSet.getDouble("k"),
                        resultSet.getDouble("p"),
                        resultSet.getDouble("ca")
                ), openId, date, date, date);
        logger.info("Finished query food log total {} records", foodLogList.size());
        return foodLogList;
    }

    public List<FoodLog> getFoodLogByDate(String openId, Date date) {
        logger.info("Query current month food log openId={}, date={}", openId, date);
        List<FoodLog> foodLogList = jdbcTemplate.query("SELECT * FROM food_log_tbl WHERE open_id =? AND " +
                        "log_date =?",
                (resultSet, i) -> new FoodLog(
                        resultSet.getString("open_id"),
                        resultSet.getDate("log_date"),
                        resultSet.getBoolean("is_logged"),
                        resultSet.getDouble("totalEnergy"),
                        resultSet.getDouble("totalProtein"),
                        resultSet.getDouble("peRatio"),
                        resultSet.getDouble("fat"),
                        resultSet.getDouble("feRatio"),
                        resultSet.getDouble("cho"),
                        resultSet.getDouble("ceRatio"),
                        resultSet.getDouble("na"),
                        resultSet.getDouble("k"),
                        resultSet.getDouble("p"),
                        resultSet.getDouble("ca")
                ), openId, date);
        logger.info("Finished query food log total {} records", foodLogList.size());
        return foodLogList;
    }

    public void addFoodLog(String openId, Date date, boolean isFullLog, FoodLog foodLog) {
        logger.info("Going to insert food log for user openId={}", openId);
        try {
            jdbcTemplate.update(
                    "REPLACE INTO food_log_tbl VALUES (?, ?, ?, ?, ? ,?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    openId,
                    date,
                    isFullLog,
                    foodLog.getTotalEnergy(),
                    foodLog.getTotalProtein(),
                    foodLog.getPeRatio(),
                    foodLog.getFat(),
                    foodLog.getFeRatio(),
                    foodLog.getCho(),
                    foodLog.getCeRatio(),
                    foodLog.getNa(),
                    foodLog.getK(),
                    foodLog.getP(),
                    foodLog.getCa());
        } catch (Exception e) {
            logger.warn("Failed to insert food log detail for user openId={}, msg={}", openId,
                    e.getMessage());
        }
    }

    public void addFoodLog(FoodLog foodLog) {
        logger.info("Going to insert food log for user openId={}", foodLog.getOpenId());
        try {
            jdbcTemplate.update(
                    "REPLACE INTO food_log_tbl VALUES (?, ?, ?, ?, ? ,?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    foodLog.getOpenId(),
                    foodLog.getDate(),
                    foodLog.isRecorded(),
                    foodLog.getTotalEnergy(),
                    foodLog.getTotalProtein(),
                    foodLog.getPeRatio(),
                    foodLog.getFat(),
                    foodLog.getFeRatio(),
                    foodLog.getCho(),
                    foodLog.getCeRatio(),
                    foodLog.getNa(),
                    foodLog.getK(),
                    foodLog.getP(),
                    foodLog.getCa());
        } catch (Exception e) {
            logger.warn("Failed to insert food log detail for user openId={}, msg={}", foodLog.getOpenId(),
                    e.getMessage());
        }
    }

    public List<FoodLogDetail> getFoodLogDetailByDate(String openId, Date date) {
        logger.info("Query current date food log detail openId={}, date={}", openId, date);
        List<FoodLogDetail> foodLogDetailList = jdbcTemplate.query("SELECT * FROM food_log_detail_tbl WHERE open_id =? " +
                        "AND log_date =?",
                (resultSet, i) -> new FoodLogDetail(
                        resultSet.getString("open_id"),
                        resultSet.getDate("log_date"),
                        resultSet.getString("mealtime"),
                        resultSet.getString("content")
                ), openId, date);
        logger.info("Finished query food log detail total {} records", foodLogDetailList.size());
        return foodLogDetailList;
    }

    public void addFoodLogDetail(FoodLogRequest request) {
        logger.info("Going to insert food log detail for user openId={}", request.getOpenId());
        try {
            jdbcTemplate.update(
                    "REPLACE INTO food_log_detail_tbl VALUES (?, ?, ? ,?)",
                    request.getOpenId(),
                    request.getDate(),
                    request.getMealTime(),
                    FoodLogUtil.toJsonStr(request.getFoodLogItemList()));
        } catch (Exception e) {
            logger.warn("Failed to insert food log detail for user openId={}, msg={}", request.getOpenId(),
                    e.getMessage());
        }
    }

    public void deleteFoodLogDetail(FoodLogRequest request) {
        logger.info("Going to delete food log detail for user openId={}", request.getOpenId());
        try {
            jdbcTemplate.update(
                    "DELETE FROM food_log_detail_tbl WHERE open_id=? AND log_date=? AND mealtime=?",
                    request.getOpenId(),
                    request.getDate(),
                    request.getMealTime());
        } catch (Exception e) {
            logger.warn("Failed to delete food log detail for user openId={}, msg={}", request.getOpenId(),
                    e.getMessage());
        }
    }

    public void deleteFoodLog(String openId, Date date) {
        logger.info("Going to delete food log for user openId={}", openId);
        try {
            jdbcTemplate.update(
                    "DELETE FROM food_log_tbl WHERE open_id=? AND log_date=?",
                    openId,
                    date);
        } catch (Exception e) {
            logger.warn("Failed to delete food log for user openId={}, msg={}", openId,
                    e.getMessage());
        }
    }

    public List<FoodLog> getLatestThreeDayFoodLog(String openId){
        logger.info("Going to get latest 3 three days food log for user openId = {}", openId);
        List<FoodLog> foodLogList = jdbcTemplate.query("SELECT * FROM food_log_tbl WHERE open_id =? and is_logged = 1 order by log_date desc limit 3",
                (resultSet, i) -> new FoodLog(
                        resultSet.getString("open_id"),
                        resultSet.getDate("log_date"),
                        resultSet.getBoolean("is_logged"),
                        resultSet.getDouble("totalEnergy"),
                        resultSet.getDouble("totalProtein"),
                        resultSet.getDouble("peRatio"),
                        resultSet.getDouble("fat"),
                        resultSet.getDouble("feRatio"),
                        resultSet.getDouble("cho"),
                        resultSet.getDouble("ceRatio"),
                        resultSet.getDouble("na"),
                        resultSet.getDouble("k"),
                        resultSet.getDouble("p"),
                        resultSet.getDouble("ca")
                ), openId);
        logger.info("Finished query food log total {} records", foodLogList.size());
        return foodLogList;
    }
}
