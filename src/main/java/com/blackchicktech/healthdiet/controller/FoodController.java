package com.blackchicktech.healthdiet.controller;

import com.blackchicktech.healthdiet.domain.*;
import com.blackchicktech.healthdiet.service.FoodService;
import com.blackchicktech.healthdiet.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/food")
//通过restful更新食物
public class FoodController {

    private static Logger LOGGER = LoggerFactory.getLogger(FoodController.class);

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

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

    @RequestMapping(value="/detail/{foodId}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public FoodDetailResponse getFoodDetailById(@PathVariable String foodId,
                                                @RequestParam("openId") String openId){
        return foodService.getFoodById(foodId, openId);
    }

    @RequestMapping(value="/detail/{foodId}", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus
    public ResponseEntity getFoodDetailById(@PathVariable String foodId,
                                            @RequestBody FoodEatFrequency foodEatFrequency){

        LOGGER.info("User " + foodEatFrequency.getOpenId() + " eat food " + foodId + ":" + foodEatFrequency.getFrequency() );
        return ResponseEntity.ok().build();
    }
}
