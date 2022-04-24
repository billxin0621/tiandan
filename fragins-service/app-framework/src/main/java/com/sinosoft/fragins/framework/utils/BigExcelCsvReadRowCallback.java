package com.sinosoft.fragins.framework.utils;

/**
 * 大Excel文件读取每行回调接口
 * 
 * @author panyu
 *
 */
@FunctionalInterface
public interface BigExcelCsvReadRowCallback {

	public void readRow(String[] data, int rowNum);

}
