package com.blackchicktech.healthdiet.controller;

import com.blackchicktech.healthdiet.util.HttpClientUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/common")
public class CommonController {

	@Value("${app.appId}")
	private String appId;

	@Value("${app.appSecret}")
	private String appSecret;

	@GetMapping(value = "/wxLogin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String login(HttpServletRequest request) {
		String code = request.getHeader("X-WX-Code");
		String result = HttpClientUtil.instance().getData(
				"https://api.weixin.qq.com/sns/jscode2session?appid=" + appId +
						"&secret=" + appSecret +
						"&grant_type=authorization_code" +
						"&js_code=" + code);
		return result;
	}
}
