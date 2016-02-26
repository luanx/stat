package com.wantdo.stat.excel.helper;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @Date : 2015-8-24
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public class ExcelReadHelper {
	
	public static Workbook getWorkbook(String excelFile) throws IOException {
		return getWorkbook(new FileInputStream(excelFile));
	}
	
	public static Workbook getWorkbook(InputStream is) throws IOException {

		Workbook wb = null;

		ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
		byte[] buffer = new byte[512];
		int count = -1;
		while ((count = is.read(buffer)) != -1)
			byteOS.write(buffer, 0, count);
		byteOS.close();
		byte[] allBytes = byteOS.toByteArray();

		try {
			wb = new XSSFWorkbook(new ByteArrayInputStream(allBytes));
		} catch (Exception ex) {
			wb = new HSSFWorkbook(new ByteArrayInputStream(allBytes));
		}

		return wb;
	}
	
	public static ArrayList<ArrayList<Object>> readAllRows(String excelFile) throws IOException {
		return readAllRows(new FileInputStream(excelFile));
	}
	
	public static ArrayList<ArrayList<Object>> readAllRows(InputStream is) throws IOException {
		Workbook wb = getWorkbook(is);
		ArrayList<ArrayList<Object>> rowList = new ArrayList<ArrayList<Object>>();
		
	    for (int i = 0; i < wb.getNumberOfSheets(); i++) {//获取每个Sheet表
	    	Sheet sheet = wb.getSheetAt(i);
	    	rowList.addAll(readRows(sheet));
        }

		return rowList;
	}

	public static ArrayList<ArrayList<Object>> readRows(String excelFile,
			int startRowIndex, int rowCount) throws IOException {
		return readRows(new FileInputStream(excelFile), startRowIndex, rowCount);
	}
	
	public static ArrayList<ArrayList<Object>> readRows(String excelFile) throws IOException {
		return readRows(new FileInputStream(excelFile));
	}

	public static ArrayList<ArrayList<Object>> readRows(InputStream is,
			int startRowIndex, int rowCount) throws IOException {
		Workbook wb = getWorkbook(is);
		Sheet sheet = wb.getSheetAt(0);

		return readRows(sheet, startRowIndex, rowCount);
	}
	
	public static ArrayList<ArrayList<Object>> readRows(InputStream is) throws IOException {
		Workbook wb = getWorkbook(is);
		Sheet sheet = wb.getSheetAt(0);
		return readRows(sheet);
	}

	public static ArrayList<ArrayList<Object>> readRows(Sheet sheet,
			int startRowIndex, int rowCount) {
		ArrayList<ArrayList<Object>> rowList = new ArrayList<ArrayList<Object>>();
		
		for (int i = startRowIndex; i < (startRowIndex + rowCount); i++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				break;
			}
			
			ArrayList<Object> cellList = new ArrayList<Object>();
            int colNum = sheet.getRow(0).getLastCellNum();
            for (int j = 0; j < colNum; j++) {
                Cell cell = row.getCell(j);
				if (isMergedRegion(sheet, i, j)) {
					Object cellValue = getMergedRegionValue(sheet, i, j);
					if (cellValue == null){
						cellValue = new String();
					}
					cellList.add(cellValue);
				} else {
					if (cell == null){
						cellList.add(new String());
					} else {
						cellList.add(readCell(cell));
					}
				}
            }

			rowList.add(cellList);
		}
		
		return rowList;
	}
	
	public static ArrayList<ArrayList<Object>> readRows(Sheet sheet) {
		int rowCount = sheet.getLastRowNum();
		return readRows(sheet, 0, rowCount);
	}

    public static int getRowNum(String excelFile) throws IOException{
        Workbook wb = getWorkbook(new FileInputStream(excelFile));
        Sheet sheet = wb.getSheetAt(0);
        return sheet.getLastRowNum();
    }

    public static int getColNum(String excelFile) throws IOException {
        Workbook wb = getWorkbook(new FileInputStream(excelFile));
        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.getRow(0);
        return row.getLastCellNum();
    }

	/**
	 * 从Excel读Cell
	 * 
	 * @param cell
	 * @return
	 */
	private static Object readCell(Cell cell) {
		if (cell == null) {
			return null;
		}
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			String str = cell.getRichStringCellValue().getString();
			return str == null ? "" : str.trim();

		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			} else {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				return cell.getStringCellValue();
			}

		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();

		case Cell.CELL_TYPE_FORMULA:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			} else {
				return cell.getCellFormula();
			}

		case Cell.CELL_TYPE_BLANK:
			return "";

			case Cell.CELL_TYPE_ERROR:
				return "";

		default:
			System.err.println("Data error for cell of excel: " + cell.getCellType());
			return "";
		}

	}

	public static String getCell(Object object){
		if (object == null || StringUtils.isEmpty(String.valueOf(object))){
			return null;
		}
		return String.valueOf(object);
	}

	/**
	 * 判断指定的单元格是否是合并单元格
	 * @param sheet
	 * @param row 行下标
	 * @param column 列下标
	 * @return
	 */
	public static boolean isMergedRegion(Sheet sheet, int row, int column){
		int sheetMergeCount = sheet.getNumMergedRegions();
		for(int i=0; i<sheetMergeCount; i++){
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row >= firstRow && row <= lastRow){
				if (column >= firstColumn && column <= lastColumn){
					return true;
				}
			}
		}
		return false;
	}


	public static Object getMergedRegionValue(Sheet sheet, int row, int column){
		int sheetMergeCount = sheet.getNumMergedRegions();

		for (int i=0; i<sheetMergeCount; i++){
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();

			if (row >= firstRow && row <= lastRow){
				if (column >= firstColumn && column <= lastColumn){
					Row fRow = sheet.getRow(firstRow);
					Cell fCell = fRow.getCell(firstColumn);
					return readCell(fCell);
				}
			}
		}
		return null;
	}


}
