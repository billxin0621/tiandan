package com.sinosoft.fragins.management.common;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Excel工具类。
 *
 * @author Administrator
 */
public class DbReportExcelUtil {

    public static void saveBook(Workbook book, String fileName) throws Exception {
        try (OutputStream outStream = new FileOutputStream(fileName)) {
            book.write(outStream);
        }
    }

    /**
     * 设置单元样式。
     *
     * @param row
     * @param colIndex
     * @param st
     */
    public static void setCellStyle(Row row, int colIndex, CellStyle st) {
        if (row != null && st != null) {
            Cell cell = row.getCell(colIndex);
            if (cell != null) {
                cell.setCellStyle(st);
            }
        }
    }

    /**
     * 创建单元格样式。
     *
     * @param book
     * @param cloneCell
     * @param color
     * @param border
     * @return
     */
    public static CellStyle createCellStyle(Workbook book, Cell cloneCell, short color, int border) {
        if (book == null) {
            return null;
        }
        CellStyle st = book.createCellStyle();
        return st;
    }

    /**
     * 设置单元背景色。
     *
     * @param row
     * @param colIndex
     * @param color
     * @param border
     */
    public static void setCellStyle(Row row, int colIndex, short color, int border) {
        if (row != null) {
            Cell cell = row.getCell(colIndex);
            if (cell != null) {
                CellStyle st = createCellStyle(row.getSheet().getWorkbook(), cell, color, border);
                cell.setCellStyle(st);
            }
        }
    }

    /**
     * 写入单元值。
     *
     * @param row
     * @param colIndex
     * @param val 单元值。
     * @param style 单元样式。
     */
    public static void setCellValue(Row row, int colIndex, String val, CellStyle style) {
        Cell cell = row.getCell(colIndex);
        if (cell == null) {
            cell = row.createCell(colIndex);
        }
        if (cell != null) {
            if (style != null) {
                setCellStyle(row, colIndex, style);
            }
            //cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(val);

        }
    }

    /**
     * 写入单元链接。
     *
     * @param row
     * @param colIndex
     * @param val 单元值。
     */
    public static void setCellLink(Row row, int colIndex, String val, String targetSheet, CellStyle style) {
        Cell cell = row.getCell(colIndex);
        if (cell == null) {
            cell = row.createCell(colIndex);
        }

        CreationHelper createHelper = row.getSheet().getWorkbook().getCreationHelper();
        if (style != null) {
            CellStyle st = row.getSheet().getWorkbook().createCellStyle();
            st.cloneStyleFrom(style);
            Font cellFont = row.getSheet().getWorkbook().createFont();
            cellFont.setUnderline((byte) 1);
            cellFont.setColor(IndexedColors.BLUE.index);
            st.setFont(cellFont);
            setCellStyle(row, colIndex, st);
        }

        Hyperlink hyperlink = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
        hyperlink.setAddress("#" + targetSheet + "!A1");
        cell.setHyperlink(hyperlink);

        //cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue(val);
    }

    /**
     * 创建黑色细边框。
     *
     * @param workbook
     * @return
     */
    public static CellStyle createDefaultCellStyle(Workbook workbook) {
        return createDefaultCellStyle(workbook, (short) 0);
    }

    /**
     * 创建黑色细边框。
     *
     * @param workbook
     * @return
     */
    public static CellStyle createHeaderCellStyle(Workbook workbook) {
        return createDefaultCellStyle(workbook, IndexedColors.YELLOW.index);
    }

    /**
     * 创建黑色细边框。
     *
     * @param workbook
     * @param backColor 背景色
     * @return
     */
    public static CellStyle createDefaultCellStyle(Workbook workbook, short backColor) {
        CellStyle st = workbook.createCellStyle();
        return st;
    }
}
