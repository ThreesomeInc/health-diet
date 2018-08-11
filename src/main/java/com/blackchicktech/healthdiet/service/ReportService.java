package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.domain.*;
import com.blackchicktech.healthdiet.util.Constants;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Eric Cen on 2018/8/3.
 */
@Service
public class ReportService {

	public static Random random = new Random();


	public ReportResponse report(ReportRequest reportRequest) {
		ReportResponse response = new ReportResponse();
		/*response.setBodyType(calBmi(reportRequest));*/
		response.setStandardWeight(String.valueOf(calStandardWeight(reportRequest)) + "公斤");
		response.setCalorie(String.valueOf(calCalorie(reportRequest)) + "卡路里");
		response.setProtein(calProtein(reportRequest));
		response.setBmi(calBmi(reportRequest));
		response.setAdvice(deduceAdvice(reportRequest));
		response.setSuggestNutrition(calSuggestNutrition(reportRequest));
		response.setSlogan(deduceSlogan());
		return response;
	}

	private String calBmi(ReportRequest reportRequest) {
		float bmiIndex = calBmiIndex(reportRequest);
		if (bmiIndex < 18.5) {
			return BodyType.MALNOURISHED.info();
		} else if (bmiIndex >= 18.5 && bmiIndex <= 23.9) {
			return BodyType.NORMAL.info();
		} else if (bmiIndex >= 24.0 && bmiIndex <= 27.9) {
			return BodyType.OVERWEIGHT.info();
		} else {
			return BodyType.FATTY.info();
		}
	}

	private float calBmiIndex(ReportRequest reportRequest) {
		String height = reportRequest.getUserDataInfo().getHeight();
		String weight = reportRequest.getUserDataInfo().getWeight();
		return (Float.parseFloat(getHeightOrWeight(weight)) / (Float.parseFloat(getHeightOrWeight(height)) / 100))
				/ (Float.parseFloat(getHeightOrWeight(height)) / 100);
	}

	private float calStandardWeight(ReportRequest reportRequest) {
		String gender = reportRequest.getUserDataInfo().getGender();
		String height = getHeightOrWeight(reportRequest.getUserDataInfo().getHeight());
		if ("male" .equals(gender) || Constants.GENDER.getOrDefault("male", "").equals(gender)) {
			return (float) ((Integer.parseInt(height) - 100) * 0.9);
		} else if ("female" .equals(gender) || Constants.GENDER.getOrDefault("female", "").equals(gender)) {
			return (float) ((Integer.parseInt(height) - 100) * 0.9 - 2.5);
		} else {
			throw new IllegalArgumentException("Gender can only be male or female");
		}
	}

	private String getHeightOrWeight(String value) {
		Matcher result = Pattern.compile("(\\d+(.\\d))?").matcher(value);
		return result.find() ? result.group() : "0";
	}

	private float calCalorie(ReportRequest reportRequest) {
		String sportRate = reportRequest.getUserDataInfo().getSportRate();
		float standardWeight = calStandardWeight(reportRequest);
		float bmi = calBmiIndex(reportRequest);
		if ("light" .equals(sportRate)
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

	private String calProtein(ReportRequest reportRequest) {
		int nephroticPeriod = Integer.parseInt(reportRequest.getUserDataInfo().getNephroticPeriod());
		List<String> treatmentMethod = reportRequest.getUserDataInfo().getTreatmentMethod();
		BigDecimal standardWeight = BigDecimal.valueOf(calStandardWeight(reportRequest));
		StringBuilder protein = new StringBuilder();
		if (nephroticPeriod >= 1 && nephroticPeriod <= 2) {
			return protein.append(standardWeight.multiply(BigDecimal.valueOf(0.8)).setScale(1, BigDecimal.ROUND_FLOOR)).append("~")
					.append(standardWeight.multiply(BigDecimal.ONE).setScale(1, BigDecimal.ROUND_FLOOR)).append("克").toString();
		} else if (nephroticPeriod >= 3 && nephroticPeriod <= 5) {
			if (treatmentMethod.stream().anyMatch(item -> item.contains("dialysis"))) {
				return protein.append(standardWeight.multiply(BigDecimal.ONE).setScale(1, BigDecimal.ROUND_FLOOR)).append("~")
						.append(standardWeight.multiply(BigDecimal.valueOf(1.2)).setScale(1, BigDecimal.ROUND_FLOOR)).append("克").toString();
			} else {
				return protein.append(standardWeight.multiply(BigDecimal.valueOf(0.6)).setScale(1, BigDecimal.ROUND_FLOOR)).append("~")
						.append(standardWeight.multiply(BigDecimal.valueOf(10.8)).setScale(1, BigDecimal.ROUND_FLOOR)).append("克").toString();
			}
		}
		return "Protein is unclear.";
	}

	private List<SuggestNutrition> calSuggestNutrition(ReportRequest reportRequest) {
		float standardWeight = calStandardWeight(reportRequest);
		float calorie = calCalorie(reportRequest);
		String protein = calProtein(reportRequest);
		List<String> irritabilities = reportRequest.getUserDataInfo().getIrritability();
		List<SuggestNutrition> suggestNutritions = new ArrayList<>();
		boolean irritableToMilk = irritabilities.contains("milk")
				|| irritabilities.contains(Constants.IRRITABILITY.getOrDefault("milk", ""));
		if (standardWeight >= 40 && standardWeight < 45) {
			if (irritableToMilk) {
				SuggestNutrition cereal = new SuggestNutrition("谷薯类", "100克");
				SuggestNutrition starch = new SuggestNutrition("淀粉", "50克");
				SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250克");
				SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "200克");
				SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "135g~155克");
				SuggestNutrition grease = new SuggestNutrition("油脂类", "35克");
				suggestNutritions.add(cereal);
				suggestNutritions.add(starch);
				suggestNutritions.add(vegetable);
				suggestNutritions.add(fruit);
				suggestNutritions.add(meatEgg);
				suggestNutritions.add(grease);

			} else {
				SuggestNutrition cereal = new SuggestNutrition("谷薯类", "100克");
				SuggestNutrition starch = new SuggestNutrition("淀粉", "50克");
				SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250克");
				SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "200克");
				SuggestNutrition milk = new SuggestNutrition("奶类", "230克");
				SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "100g~120克");
				SuggestNutrition grease = new SuggestNutrition("油脂类", "35克");
				suggestNutritions.add(cereal);
				suggestNutritions.add(starch);
				suggestNutritions.add(vegetable);
				suggestNutritions.add(fruit);
				suggestNutritions.add(milk);
				suggestNutritions.add(meatEgg);
				suggestNutritions.add(grease);

			}
		} else if (standardWeight >= 45 && standardWeight < 50) {
			if (irritableToMilk) {
				SuggestNutrition cereal = new SuggestNutrition("谷薯类", "125克");
				SuggestNutrition starch = new SuggestNutrition("淀粉", "50克");
				SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250克");
				SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "200克");
				SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "135g~155克");
				SuggestNutrition grease = new SuggestNutrition("油脂类", "40克");
				suggestNutritions.add(cereal);
				suggestNutritions.add(starch);
				suggestNutritions.add(vegetable);
				suggestNutritions.add(fruit);
				suggestNutritions.add(meatEgg);
				suggestNutritions.add(grease);

			} else {
				SuggestNutrition cereal = new SuggestNutrition("谷薯类", "125克");
				SuggestNutrition starch = new SuggestNutrition("淀粉", "50克");
				SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250克");
				SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "200克");
				SuggestNutrition milk = new SuggestNutrition("奶类", "230克");
				SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "100g~120克");
				SuggestNutrition grease = new SuggestNutrition("油脂类", "40克");
				suggestNutritions.add(cereal);
				suggestNutritions.add(starch);
				suggestNutritions.add(vegetable);
				suggestNutritions.add(fruit);
				suggestNutritions.add(milk);
				suggestNutritions.add(meatEgg);
				suggestNutritions.add(grease);
			}
		} else if (standardWeight >= 50 && standardWeight < 55) {
			if (irritableToMilk) {
				SuggestNutrition cereal = new SuggestNutrition("谷薯类", "150克");
				SuggestNutrition starch = new SuggestNutrition("淀粉", "50克");
				SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250克");
				SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "200克");
				SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "160g~185克");
				SuggestNutrition grease = new SuggestNutrition("油脂类", "45克");
				suggestNutritions.add(cereal);
				suggestNutritions.add(starch);
				suggestNutritions.add(vegetable);
				suggestNutritions.add(fruit);
				suggestNutritions.add(meatEgg);
				suggestNutritions.add(grease);

			} else {
				SuggestNutrition cereal = new SuggestNutrition("谷薯类", "150克");
				SuggestNutrition starch = new SuggestNutrition("淀粉", "50克");
				SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250克");
				SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "200克");
				SuggestNutrition milk = new SuggestNutrition("奶类", "230克");
				SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "125g~150克");
				SuggestNutrition grease = new SuggestNutrition("油脂类", "45克");
				suggestNutritions.add(cereal);
				suggestNutritions.add(starch);
				suggestNutritions.add(vegetable);
				suggestNutritions.add(fruit);
				suggestNutritions.add(milk);
				suggestNutritions.add(meatEgg);
				suggestNutritions.add(grease);
			}
		} else if (standardWeight >= 55 && standardWeight < 60) {
			if (irritableToMilk) {
				SuggestNutrition cereal = new SuggestNutrition("谷薯类", "175克");
				SuggestNutrition starch = new SuggestNutrition("淀粉", "50克");
				SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250克");
				SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "400克");
				SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "160g~185克");
				SuggestNutrition grease = new SuggestNutrition("油脂类", "50克");
				suggestNutritions.add(cereal);
				suggestNutritions.add(starch);
				suggestNutritions.add(vegetable);
				suggestNutritions.add(fruit);
				suggestNutritions.add(meatEgg);
				suggestNutritions.add(grease);

			} else {
				SuggestNutrition cereal = new SuggestNutrition("谷薯类", "175克");
				SuggestNutrition starch = new SuggestNutrition("淀粉", "50克");
				SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250克");
				SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "400克");
				SuggestNutrition milk = new SuggestNutrition("奶类", "230克");
				SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "125g~150克");
				SuggestNutrition grease = new SuggestNutrition("油脂类", "50克");
				suggestNutritions.add(cereal);
				suggestNutritions.add(starch);
				suggestNutritions.add(vegetable);
				suggestNutritions.add(fruit);
				suggestNutritions.add(milk);
				suggestNutritions.add(meatEgg);
				suggestNutritions.add(grease);
			}
		} else if (standardWeight >= 60 && standardWeight < 65) {
			if (irritableToMilk) {
				SuggestNutrition cereal = new SuggestNutrition("谷薯类", "200克");
				SuggestNutrition starch = new SuggestNutrition("淀粉", "50克");
				SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250克");
				SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "400克");
				SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "185g~215克");
				SuggestNutrition grease = new SuggestNutrition("油脂类", "50克");
				suggestNutritions.add(cereal);
				suggestNutritions.add(starch);
				suggestNutritions.add(vegetable);
				suggestNutritions.add(fruit);
				suggestNutritions.add(meatEgg);
				suggestNutritions.add(grease);

			} else {
				SuggestNutrition cereal = new SuggestNutrition("谷薯类", "200克");
				SuggestNutrition starch = new SuggestNutrition("淀粉", "50克");
				SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250克");
				SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "400克");
				SuggestNutrition milk = new SuggestNutrition("奶类", "230克");
				SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "150g~180克");
				SuggestNutrition grease = new SuggestNutrition("油脂类", "50克");
				suggestNutritions.add(cereal);
				suggestNutritions.add(starch);
				suggestNutritions.add(vegetable);
				suggestNutritions.add(fruit);
				suggestNutritions.add(milk);
				suggestNutritions.add(meatEgg);
				suggestNutritions.add(grease);
			}
		} else if (standardWeight >= 65 && standardWeight < 70) {
			if (irritableToMilk) {
				SuggestNutrition cereal = new SuggestNutrition("谷薯类", "200克");
				SuggestNutrition starch = new SuggestNutrition("淀粉", "50克");
				SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250克");
				SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "400克");
				SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "210g~245克");
				SuggestNutrition grease = new SuggestNutrition("油脂类", "55克");
				suggestNutritions.add(cereal);
				suggestNutritions.add(starch);
				suggestNutritions.add(vegetable);
				suggestNutritions.add(fruit);
				suggestNutritions.add(meatEgg);
				suggestNutritions.add(grease);

			} else {
				SuggestNutrition cereal = new SuggestNutrition("谷薯类", "200克");
				SuggestNutrition starch = new SuggestNutrition("淀粉", "50克");
				SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250克");
				SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "400克");
				SuggestNutrition milk = new SuggestNutrition("奶类", "230克");
				SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "175g~210克");
				SuggestNutrition grease = new SuggestNutrition("油脂类", "55克");
				suggestNutritions.add(cereal);
				suggestNutritions.add(starch);
				suggestNutritions.add(vegetable);
				suggestNutritions.add(fruit);
				suggestNutritions.add(milk);
				suggestNutritions.add(meatEgg);
				suggestNutritions.add(grease);
			}
		} else if (standardWeight >= 70 && standardWeight < 75) {
			if (irritableToMilk) {
				SuggestNutrition cereal = new SuggestNutrition("谷薯类", "225克");
				SuggestNutrition starch = new SuggestNutrition("淀粉", "75克");
				SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250克");
				SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "400克");
				SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "235g~275克");
				SuggestNutrition grease = new SuggestNutrition("油脂类", "55克");
				suggestNutritions.add(cereal);
				suggestNutritions.add(starch);
				suggestNutritions.add(vegetable);
				suggestNutritions.add(fruit);
				suggestNutritions.add(meatEgg);
				suggestNutritions.add(grease);

			} else {
				SuggestNutrition cereal = new SuggestNutrition("谷薯类", "225克");
				SuggestNutrition starch = new SuggestNutrition("淀粉", "75克");
				SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250克");
				SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "400克");
				SuggestNutrition milk = new SuggestNutrition("奶类", "230克");
				SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "200g~240克");
				SuggestNutrition grease = new SuggestNutrition("油脂类", "55克");
				suggestNutritions.add(cereal);
				suggestNutritions.add(starch);
				suggestNutritions.add(vegetable);
				suggestNutritions.add(fruit);
				suggestNutritions.add(milk);
				suggestNutritions.add(meatEgg);
				suggestNutritions.add(grease);
			}
		} else if (standardWeight >= 75) {
			if (irritableToMilk) {
				SuggestNutrition cereal = new SuggestNutrition("谷薯类", "250克");
				SuggestNutrition starch = new SuggestNutrition("淀粉", "75克");
				SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250克");
				SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "400克");
				SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "235g~275克");
				SuggestNutrition grease = new SuggestNutrition("油脂类", "60克");
				suggestNutritions.add(cereal);
				suggestNutritions.add(starch);
				suggestNutritions.add(vegetable);
				suggestNutritions.add(fruit);
				suggestNutritions.add(meatEgg);
				suggestNutritions.add(grease);

			} else {
				SuggestNutrition cereal = new SuggestNutrition("谷薯类", "250克");
				SuggestNutrition starch = new SuggestNutrition("淀粉", "75克");
				SuggestNutrition vegetable = new SuggestNutrition("绿叶蔬菜", "250克");
				SuggestNutrition fruit = new SuggestNutrition("瓜果蔬菜", "400克");
				SuggestNutrition milk = new SuggestNutrition("奶类", "230克");
				SuggestNutrition meatEgg = new SuggestNutrition("肉蛋类", "200g~240克");
				SuggestNutrition grease = new SuggestNutrition("油脂类", "60克");
				suggestNutritions.add(cereal);
				suggestNutritions.add(starch);
				suggestNutritions.add(vegetable);
				suggestNutritions.add(fruit);
				suggestNutritions.add(milk);
				suggestNutritions.add(meatEgg);
				suggestNutritions.add(grease);
			}
		} else {
			suggestNutritions.add(new SuggestNutrition("没有合适的食材", "0克"));
		}
		return suggestNutritions;
	}

	private String deduceAdvice(ReportRequest reportRequest) {
		int nephroticPeriod = Integer.parseInt(reportRequest.getUserDataInfo().getNephroticPeriod());
		List<String> otherDiseases = reportRequest.getUserDataInfo().getOtherDisease();
		StringBuilder suggestDiets = new StringBuilder();
		StringBuilder otherDiseaseResult = new StringBuilder();
		for (String otherDisease : otherDiseases) {
			OtherDiseaseSuggestDiet otherDiseaseSuggestDiet = Constants.SUGGESTED_DIET.get(otherDisease);
			if (otherDiseaseSuggestDiet != null) {
				String separator = suggestDiets.length() > 0 ? "、" : "";
				suggestDiets.append(separator).append(otherDiseaseSuggestDiet.getSuggestDiet());
				otherDiseaseResult.append(separator).append(otherDiseaseSuggestDiet.getElement());
			}
		}
		return String.format(Constants.ADVICE_TEMPLATE, nephroticPeriod,
				otherDiseaseResult.toString(), suggestDiets.toString());
	}

	private String deduceSlogan() {
		int seed = random.nextInt(10);
		System.out.println("Seed: " + seed);
		return Constants.SLOGAN[seed % Constants.SLOGAN.length];
	}


}
