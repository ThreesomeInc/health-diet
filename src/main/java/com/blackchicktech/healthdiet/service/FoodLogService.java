package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.domain.FoodLogRequest;
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

    public void updateFoodLog(FoodLogRequest request) {
        if (request.getFoodLogItemList() != null && !request.getFoodLogItemList().isEmpty()) {
            foodLogDao.addFoodLogDetail(request);
            foodLogDao.addFoodLog(request); //transaction
        } else {
            foodLogDao.deleteFoodLogDetail(request);
            //TODO 这里需要判断一天还有没有其余记录
//            foodLogDao.deleteFoodLog(request);
        }
    }
}
