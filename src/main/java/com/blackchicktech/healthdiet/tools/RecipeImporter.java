package com.blackchicktech.healthdiet.tools;

import com.blackchicktech.healthdiet.entity.Recipe;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RecipeImporter {

    public static void main(String[] args) throws Exception {

        List<Recipe> recipeList = new ArrayList<>();
        BufferedReader in = null;
        int skipLineCount = 2;
        int lineNumber = 0;
        int totalLine = 5;
        try {
            in = Files.newBufferedReader(Paths.get("./recipe.csv"));
            Iterable<CSVRecord> records = CSVFormat.RFC4180
                    .withSkipHeaderRecord()
                    .parse(in);


            for (CSVRecord record : records) {
                if (lineNumber <= skipLineCount) {
                    lineNumber++;
                    continue;
                }

                Recipe recipe = new Recipe();
                recipe.setRecipeId(record.get(0));
                recipe.setRecipeName(record.get(1));
                recipe.setCookMethod(record.get(2));
                recipe.setTaste(record.get(3));
                recipe.setCuisine(record.get(4));
                recipe.setMealTime(record.get(9));
                recipe.setCategory(record.get(10));
                recipe.setMaterial(record.get(11));
                recipe.setMainIngredients(record.get(12));
                recipe.setSupplementary(record.get(13));
                recipe.setCookingnote(record.get(14));
                recipe.setEnergy(Float.valueOf(record.get(16)));
                recipe.setProtein(Float.valueOf(record.get(17)));
                recipe.setProteinWeight(Integer.valueOf(record.get(18)));
                recipe.setFatWeight(Integer.valueOf(record.get(19)));
                recipe.setChoWeight(Integer.valueOf(record.get(20)));
                recipe.setNaWeight(Integer.valueOf(record.get(24)));
                recipe.setCholesterolWeight(Integer.valueOf(record.get(21)));
                recipe.setPurineWeight(Integer.valueOf(record.get(22)));
                recipe.setCkdCategory(record.get(26));
                recipeList.add(recipe);
                lineNumber++;

                if (lineNumber >= totalLine) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
        System.out.println("Totally " + lineNumber + "read.");
        saveRecipeTblToCsv(recipeList);
    }

    private static void saveRecipeTblToCsv(List<Recipe> recipeList) throws IOException {
        recipeList.forEach(System.out::println);
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("./recipe_tbl.csv"));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("recipe_id", "recipe_name", "cook_method", "taste", "cuisine", "meal_time",
                                "category", "material", "main_ingredients", "supplementary", "cookingnote",
                                "energy", "protein", "protein_weight", "fat_weight", "cho_weight", "na_weight", "cholesterol_weight", "purine_weight", "ckd_category"))
        ) {
            for (Recipe recipe : recipeList) {
                csvPrinter.printRecord(recipe.getRecipeId(),
                        recipe.getRecipeName(),
                        recipe.getCookMethod(),
                        recipe.getTaste(),
                        recipe.getCuisine(),
                        recipe.getMealTime(),
                        recipe.getCategory(),
                        recipe.getMaterial(),
                        recipe.getMainIngredients(),
                        recipe.getSupplementary(),
                        recipe.getCookingnote(),
                        recipe.getEnergy(),
                        recipe.getProtein(),
                        recipe.getProteinWeight(),
                        recipe.getFatWeight(),
                        recipe.getChoWeight(),
                        recipe.getNaWeight(),
                        recipe.getCholesterolWeight(),
                        recipe.getPurineWeight(),
                        recipe.getCkdCategory()
                );
            }
            csvPrinter.flush();
        }
    }
}
