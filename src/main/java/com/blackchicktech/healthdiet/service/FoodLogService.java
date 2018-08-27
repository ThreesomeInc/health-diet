package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.domain.*;
import com.blackchicktech.healthdiet.entity.FoodLog;
import com.blackchicktech.healthdiet.entity.FoodLogDetail;
import com.blackchicktech.healthdiet.entity.FoodTbl;
import com.blackchicktech.healthdiet.entity.User;
import com.blackchicktech.healthdiet.repository.FoodDaoImpl;
import com.blackchicktech.healthdiet.repository.FoodLogDao;
import com.blackchicktech.healthdiet.repository.UserDaoImpl;
import com.blackchicktech.healthdiet.util.Constants;
import com.blackchicktech.healthdiet.util.FoodLogUtil;
import com.google.common.collect.ImmutableMap;
import org.jooq.lambda.Seq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FoodLogService {

    private static final Logger logger = LoggerFactory.getLogger(FoodLogService.class);

    private static final long ONE_DAY_MILI_SECONDS = 1000*3600*24;

    @Autowired
    private FoodLogDao foodLogDao;

    @Autowired
    private FoodDaoImpl foodDao;

    @Autowired
    private UserDaoImpl userDao;

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

        } else {
            analysis.setLogTypeInfo("您使用的是折衷三日膳食记录，建议您使用标准三日膳食记录，推荐结果会更准确.标准三日膳食记录法: 连续三日，包括两个工作日，一个休息日，计算三日膳食营养物质平均值。");

        }
        Map<String, Double> elementEvgs = deduceElementEvgs(threeDayFoodLog);
        analysis.setElementEvgs(elementEvgs);
        analysis.setDieticianAdvice(deduceDieticianAdvice(threeDayFoodLog,elementEvgs,openId));
        return analysis;
    }

    public Map<String, Double> deduceElementEvgs(List<FoodLog> foodLogList){
        logger.info("Deducing elements average.");
        double ca = 0.0d;
        double cho = 0.0d;
        double fat = 0.0d;
        double k = 0.0d;
        double p = 0.0d;
        double na = 0.0d;
        double protein = 0.0;
        double energy = 0.0;
        double ceRatio = 0.0;
        double feRatio = 0.0;
        double peRatio = 0.0;
        for(FoodLog foodLog : foodLogList){
            ca += foodLog.getCa();
            cho += foodLog.getCho();
            fat += foodLog.getFat();
            k += foodLog.getK();
            p += foodLog.getP();
            na += foodLog.getNa();
            protein += foodLog.getTotalProtein();
            energy += foodLog.getTotalEnergy();
            ceRatio += foodLog.getCeRatio();
            feRatio += foodLog.getFeRatio();
            peRatio += foodLog.getPeRatio();
        }

        return ImmutableMap.<String, Double>builder()
                .put("钙", BigDecimal.valueOf(ca / 3).setScale(1, BigDecimal.ROUND_FLOOR).doubleValue())
                .put("碳水化合物", BigDecimal.valueOf(cho / 3).setScale(1, BigDecimal.ROUND_FLOOR).doubleValue())
                .put("脂肪", BigDecimal.valueOf(fat / 3).setScale(1, BigDecimal.ROUND_FLOOR).doubleValue())
                .put("钾", BigDecimal.valueOf(k / 3).setScale(1, BigDecimal.ROUND_FLOOR).doubleValue())
                .put("磷", BigDecimal.valueOf(p / 3).setScale(1, BigDecimal.ROUND_FLOOR).doubleValue())
                .put("钠", BigDecimal.valueOf(na / 3).setScale(1, BigDecimal.ROUND_FLOOR).doubleValue())
                .put("蛋白质", BigDecimal.valueOf(protein / 3).setScale(1, BigDecimal.ROUND_FLOOR).doubleValue())
                .put("能量", BigDecimal.valueOf(energy / 3).setScale(1, BigDecimal.ROUND_FLOOR).doubleValue())
                .put("碳水化合物供能比", BigDecimal.valueOf(ceRatio / 3).setScale(1, BigDecimal.ROUND_FLOOR).doubleValue())
                .put("脂肪供能比", BigDecimal.valueOf(feRatio / 3).setScale(1, BigDecimal.ROUND_FLOOR).doubleValue())
                .put("蛋白质供能比", BigDecimal.valueOf(peRatio / 3).setScale(1, BigDecimal.ROUND_FLOOR).doubleValue())
                .build();
    }

    public boolean isStandardLogType(List<FoodLog> foodLogList){
        int size = 2;
        return Seq.seq(foodLogList).map(FoodLog::getDate).map(Date::getTime)// i.e. [1,2,3,5]
                .window(0, size - 1).filter(w -> w.count() == size)// [[1,2],[2,3],[3,4]]
                .map(w -> w.window().reduce((left, right) -> Math.abs(right - left)).orElse(0L))// [1,1,2]
                .allMatch(item -> (item / ONE_DAY_MILI_SECONDS) == 1L);
    }

    public String deduceDieticianAdvice(List<FoodLog> foodLogList, Map<String, Double> elementAvgs, String openId){
        User user = userDao.getUserByOpenId(openId);
        double energy = elementAvgs.get("能量");
        double protein = elementAvgs.get("蛋白质");
        double na = elementAvgs.get("钠");
        double ca = elementAvgs.get("钙");
        double p = elementAvgs.get("磷");
        double ceRatio = elementAvgs.get("碳水化合物供能比");
        double feRatio = elementAvgs.get("脂肪供能比");

        float standardCalorie = calCalorie(user);
        String[] standardProtein = calProtein(user).split("~");

        StringBuilder diticianAdvice = new StringBuilder();
        if(energy - standardCalorie >= 100 || standardCalorie - energy <= 100 ){
            diticianAdvice.append("能量摄入基本接近推荐值,");
        } else if (energy - standardCalorie > 100 && energy - standardCalorie <= 200){
            diticianAdvice.append("能量摄入略高于推荐值,");
        } else if(standardCalorie - energy > 100 && standardCalorie - energy <= 200){
            diticianAdvice.append("能量摄入略低于推荐值,");
        } else if (energy - standardCalorie > 200 ){
            diticianAdvice.append("能量摄入高于推荐值,");
        } else if(standardCalorie - energy > 200){
            diticianAdvice.append("能量摄入低于推荐值,");
        }

        if(protein - Double.valueOf(standardProtein[1]) <= 2 && Double.valueOf(standardProtein[0]) - protein <= 2 ){
            diticianAdvice.append("蛋白质摄入量基本接近推荐值，");
        } else if(protein - Double.valueOf(standardProtein[1]) > 2){
            diticianAdvice.append("蛋白质摄入量高于推荐值，");
        } else if(Double.valueOf(standardProtein[0]) - protein > 2){
            diticianAdvice.append("蛋白质摄入量低于推荐值，");
        }

        if(na > 2000){
            diticianAdvice.append("钠摄入量高于推荐值,");
        }
        if(ca > 2000 ){
            diticianAdvice.append("钙摄入量高于推荐值,");
        }
        if(p > 800){
           diticianAdvice.append("磷摄入量高于推荐值，");
        }
        return diticianAdvice.toString();


    }

    private float calCalorie(User user) {
        String sportRate = user.getSportRate();
        float standardWeight = calStandardWeight(user);
        float bmi = calBmiIndex(user);
        if ("light".equals(sportRate)
                || Constants.SPORT_RATE.getOrDefault("light", "").equals(sportRate)) {
            if (bmi < 18.5) {
                return standardWeight * 35;
            } else if (bmi >= 18.5 && bmi < 23.9) {
                return (float) (standardWeight * 32.5);
            } else {
                return standardWeight * 30;
            }
        } else {
            if (bmi < 18.5) {
                return standardWeight * 40;
            } else if (bmi >= 18.5 && bmi < 23.9) {
                return (standardWeight * 37);
            } else {
                return standardWeight * 35;
            }
        }


    }

    public float calStandardWeight(User user) {
        String gender = user.getGender();
        String height = getHeightOrWeight(user.getHeight());
        if ("male".equals(gender) || Constants.GENDER.getOrDefault("male", "").equals(gender)) {
            return (float) ((Integer.parseInt(height) - 100) * 0.9);
        } else if ("female".equals(gender) || Constants.GENDER.getOrDefault("female", "").equals(gender)) {
            return (float) ((Integer.parseInt(height) - 100) * 0.9 - 2.5);
        } else {
            throw new IllegalArgumentException("Gender can only be male or female");
        }
    }

    private String getHeightOrWeight(String value) {
        Matcher result = Pattern.compile("(\\d+(.\\d))?").matcher(value);
        return result.find() ? result.group() : "0";
    }

    private float calBmiIndex(User user) {
        String height = user.getHeight();
        String weight = user.getWeight();
        return (Float.parseFloat(getHeightOrWeight(weight)) / (Float.parseFloat(getHeightOrWeight(height)) / 100))
                / (Float.parseFloat(getHeightOrWeight(height)) / 100);
    }

    private String calProtein(User user) {
        int nephroticPeriod = Integer.parseInt(user.getNephroticPeriod());
        List<String> treatmentMethod = Arrays.asList(user.getTreatmentMethod().split(","));
        BigDecimal standardWeight = BigDecimal.valueOf(calStandardWeight(user));
        StringBuilder protein = new StringBuilder();
        if (nephroticPeriod >= 1 && nephroticPeriod <= 2) {
            return protein.append(standardWeight.multiply(BigDecimal.valueOf(0.8)).setScale(1, BigDecimal.ROUND_FLOOR)).append("~")
                    .append(standardWeight.multiply(BigDecimal.ONE).setScale(1, BigDecimal.ROUND_FLOOR)).toString();
        } else if (nephroticPeriod >= 3 && nephroticPeriod <= 5) {
            if (treatmentMethod.stream().anyMatch(item -> item.contains("dialysis"))) {
                return protein.append(standardWeight.multiply(BigDecimal.ONE).setScale(1, BigDecimal.ROUND_FLOOR)).append("~")
                        .append(standardWeight.multiply(BigDecimal.valueOf(1.2)).setScale(1, BigDecimal.ROUND_FLOOR)).toString();
            } else {
                return protein.append(standardWeight.multiply(BigDecimal.valueOf(0.6)).setScale(1, BigDecimal.ROUND_FLOOR)).append("~")
                        .append(standardWeight.multiply(BigDecimal.valueOf(10.8)).setScale(1, BigDecimal.ROUND_FLOOR)).toString();
            }
        }
        return "Protein is unclear.";
    }

}
