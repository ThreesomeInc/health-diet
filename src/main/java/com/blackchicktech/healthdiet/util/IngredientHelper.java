package com.blackchicktech.healthdiet.util;

import com.blackchicktech.healthdiet.domain.MainIngredient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IngredientHelper {

    private static final Logger logger = LoggerFactory.getLogger(IngredientHelper.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static List<MainIngredient> parse(String rawString) {
        rawString = rawString.substring(1, rawString.lastIndexOf("}"));
        List<MainIngredient> list = new ArrayList<>();
        try {
            JsonNode array = objectMapper.readTree(rawString);
            for (int i = 0; i < array.size(); i++) {
                JsonNode obj = array.get(i);
                MainIngredient mainIngredient = new MainIngredient();
                mainIngredient.setFoodName(obj.get("食材").asText());
                mainIngredient.setWeight(obj.get("重量").asText());
                list.add(mainIngredient);
            }
        } catch (IOException e) {
            logger.warn("Failed to parse json rawString={}", rawString);
        }
        return list;
    }
}
