package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.domain.MonthFoodLog;
import com.blackchicktech.healthdiet.repository.FoodLogDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodLogService {

    private static final Logger logger = LoggerFactory.getLogger(FoodLogService.class);

    @Autowired
    private FoodLogDao foodLogDao;

    public List<MonthFoodLog> getCurrentMonthFoodLog(String openId, Date currentDate) {
        return foodLogDao.getCurrentMonthFoodLog(openId, currentDate).stream()
                .map(MonthFoodLog::new).collect(Collectors.toList());
    }

}
