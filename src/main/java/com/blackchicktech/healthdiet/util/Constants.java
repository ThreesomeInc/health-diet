package com.blackchicktech.healthdiet.util;

import com.blackchicktech.healthdiet.domain.OtherDiseaseSuggestDiet;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by Eric Cen on 2018/8/6.
 */
public class Constants {

	public final static String ADVICE_TEMPLATE = "您的肾脏功能属于第%s期，需要控制蛋白质摄入以延缓肾脏功能的进一步恶化。同时，您的%s偏高，建议%s饮食。";

	public final static Map<String, OtherDiseaseSuggestDiet> SUGGESTED_DIET = ImmutableMap.<String, OtherDiseaseSuggestDiet>builder()
			.put("hyperuricacidemia", new OtherDiseaseSuggestDiet("高尿酸", "尿酸", "低嘌呤"))
			.put("hyperglycemia", new OtherDiseaseSuggestDiet("高血糖", "血糖", "低糖"))
			.put("cholesterol", new OtherDiseaseSuggestDiet("高胆固醇", "胆固醇", "低胆固醇"))
			.put("triglyceride", new OtherDiseaseSuggestDiet("高甘油三酯", "甘油三酯", "低脂肪"))
			.put("hypertension", new OtherDiseaseSuggestDiet("高血压", "血压", "低钠"))
			.build();

	public final static String[] SLOGAN = {"你知道怎么吃得饱又吃得好吗？", "没有垃圾食品，只有垃圾食法！"};

	public final static Map<String, String> GENDER = ImmutableMap.of(
			"male", "男",
			"female", "女");

	public final static Map<String, String> TREATMENT_METHOD = ImmutableMap.<String, String>builder()
			.put("medication", "药物治疗")
			.put("hemodialysis", "血液透析治疗")
			.put("peritoneal-dialysis", "腹膜透析治疗")
			.put("transplantation", "肾脏移植").build();

	public final static Map<String, String> SPORT_RATE = ImmutableMap.<String, String>builder()
			.put("light", "轻度（如：长期坐办公室）")
			.put("medium", "中度（如：不时外出跑业务）")
			.put("severe", "重度（如：搬运）").build();

	public final static Map<String, String> OTHER_DISEASE = ImmutableMap.<String, String>builder()
			.put("hypertension", "高血压")
			.put("triglyceride", "高甘油三酯")
			.put("cholesterol", "高胆固醇")
			.put("hyperglycemia", "高血糖")
			.put("hyperuricacidemia", "高尿酸").build();


	public final static Map<String, String> IRRITABILITY = ImmutableMap.<String, String>builder()
			.put("milk", "奶")
			.put("egg", "蛋")
			.put("crostacei", "贝壳")
			.put("fish-prawn-crab", "鱼虾蟹")
			.put("flour", "面粉")
			.put("nuts", "坚果")
			.put("soya", "黄豆")
			.put("corn", "玉米")
			.build();

}
