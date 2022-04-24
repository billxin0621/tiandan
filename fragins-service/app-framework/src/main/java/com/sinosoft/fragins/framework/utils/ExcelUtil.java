package com.sinosoft.fragins.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

@Slf4j
public class ExcelUtil<T> {



    private XSSFWorkbook workBook;

    /**
     * 表格列头单元格样式
     */
    private XSSFCellStyle headCellStyle = null;

    /**
     * 字符单元格样式
     */
    private XSSFCellStyle stringCellStyle = null;

    /**
     * 字符格式
     */
    private String stringFormat = "@";

    private String sheetName;

    private String[] headArr;

    private String[] colArr;

    private String[] colArrSelf;

    private List<T> dataResource;

    private String path;// 模板资源路径

    private int startRow = 0;

    private Map<String, Map<String, ?>> putCommCode = new HashMap<String, Map<String, ?>>();

    private Map<String, Object> param = new HashMap<>();//excel_table_tail指表尾

    public ExcelUtil() {
    }

    public ExcelUtil(String sheetName) {
        this.sheetName = sheetName;
        workBook = new XSSFWorkbook();
    }

    public ExcelUtil(String path, String sheetName) {
        this.sheetName = sheetName;
        this.path = path;
        log.info("path = {}, sheetName = {} ", path,sheetName);
        if(StringUtils.isBlank(path) ){
            log.info("ExcelUtil path is null");
        }else{
            InputStream inputstream = null;
            try {
                if(null != this.getClass() && null != this.getClass().getClassLoader()){
                    inputstream = this.getClass().getClassLoader().getResourceAsStream(path);
                    if(null!= inputstream){
                        workBook = new XSSFWorkbook(inputstream);
                        workBook.setSheetName(0, sheetName);
                    } else {
                        log.info("path = {}, sheetName = {} , inputstream is null", path,sheetName);
                    }
                }
            } catch (IOException e) {
                log.info(e.getLocalizedMessage());
            } finally {
                if(null != inputstream){
                    try {
                        inputstream.close();
                    } catch (IOException e) {
                        log.info(e.getLocalizedMessage());
                    }
                }
            }
        }
    }

    public XSSFSheet createSheet() {
        return workBook.createSheet(sheetName);
    }

    /**
     * 创建头
     *
     * @param sheet
     */
    public void createHead(Sheet sheet) {
        XSSFRow row =(XSSFRow) sheet.createRow(startRow++);
        for (int i = 0; i < headArr.length; i++) {
            XSSFCell headCell = row.createCell(i);
            XSSFRichTextString head = new XSSFRichTextString(headArr[i]);
            headCell.setCellValue(head);
            headCell.setCellStyle(getHeadCellStyle());
        }
    }

    public void createBody(XSSFSheet sheet) {

        for (int i = 0; i < dataResource.size(); i++) {
            XSSFRow row = sheet.createRow(startRow++);
            T dto = dataResource.get(i);
            for (int j = 0; j < colArr.length; j++) {
                XSSFCell cell = row.createCell(j);
                cell.setCellStyle(getStringCellStyle());
                cell.setCellType(CellType.STRING);
                try {
                    String v = BeanUtils.getProperty(dto, colArr[j]);
                    cell.setCellValue(changeValue(colArr[j], v));
                } catch (Exception e) {
                    log.info("获取列值异常："+ e);
                    cell.setCellValue("");
                }
            }

        }

    }

    /**
     * 设置单个单元格的值，
     * key=“1,2”,value="name":表示设置1和2为单元格坐标，map.get(name)=单元格的值
     * @guoquanjun
     * @date 2017-12-19
     * @param sheet
     * @param map
     */
    private void setValueByCell(XSSFSheet sheet, Map<String, Object> map) {
        for (Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            if(StringUtils.isNotBlank(key)&&key.contains(",")){
                String[] keys = key.split(",");
                String a=keys[0];
                String b=keys[1];
                if(StringUtils.isNumeric(a)&&StringUtils.isNumeric(b)){
                    String value = (String) entry.getValue();
                    String valueKey = (String) map.get(value);
                    XSSFCell cell = sheet.getRow(Integer.parseInt(a)).getCell(Integer.parseInt(b));
                    if(Objects.isNull(cell)){
                        cell=sheet.getRow(Integer.parseInt(a)).createCell(Integer.parseInt(b));
                    }
                    cell.setCellValue(valueKey);
                }
            }
        }
    }

    public void doColArr() {
        colArrSelf = new String[colArr.length];
        for (int i = 0; i < colArr.length; i++) {
            String col = colArr[i];
            colArrSelf[i] = String.valueOf(col.charAt(0)).toUpperCase() + col.substring(1);
        }

    }

    public String changeValue(String k, String v) {
        if (putCommCode.containsKey(k)) {
            Map<String, ?> map = putCommCode.get(k);
            Object obj = map.get(v);
            if (obj == null) {
                return "";
            }
            return String.valueOf(obj);
        } else {
            return v;
        }
    }

    /**
     * 列头的单元格格式
     *
     * @return headCellStyle 列头的单元格格式
     */
    public XSSFCellStyle getHeadCellStyle() {
        if (headCellStyle != null) {
            return headCellStyle;
        }
        headCellStyle = workBook.createCellStyle();
        // 列头加粗
        XSSFFont headFont = workBook.createFont();
        headCellStyle.setFont(headFont);
        return headCellStyle;
    }

    /**
     * 字符单元格格式
     *
     * @return stringCellStyle 字符单元格格式
     */
    public XSSFCellStyle getStringCellStyle() {
        if (stringCellStyle != null) {
            return stringCellStyle;
        }
        stringCellStyle = workBook.createCellStyle();
        XSSFDataFormat format = workBook.createDataFormat();
        stringCellStyle.setDataFormat(format.getFormat(stringFormat));
        return stringCellStyle;
    }

    private void equipWorkbook() {
        XSSFSheet sheet = null;
        if (!Objects.isNull(path)) {
            sheet = workBook.getSheetAt(0);
//            startRow = sheet.getLastRowNum() + 1;
            startRow = sheet.getLastRowNum() ;
        } else {
            sheet = this.createSheet();
            createHead(sheet);
        }
        createBody(sheet);
        createTail(sheet);
    }

    private void createTail(XSSFSheet sheet) {
        if(!Objects.isNull(param)&&param.size()>0){
            this.setValueByCell(sheet, param);
            if(param.containsKey("excel_table_tail")){
                int lastRow = sheet.getLastRowNum();
                int tailRow = lastRow+2;
                CellRangeAddress cellRangeAddress = new CellRangeAddress(tailRow,tailRow,0,sheet.getRow(lastRow).getPhysicalNumberOfCells()-1);
                sheet.addMergedRegion(cellRangeAddress);
                XSSFRow row = sheet.getRow(tailRow);
                if(Objects.isNull(row)){
                    XSSFCell cell = sheet.createRow(tailRow).createCell(0);
                    cell.setCellValue((String) param.get("excel_table_tail"));
                }
            }
        }
    }

    /**
     * 导出成为Excel
     *
     * @param outputFileName
     *            输出文件名
     * @throws Exception
     *             异常
     * @param response
     *            响应
     */
    public void exportExcel(String outputFileName, HttpServletResponse response) throws Exception {
        if( null == response || StringUtils.isBlank(outputFileName)){
            return;
        }
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(outputFileName, "UTF-8"));
            doExport(response.getOutputStream());
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/html; charset=UTF-8");
            String s ="wrong";
            response.getOutputStream().print(s);
            log.error("文件{} 导出失败：",outputFileName,e);
        } finally {
            try{
                response.flushBuffer();
            }catch (Exception e){

            }
        }
    }

    /**
     * 准备Excel并导出
     *
     * @param outputStream
     *            输出流
     * @throws Exception
     *             异常
     */
    public void doExport(OutputStream outputStream) throws Exception {
        getWorkBook().write(outputStream);
    }

    /**
     * 获取工作簿
     *
     * @return workBook 工作簿
     */
    public XSSFWorkbook getWorkBook() {
        if (workBook != null) {
            equipWorkbook();
        }
        return workBook;
    }

    public String[] getHeadArr() {
        return headArr;
    }

    public void setHeadArr(String[] headArr) {
        this.headArr = headArr;
    }

    public List<?> getDataResource() {
        return dataResource;
    }

    public void setDataResource(List<T> dataResource) {
        this.dataResource = dataResource;
    }

    public String[] getColArr() {
        return colArr;
    }

    public void setColArr(String[] colArr) {
        this.colArr = colArr;
    }

    public void setPutCommCode(String key, Map<String, Map<String, ?>> putCommCode) {
        this.putCommCode = putCommCode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

}
