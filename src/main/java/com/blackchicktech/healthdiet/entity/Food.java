package com.blackchicktech.healthdiet.entity;

public class Food implements Entity {

    private String foodId; //保留字段做key

    private String foodName;

    private String picUrl;

    private String diescription; //预留字段用于说明

    private String type; //预留字段

    private String unit; //一份单位 例如卡路里

    private String value; //默认一份所包含的数值
}
