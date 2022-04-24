package com.sinosoft.fragins.management.common;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DbReport {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

   @Autowired
   private DataSource dataSource;

    public void saveToFile(String fileName) throws Exception {

        List<String[]> tables = getAllTables();
        Map<String,List<String[]>> mapTableSchemas = getMapTableSchemas(tables);

        try {
            Workbook workbook = new XSSFWorkbook();

            //生成表名目录
            Sheet sheet0 = workbook.createSheet("目录");
            Row row0 = sheet0.createRow(0);
            sheet0.setColumnWidth(0, 4000);
            sheet0.setColumnWidth(1, 5000);
            sheet0.createFreezePane(0, 1);
            CellStyle headerStyle = DbReportExcelUtil.createHeaderCellStyle(workbook);
            CellStyle style = DbReportExcelUtil.createDefaultCellStyle(workbook);
            DbReportExcelUtil.setCellValue(row0, 0, "表名", headerStyle);
            DbReportExcelUtil.setCellValue(row0, 1, "说明", headerStyle);
            Map<String,String> tableNameMap = new HashMap();
            for (int i = 0; i < tables.size(); i++) {
                String tableName = tables.get(i)[0];
                if(tableNameMap.containsKey(tableName)){
                    continue;
                }else {
                    tableNameMap.put(tableName,tableName);
                }
                Sheet tableSheet = workbook.createSheet(tableName);
                tableSheet.setColumnWidth(0, 4000);
                tableSheet.setColumnWidth(1, 4000);
                tableSheet.setColumnWidth(2, 5000);

                Row r0 = tableSheet.createRow(0);
                DbReportExcelUtil.setCellValue(r0, 0, "字段", headerStyle);
                DbReportExcelUtil.setCellValue(r0, 1, "字段描述", headerStyle);
                DbReportExcelUtil.setCellValue(r0, 2, "类型", headerStyle);
                DbReportExcelUtil.setCellValue(r0, 3, "是否允许为空", headerStyle);
                DbReportExcelUtil.setCellValue(r0, 4, "默认值", headerStyle);
                DbReportExcelUtil.setCellLink(r0, 7, "返回目录","目录", headerStyle);
                List<String[]> tableSchemas = mapTableSchemas.get(tableName);
                for (int r = 0; r < tableSchemas.size(); r++) {
                    String columnName = tableSchemas.get(r)[0];//字段名
                    String comment = tableSchemas.get(r)[1];//字段描述
                    String dataType = tableSchemas.get(r)[2];//字段类型
                    String isNull = tableSchemas.get(r)[3];//是否允许为空
                    String defaultValue = tableSchemas.get(r)[4];//默认值
                    Row r1 = tableSheet.createRow(r + 1);
                    DbReportExcelUtil.setCellValue(r1, 0, columnName, style);
                    DbReportExcelUtil.setCellValue(r1, 1, comment, style);
                    DbReportExcelUtil.setCellValue(r1, 2, dataType, style);
                    DbReportExcelUtil.setCellValue(r1, 3, isNull, style);
                    DbReportExcelUtil.setCellValue(r1, 4, defaultValue, style);
                }
                System.out.print(" table '"+tableName+"' create sheet ok \n");
            }
            //添加表名目录中的链接
            Map<String,String> tableNameMap2 = new HashMap();

            List<String> tableName_tableComment = new ArrayList<>();
            for (int i = 0; i < tables.size(); i++) {
                String tableName = tables.get(i)[0];
                String tableComment = tables.get(i)[1];
                if(tableNameMap2.containsKey(tableName)){
                    continue;
                }else {
                    tableNameMap2.put(tableName,tableName);
                }
                tableName_tableComment.add(tableName+"---"+tableComment);
            }

            for (int i = 0; i < tableName_tableComment.size(); i++) {
                Row row = sheet0.createRow(i + 1);
                DbReportExcelUtil.setCellValue(row, 0,
                        tableName_tableComment.get(i).toString().split("---")[0], style);
                DbReportExcelUtil.setCellLink(row, 1,
                        tableName_tableComment.get(i).toString().split("---")[1],
                        tableName_tableComment.get(i).toString().split("---")[0],style);
            }

            DbReportExcelUtil.saveBook(workbook, fileName);
        } catch (Exception ex) {

        	logger.error("获取表结构失败：{}",ex.getStackTrace());
        }
    }

    /**
     * 获取所有表的名称。
     *
     * @return
     */
    private List<String[]> getAllTables() throws SQLException {
        List<String[]> tableNames = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = " SELECT A.TABLE_NAME,A.TABLE_COMMENT FROM information_schema.tables A   \n"
                + " WHERE A.TABLE_SCHEMA in('policy_main','claim_main','nexus_main') ORDER BY A.TABLE_NAME,A.TABLE_SCHEMA  ";
        try{
            System.out.println(dataSource);
            System.out.println(dataSource.getClass().getName());
            con = dataSource.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                tableNames.add(new String[]{
                        rs.getString(1),    //表名
                        rs.getString(2)     //表注释
                });
            }
        }catch (Exception e){
            logger.error("获取DataSource失败：{}",e.getStackTrace());
        }finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null){
                con.close();
            }
        }
        return tableNames;
    }

    /**
     * 获取表结构。
     *
     * @return
     */
    private  Map<String,List<String[]>> getMapTableSchemas(List<String[]> tables) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String[]> tableSchemas = null;
        Map<String,List<String[]>> mapTableSchemas = new HashMap<>();
        String sql = " SELECT  A.COLUMN_NAME AS COLUMN_NAMES, \n"
                + " A.COLUMN_COMMENT AS COMMENTS, A.COLUMN_TYPE AS DATA_TYPES,  \n"
                + " A.IS_NULLABLE AS NULLABLESS, \n"
                + " A.COLUMN_DEFAULT AS DEFAULT_VALUES \n"
                + " FROM  information_schema.columns A  \n"
                + " WHERE A.TABLE_NAME = ? ";
        try{
            con = dataSource.getConnection();
            for (int i = 0; i < tables.size(); i++) {
                try{
                    tableSchemas = new ArrayList<>();
                    String strTableName = tables.get(i)[0];
                    ps = con.prepareStatement(sql);
                    ps.setString(1,strTableName);
                    rs = ps.executeQuery();
                    while (rs.next()){
                        tableSchemas.add(new String[]{
                                rs.getString(1),    //字段名
                                rs.getString(2),    //字段描述
                                rs.getString(3),    //字段类型
                                rs.getString(4),    //是否允许为空
                                rs.getString(5),    //默认值
                        });
                    }
                    System.out.print("'"+strTableName +"' column query ok \n");
                    mapTableSchemas.put(strTableName,tableSchemas);
                }catch (Exception e){
                    /*e.printStackTrace();*/
                    logger.error("字段存值失败：{}",e.getStackTrace());
                }finally {
                    if (rs != null) {
                        rs.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                }
            }
        }catch (Exception e){
            /*e.printStackTrace();*/
            logger.error("流程关闭失败：{}",e.getStackTrace());
        }finally {
            if (con != null){
                con.close();
            }
        }


        return mapTableSchemas;
    }

    public void save(String fileName){
        try {
            this.saveToFile(fileName);
        } catch (Exception e) {
            /*e.printStackTrace();*/
            logger.error("文件保存失败：{}",e.getStackTrace());
        }
    }

}
