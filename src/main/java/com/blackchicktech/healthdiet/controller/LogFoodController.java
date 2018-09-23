package com.blackchicktech.healthdiet.controller;

import com.blackchicktech.healthdiet.domain.*;
import com.blackchicktech.healthdiet.entity.FoodLog;
import com.blackchicktech.healthdiet.entity.FoodLogDetail;
import com.blackchicktech.healthdiet.entity.FoodUnit;
import com.blackchicktech.healthdiet.entity.User;
import com.blackchicktech.healthdiet.service.FoodLogService;
import com.blackchicktech.healthdiet.service.FoodService;
import com.blackchicktech.healthdiet.service.ReportService;
import com.blackchicktech.healthdiet.service.UserService;
import com.blackchicktech.healthdiet.util.FoodLogUtil;
import com.blackchicktech.healthdiet.util.UserUtil;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/foodLog")
public class LogFoodController {

	private static final Logger logger = LoggerFactory.getLogger(LogFoodController.class);
	private final FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd");//Thread safe

	@Autowired
	private FoodLogService foodLogService;

	@Autowired
	private FoodService foodService;

	@Autowired
	private UserService userService;

	@Autowired
	private ReportService reportService;

	//logFood?openId=xxxx
	//获取当月食物
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public MonthFoodLogResponse getCurrentMonthFoodLog(@RequestParam String openId) {
		Date date = new Date();
		List<MonthFoodLog> monthFoodLogList = foodLogService.getCurrentMonthFoodLog(openId, date);
		User user = userService.getUserByOpenId(openId);
		ReportResponse response = reportService.report(UserUtil.createReportRequest(user));
		monthFoodLogList.forEach(item -> {
			item.setExpectEnergy(response.getCalorie());
			item.setExpectProtein(response.getProtein());
		});
		return new MonthFoodLogResponse(monthFoodLogList);
	}

	//获取当月哪日有3餐报告的接口 month=yyyy-MM
	@RequestMapping(value = "/reports", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ThreeDayReportsResponse getThreeDayReports(@RequestBody ThreeDayReportsRequest reportsRequest) {
		List<MonthFoodLog> monthFoodLogList = foodLogService.getThreeDaysMonthFoodLog(reportsRequest.getOpenId(),
				parseDate(reportsRequest.getMonth() + "-01"));
		List<Date> dateList = new ArrayList<>();
		if (monthFoodLogList.size() <= 2) {
			return new ThreeDayReportsResponse(dateList);
		}
		for (int i = 2; i < monthFoodLogList.size(); i++) {
			if (foodLogService.isStandardLogType(monthFoodLogList.get(i).getDate(),
					monthFoodLogList.get(i-1).getDate(),
					monthFoodLogList.get(i-2).getDate())) {
				dateList.add(monthFoodLogList.get(i).getDate());
			}
		}
		return new ThreeDayReportsResponse(dateList);
	}

	//记录每日膳食 有的话覆盖
	@PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public DietRecordResponse addFoodLog(@RequestBody FoodLogRequest request) {
		logger.info("Begin to log food for user openId={}, date={}, mealTime={}, content={}",
				request.getOpenId(),
				request.getDate(),
				request.getMealTime(),
				FoodLogUtil.toJsonStr(request.getFoodLogItemList()));
		AccumulativeEnergy energy = foodLogService.updateFoodLog(request);
		return new DietRecordResponse(energy);
	}

	//获取每日膳食
	@RequestMapping(value = "/single", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public DietHistoryResponse getDietHistory(@RequestParam String openId,
											  @RequestParam(required = false) String mealtime,
											  @RequestParam String date) {
		List<FoodLogDetail> foodLogDetails = foodLogService.getFoodLogDetail(openId, parseDate(date), mealtime);

		User user = userService.getUserByOpenId(openId);
		ReportResponse response = reportService.report(UserUtil.createReportRequest(user));
		MonthFoodLog monthFoodLog = new MonthFoodLog(getExpectedValue(response.getProtein()), getExpectedValue(response.getCalorie()));
		if (foodLogDetails.isEmpty()) {
			return new DietHistoryResponse(Collections.emptyList(), monthFoodLog);
		}

		FoodLog foodLog = foodLogService.getMonthFoodLog(openId, parseDate(date));
		if (foodLog == null) {
			return new DietHistoryResponse(foodLogDetails.stream().map(DietRecord::new).collect(Collectors.toList()), monthFoodLog);
		}
		monthFoodLog = new MonthFoodLog(foodLog);
		monthFoodLog.setExpectProtein(getExpectedValue(response.getProtein()));
		monthFoodLog.setExpectEnergy(getExpectedValue(response.getCalorie()));
		return new DietHistoryResponse(foodLogDetails.stream().map(DietRecord::new).collect(Collectors.toList()), monthFoodLog);
	}

	//获取食部信息
	@RequestMapping(value = "/food/{foodId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public FoodUnitResponse getFoodUnit(@PathVariable String foodId) {
		FoodUnit foodUnit = foodService.getFoodUnit(foodId);
		if (foodUnit == null) {
			return new FoodUnitResponse();
		}

		return new FoodUnitResponse(foodUnit);
	}

	@RequestMapping(value = "/isCompletedLog", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public BasicResponse updateIsCompletedLog(@RequestBody CompletedLogInfo completedLogInfo) {
		foodLogService.updateIsCompletedLog(completedLogInfo.getOpenId(), parseDate(completedLogInfo.getDate()), completedLogInfo.isChecked());
		return new BasicResponse();
	}

	private Date parseDate(String dateString) {
		try {
			return sdf.parse(dateString);
		} catch (Exception e) {
			logger.warn("Failed to parse date to format yyyy-MM-dd dateString={}", dateString);
			throw new RuntimeException(e);
		}
	}


	@RequestMapping(value = "/analysis", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ThreeDayFoodLogAnalysis getFoodLogAnalysis(@RequestParam String openId, @RequestParam String date) {
		logger.info("Begin to analytics three days' food log for user opend id = {}", openId);
		return foodLogService.deduceThreeDayFoodLogAnalysis(openId, date);
	}

	private String getExpectedValue(String value) {
		Matcher result = Pattern.compile("(\\d+(.\\d))?").matcher(value);
		return result.find() ? result.group() : "0";
	}
}
