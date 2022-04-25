package com.sinosoft.fragins.management.service;

import com.alibaba.fastjson.JSON;
import com.sinosoft.fragins.management.common.ApiResponse;
import com.sinosoft.fragins.management.dao.BranchDataDao;
import com.sinosoft.fragins.management.dao.BranchDataResultDao;
import com.sinosoft.fragins.management.dao.SaleDataDao;
import com.sinosoft.fragins.management.dto.dataClear.DataClearDTO;
import com.sinosoft.fragins.management.dto.dataClear.DataClearQueryCondition;
import com.sinosoft.fragins.management.dto.dataClear.DataClearUploadFile;
import com.sinosoft.fragins.management.po.BranchData;
import com.sinosoft.fragins.management.po.BranchDataResult;
import com.sinosoft.fragins.management.po.SaleData;
import com.sinosoft.fragins.management.vo.DataClear.DataClear;
import com.sinosoft.fragins.management.vo.DataClear.DataClearVo;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class DataClearService {

    private final static Logger logger = LoggerFactory.getLogger(DataClearService.class);

    @Autowired
    private BranchDataDao branchDataDao;
    @Autowired
    private BranchDataResultDao branchDataResultDao;
    @Autowired
    private SaleDataDao saleDataDao;

    DecimalFormat decimalFormat = new DecimalFormat("###,###.##%");
    LocalDate localDate = LocalDate.now();
    LocalDate localDateLastYear = localDate.minusYears(1);
    LocalDate localDateLastMonth = localDate.minusMonths(1);
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
    DateTimeFormatter dateTimeFormatterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String date = localDate.format(dateTimeFormatter);//今年今月
    String dateDay = localDate.format(dateTimeFormatterDay);//今年今月今日
    String dateLastYear = localDateLastYear.format(dateTimeFormatter);//去年今月
    String dateLastMonth = localDateLastMonth.format(dateTimeFormatter);//今年上月

    public String excute() throws Exception{
        // 1.读取部门excel信息
        List<BranchData> branchDataList = this.readBranchExcel();
        for (int i = 0; i < branchDataList.size(); i++) {
            branchDataDao.insertSelective(branchDataList.get(i));
        }
        // 2.读取采销excel信息
        List<SaleData> saleDataList = this.readSaleExcel();
        for (int i = 0; i < saleDataList.size(); i++) {
            saleDataDao.insertSelective(saleDataList.get(i));
        }

        // 3.跨境母婴自营组2022-04 数据
        String branch = "跨境母婴自营组";
        this.caculateBranch(branch, date);
        // 4.跨境母婴自营组2022-03 数据
        branch = "跨境母婴自营组";
        this.caculateBranch(branch, dateLastMonth);
        // 5.跨境母婴自营组2021-04 数据
        branch = "跨境母婴自营组";
        this.caculateBranch(branch, dateLastYear);
        // 6.跨境母婴POP组2022-04 数据
        branch = "跨境母婴POP组";
        this.caculateBranch(branch, date);
        // 7.跨境母婴POP组2022-03 数据
        branch = "跨境母婴POP组";
        this.caculateBranch(branch, dateLastMonth);
        // 8.跨境母婴POP组2021-04 数据
        branch = "跨境母婴POP组";
        this.caculateBranch(branch, dateLastYear);

        List<SaleData> saleDataGroup = saleDataDao.selectGroupBySaleNameAndDate();
        for (int i = 0; i < saleDataGroup.size(); i++) {
            this.caculateSale(saleDataGroup.get(i).getSaleName(), saleDataGroup.get(i).getDealDate());
        }

        // 9.跨境母婴自营组 总用户 数据
        branch = "跨境母婴自营组";
        String dataType = "总用户";
        this.caculateBranchResult(branch, dataType);
        // 10.跨境母婴自营组 老用户 数据
        branch = "跨境母婴自营组";
        dataType = "老用户";
        this.caculateBranchResult(branch, dataType);
        // 11.跨境母婴自营组 站内新 数据
        branch = "跨境母婴自营组";
        dataType = "站内新";
        this.caculateBranchResult(branch, dataType);
        // 12.跨境母婴自营组 站外新 数据
        branch = "跨境母婴自营组";
        dataType = "站外新";
        this.caculateBranchResult(branch, dataType);

        // 13.跨境母婴自营组 总用户 结果
        branch = "跨境母婴自营组";
        dataType = "总用户";
        List<String> listExcelTotal = this.excelResult(branch, dataType);
        // 14.跨境母婴自营组 老用户 结果
        branch = "跨境母婴自营组";
        dataType = "老用户";
        List<String> listExcelOld = this.excelResult(branch, dataType);
        // 15.跨境母婴自营组 站外新 结果
        branch = "跨境母婴自营组";
        dataType = "站外新";
        List<String> listExcelNewWai = this.excelResult(branch, dataType);
        // 16.跨境母婴自营组 站内新 结果
        branch = "跨境母婴自营组";
        dataType = "站内新";
        List<String> listExcelNewNei = this.excelResult(branch, dataType);
        List<String> listExcel = new ArrayList<>();
        listExcel.addAll(listExcelTotal);
        listExcel.addAll(listExcelOld);
        listExcel.addAll(listExcelNewWai);
        listExcel.addAll(listExcelNewNei);
        ReadExcelYsx.exportonefile("D:\\workspace\\workspaceMyself\\部门result.xls", listExcel);

        // 5.删除数据
        branchDataDao.deleteAll();
        branchDataResultDao.deleteAll();
//        saleDataDao.deleteAll();
        return "true";

    }

    public List<String> excelResult(String branch, String dataWeidu){
        List<BranchDataResult> branchDataResultList = branchDataResultDao.selectByBranchNameAnddataWeidu(branch, dataWeidu);
        List<String> listExcel = new ArrayList<>();
        String str = date + "-01 - " + dateDay;
        str = str + "," + dataWeidu;
        listExcel.add(str);
        String strTitle = branch + "," + "用户数" + "," + "成交金额" + "," + "订单" + "," + "ARPU" + "," + "购买频次" + "," + "客单价";
        listExcel.add(strTitle);
        for (int i = 0; i < branchDataResultList.size(); i++) {
            String strResult = "";
            strResult = strResult + branchDataResultList.get(i).getDataType() + ",";
            if (i < 2){
                strResult = strResult + branchDataResultList.get(i).getDealPin().setScale(2, BigDecimal.ROUND_HALF_UP) + ",";
                strResult = strResult + branchDataResultList.get(i).getDealAmt().setScale(2, BigDecimal.ROUND_HALF_UP) + ",";
                strResult = strResult + branchDataResultList.get(i).getDealParentId().setScale(2, BigDecimal.ROUND_HALF_UP) + ",";
                strResult = strResult + branchDataResultList.get(i).getArpu().setScale(2, BigDecimal.ROUND_HALF_UP) + ",";
                strResult = strResult + branchDataResultList.get(i).getPinCount().setScale(2, BigDecimal.ROUND_HALF_UP) + ",";
                strResult = strResult + branchDataResultList.get(i).getParentIdAmt().setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            if (i >= 2){
                strResult = strResult + decimalFormat.format(branchDataResultList.get(i).getDealPin()) + ",";
                strResult = strResult + decimalFormat.format(branchDataResultList.get(i).getDealAmt()) + ",";
                strResult = strResult + decimalFormat.format(branchDataResultList.get(i).getDealParentId()) + ",";
                strResult = strResult + decimalFormat.format(branchDataResultList.get(i).getArpu()) + ",";
                strResult = strResult + decimalFormat.format(branchDataResultList.get(i).getPinCount()) + ",";
                strResult = strResult + decimalFormat.format(branchDataResultList.get(i).getParentIdAmt());
            }
            listExcel.add(strResult);
        }
        return listExcel;
    }

    public void caculateBranch(String branch, String date){
        List<BranchData> scendList = branchDataDao.selectByBranchNameAndDate(branch, date);
        BigDecimal dealPinCount = new BigDecimal("0");
        BigDecimal dealParentIdCount = new BigDecimal("0");
        BigDecimal dealAmtCount = new BigDecimal("0");
        for (int i = 0; i < scendList.size(); i++) {
            BranchData data = scendList.get(i);
            scendList.get(i).setArpuCount(data.getDealAmt().divide(data.getDealPin(),2, BigDecimal.ROUND_HALF_UP));
            scendList.get(i).setPinCount(data.getDealParentId().divide(data.getDealPin(),2, BigDecimal.ROUND_HALF_UP));
            scendList.get(i).setParentIdAmt(data.getDealAmt().divide(data.getDealParentId(),2, BigDecimal.ROUND_HALF_UP));
            dealPinCount = dealPinCount.add(data.getDealPin());
            dealParentIdCount = dealPinCount.add(data.getDealParentId());
            dealAmtCount = dealPinCount.add(data.getDealAmt());
            branchDataDao.updateByPrimaryKey(scendList.get(i));
        }
        BranchData data = new BranchData();
        data.setDealDate(date);
        data.setBranchName(branch);
        data.setDataType("总用户");
        data.setDealEarth("0");
        data.setDealPin(dealPinCount);
        data.setDealParentId(dealParentIdCount);
        data.setDealAmt(dealAmtCount);
        data.setArpuCount(data.getDealAmt().divide(data.getDealPin(),2, BigDecimal.ROUND_HALF_UP));
        data.setPinCount(data.getDealParentId().divide(data.getDealPin(),2, BigDecimal.ROUND_HALF_UP));
        data.setParentIdAmt(data.getDealAmt().divide(data.getDealParentId(),2, BigDecimal.ROUND_HALF_UP));
        branchDataDao.insertSelective(data);
    }

    public void caculateSale(String sale, String dateLocal){
        List<SaleData> scendList = saleDataDao.selectBySaleNameAndDate(sale, dateLocal);
        BigDecimal dealPinCount = new BigDecimal("0");
        BigDecimal dealParentIdCount = new BigDecimal("0");
        BigDecimal dealAmtCount = new BigDecimal("0");
        BigDecimal dealSumCount = new BigDecimal("0");
        for (int i = 0; i < scendList.size(); i++) {
            SaleData data = scendList.get(i);
            scendList.get(i).setArpuCount(data.getDealAmt().divide(data.getDealPin(),2, BigDecimal.ROUND_HALF_UP));
            scendList.get(i).setPinCount(data.getDealParentId().divide(data.getDealPin(),2, BigDecimal.ROUND_HALF_UP));
            scendList.get(i).setParentIdAmt(data.getDealAmt().divide(data.getDealParentId(),2, BigDecimal.ROUND_HALF_UP));
            dealPinCount = dealPinCount.add(data.getDealPin());
            dealParentIdCount = dealPinCount.add(data.getDealParentId());
            dealAmtCount = dealPinCount.add(data.getDealAmt());
            dealSumCount = dealSumCount.add(data.getDealSum());
            saleDataDao.updateByPrimaryKey(scendList.get(i));
        }
        SaleData data = new SaleData();
        data.setDealDate(dateLocal);
        data.setSaleName(sale);
        data.setDataType("总用户");
        data.setDealEarth("0");
        data.setDealPin(dealPinCount);
        data.setDealParentId(dealParentIdCount);
        data.setDealAmt(dealAmtCount);
        data.setDealSum(dealSumCount);
        data.setArpuCount(data.getDealAmt().divide(data.getDealPin(),2, BigDecimal.ROUND_HALF_UP));
        data.setPinCount(data.getDealParentId().divide(data.getDealPin(),2, BigDecimal.ROUND_HALF_UP));
        data.setParentIdAmt(data.getDealAmt().divide(data.getDealParentId(),2, BigDecimal.ROUND_HALF_UP));
        saleDataDao.insertSelective(data);
    }


    public void caculateBranchResult(String branch, String dataType) throws Exception {
//        String dataType = "总用户";
//        String date = "2022-04";
        LocalDate localDate = LocalDate.now();
        LocalDate localDateLastYear = localDate.minusYears(1);
        LocalDate localDateLastMonth = localDate.minusMonths(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String date = localDate.format(dateTimeFormatter);//今年今月
        String dateLastYear = localDateLastYear.format(dateTimeFormatter);//去年今月
        String dateLastMonth = localDateLastMonth.format(dateTimeFormatter);//今年上月
        List<BranchData> scendList = branchDataDao.selectByBranchNameAndDateAnddataType(branch, date, dataType);
        if (scendList == null || scendList.size() != 1){
            throw new Exception("总用户数据不对");
        }
        List<BranchData> scendListLastYear = branchDataDao.selectByBranchNameAndDateAnddataType(branch, dateLastYear, dataType);
        if (scendList == null || scendListLastYear.size() != 1){
            throw new Exception("总用户数据不对");
        }
        List<BranchData> scendListLastMonth = branchDataDao.selectByBranchNameAndDateAnddataType(branch, dateLastMonth, dataType);
        if (scendList == null || scendListLastMonth.size() != 1){
            throw new Exception("总用户数据不对");
        }
        BranchDataResult resultLastYear = new BranchDataResult();
        resultLastYear.setBranchName(branch);
        resultLastYear.setDataWeidu(dataType);
        resultLastYear.setDataType("21年");
        resultLastYear.setDealPin(scendListLastYear.get(0).getDealPin());
        resultLastYear.setDealAmt(scendListLastYear.get(0).getDealAmt());
        resultLastYear.setDealParentId(scendListLastYear.get(0).getDealParentId());
        resultLastYear.setArpu(scendListLastYear.get(0).getArpuCount());
        resultLastYear.setPinCount(scendListLastYear.get(0).getPinCount());
        resultLastYear.setParentIdAmt(scendListLastYear.get(0).getParentIdAmt());
        branchDataResultDao.insertSelective(resultLastYear);

        BranchDataResult result = new BranchDataResult();
        result.setBranchName(branch);
        result.setDataWeidu(dataType);
        result.setDataType("22年");
        result.setDealPin(scendList.get(0).getDealPin());
        result.setDealAmt(scendList.get(0).getDealAmt());
        result.setDealParentId(scendList.get(0).getDealParentId());
        result.setArpu(scendList.get(0).getArpuCount());
        result.setPinCount(scendList.get(0).getPinCount());
        result.setParentIdAmt(scendList.get(0).getParentIdAmt());
        branchDataResultDao.insertSelective(result);

        BranchDataResult resultLastYearDevide = new BranchDataResult();
        resultLastYearDevide.setBranchName(branch);
        resultLastYearDevide.setDataWeidu(dataType);
        resultLastYearDevide.setDataType("同比");
        resultLastYearDevide.setDealPin(scendList.get(0).getDealPin().divide(scendListLastYear.get(0).getDealPin(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastYearDevide.setDealAmt(scendList.get(0).getDealAmt().divide(scendListLastYear.get(0).getDealAmt(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastYearDevide.setDealParentId(scendList.get(0).getDealParentId().divide(scendListLastYear.get(0).getDealParentId(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastYearDevide.setArpu(scendList.get(0).getArpuCount().divide(scendListLastYear.get(0).getArpuCount(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastYearDevide.setPinCount(scendList.get(0).getPinCount().divide(scendListLastYear.get(0).getPinCount(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastYearDevide.setParentIdAmt(scendList.get(0).getParentIdAmt().divide(scendListLastYear.get(0).getParentIdAmt(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        branchDataResultDao.insertSelective(resultLastYearDevide);

        BranchDataResult resultLastMonthDevide = new BranchDataResult();
        resultLastMonthDevide.setBranchName(branch);
        resultLastMonthDevide.setDataWeidu(dataType);
        resultLastMonthDevide.setDataType("环比");
        resultLastMonthDevide.setDealPin(scendList.get(0).getDealPin().divide(scendListLastMonth.get(0).getDealPin(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastMonthDevide.setDealAmt(scendList.get(0).getDealAmt().divide(scendListLastMonth.get(0).getDealAmt(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastMonthDevide.setDealParentId(scendList.get(0).getDealParentId().divide(scendListLastMonth.get(0).getDealParentId(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastMonthDevide.setArpu(scendList.get(0).getArpuCount().divide(scendListLastMonth.get(0).getArpuCount(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastMonthDevide.setPinCount(scendList.get(0).getPinCount().divide(scendListLastMonth.get(0).getPinCount(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastMonthDevide.setParentIdAmt(scendList.get(0).getParentIdAmt().divide(scendListLastMonth.get(0).getParentIdAmt(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        branchDataResultDao.insertSelective(resultLastMonthDevide);

    }

    public void caculateResult(String branch, String dataType) throws Exception {
//        String dataType = "总用户";
//        String date = "2022-04";
        LocalDate localDate = LocalDate.now();
        LocalDate localDateLastYear = localDate.minusYears(1);
        LocalDate localDateLastMonth = localDate.minusMonths(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String date = localDate.format(dateTimeFormatter);//今年今月
        String dateLastYear = localDateLastYear.format(dateTimeFormatter);//去年今月
        String dateLastMonth = localDateLastMonth.format(dateTimeFormatter);//今年上月
        List<BranchData> scendList = branchDataDao.selectByBranchNameAndDateAnddataType(branch, date, dataType);
        if (scendList == null || scendList.size() != 1){
            throw new Exception("总用户数据不对");
        }
        List<BranchData> scendListLastYear = branchDataDao.selectByBranchNameAndDateAnddataType(branch, dateLastYear, dataType);
        if (scendList == null || scendListLastYear.size() != 1){
            throw new Exception("总用户数据不对");
        }
        List<BranchData> scendListLastMonth = branchDataDao.selectByBranchNameAndDateAnddataType(branch, dateLastMonth, dataType);
        if (scendList == null || scendListLastMonth.size() != 1){
            throw new Exception("总用户数据不对");
        }
        BranchDataResult resultLastYear = new BranchDataResult();
        resultLastYear.setBranchName(branch);
        resultLastYear.setDataWeidu(dataType);
        resultLastYear.setDataType("21年");
        resultLastYear.setDealPin(scendListLastYear.get(0).getDealPin());
        resultLastYear.setDealAmt(scendListLastYear.get(0).getDealAmt());
        resultLastYear.setDealParentId(scendListLastYear.get(0).getDealParentId());
        resultLastYear.setArpu(scendListLastYear.get(0).getArpuCount());
        resultLastYear.setPinCount(scendListLastYear.get(0).getPinCount());
        resultLastYear.setParentIdAmt(scendListLastYear.get(0).getParentIdAmt());
        branchDataResultDao.insertSelective(resultLastYear);

        BranchDataResult result = new BranchDataResult();
        result.setBranchName(branch);
        result.setDataWeidu(dataType);
        result.setDataType("22年");
        result.setDealPin(scendList.get(0).getDealPin());
        result.setDealAmt(scendList.get(0).getDealAmt());
        result.setDealParentId(scendList.get(0).getDealParentId());
        result.setArpu(scendList.get(0).getArpuCount());
        result.setPinCount(scendList.get(0).getPinCount());
        result.setParentIdAmt(scendList.get(0).getParentIdAmt());
        branchDataResultDao.insertSelective(result);

        BranchDataResult resultLastYearDevide = new BranchDataResult();
        resultLastYearDevide.setBranchName(branch);
        resultLastYearDevide.setDataWeidu(dataType);
        resultLastYearDevide.setDataType("同比");
        resultLastYearDevide.setDealPin(scendList.get(0).getDealPin().divide(scendListLastYear.get(0).getDealPin(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastYearDevide.setDealAmt(scendList.get(0).getDealAmt().divide(scendListLastYear.get(0).getDealAmt(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastYearDevide.setDealParentId(scendList.get(0).getDealParentId().divide(scendListLastYear.get(0).getDealParentId(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastYearDevide.setArpu(scendList.get(0).getArpuCount().divide(scendListLastYear.get(0).getArpuCount(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastYearDevide.setPinCount(scendList.get(0).getPinCount().divide(scendListLastYear.get(0).getPinCount(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastYearDevide.setParentIdAmt(scendList.get(0).getParentIdAmt().divide(scendListLastYear.get(0).getParentIdAmt(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        branchDataResultDao.insertSelective(resultLastYearDevide);

        BranchDataResult resultLastMonthDevide = new BranchDataResult();
        resultLastMonthDevide.setBranchName(branch);
        resultLastMonthDevide.setDataWeidu(dataType);
        resultLastMonthDevide.setDataType("环比");
        resultLastMonthDevide.setDealPin(scendList.get(0).getDealPin().divide(scendListLastMonth.get(0).getDealPin(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastMonthDevide.setDealAmt(scendList.get(0).getDealAmt().divide(scendListLastMonth.get(0).getDealAmt(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastMonthDevide.setDealParentId(scendList.get(0).getDealParentId().divide(scendListLastMonth.get(0).getDealParentId(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastMonthDevide.setArpu(scendList.get(0).getArpuCount().divide(scendListLastMonth.get(0).getArpuCount(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastMonthDevide.setPinCount(scendList.get(0).getPinCount().divide(scendListLastMonth.get(0).getPinCount(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        resultLastMonthDevide.setParentIdAmt(scendList.get(0).getParentIdAmt().divide(scendListLastMonth.get(0).getParentIdAmt(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        branchDataResultDao.insertSelective(resultLastMonthDevide);

    }

    public List<BranchData> readBranchExcel() throws Exception{
        List<String> list = ReadExcelYsx.readExcel("D:\\workspace\\workspaceMyself\\部门.xlsx");
        List<BranchData> listData = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            String[] arr = str.split(",");
            BranchData data = new BranchData();
            data.setDealDate(arr[0]);
            data.setBranchName(arr[1]);
            data.setDataType(arr[2]);
            data.setDealEarth(arr[3]);
            data.setDealPin(arr[4] == null || arr[4].equals("") ? new BigDecimal("0.00"): new BigDecimal(arr[4]).setScale(2, BigDecimal.ROUND_HALF_UP));
            data.setDealAmt(arr[5] == null || arr[5].equals("") ? new BigDecimal("0.00"): new BigDecimal(arr[5]).setScale(2, BigDecimal.ROUND_HALF_UP));
            data.setDealParentId(arr[6] == null || arr[6].equals("") ? new BigDecimal("0.00"): new BigDecimal(arr[6]).setScale(2, BigDecimal.ROUND_HALF_UP));
            listData.add(data);
        }
        return listData;
    }


    public List<SaleData> readSaleExcel() throws Exception{
        List<String> list = ReadExcelYsx.readExcel("D:\\workspace\\workspaceMyself\\采销.xlsx");
        List<SaleData> listData = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            String[] arr = str.split(",");
            SaleData data = new SaleData();
            data.setDealDate(arr[0]);
//            data.setBranchId(arr[1]);
//            data.setBranchName(arr[2]);
//            data.setSaleId(arr[1]);
            data.setSaleName(arr[1]);
            data.setDataType(arr[2]);
            data.setDealEarth(arr[3]);
            data.setDealPin(arr[4] == null || arr[4].equals("") ? new BigDecimal("0.00"): new BigDecimal(arr[4]).setScale(2, BigDecimal.ROUND_HALF_UP));
            data.setDealSum(arr[5] == null || arr[5].equals("") ? new BigDecimal("0.00"): new BigDecimal(arr[5]).setScale(2, BigDecimal.ROUND_HALF_UP));
            data.setDealAmt(arr[6] == null || arr[6].equals("") ? new BigDecimal("0.00"): new BigDecimal(arr[6]).setScale(2, BigDecimal.ROUND_HALF_UP));
            data.setDealParentId(arr[7] == null || arr[7].equals("") ? new BigDecimal("0.00"): new BigDecimal(arr[7]).setScale(2, BigDecimal.ROUND_HALF_UP));
            listData.add(data);
        }
        return listData;
    }

}
