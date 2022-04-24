package com.sinosoft.fragins.management.service;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReadExcelYsx {
    public static void main(String[] args) throws Exception {
        long t1 = new Date().getTime();
        List list = readExcel("D:\\workspace\\workspaceMyself\\啊啊啊啊.xlsx");
        exportonefile("D:\\workspace\\workspaceMyself\\bbbb.xls", list);
        long t2 = new Date().getTime();

        System.out.println((t2-t1)/1000 + "秒");
    }

    public static List<String> readExcel(String filePath) throws Exception {
        List<String> list = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()){
            throw new Exception("文件不存在!");
        }
        InputStream in = new FileInputStream(file);

        // 读取整个Excel
        XSSFWorkbook sheets = new XSSFWorkbook(in);
        // 获取第一个表单Sheet
        XSSFSheet sheetAt = sheets.getSheetAt(0);

        //默认第一行为标题行，i = 0
        XSSFRow titleRow = sheetAt.getRow(0);
        // 循环获取每一行数据
        for (int i = 0; i < sheetAt.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = sheetAt.getRow(i);
            // 读取每一格内容
            StringBuilder sb = new StringBuilder();
            for (int index = 0; index < row.getPhysicalNumberOfCells(); index++) {
                XSSFCell titleCell = titleRow.getCell(index);
                XSSFCell cell = row.getCell(index);
                if (cell != null){
                    cell.setCellType(CellType.STRING);
                    sb.append(cell);
                    if (index != row.getPhysicalNumberOfCells() - 1){
                        sb.append(",");
                    }
                }
            }
            System.out.println(i + "\t" + sb);
            list.add(sb.toString());
        }
        return list;
    }

    /**
     * @param filePath 生成的文件绝对路径
     * @param list 数据列表，每个数据以逗号“,”隔开
     */
    public static void exportonefile(String filePath, List<String> list) {
        StringBuffer stringBuffer = new StringBuffer("");

        //创建工作薄
        HSSFWorkbook workbook=new HSSFWorkbook();
        //创建sheet
        HSSFSheet sheet=workbook.createSheet();
        //创建第一行row
        HSSFRow header=sheet.createRow(0);
        //创建单元格并插入表头
        HSSFCell cell=null;
//        String[] infos={"序号","考生姓名","考生身份证号","所属单位","考试得分"};
//        for(int i=0;i<infos.length;i++){
//            cell=header.createCell(i);
//            cell.setCellValue(infos[i]);
//        }
        //
        HSSFRow body=null;
        for(int i=0;i<list.size();i++){
            body=sheet.createRow(i);
            String str = list.get(i);
            String[] arr = str.split(",");
            for (int j = 0; j < arr.length; j++) {
                cell=body.createCell(j);
                cell.setCellValue(arr[j]);
            }
            System.out.println("realName" + "userName" + "dw");
        }

        //创建文件
        File file=new File(filePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //文件输出流
        try {
            FileOutputStream stream= FileUtils.openOutputStream(file);
            //写入
            workbook.write(stream);
            //关闭输出流
            stream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}