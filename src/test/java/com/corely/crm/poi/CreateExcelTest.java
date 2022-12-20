package com.corely.crm.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateExcelTest {
    public static void main(String[] args) throws IOException {
        HSSFWorkbook wk = new HSSFWorkbook();
        HSSFCellStyle style = wk.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        HSSFSheet sheet = wk.createSheet("学生列表");
        HSSFRow row0 = sheet.createRow(0);
        HSSFCell cell = row0.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("学号");
        cell = row0.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue("姓名");
        cell = row0.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue("年龄");
        for (int i = 1; i <= 10; i++) {
            HSSFRow row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue("100"+i);
            cell = row.createCell(1);
            cell.setCellValue("Name"+i);
            cell = row.createCell(2);
            cell.setCellValue("10"+i);
        }
        FileOutputStream stream = new FileOutputStream("D:\\1javacode\\经验总结\\studentList.xls");
        wk.write(stream);
        stream.close();
        wk.close();
    }
}
