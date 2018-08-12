package com.blackchicktech.healthdiet.controller;

import com.blackchicktech.healthdiet.domain.ReportRequest;
import com.blackchicktech.healthdiet.domain.ReportResponse;
import com.blackchicktech.healthdiet.service.ReportService;
import com.blackchicktech.healthdiet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    public ReportService reportService;

    @Autowired
    public UserService userService;

    @RequestMapping(value = "/report", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReportResponse getReport(@RequestBody ReportRequest reportRequest) {
        userService.createUser(reportRequest.getUserInfo(), reportRequest.getUserDataInfo());
        return reportService.report(reportRequest);
    }
}
