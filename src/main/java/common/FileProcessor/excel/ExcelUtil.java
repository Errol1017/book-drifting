package common.FileProcessor.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import project.resource.properties.ServerProperties;


import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Errol on 17/6/3.
 */
public class ExcelUtil {

    private static String fileBathPath = ServerProperties.getInstance().getFileBasePath();
    private static final String path = "excel/";

    public static String write2SimpleExcel(List<List<String>> lists, String filename) {
        try {
            if (filename.equals("")) {
                filename = System.currentTimeMillis() + "_" + new Random().nextInt(100);
            }
            filename += ".xlsx";
            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("sheet1");
            short r = 0;
            for (List<String> list: lists) {
                Row row = sheet.createRow(r++);
                short c = 0;
                for (String s: list) {
                    row.createCell(c++).setCellValue(s);
                }
            }
            FileOutputStream file = new FileOutputStream(fileBathPath + path + filename);
            wb.write(file);
            file.close();
            return path + filename;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void print(){
        try {
//            System.out.println(fileBathPath);
//            Workbook wb = new HSSFWorkbook();
////            Workbook wb = new XSSFWorkbook();
//            CreationHelper createHelper = wb.getCreationHelper();
//            Sheet sheet = wb.createSheet("new sheet");
//
//            // Create a row and put some cells in it. Rows are 0 based.
//            Row row = sheet.createRow((short) 0);
//            // Create a cell and put a value in it.
//            Cell cell = row.createCell(0);
//            cell.setCellValue(1);
//
//            // Or do it on one line.
//            row.createCell(1).setCellValue(1.2);
//            row.createCell(2).setCellValue(
//                    createHelper.createRichTextString("This is a string"));
//            row.createCell(3).setCellValue(true);
//
//            // Write the output to a file
//            FileOutputStream fileOut = new FileOutputStream(fileBathPath + "workbook.xls");
//            wb.write(fileOut);
//            fileOut.close();
            Workbook wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet("new sheet");
            Row row = sheet.createRow((short)2);
            row.createCell(0).setCellValue(1.1);
            row.createCell(1).setCellValue(new Date());
            row.createCell(2).setCellValue(Calendar.getInstance());
            row.createCell(3).setCellValue("a string");
            row.createCell(4).setCellValue(true);
            row.createCell(5).setCellType(CellType.ERROR);

            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(fileBathPath + "workbook.xls");
            wb.write(fileOut);
            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
