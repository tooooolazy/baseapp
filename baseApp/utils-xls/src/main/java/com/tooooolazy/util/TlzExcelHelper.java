package com.tooooolazy.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TlzExcelHelper {

	public static Workbook open(String filename) throws IOException {
		File _wb = new File(filename);
		return open(_wb);
	}
	/**
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Workbook open(File file) throws FileNotFoundException, IOException {
		Workbook wb = null;
		InputStream in = TLZUtils.getInputStream(file);

		if (file.getName().endsWith("xls")) {
			wb = new HSSFWorkbook(in);
		} else 
		if (file.getName().endsWith("xlsx")) {
			wb = new XSSFWorkbook(in);
		}

		in.close();

		return wb;
	}
	public static String getStringCellValue(Cell cell) {
		String value = "";
		if (cell != null) {
			try {
				value = cell.getStringCellValue();
			} catch (Exception e) {
				value = String.valueOf( cell.getNumericCellValue() );
			}
			if (value != null) {
				value = value.trim();
			} else
				value = "";
		}
		return value;
	}
	public static int getIntegerCellValue(Cell cell) {
		int value = 0;
		if (cell != null) {
			try {
				value = (int) cell.getNumericCellValue();
			} catch (Exception e) {
				try {
					String s = cell.getStringCellValue();
					value = Integer.parseInt(s);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		return value;
	}
}
