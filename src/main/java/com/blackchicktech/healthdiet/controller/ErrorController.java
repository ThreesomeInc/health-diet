package com.blackchicktech.healthdiet.controller;

import com.blackchicktech.healthdiet.advice.RestExceptionHandlerAdvice;
import com.blackchicktech.healthdiet.util.rest.ApiCode;
import com.blackchicktech.healthdiet.util.rest.JsonResult;
import com.blackchicktech.healthdiet.util.rest.ResultHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ErrorController {
	private static Logger logger = LoggerFactory.getLogger(RestExceptionHandlerAdvice.class);

	@GetMapping("/error")
	public JsonResult error(HttpServletRequest request) {
		Integer status = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (status == HttpStatus.NOT_FOUND.value()) {
			return ResultHelper.build(ApiCode.REQUEST_NOT_FOUND_ERROR);
		} else if (status == HttpStatus.FORBIDDEN.value()) {
			return ResultHelper.forbidden();
		}
		return ResultHelper.error();
	}
}
