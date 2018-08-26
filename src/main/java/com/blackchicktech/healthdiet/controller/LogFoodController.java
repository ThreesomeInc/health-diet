package com.blackchicktech.healthdiet.controller;

import com.blackchicktech.healthdiet.domain.*;
import com.blackchicktech.healthdiet.service.FoodLogService;
import com.blackchicktech.healthdiet.service.UserService;
import com.blackchicktech.healthdiet.util.FoodLogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/foodLog")
public class LogFoodController {

    private static final Logger logger = LoggerFactory.getLogger(LogFoodController.class);
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private FoodLogService foodLogService;

    @Autowired
    private UserService userService;

    //logFood?openId=xxxx
    //获取当月食物
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public MonthFoodLogResponse getCurrentMonthFoodLog(@RequestParam String openId) {
        Date date = new Date();
        List<MonthFoodLog> monthFoodLogList = foodLogService.getCurrentMonthFoodLog(openId, date);
        return new MonthFoodLogResponse(monthFoodLogList);
    }


    //记录每日膳食 有的话覆盖
    @PostMapping(MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public DietRecordResponse addFoodLog(@RequestBody FoodLogRequest request) {
        logger.info("Begin to log food for user openId={}, date={}, mealTime={}, content",
                request.getOpenId(),
                request.getDate(),
                request.getMealTime(),
                FoodLogUtil.toJsonStr(request.getFoodLogItemList()));
        AccumulativeEnergy energy = foodLogService.updateFoodLog(request);
        return new DietRecordResponse(energy);
    }

    //获取每日膳食
    @GetMapping(MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public DietHistoryResponse getDietHistory(@RequestParam String period) { //period=month, period=day eg: period='2018-06', period='2018-06-01'
        return new DietHistoryResponse();
    }

    private Date parseDate(String dateString) {
        try {
            return sdf.parse(dateString);
        } catch (Exception e) {
            logger.warn("Failed to parse date to format yyyy-MM-dd dateString={}", dateString);
            throw new RuntimeException(e);
        }
    }


    @RequestMapping(value="/analysis", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ThreeDayFoodLogAnalysis getFoodLogAnalysis(@RequestParam String openId){
        return new ThreeDayFoodLogAnalysis();
    }

}
