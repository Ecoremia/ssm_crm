package com.corely.crm.poi;

import com.corely.crm.common.utils.HSSFUtils;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 将excel文件读取到程序中的测试
 */
public class parseExcelTest {
    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("D:\\1javacode\\资料\\aa模拟接收\\服务器\\ActivityList.xls");
        HSSFWorkbook wb = new HSSFWorkbook(inputStream);
        HSSFSheet sheetAt = wb.getSheetAt(0);
        HSSFRow row = null;
        HSSFCell cell =null;
        //注意：当getlastrow时，是为最后一行，getlastcell时为最后一行+1
        for (int i = 0; i <= sheetAt.getLastRowNum(); i++) {
            row = sheetAt.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                cell = row.getCell(j);
                System.out.print(HSSFUtils.getCellValueStr(cell)+" ");
            }
            System.out.println("");
        }

    }
}
