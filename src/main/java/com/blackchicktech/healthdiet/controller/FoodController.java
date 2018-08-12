package com.blackchicktech.healthdiet.controller;

import com.blackchicktech.healthdiet.domain.*;
import com.blackchicktech.healthdiet.service.FoodService;
import com.blackchicktech.healthdiet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/food")
//通过restful更新食物
public class FoodController {

    @Autowired
    private UserService userService;

    @Autowired
    private FoodService foodService;

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public FoodRecordResponse addFood(@RequestBody FoodRecordRequest recordRequest) {
        return new FoodRecordResponse();
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public FoodRecordResponse updateFood(@RequestBody FoodRecordRequest recordRequest) {
        return new FoodRecordResponse();
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public FoodRecordResponse deleteFood(@RequestBody FoodRecordRequest recordRequest) {
        return new FoodRecordResponse();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public FoodListResponse getFoodList(@RequestParam("typeId") String typeId) {
        return new FoodListResponse(foodService.listFood(typeId));
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public FoodListResponse getFoodListByName(@RequestParam("name") String name) {
        return new FoodListResponse(foodService.listFoodByName(name));
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public FoodVerifyResponse verifyFood(@RequestBody FoodVerifyRequest foodVerifyRequest) {
        return new FoodVerifyResponse();
    }

    @RequestMapping(value = "/type", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public FoodTypeListResponse getFoodTypeList() {
        return new FoodTypeListResponse(foodService.listFoodType());
    }

    @RequestMapping(value="/{foodId}", method=RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public FoodDetailResponse getFoodDetailById(@PathVariable String foodId, @RequestParam("openId") String openId){
        return foodService.getFoodById(foodId, openId);
    }
}
