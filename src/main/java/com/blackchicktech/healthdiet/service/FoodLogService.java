package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.domain.*;
import com.blackchicktech.healthdiet.entity.FoodLog;
import com.blackchicktech.healthdiet.entity.FoodLogDetail;
import com.blackchicktech.healthdiet.entity.FoodTbl;
import com.blackchicktech.healthdiet.repository.FoodDaoImpl;
import com.blackchicktech.healthdiet.repository.FoodLogDao;
import com.blackchicktech.healthdiet.util.FoodLogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodLogService {

    private static final Logger logger = LoggerFactory.getLogger(FoodLogService.class);

    @Autowired
    private FoodLogDao foodLogDao;

    @Autowired
    private FoodDaoImpl foodDao;

    public List<MonthFoodLog> getCurrentMonthFoodLog(String openId, Date currentDate) {
        return foodLogDao.getCurrentMonthFoodLog(openId, currentDate).stream()
                .map(MonthFoodLog::new).collect(Collectors.toList());
    }

    public AccumulativeEnergy updateFoodLog(FoodLogRequest request) {
        if (request.getFoodLogItemList() != null && !request.getFoodLogItemList().isEmpty()) {
            foodLogDao.addFoodLogDetail(request);
        } else {
            foodLogDao.deleteFoodLogDetail(request);
        }

        //获取全天记录的食物计算能量
        List<FoodLogDetail> todayDetail = foodLogDao.getFoodLogDetailByDate(request.getOpenId(), request.getDate());
        if (todayDetail.isEmpty()) {
            //全天没有食物，删除当日食物记录
            foodLogDao.deleteFoodLog(request.getOpenId(), request.getDate());
            return AccumulativeEnergy.emptyEnergy();
        }

        AccumulativeEnergy accumulativeEnergy = new AccumulativeEnergy();
        int totalMealtime = 0; //计算是否累计三餐
        for (FoodLogDetail foodLogDetail : todayDetail) {
            totalMealtime += calTotalMeal(foodLogDetail.getMealTime());

            //计算每一个食材的能量
            for (FoodLogItem foodLogItem : FoodLogUtil.readFromJson(foodLogDetail.getContent())) {
                calEnergy(accumulativeEnergy, foodLogItem);
                accumulativeEnergy.setEmpty(false);
            }
        }

        if (!accumulativeEnergy.isEmpty()) {
            //持久化每日能量
            FoodLog foodLog = new FoodLog(request.getOpenId(), request.getDate(),
                    totalMealtime >= 3,
                    accumulativeEnergy);
            foodLogDao.addFoodLog(foodLog);
        } else {
            foodLogDao.deleteFoodLog(request.getOpenId(), request.getDate());
        }
        return accumulativeEnergy;
    }

    //只有早午晚计入三餐
    private int calTotalMeal(String mealtime) {
        switch (mealtime) {
            case "早餐":
            case "午餐":
            case "晚餐":
                return 1;
            default:
                return 0;
        }
    }

    private void calEnergy(AccumulativeEnergy accumulativeEnergy, FoodLogItem foodLogItem) {
        FoodTbl foodTbl = foodDao.getFoodById(foodLogItem.getFoodId());
        double calPercent = getCalPercent(foodLogItem.getChannel(), foodTbl);
        double totalEnergy = accumulativeEnergy.getTotalEnergy() + foodTbl.getEnergy() * calPercent;
        double totalProtein = accumulativeEnergy.getTotalEnergy() + foodTbl.getProtein() * calPercent;
        double peRatio = calPeRatio(totalEnergy, totalProtein);

        double totalFat = accumulativeEnergy.getFat() + readDouble(foodTbl.getFat(), 0) * calPercent;
        double feRatio = calFaRatio(totalEnergy, totalFat);

        double totalCho = accumulativeEnergy.getCho() + readDouble(foodTbl.getCho(), 0) * calPercent;
        double ceRatio = calChoRatio(totalEnergy, totalCho);

        double na = readDouble(foodTbl.getNa(), 0) * calPercent;
        double k = readDouble(foodTbl.getK(), 0) * calPercent;
        double p = readDouble(foodTbl.getP(), 0) * calPercent;
        double ca = readDouble(foodTbl.getCa(), 0) * calPercent;
        //应该将计算重构到最后?

        accumulativeEnergy.setTotalEnergy(totalEnergy);
        accumulativeEnergy.setTotalProtein(totalProtein);
        accumulativeEnergy.setPeRatio(peRatio);
        accumulativeEnergy.setFat(totalFat);
        accumulativeEnergy.setFeRatio(feRatio);
        accumulativeEnergy.setCho(totalCho);
        accumulativeEnergy.setCeRatio(ceRatio);
        accumulativeEnergy.setNa(na);
        accumulativeEnergy.setK(k);
        accumulativeEnergy.setP(p);
        accumulativeEnergy.setCa(ca);
    }

    //通过超市或者市场按照食补计算百分比
    private double getCalPercent(String channel, FoodTbl foodTbl) {
        if ("超市".equals(channel)) {
            return 1;
        } else if ("市场".equals(channel)) {
            return foodTbl.getEdible() / 100;
        }

        logger.warn("Unknown channel ={} cal as 100 percent", channel);
        return 1;
    }

    //蛋白质供能比
    private double calPeRatio(double totalEnergy, double totalProtein) {
        return totalProtein * 4 / totalEnergy;
    }

    //脂肪供能比
    private double calFaRatio(double totalEnergy, double totalFat) {
        return totalFat * 9 / totalEnergy;
    }

    //碳水化物供能bi
    private double calChoRatio(double totalEnergy, double totalCho) {
        return totalCho * 4 / totalEnergy;
    }

    private double readDouble(String dStr, double defaultValue) {
        try {
            return Double.valueOf(dStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public ThreeDayFoodLogAnalysis deduceThreeDayFoodLogAnalysis(String openId){
        List<FoodLog> threeDayFoodLog = foodLogDao.getLatestThreeDayFoodLog(openId);
        if(threeDayFoodLog.size() < 3){
            logger.info("Food log is less than 3 days, fail to analytic.");
            return new ThreeDayFoodLogAnalysis("膳食记录少于3天，无法分析。");
        }
        return new ThreeDayFoodLogAnalysis();
    }

}
