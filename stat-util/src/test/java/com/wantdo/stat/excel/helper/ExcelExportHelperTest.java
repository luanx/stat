package com.wantdo.stat.excel.helper;

import com.google.common.collect.Lists;
import com.wantdo.stat.excel.helper.ExcelExportHelper.ExcelType;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;

import java.util.List;

/**
 * @Date : 2015-9-17
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public class ExcelExportHelperTest {

    @Test
    public void test2003(){
        ExcelExportHelper excelExportHelper = new ExcelExportHelper();

        excelExportHelper.createRow();
        excelExportHelper.setCellHeader(0, "test header");

        excelExportHelper.createRow();
        excelExportHelper.setCell(0, "test content");

        excelExportHelper.export("d:/test2003.xls");
    }

    @Test
    public void test2007(){
        ExcelExportHelper excelExportHelper = new ExcelExportHelper(ExcelType.XLSX);

        excelExportHelper.createRow();
        excelExportHelper.setCellHeader(0, "test header");

        excelExportHelper.createRow();
        excelExportHelper.setCell(0, "test content");

        excelExportHelper.export("d:/test2007.xlsx");
    }

    @Test
    public void testMultiExcel() {

        List<String> strList = Lists.newArrayList();
        for(int i=0; i<10; i++){
            strList.add(String.valueOf(i));
        }

        ExcelExportHelper excelExportHelper = new ExcelExportHelper(strList);
        List<Sheet> sheets = excelExportHelper.getSheets();

        for(Sheet sheet: sheets){
            excelExportHelper.createRow(sheet);
            excelExportHelper.setCellHeader(0, "test");

            excelExportHelper.createRow(sheet);
            excelExportHelper.setCell(0, "content");
        }

        excelExportHelper.export("/usr/local/test2003.xls");

    }

}
