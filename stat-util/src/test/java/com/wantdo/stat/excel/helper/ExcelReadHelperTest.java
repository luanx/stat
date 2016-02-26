package com.wantdo.stat.excel.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Date : 2015-9-17
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class ExcelReadHelperTest {

    public static void main(String[] args) throws Exception{

        ArrayList<ArrayList<Object>> title = ExcelReadHelper.readRows("/usr/local/4.xls", 1, 5000);
        for(List<Object> row: title){
            for(Object cell: row){
                System.out.println(cell);
            }
        }

       /* Workbook workbook = ExcelReadHelper.getWorkbook("/usr/local/1688.xls");
        Sheet sheet = workbook.getSheetAt(0);
        boolean isMerged = ExcelReadHelper.isMergedRegion(sheet, 1, 0);
        System.out.println(isMerged);
        Object object = ExcelReadHelper.getMergedRegionValue(sheet, 1, 0);
        System.out.println(object);*/
    }

}
