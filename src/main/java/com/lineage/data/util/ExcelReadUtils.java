package com.lineage.data.util;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YangJunNan
 * @description
 * @date 2021/9/26
 */
public class ExcelReadUtils {
    public static List<List<String>> readExcel(String path) throws Exception {
        InputStream is = new FileInputStream(path);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        List<List<String>> result = new ArrayList<>();
        //循环每一页，并处理当前循环页
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            //处理当前页，循环读取每一行
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);

                int minColIx = xssfRow.getFirstCellNum();
                int maxColIx = xssfRow.getLastCellNum();
                List<String> rowList = new ArrayList<>();
                // 遍历该行获取处理每个cell元素
                for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                    XSSFCell cell = xssfRow.getCell(colIx);
                    if (cell == null) {
                        continue;
                    }
                    cell.setCellType(CellType.STRING);
                    rowList.add(cell.getStringCellValue());
                }

                result.add(rowList);
            }
        }
        return result;
    }
}