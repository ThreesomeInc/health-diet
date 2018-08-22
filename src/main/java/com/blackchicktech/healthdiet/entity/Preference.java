package com.blackchicktech.healthdiet.entity;

import java.util.HashMap;
import java.util.Map;

public class Preference implements Entity {
	private String userOpenId;
	private String foodId;
	private int frequency;
	private String foodName;
	private String userName;

	private String type;

	public Preference(String userOpenId, String foodId, int frequency, String type) {
		this.userOpenId = userOpenId;
		this.foodId = foodId;
		this.frequency = frequency;
		this.type = type;
	}

	public Preference() {
	}

	public Map<String, Object[]> getFieldInfo() {
		Map<String, Object[]> map = new HashMap<>();
		map.put("user_open_id", new Object[]{userOpenId, "setUserOpenId", String.class});
		map.put("food_id", new Object[]{foodId, "setFoodId", String.class});
		map.put("food_name", new Object[]{foodName, "setFoodName", String.class});
		map.put("type", new Object[]{type, "setType", String.class});
		map.put("frequency", new Object[]{frequency, "setFrequency", int.class});
		return map;
	}

	public String getUserOpenId() {
		return userOpenId;
	}

	public void setUserOpenId(String userOpenId) {
		this.userOpenId = userOpenId;
	}

	public String getFoodId() {
		return foodId;
	}

	public void setFoodId(String foodId) {
		this.foodId = foodId;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
