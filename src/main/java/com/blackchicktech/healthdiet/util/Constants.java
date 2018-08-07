package com.blackchicktech.healthdiet.util;

import com.blackchicktech.healthdiet.domain.OtherDiseaseSuggestDiet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric Cen on 2018/8/6.
 */
public class Constants {

    public static String ADVICE_TEMPLATE = "您的肾脏功能属于第%s期，需要控制蛋白质摄入以延缓肾脏功能的进一步恶化。同时，您的%s偏高，建议%s饮食。";
    public static Map<String, OtherDiseaseSuggestDiet> suggestDiet= new HashMap<>();

    public static String[] SLOGAN = {"你知道怎么吃得饱又吃得好吗？", "没有垃圾食品，只有垃圾食法！"};

    static{
        suggestDiet.put("高尿酸", new OtherDiseaseSuggestDiet("高尿酸","尿酸","低嘌呤"));
        suggestDiet.put("高血糖", new OtherDiseaseSuggestDiet("高血糖","血糖","低糖"));
        suggestDiet.put("高胆固醇",new OtherDiseaseSuggestDiet("高胆固醇","胆固醇","低胆固醇"));
        suggestDiet.put("高甘油三酯", new OtherDiseaseSuggestDiet("高甘油三酯","甘油三酯","低脂肪"));
        suggestDiet.put("高血压", new OtherDiseaseSuggestDiet("高血压","血压","低钠"));
    }
}
