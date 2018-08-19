package com.blackchicktech.healthdiet.tools;

import com.blackchicktech.healthdiet.entity.FoodTbl;
import com.blackchicktech.healthdiet.entity.FoodWeight;
import com.blackchicktech.healthdiet.entity.Recipe;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.blackchicktech.healthdiet.tools.ExcelImporter.*;

public class RecipeImporter {

    public static void main(String[] args) throws Exception {
        excelImporter();
        //csvImporter();
    }

    private static void excelImporter() throws IOException {

        List<Recipe> recipeList = new ArrayList<>();

        File file = new File("/Users/quan/Documents/Apps/recipe.xlsx");
        XSSFWorkbook workbook = null;
        try {
            System.out.println("Begin to load workbook " + file.getName());
            workbook = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet sheet1 = workbook.getSheetAt(0);
            int totalRows = 53;  //后面是有格式的脏数据
            System.out.println("Read sheet " + sheet1.getSheetName() + " total " + totalRows + " rows");
            //row 0 是header
            for (int i = 3; i < totalRows; i++) {
                XSSFRow row = sheet1.getRow(i);
                Recipe recipe = new Recipe();
                recipe.setRecipeId(readCellAsIntString(row.getCell(0)));
                recipe.setRecipeName(readCellAsString(row.getCell(1)));
                recipe.setCookMethod(readCellAsString(row.getCell(2)));
                recipe.setTaste(readCellAsString(row.getCell(3)));
                recipe.setCuisine(readCellAsString(row.getCell(4)));
                recipe.setAgeGroup(readCellAsString(row.getCell(5)));
                recipe.setDifficulty(readCellAsString(row.getCell(6)));
                recipe.setPrepareTime(readAsInt(row.getCell(7)));
                recipe.setCookingTime(readAsInt(row.getCell(8)));
                recipe.setMealTime(readCellAsString(row.getCell(9)));
                recipe.setCategory(readCellAsString(row.getCell(10)));
                recipe.setMaterial(readCellAsString(row.getCell(11)));
                recipe.setMainIngredients(readCellAsString(row.getCell(12)));
                recipe.setSupplementary(readCellAsString(row.getCell(13)));
                recipe.setEnergy(readCellAsFloat(row.getCell(14)));
                recipe.setProtein(readCellAsFloat(row.getCell(15)));
                recipe.setProteinWeight(readAsInt(row.getCell(16)));
                recipe.setFatWeight(readAsInt(row.getCell(17)));
                recipe.setChoWeight(readAsInt(row.getCell(18)));
                recipe.setCholesterolWeight(readAsInt(row.getCell(19)));
                recipe.setPurineWeight(readAsInt(row.getCell(20)));
                recipe.setkWeight(readAsInt(row.getCell(21)));
                recipe.setNaWeight(readAsInt(row.getCell(22)));
                recipe.setCkdCategory(readCellAsString(row.getCell(24)));
                recipe.setCookingnote(readCellAsString(row.getCell(25)));
                recipeList.add(recipe);
            }

            System.out.println("Totally " + totalRows + "read.");
            saveRecipeTblToCsv(recipeList);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Finished to load workbook " + file.getName());
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    //by pass
                }
            }
        }


    }

    private static void csvImporter() throws IOException {
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
                        .withHeader("recipe_id", "recipe_name", "cook_method", "taste", "cuisine", "age_group", "difficulty", "prepare_time", "cooking_time", "meal_time",
                                "category", "material", "main_ingredients", "supplementary", "cookingnote",
                                "energy", "protein", "protein_weight", "fat_weight", "cho_weight", "na_weight", "cholesterol_weight", "purine_weight", "k_weight", "ckd_category"))
        ) {
            for (Recipe recipe : recipeList) {
                csvPrinter.printRecord(recipe.getRecipeId(),
                        recipe.getRecipeName(),
                        recipe.getCookMethod(),
                        recipe.getTaste(),
                        recipe.getCuisine(),
                        recipe.getAgeGroup(),
                        recipe.getDifficulty(),
                        recipe.getPrepareTime(),
                        recipe.getCookingTime(),
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
                        recipe.getkWeight(),
                        recipe.getCkdCategory()
                );
            }
            csvPrinter.flush();
        }
    }
}
