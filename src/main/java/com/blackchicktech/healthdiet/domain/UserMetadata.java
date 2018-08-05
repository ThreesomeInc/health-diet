package com.blackchicktech.healthdiet.domain;

//用于存储处理上下文数据，缓存 临时使用
public class UserMetadata {

    private String openId;

    private String nickName;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
