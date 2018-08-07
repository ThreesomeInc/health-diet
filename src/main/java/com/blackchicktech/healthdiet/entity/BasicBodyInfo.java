package com.blackchicktech.healthdiet.entity;

public class BasicBodyInfo extends BasicEntity {
	private long id;
	private String opend;
	private String tempId;
	private String gender;
	private String birth;
	private String height;
	private String weight;
	private String sportRate;
	private String nephroticPeriod;
	private String treatmentMethod;
	private String otherDisease;
	private String irritability;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOpend() {
		return opend;
	}

	public void setOpend(String opend) {
		this.opend = opend;
	}

	public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getSportRate() {
		return sportRate;
	}

	public void setSportRate(String sportRate) {
		this.sportRate = sportRate;
	}

	public String getNephroticPeriod() {
		return nephroticPeriod;
	}

	public void setNephroticPeriod(String nephroticPeriod) {
		this.nephroticPeriod = nephroticPeriod;
	}

	public String getTreatmentMethod() {
		return treatmentMethod;
	}

	public void setTreatmentMethod(String treatmentMethod) {
		this.treatmentMethod = treatmentMethod;
	}

	public String getOtherDisease() {
		return otherDisease;
	}

	public void setOtherDisease(String otherDisease) {
		this.otherDisease = otherDisease;
	}

	public String getIrritability() {
		return irritability;
	}

	public void setIrritability(String irritability) {
		this.irritability = irritability;
	}
}
