package com.sinosoft.fragins.framework.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.monitorjbl.xlsx.StreamingReader;

/**
 * 大Excel文件流式读取工具类
 *
 * @author panyu
 */
public class BigExcelCsvReadUtils {

    /**
     * 读取Excel文件
     *
     * @param in       文件流
     * @param sheetNum 读取第几个sheet，从0开始
     * @param callback 回调接口
     */
    public static void readExcel(InputStream in, int sheetNum, BigExcelCsvReadRowCallback callback) {
        Workbook workbook = StreamingReader.builder().rowCacheSize(10).bufferSize(4096).open(in);
        Sheet sheet = workbook.getSheetAt(sheetNum);
        readExcelSheet(sheet, callback);
    }

    /**
     * 读取Excel文件
     *
     * @param in        文件流
     * @param sheetName sheet名称
     * @param callback  回调接口
     */
    public static void readExcel(InputStream in, String sheetName, BigExcelCsvReadRowCallback callback) {
        Workbook workbook = StreamingReader.builder().rowCacheSize(10).bufferSize(4096).open(in);
        Sheet sheet = workbook.getSheet(sheetName);
        readExcelSheet(sheet, callback);
    }

    private static void readExcelSheet(Sheet sheet, BigExcelCsvReadRowCallback callback) {
        int rowNum = 0;
        for (Row r : sheet) {
            if (r.getLastCellNum() <= 0) {
                continue;
            }
            String[] data = new String[r.getLastCellNum()];
            int colNum = 0;
            for (int i = 0; i < r.getLastCellNum(); i++) {
                Cell c = r.getCell(i);
                if (c == null) {
                    data[colNum++] = "";
                } else {
                    String value = null;
                    if (c.getCellType() == CellType.NUMERIC) {
                        value = "" + c.getNumericCellValue();
                    } else {
                        try {
                            value = c.getStringCellValue();
                        } catch (Exception e) {
                            // ignore cell read error
                        }
                    }
                    data[colNum++] = value;
                }
            }
            callback.readRow(data, rowNum);
            rowNum++;
        }
    }

    /**
     * 读取CSV文件
     *
     * @param in       文件流
     * @param encoding 文件中文编码
     * @param splitter 分隔符
     * @param callback 回调接口
     */
    public static void readCsv(InputStream in, String encoding, String splitter, BigExcelCsvReadRowCallback callback) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, encoding))) {
            String line = reader.readLine();
            int rowNum = 0;
            while (line != null) {
                if (StringUtils.isNotBlank(line)) {
                    String[] data = line.split(splitter);
                    callback.readRow(data, rowNum);
                }
                line = reader.readLine();
                rowNum++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
