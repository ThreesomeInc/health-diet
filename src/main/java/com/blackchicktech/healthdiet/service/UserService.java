package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.domain.UserInfo;
import com.blackchicktech.healthdiet.domain.UserMetadata;
import com.blackchicktech.healthdiet.repository.MockUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    @Autowired
    private MockUserDao userDao;

    private Map<String, UserMetadata> cache = new ConcurrentHashMap<>();

    public UserMetadata getUser(String openId) {
       UserMetadata metadata = cache.get(openId);
       if (metadata != null) {
           return metadata;
       }
       return createMeta(userDao.getUserInfo(openId));
    }

    public UserMetadata getUser(UserInfo userInfo) {
        UserMetadata metadata = cache.get(userInfo.getOpenId());
        if (metadata != null) {
            return metadata;
        }
        return createMeta(userInfo);
    }

    private UserMetadata createMeta(UserInfo userInfo) {
        if (userInfo == null) {
            return new UserMetadata();
        }

        UserMetadata metadata = new UserMetadata();
        metadata.setOpenId(userInfo.getOpenId());
        //做一些计算之后缓存
        cache.put(userInfo.getOpenId(), metadata);
        return metadata;
    }
}
