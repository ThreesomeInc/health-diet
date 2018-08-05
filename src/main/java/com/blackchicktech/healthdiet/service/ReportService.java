package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric Cen on 2018/8/3.
 */
@Service
public class ReportService {



    public ReportResponse report(ReportRequest reportRequest){
        ReportResponse response = new ReportResponse();
        /*response.setBodyType(calWeightIndex(reportRequest));*/
        response.setStandardWeight(calStandardWeight(reportRequest));
        response.setCalorie(calCalorie(reportRequest));
        response.setProtein(calProtein(reportRequest));
        response.setHealthEstimation(calWeightIndex(reportRequest));
        response.setAdvice("No Advice");
        response.setSuggestNutrition(calSuggestNutrition(reportRequest));
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
        } else {
            if(bmi < 18.5){
                return standardWeight*40;
            } else if(bmi >= 18.5 && bmi < 23.9){
                return (standardWeight*37);
            } else {
                return  standardWeight*35;
            }
        }


    }

    private String calProtein(ReportRequest reportRequest){
        int nephroticPeriod = Integer.parseInt(reportRequest.getUserDataInfo().getNephroticPeriod());
        String treatmentMethod = reportRequest.getUserDataInfo().getTreatmentMethod();
        float standardWeight = calStandardWeight(reportRequest);
        StringBuffer protein = new StringBuffer();
        if(nephroticPeriod >= 1 && nephroticPeriod <=2 ){
            return protein.append(standardWeight*0.8).append("~")
                    .append(standardWeight*1).toString();
        } else if (nephroticPeriod >=3 && nephroticPeriod <=5){
            if(treatmentMethod.contains("dialysis")){
                return protein.append(standardWeight*1).append("~")
                        .append(standardWeight*1.2).toString();
            } else{
                return protein.append(standardWeight*0.6).append("~")
                        .append(standardWeight*10.8).toString();
            }
        }
        return "Protein is unclear.";
    }

    private List<SuggestNutrition> calSuggestNutrition(ReportRequest reportRequest){
        float standardWeight = calStandardWeight(reportRequest);
        float calorie = calCalorie(reportRequest);
        String protein = calProtein(reportRequest);
        List<String> irritabilities = reportRequest.getUserDataInfo().getIrritability();
        List<SuggestNutrition> suggestNutritions = new ArrayList<>();
        boolean irritableToMilk = irritabilities.contains("milk");
        if(standardWeight >= 40 && standardWeight < 45){
            if(irritableToMilk){
                SuggestNutrition cereal = new SuggestNutrition("谷薯类", "100g" );
                SuggestNutrition starch = new SuggestNutrition("淀粉", "50g");
                SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250g");
                SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "200g");
                SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "330g~350g");
                SuggestNutrition grease = new SuggestNutrition("油脂类", "35g");
                suggestNutritions.add(cereal);
                suggestNutritions.add(starch);
                suggestNutritions.add(vegetable);
                suggestNutritions.add(fruit);
                suggestNutritions.add(meatEgg);
                suggestNutritions.add(grease);

            } else {
                SuggestNutrition cereal = new SuggestNutrition("谷薯类", "100g" );
                SuggestNutrition starch = new SuggestNutrition("淀粉", "50g");
                SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250g");
                SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "200g");
                SuggestNutrition milk = new SuggestNutrition("奶类", "230g");
                SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "100g~120g");
                SuggestNutrition grease = new SuggestNutrition("油脂类", "35g");
                suggestNutritions.add(cereal);
                suggestNutritions.add(starch);
                suggestNutritions.add(vegetable);
                suggestNutritions.add(fruit);
                suggestNutritions.add(milk);
                suggestNutritions.add(meatEgg);
                suggestNutritions.add(grease);

            }
        } else if(standardWeight >= 45 && standardWeight < 50){
            if(irritableToMilk){
                SuggestNutrition cereal = new SuggestNutrition("谷薯类", "125g" );
                SuggestNutrition starch = new SuggestNutrition("淀粉", "50g");
                SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250g");
                SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "200g");
                SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "330g~350g");
                SuggestNutrition grease = new SuggestNutrition("油脂类", "40g");
                suggestNutritions.add(cereal);
                suggestNutritions.add(starch);
                suggestNutritions.add(vegetable);
                suggestNutritions.add(fruit);
                suggestNutritions.add(meatEgg);
                suggestNutritions.add(grease);

            } else {
                SuggestNutrition cereal = new SuggestNutrition("谷薯类", "100g" );
                SuggestNutrition starch = new SuggestNutrition("淀粉", "50g");
                SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250g");
                SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "200g");
                SuggestNutrition milk = new SuggestNutrition("奶类", "230g");
                SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "100g~120g");
                SuggestNutrition grease = new SuggestNutrition("油脂类", "40g");
                suggestNutritions.add(cereal);
                suggestNutritions.add(starch);
                suggestNutritions.add(vegetable);
                suggestNutritions.add(fruit);
                suggestNutritions.add(milk);
                suggestNutritions.add(meatEgg);
                suggestNutritions.add(grease);
            }
        } else if(standardWeight >= 50 && standardWeight < 55){
            if(irritableToMilk){
                SuggestNutrition cereal = new SuggestNutrition("谷薯类", "150g" );
                SuggestNutrition starch = new SuggestNutrition("淀粉", "50g");
                SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250g");
                SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "200g");
                SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "355g~380g");
                SuggestNutrition grease = new SuggestNutrition("油脂类", "45g");
                suggestNutritions.add(cereal);
                suggestNutritions.add(starch);
                suggestNutritions.add(vegetable);
                suggestNutritions.add(fruit);
                suggestNutritions.add(meatEgg);
                suggestNutritions.add(grease);

            } else {
                SuggestNutrition cereal = new SuggestNutrition("谷薯类", "100g" );
                SuggestNutrition starch = new SuggestNutrition("淀粉", "50g");
                SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250g");
                SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "200g");
                SuggestNutrition milk = new SuggestNutrition("奶类", "230g");
                SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "125g~150g");
                SuggestNutrition grease = new SuggestNutrition("油脂类", "45g");
                suggestNutritions.add(cereal);
                suggestNutritions.add(starch);
                suggestNutritions.add(vegetable);
                suggestNutritions.add(fruit);
                suggestNutritions.add(milk);
                suggestNutritions.add(meatEgg);
                suggestNutritions.add(grease);
            }
        } else if(standardWeight >=55 && standardWeight < 60){
            if(irritableToMilk){
                SuggestNutrition cereal = new SuggestNutrition("谷薯类", "175g" );
                SuggestNutrition starch = new SuggestNutrition("淀粉", "50g");
                SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250g");
                SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "400g");
                SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "355g~380g");
                SuggestNutrition grease = new SuggestNutrition("油脂类", "50g");
                suggestNutritions.add(cereal);
                suggestNutritions.add(starch);
                suggestNutritions.add(vegetable);
                suggestNutritions.add(fruit);
                suggestNutritions.add(meatEgg);
                suggestNutritions.add(grease);

            } else {
                SuggestNutrition cereal = new SuggestNutrition("谷薯类", "100g" );
                SuggestNutrition starch = new SuggestNutrition("淀粉", "50g");
                SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250g");
                SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "200g");
                SuggestNutrition milk = new SuggestNutrition("奶类", "230g");
                SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "125g~150g");
                SuggestNutrition grease = new SuggestNutrition("油脂类", "50g");
                suggestNutritions.add(cereal);
                suggestNutritions.add(starch);
                suggestNutritions.add(vegetable);
                suggestNutritions.add(fruit);
                suggestNutritions.add(milk);
                suggestNutritions.add(meatEgg);
                suggestNutritions.add(grease);
            }
        } else if(standardWeight >= 60 && standardWeight < 65){
            if(irritableToMilk){
                SuggestNutrition cereal = new SuggestNutrition("谷薯类", "200g" );
                SuggestNutrition starch = new SuggestNutrition("淀粉", "50g");
                SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250g");
                SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "400g");
                SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "380g~410g");
                SuggestNutrition grease = new SuggestNutrition("油脂类", "50g");
                suggestNutritions.add(cereal);
                suggestNutritions.add(starch);
                suggestNutritions.add(vegetable);
                suggestNutritions.add(fruit);
                suggestNutritions.add(meatEgg);
                suggestNutritions.add(grease);

            } else {
                SuggestNutrition cereal = new SuggestNutrition("谷薯类", "200g" );
                SuggestNutrition starch = new SuggestNutrition("淀粉", "50g");
                SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250g");
                SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "400g");
                SuggestNutrition milk = new SuggestNutrition("奶类", "230g");
                SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "150g~180g");
                SuggestNutrition grease = new SuggestNutrition("油脂类", "50g");
                suggestNutritions.add(cereal);
                suggestNutritions.add(starch);
                suggestNutritions.add(vegetable);
                suggestNutritions.add(fruit);
                suggestNutritions.add(milk);
                suggestNutritions.add(meatEgg);
                suggestNutritions.add(grease);
            }
        } else if(standardWeight >= 65 && standardWeight < 70){
            if(irritableToMilk){
                SuggestNutrition cereal = new SuggestNutrition("谷薯类", "200g" );
                SuggestNutrition starch = new SuggestNutrition("淀粉", "50g");
                SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250g");
                SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "400g");
                SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "405g~440g");
                SuggestNutrition grease = new SuggestNutrition("油脂类", "55g");
                suggestNutritions.add(cereal);
                suggestNutritions.add(starch);
                suggestNutritions.add(vegetable);
                suggestNutritions.add(fruit);
                suggestNutritions.add(meatEgg);
                suggestNutritions.add(grease);

            } else {
                SuggestNutrition cereal = new SuggestNutrition("谷薯类", "200g" );
                SuggestNutrition starch = new SuggestNutrition("淀粉", "50g");
                SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250g");
                SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "400g");
                SuggestNutrition milk = new SuggestNutrition("奶类", "230g");
                SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "175g~210g");
                SuggestNutrition grease = new SuggestNutrition("油脂类", "55g");
                suggestNutritions.add(cereal);
                suggestNutritions.add(starch);
                suggestNutritions.add(vegetable);
                suggestNutritions.add(fruit);
                suggestNutritions.add(milk);
                suggestNutritions.add(meatEgg);
                suggestNutritions.add(grease);
            }
        } else if(standardWeight >= 70 && standardWeight < 75){
            if(irritableToMilk){
                SuggestNutrition cereal = new SuggestNutrition("谷薯类", "225g" );
                SuggestNutrition starch = new SuggestNutrition("淀粉", "75g");
                SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250g");
                SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "400g");
                SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "430g~470g");
                SuggestNutrition grease = new SuggestNutrition("油脂类", "55g");
                suggestNutritions.add(cereal);
                suggestNutritions.add(starch);
                suggestNutritions.add(vegetable);
                suggestNutritions.add(fruit);
                suggestNutritions.add(meatEgg);
                suggestNutritions.add(grease);

            } else {
                SuggestNutrition cereal = new SuggestNutrition("谷薯类", "225g" );
                SuggestNutrition starch = new SuggestNutrition("淀粉", "75g");
                SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250g");
                SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "400g");
                SuggestNutrition milk = new SuggestNutrition("奶类", "230g");
                SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "200g~240g");
                SuggestNutrition grease = new SuggestNutrition("油脂类", "55g");
                suggestNutritions.add(cereal);
                suggestNutritions.add(starch);
                suggestNutritions.add(vegetable);
                suggestNutritions.add(fruit);
                suggestNutritions.add(milk);
                suggestNutritions.add(meatEgg);
                suggestNutritions.add(grease);
            }
        } else if(standardWeight >= 75){
            if(irritableToMilk){
                SuggestNutrition cereal = new SuggestNutrition("谷薯类", "250g" );
                SuggestNutrition starch = new SuggestNutrition("淀粉", "75g");
                SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250g");
                SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "400g");
                SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "430g~470g");
                SuggestNutrition grease = new SuggestNutrition("油脂类", "60g");
                suggestNutritions.add(cereal);
                suggestNutritions.add(starch);
                suggestNutritions.add(vegetable);
                suggestNutritions.add(fruit);
                suggestNutritions.add(meatEgg);
                suggestNutritions.add(grease);

            } else {
                SuggestNutrition cereal = new SuggestNutrition("谷薯类", "250g" );
                SuggestNutrition starch = new SuggestNutrition("淀粉", "75g");
                SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250g");
                SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "400g");
                SuggestNutrition milk = new SuggestNutrition("奶类", "230g");
                SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "200g~240g");
                SuggestNutrition grease = new SuggestNutrition("油脂类", "60g");
                suggestNutritions.add(cereal);
                suggestNutritions.add(starch);
                suggestNutritions.add(vegetable);
                suggestNutritions.add(fruit);
                suggestNutritions.add(milk);
                suggestNutritions.add(meatEgg);
                suggestNutritions.add(grease);
            }
        } else{
            suggestNutritions.add(new SuggestNutrition("没有合适的食材", "0g"));
        }
        return suggestNutritions;
    }


}
