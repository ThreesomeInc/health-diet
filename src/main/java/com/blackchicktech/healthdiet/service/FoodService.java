package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.domain.FoodDetailResponse;
import com.blackchicktech.healthdiet.domain.FoodListItem;
import com.blackchicktech.healthdiet.domain.FoodType;
import com.blackchicktech.healthdiet.entity.FoodTbl;
import com.blackchicktech.healthdiet.entity.FoodWeight;
import com.blackchicktech.healthdiet.entity.User;
import com.blackchicktech.healthdiet.repository.FoodDaoImpl;
import com.blackchicktech.healthdiet.repository.FoodWeightDaoImpl;
import com.blackchicktech.healthdiet.util.Constants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

//食材相关
@Service
public class FoodService {

    @Autowired
    private FoodDaoImpl foodDao;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodWeightDaoImpl foodWeightDao;

    private static final Logger logger = LoggerFactory.getLogger(FoodService.class);

    private List<FoodType> typeCache = new LinkedList<>();


    public List<FoodListItem> listFood(String foodTypeCode) {  //分页
        return foodDao.getFoodByTypeId(foodTypeCode);
    }

    public List<FoodListItem> listFoodByName(String foodName) {
        return foodDao.getFoodByName(foodName);
    }

    public List<FoodType> listFoodType() {
        return new ArrayList<>(typeCache);
    }

    public FoodType getFoodType(String typeCode, String subTypeCode) {
        return null;
    }

    private void reloadFoodRanking() {
        Reader in = null;
        try {
            in = new InputStreamReader(FoodService.class.getResourceAsStream("/food-type-rank.csv"));
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader("rank","alias","typeId")
                    .withSkipHeaderRecord()
                    .parse(in);
            for (CSVRecord record : records) {
                String rank = record.get("rank"); //貌似没用
                String name = record.get("alias");
                String typeId = record.get("typeId");
                typeCache.add(new FoodType(typeId, name, typeId+".pic"));
            }
            logger.info("Finished to load food rank");
        } catch (Exception e) {
            logger.warn("Fail to read food rank " + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public FoodDetailResponse getFoodById(String foodId, String openId){
        User user = userService.getUserByOpenId(openId);
        FoodTbl food = foodDao.getFoodById(foodId);
        FoodWeight foodWeight = foodWeightDao.getFoodWeightByFoodId(foodId);
        FoodDetailResponse foodDetailResponse = new FoodDetailResponse();
        foodDetailResponse.setName(food.getFoodName());
        foodDetailResponse.setDieticianAdvice(deduceDieticianAdvice(food, foodWeight,user));
        foodDetailResponse.setComposition(deduceCompostions(food));
        foodDetailResponse.setLabel(deduceLabel(foodWeight));
        return foodDetailResponse;

    }

    private String deduceDieticianAdvice(FoodTbl food, FoodWeight foodWeight, User user){
        //TODO: user null validation ? return temporarily for front-end testing
        if (user == null) {
            return "";
        }
        int nephroticPeriod = Integer.valueOf(user.getNephroticPeriod());
        String otherDiseases = user.getOtherDiseases();
        String foodId = food.getFoodId();
        String subCode = food.getSubCode();
        int proteinWeight = Integer.parseInt(foodWeight.getProteinWeight());
        int purineWeight = Integer.valueOf(foodWeight.getPurineWeight());
        int cholesterolWeight = Integer.valueOf(foodWeight.getCholesterolWeight());
        int naWeight = Integer.valueOf(foodWeight.getNaWeight());
        int fatWeight = Integer.valueOf(foodWeight.getFatWeight());
        StringBuilder dieticianAdvice = new StringBuilder();
        StringBuilder otherDiseasesConbinations = new StringBuilder();
        if(otherDiseases != null && !StringUtils.isEmpty(otherDiseases)){
            String[] otherDiseasesArray = otherDiseases.split(",");
            for(int i = 0; i < otherDiseasesArray.length; i++){
                otherDiseasesConbinations.append(Constants.OTHER_DISEASE_ELEMENTS.get(otherDiseasesArray[i]));
            }
            dieticianAdvice.append(String.format(Constants.DIETICIAN_ADVICE_TEMPLATE,
                    nephroticPeriod, otherDiseasesConbinations));
            if(proteinWeight == 1){
                dieticianAdvice.append("该食物蛋白含量低，建议经常食用");
            } else if(proteinWeight == 2){
                dieticianAdvice.append("该食物蛋白含量适中，可适量食用");
            } else {
                dieticianAdvice.append("该食物蛋白含量偏高，不适宜您食用。推荐低蛋白食物有:");
                List<FoodWeight> foodWeights = foodWeightDao.getFoodWeightByProteinWeightAndSubCode("1", subCode);
                List<FoodTbl> foodList = deduceRecommendFood(foodWeights);
                for(int i = 0; i < foodList.size(); i++){
                    dieticianAdvice.append(foodList.get(i));
                    if(i != foodList.size()){
                        dieticianAdvice.append(",");
                    }
                }
            }
            
        } else {
            dieticianAdvice.append(String.format(Constants.DIETICIAN_ADVICE_WITHOUT_NEOPATHY_TEMPLATE, nephroticPeriod));
            if(proteinWeight == 1){
                dieticianAdvice.append("该食物蛋白含量低，建议经常食用");
            } else if(proteinWeight == 2){
                dieticianAdvice.append("该食物蛋白含量适中，可适量食用");
            } else {
                dieticianAdvice.append("该食物蛋白含量偏高，不适宜您食用。推荐低蛋白食物有:");
                List<FoodWeight> foodWeights = foodWeightDao.getFoodWeightByProteinWeightAndSubCode("1", subCode);
                List<FoodTbl> foodList = deduceRecommendFood(foodWeights);
                for(int i = 0; i < foodList.size(); i++){
                    dieticianAdvice.append(foodList.get(i));
                    if(i != foodList.size()){
                        dieticianAdvice.append(",");
                    }
                }
            }
        }
        return dieticianAdvice.toString();
    }

    private List<String> deduceLabel(FoodWeight foodWeight){
        List<String> label = new ArrayList<>();

        int proteinWeight = Integer.valueOf(foodWeight.getProteinWeight());
        int choWeight = Integer.valueOf(foodWeight.getChoWeight());
        int fatWeight = Integer.valueOf(foodWeight.getFatWeight());
        int kWeight = Integer.valueOf(foodWeight.getkWeight());
        int naWeight = Integer.valueOf(foodWeight.getNaWeight());
        int pWeight = Integer.valueOf(foodWeight.getpWeight().equals("null") ? "1":foodWeight.getpWeight());
        int purineWeight = Integer.valueOf(foodWeight.getPurineWeight());
        int cholesterolWeight = Integer.valueOf(foodWeight.getCholesterolWeight());
        if(proteinWeight == 1){
            label.add("低蛋白");
        } else if (proteinWeight == 3){
            label.add("高蛋白");
        }
        if(choWeight == 1){
            label.add("低碳水化合物");
        } else if(choWeight == 3){
            label.add("高碳水化合物");
        }
        if(fatWeight == 1){
            label.add("低脂肪");
        } else if(fatWeight == 3){
            label.add("高脂肪");
        }
        if(kWeight == 1){
            label.add("低钾");
        } else if(kWeight == 3){
            label.add("高钾");
        }
        if(naWeight == 1){
            label.add("低钠");
        } else if(naWeight == 3){
            label.add("高钠");
        }
        if(purineWeight == 1){
            label.add("低嘌呤");
        } else if(purineWeight == 3){
            label.add("高嘌呤");
        }
        if(cholesterolWeight == 1){
            label.add("低胆固醇");
        }else if(cholesterolWeight == 3){
            label.add("高胆固醇");
        }
        if(pWeight == 1){
            label.add("低磷");
        } else if(pWeight == 3){
            label.add("高磷");
        }
        return label;
    }

    private List<FoodTbl> deduceRecommendFood(List<FoodWeight> foodWeights){
        List<FoodTbl> foodList = new ArrayList<>();
        for(FoodWeight foodWeight : foodWeights){
            FoodTbl food = foodDao.getFoodById(foodWeight.getFoodId());
            foodList.add(food);
        }
        return foodList;
    }

    private Map<String, String> deduceCompostions(FoodTbl food){
        Map<String, String> compositions = new HashMap<>();
        String waterQuantity = food.getWater();
        compositions.put("水", waterQuantity+"克");
        String energyQuantity = food.getEnergy();
        compositions.put("热量", energyQuantity+"千卡");
        String proteinQuantity = food.getProtein();
        compositions.put("蛋白质", proteinQuantity+"克");
        String fatQuantity = food.getFat();
        compositions.put("脂肪", fatQuantity+"克");
        String choQuantity = food.getCho();
        compositions.put("碳水化合物", choQuantity + "克");
        String pQuantity = food.getP();
        compositions.put("磷", pQuantity+"克");
        String kQuantity = food.getK();
        compositions.put("钾", kQuantity+"克");
        String naQuantity = food.getNa();
        compositions.put("钠", naQuantity+"克");
        return compositions;
    }


}
