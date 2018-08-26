package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.domain.*;
import com.blackchicktech.healthdiet.entity.FoodLog;
import com.blackchicktech.healthdiet.entity.FoodLogDetail;
import com.blackchicktech.healthdiet.entity.FoodTbl;
import com.blackchicktech.healthdiet.repository.FoodDaoImpl;
import com.blackchicktech.healthdiet.repository.FoodLogDao;
import com.blackchicktech.healthdiet.util.FoodLogUtil;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FoodLogService {

    private static final Logger logger = LoggerFactory.getLogger(FoodLogService.class);

    private static final long ONE_DAY_MILI_SECONDS = 1000*3600*24;

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
            return new ThreeDayFoodLogAnalysis("膳食记录少于3天，无法准确分析。");
        }
        ThreeDayFoodLogAnalysis analysis = new ThreeDayFoodLogAnalysis();
        boolean isStandardLogType = isStandardLogType(threeDayFoodLog);
        if(isStandardLogType){
            analysis.setLogTypeInfo("您使用的是标准三日膳食记录。");
            analysis.setElementEvgs(deduceElementEvgs(threeDayFoodLog));
        } else {
            analysis.setLogTypeInfo("您使用的是折衷三日膳食记录，建议您使用标准三日膳食记录，推荐结果会更准确.标准三日膳食记录法: 连续三日，包括两个工作日，一个休息日，计算三日膳食营养物质平均值。");
            analysis.setElementEvgs(deduceElementEvgs(threeDayFoodLog));
        }
        return analysis;
    }

    public Map<String, String> deduceElementEvgs(List<FoodLog> foodLogList){
        logger.info("Deducing elements average.");
        double ca = 0.0d;
        double cho = 0.0d;
        double fat = 0.0d;
        double k = 0.0d;
        double p = 0.0d;
        double na = 0.0d;
        double protein = 0.0;

        for(FoodLog foodLog : foodLogList){
            ca += foodLog.getCa();
            cho += foodLog.getCho();
            fat += foodLog.getFat();
            k += foodLog.getK();
            p += foodLog.getP();
            na += foodLog.getNa();
            protein += foodLog.getTotalProtein();
        }

        Map<String, String> elementAvgs = ImmutableMap.<String, String>builder()
                                            .put("钙", ca/3 + "毫克")
                                            .put("碳水化合物", cho/3 + "克")
                                            .put("脂肪", fat/3 + "克")
                                            .put("钾", k/3 + "毫克")
                                            .put("磷", p/3 + "毫克")
                                            .put("钠", na/3 + "毫克")
                                            .put("蛋白质", protein/3 + "克")
                                            .build();
        return elementAvgs;
    }

    public boolean isStandardLogType(List<FoodLog> foodLogList){
        Date dateOne = foodLogList.get(0).getDate();
        Date dateTwo = foodLogList.get(1).getDate();
        Date dateThree = foodLogList.get(2).getDate();
        if((int)((dateOne.getTime() - dateTwo.getTime()) / ONE_DAY_MILI_SECONDS) == 1 &&
                (int)((dateTwo.getTime() - dateThree.getTime()) / ONE_DAY_MILI_SECONDS) == 1){
            return true;
        } else {
            return false;
        }

    }

}
