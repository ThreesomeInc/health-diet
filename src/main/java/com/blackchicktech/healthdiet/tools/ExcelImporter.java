package com.blackchicktech.healthdiet.tools;

import com.blackchicktech.healthdiet.entity.FoodTbl;
import com.blackchicktech.healthdiet.entity.FoodWeight;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ExcelImporter {

    public static void main(String[] args) throws Exception {
        List<FoodTbl> foodTbls = new ArrayList<>();
        List<FoodWeight> foodWeights = new ArrayList<>();

        File file = new File("/Users/quan/Documents/Apps/diet.xlsx");
        XSSFWorkbook workbook = null;
        try {
            System.out.println("Begin to load workbook " + file.getName());
            workbook = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet sheet1 = workbook.getSheetAt(0);
            int totalRows = 2054;  //后面是有格式的脏数据
            System.out.println("Read sheet " + sheet1.getSheetName() + " total " + totalRows + " rows");
            //row 0 是header
            for (int i = 3; i < totalRows; i++) {
                XSSFRow row = sheet1.getRow(i);
                FoodTbl foodTbl = new FoodTbl();
                foodTbl.setFoodCode(readCellAsIntString(row.getCell(0))); //类编码
                foodTbl.setSubCode(readCellAsIntString(row.getCell(2)));
                foodTbl.setSubName(readCellAsString(row.getCell(3)));
                foodTbl.setFoodId(readCellAsString(row.getCell(4)));
                foodTbl.setFoodName(readCellAsString(row.getCell(5)));
                foodTbl.setWater(readCellAsString(row.getCell(7)));
                foodTbl.setEnergy(readCellAsString(row.getCell(8)));
                foodTbl.setProtein(readCellAsString(row.getCell(10)));
                foodTbl.setFat(readCellAsString(row.getCell(12)));
                foodTbl.setCho(readCellAsString(row.getCell(14)));
                foodTbl.setP(readCellAsString(row.getCell(35)));
                foodTbl.setK(readCellAsString(row.getCell(36)));
                foodTbl.setNa(readCellAsString(row.getCell(38)));
                foodTbl.setUnit(readCellAsString(row.getCell(59)));
                foodTbls.add(foodTbl);

                FoodWeight foodWeight = new FoodWeight();
                foodWeight.setFoodId(readCellAsString(row.getCell(4)));
                foodWeight.setFoodCode(readCellAsIntString(row.getCell(0))); //类编码
                foodWeight.setSubCode(readCellAsIntString(row.getCell(2)));
                foodWeight.setProteinWeight(readAsInt(row.getCell(11)));
                foodWeight.setFatWeight(readAsInt(row.getCell(13)));
                foodWeight.setChoWeight(readAsInt(row.getCell(16)));
                foodWeight.setkWeight(readAsInt(row.getCell(37)));
                foodWeight.setNaWeight(readAsInt(row.getCell(39)));
//                foodWeight.setpWeight(readAsInt(row.getCell(58)));
                foodWeight.setCholesterolWeight(readAsInt(row.getCell(19)));
                foodWeight.setPurineWeight(readAsInt(row.getCell(22)));
                foodWeights.add(foodWeight);
            }
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

        saveFoodTblToCsv(foodTbls);
        saveFoodWeightToCsv(foodWeights);
    }

    private static void saveFoodTblToCsv(List<FoodTbl> foodTbls) throws IOException {
        foodTbls.forEach(System.out::println);
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("./food_tbl.csv"));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("food_code", "sub_code", "sub_name", "food_id", "food_name", "water", "energy", "protein", "fat", "cho", "cholesterol", "p", "k", "na", "unit"))
        ) {
            for (FoodTbl foodTbl : foodTbls) {
                csvPrinter.printRecord(foodTbl.getFoodCode(),
                        foodTbl.getSubCode(),
                        foodTbl.getSubName(),
                        foodTbl.getFoodId(),
                        foodTbl.getFoodName(),
                        foodTbl.getWater(),
                        foodTbl.getEnergy(),
                        foodTbl.getProtein(),
                        foodTbl.getFat(),
                        foodTbl.getCho(),
                        foodTbl.getCholesterol(),
                        foodTbl.getP(),
                        foodTbl.getK(),
                        foodTbl.getNa(),
                        foodTbl.getUnit()
                );
            }
            csvPrinter.flush();
        }
    }

    private static void saveFoodWeightToCsv(List<FoodWeight> foodWeights) throws IOException {
        foodWeights.forEach(System.out::println);
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("./food_weight_tbl.csv"));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("food_id", "food_code", "sub_code", "protein_weight", "fat_weight", "cho_weight", "na_weight", "cholesterol_weight", "purine_weight"))
        ) {
            for (FoodWeight foodWeight : foodWeights) {
                csvPrinter.printRecord(foodWeight.getFoodId(),
                        foodWeight.getFoodCode(),
                        foodWeight.getSubCode(),
                        foodWeight.getProteinWeight(),
                        foodWeight.getFatWeight(),
                        foodWeight.getChoWeight(),
//                        foodWeight.getkWeight(),
                        foodWeight.getNaWeight(),
                        foodWeight.getCholesterolWeight(),
                        foodWeight.getPurineWeight()
//                        foodWeight.getpWeight()
                );
            }
            csvPrinter.flush();
        }
    }

    private static String readCellAsString(XSSFCell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellTypeEnum()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return "" + cell.getNumericCellValue();
            case BLANK:
                return null;
            default:
                throw new RuntimeException("Not supported cell type");
        }
    }

    // read number field 1.0 as string 1
    private static String readCellAsIntString(XSSFCell cell) {
        if (cell == null) {
            throw new RuntimeException("Id could not be null");
        }
        switch (cell.getCellTypeEnum()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                throw new RuntimeException("Not supported cell type");
        }
    }

    private static int readAsInt(XSSFCell cell) {
        if (cell == null) {
            throw new RuntimeException("Int could not be null");
        }
        switch (cell.getCellTypeEnum()) {
            case STRING:
                return Integer.parseInt(cell.getStringCellValue());
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            default:
                System.out.println("Not supported cell type " + cell.getCellTypeEnum().name());
                return 9999;
        }
    }

    private static double readCellAsDouble(XSSFCell cell) {
        if (cell == null) {
            return 0.0;
        }
        switch (cell.getCellTypeEnum()) {
            case STRING:
                return parseDataToDouble(cell.getStringCellValue());
            case NUMERIC:
                return cell.getNumericCellValue();
            default:
                return 0.0;
        }
    }

    private static double parseDataToDouble(String rawData) {
        return 0.0;
    }
}
