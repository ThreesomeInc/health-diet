package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.domain.*;
import org.springframework.stereotype.Service;

/**
 * Created by Eric Cen on 2018/8/3.
 */
@Service
public class ReportService {



    public ReportResponse report(ReportRequest reportRequest){
        ReportResponse response = new ReportResponse();
        response.setBodyType(calWeightIndex(reportRequest));
        response.setStandardWeight(calStandardWeight(reportRequest));
        response.setCalorie(calCalorie(reportRequest));
        response.setProtein(calProtein(reportRequest));
        response.setHealthEstimation("Normal");
        response.setAdvice("No Advice");
        SuggestNutrition sn1 = new SuggestNutrition("energy", "热量", "1575", "kcal");
        SuggestNutrition sn2 = new SuggestNutrition("protein", "蛋白质", "45", "g");
        response.addSuggestNutrition(sn1);
        response.addSuggestNutrition(sn2);

        Nutrition n1 = new Nutrition("milk", "牛奶", "220", "ml");
        Nutrition n2 = new Nutrition("fruit", "水果", "176", "g");
        Nutrition n3 = new Nutrition("egg", "鸡蛋", "1", "ge");
        response.addNutrition(n1);
        response.addNutrition(n2);
        response.addNutrition(n3);
        return response;
    }

    private String calWeightIndex(ReportRequest reportRequest){
       float bmi = calBmi(reportRequest);
        if(bmi < 18.5){
            return BodyType.MALNOURISHED.info();
        } else if(bmi >= 18.5 && bmi < 23.9){
            return BodyType.NORMAL.info();
        } else if(bmi >= 24.0 && bmi < 27.9){
            return BodyType.OVERWEIGHT.info();
        } else {
            return BodyType.FATTY.info();
        }
    }

    private float calBmi(ReportRequest reportRequest){
        String height = reportRequest.getUserDataInfo().getHeight();
        String weight = reportRequest.getUserDataInfo().getWeight();
        return Float.parseFloat(weight)/(Integer.parseInt(height)/100)/(Integer.parseInt(height)/100);
    }

    private float calStandardWeight(ReportRequest reportRequest){
        String gender = reportRequest.getUserDataInfo().getGender();
        String height = reportRequest.getUserDataInfo().getHeight();
        if("male".equals(gender)){
            return (float)((Integer.parseInt(height) - 100) * 0.9);
        } else if("female".equals(gender)){
            return (float)((Integer.parseInt(height) - 100) * 0.9 - 2.5);
        } else {
            throw new IllegalArgumentException("Gender can only be male or female");
        }
    }

    private float calCalorie(ReportRequest reportRequest){
        String sportRate = reportRequest.getUserDataInfo().getSportRate();
        float standardWeight = calStandardWeight(reportRequest);
        float bmi = calBmi(reportRequest);
        if("light".equals(sportRate)){
          if(bmi < 18.5){
              return standardWeight*35;
          } else if(bmi >= 18.5 && bmi < 23.9){
              return (float)(standardWeight*32.5);
          } else {
              return  standardWeight*30;
          }
        }
        return 0;

    }

    private float calProtein(ReportRequest reportRequest){
        return 0;
    }
}
