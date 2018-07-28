package com.blackchicktech.healthdiet.controller;

import com.blackchicktech.healthdiet.repository.HelloDao;
import com.blackchicktech.healthdiet.util.rest.ApiCode;
import com.blackchicktech.healthdiet.util.rest.JsonResult;
import com.blackchicktech.healthdiet.util.rest.ResultHelper;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/hello")
public class HelloController {
//	@Autowired
//	private HelloDao helloDao;

	@GetMapping("/{name}")
	public JsonResult greeting(@PathVariable("name") String name) {
		return ResultHelper.build(ApiCode.SUCCESS, "Hello " + name);
	}

	@GetMapping("/view")
	public ModelAndView greetingView(@RequestParam("name") String name) {
		ModelAndView mav = new ModelAndView("hello");
		mav.addObject("username", name);
//		mav.addObject("existing", helloDao.queryList(ImmutableMap.of("name", "Hello")));
		return mav;
	}
}
