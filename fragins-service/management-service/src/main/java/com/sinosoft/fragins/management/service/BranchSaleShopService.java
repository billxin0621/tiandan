package com.sinosoft.fragins.management.service;

import com.sinosoft.fragins.management.dao.*;
import com.sinosoft.fragins.management.po.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class BranchSaleShopService {

    private final static Logger logger = LoggerFactory.getLogger(BranchSaleShopService.class);

    @Autowired
    private BranchSaleShopDataDao branchSaleShopDataDao;
    @Autowired
    private BranchSaleShopResultDataDao branchSaleShopResultDataDao;
    @Autowired
    private BranchDataResultDao branchDataResultDao;
    @Autowired
    private SaleDataDao saleDataDao;
    @Autowired
    private SaleDataResultDao saleDataResultDao;

    @Value("${sale}")
    private String saleTarget;

    DecimalFormat decimalFormat = new DecimalFormat("###,###.##%");
    String strTitle = ",,总用户,,同比,,新用户,,同比,,老用户,,同比";

    String dateSmall = "";//小日期
    String dateBig = "";//大日期

    public String excute(String fileSource, String fileResult) throws Exception{
        try{
            // 1.读取部门采销店铺excel信息
            List<BranchSaleShopData> branchDataList = this.readBranchExcel(fileSource);
            branchSaleShopDataDao.insertBatch(branchDataList);

            // 2.获取日期*****
            List<BranchSaleShopData> dealDateList = branchSaleShopDataDao.selectDealDate();
            if (dealDateList.get(0).getDealDate().compareTo(dealDateList.get(1).getDealDate()) > 0){
                dateSmall = dealDateList.get(1).getDealDate();
                dateBig = dealDateList.get(0).getDealDate();
            }else {
                dateSmall = dealDateList.get(0).getDealDate();
                dateBig = dealDateList.get(1).getDealDate();
            }

            // 2.读取采销excel信息
//            String[] saleTargetArr = saleTarget.split(",");//符合的采销
//            List<String> saleTargetList = Arrays.asList(saleTargetArr);
//            List<SaleData> saleDataList = this.readSaleExcel();
//            List<SaleData> saleDataAgainList = new ArrayList<>();
//            for (int i = 0; i < saleDataList.size(); i++) {
//                if (saleTargetList.contains(saleDataList.get(i).getSaleName())){
//                    saleDataAgainList.add(saleDataList.get(i));
//                }
//            }
//            saleDataDao.insertBatch(saleDataAgainList);

            // 3.删除全球的
            branchSaleShopDataDao.deleteDealEarth();

            // 4.完善
            List<BranchSaleShopData> dataGroup = branchSaleShopDataDao.selectGroupByNameAndDate();
            for (int i = 0; i < dataGroup.size(); i++) {
                this.caculateData(dataGroup.get(i).getName(), dataGroup.get(i).getDealDate());
            }

            // 5.删除站内新、站外新数据
            branchSaleShopDataDao.deleteTwoNew();

            // 3.跨境母婴自营组 总用户 数据
            List<BranchSaleShopData> dataGroupResult = branchSaleShopDataDao.selectGroupByName();
            for (int i = 0; i < dataGroupResult.size(); i++) {
                this.caculateResult(dataGroupResult.get(i).getName());
            }


            // 13.跨境母婴自营组 总用户 结果
            List<BranchSaleShopResultData> resultList = branchSaleShopResultDataDao.selectAll();
            List<String> listBranchExcel = new ArrayList<>();
            listBranchExcel.add(strTitle);
            for (int i = 0; i < resultList.size(); i++) {
                BranchSaleShopResultData data = resultList.get(i);
                String strNow = this.excuteResult(data);
                listBranchExcel.add(strNow);
            }
            ReadExcelYsx.exportonefile(fileResult, listBranchExcel);

        }catch (Exception e){
            throw e;
        }finally {
            // 5.删除数据
            branchSaleShopDataDao.deleteAll();
            branchSaleShopResultDataDao.deleteAll();
        }

        return "true";

    }

    public String excuteSale(String fileSource, String fileResult) throws Exception{
        try{
            // 1.读取部门采销店铺excel信息
            List<BranchSaleShopData> branchDataList = this.readBranchExcel(fileSource);
            String[] saleTargetArr = saleTarget.split(",");//符合的采销
            List<String> saleTargetList = Arrays.asList(saleTargetArr);
            List<BranchSaleShopData> saleDataAgainList = new ArrayList<>();
            for (int i = 0; i < branchDataList.size(); i++) {
                if (saleTargetList.contains(branchDataList.get(i).getName())){
                    saleDataAgainList.add(branchDataList.get(i));
                }
            }
            branchSaleShopDataDao.insertBatch(saleDataAgainList);

            // 2.获取日期
            List<BranchSaleShopData> dealDateList = branchSaleShopDataDao.selectDealDate();
            if (dealDateList.get(0).getDealDate().compareTo(dealDateList.get(1).getDealDate()) > 0){
                dateSmall = dealDateList.get(1).getDealDate();
                dateBig = dealDateList.get(0).getDealDate();
            }else {
                dateSmall = dealDateList.get(0).getDealDate();
                dateBig = dealDateList.get(1).getDealDate();
            }

            // 3.删除全球的
            branchSaleShopDataDao.deleteDealEarth();

            // 4.完善
            List<BranchSaleShopData> dataGroup = branchSaleShopDataDao.selectGroupByNameAndDate();
            for (int i = 0; i < dataGroup.size(); i++) {
                this.caculateData(dataGroup.get(i).getName(), dataGroup.get(i).getDealDate());
            }

            // 5.删除站内新、站外新数据
            branchSaleShopDataDao.deleteTwoNew();

            // 3.跨境母婴自营组 总用户 数据
            List<BranchSaleShopData> dataGroupResult = branchSaleShopDataDao.selectGroupByName();
            for (int i = 0; i < dataGroupResult.size(); i++) {
                this.caculateResult(dataGroupResult.get(i).getName());
            }


            // 13.跨境母婴自营组 总用户 结果
            List<BranchSaleShopResultData> resultList = branchSaleShopResultDataDao.selectAll();
            List<String> listBranchExcel = new ArrayList<>();
            listBranchExcel.add(strTitle);
            for (int i = 0; i < resultList.size(); i++) {
                BranchSaleShopResultData data = resultList.get(i);
                String strNow = this.excuteResult(data);
                listBranchExcel.add(strNow);
            }
            ReadExcelYsx.exportonefile(fileResult, listBranchExcel);

        }catch (Exception e){
            throw e;
        }finally {
            // 5.删除数据
            branchSaleShopDataDao.deleteAll();
            branchSaleShopResultDataDao.deleteAll();
        }

        return "true";

    }


    public String excuteDianpu(String fileSource, String fileResult) throws Exception{
        try{
            // 1.读取部门采销店铺excel信息
            List<BranchSaleShopData> branchDataList = this.readBranchExcel(fileSource);
            branchSaleShopDataDao.insertBatch(branchDataList);


            // 2.获取日期
            List<BranchSaleShopData> dealDateList = branchSaleShopDataDao.selectDealDate();
            if (dealDateList.get(0).getDealDate().compareTo(dealDateList.get(1).getDealDate()) > 0){
                dateSmall = dealDateList.get(1).getDealDate();
                dateBig = dealDateList.get(0).getDealDate();
            }else {
                dateSmall = dealDateList.get(0).getDealDate();
                dateBig = dealDateList.get(1).getDealDate();
            }

            // 3.删除全球的
            branchSaleShopDataDao.deleteDealEarth();


            List<BranchSaleShopData> delList = branchSaleShopDataDao.selectCountGroupByName();
            for (int i = 0; i < delList.size(); i++) {
                if (delList.get(i).getCount().compareTo(new BigDecimal("6")) < 0){
                    branchSaleShopDataDao.deleteByName(delList.get(i).getName());
                }
            }

            // 4.完善
            List<BranchSaleShopData> dataGroup = branchSaleShopDataDao.selectGroupByNameAndDate();
            for (int i = 0; i < dataGroup.size(); i++) {
                this.caculateData(dataGroup.get(i).getName(), dataGroup.get(i).getDealDate());
            }

            // 5.删除站内新、站外新数据
            branchSaleShopDataDao.deleteTwoNew();

            // 3.跨境母婴自营组 总用户 数据
            List<BranchSaleShopData> dataGroupResult = branchSaleShopDataDao.selectGroupByName();
            for (int i = 0; i < dataGroupResult.size(); i++) {
                this.caculateResult(dataGroupResult.get(i).getName());
            }


            // 13.跨境母婴自营组 总用户 结果
            List<BranchSaleShopResultData> resultList = branchSaleShopResultDataDao.selectAll();
            List<String> listBranchExcel = new ArrayList<>();
            listBranchExcel.add(strTitle);
            for (int i = 0; i < resultList.size(); i++) {
                BranchSaleShopResultData data = resultList.get(i);
                String strNow = this.excuteResult(data);
                listBranchExcel.add(strNow);
            }
            ReadExcelYsx.exportonefile(fileResult, listBranchExcel);

        }catch (Exception e){
            throw e;
        }finally {
            // 5.删除数据
            branchSaleShopDataDao.deleteAll();
            branchSaleShopResultDataDao.deleteAll();
        }

        return "true";

    }




    public String excuteResult(BranchSaleShopResultData data){
        String str = "";
        str = str + data.getName() + ",,";
        str = str + data.getDealPinTotal() + ",,";
        str = str + decimalFormat.format(data.getTongbiTotal()) + ",,";
        str = str + data.getDealPinNew() + ",,";
        str = str + decimalFormat.format(data.getTongbiNew()) + ",,";
        str = str + data.getDealPinOld() + ",,";
        str = str + decimalFormat.format(data.getTongbiOld());
        return str;
    }


    public void caculateResult(String name) throws Exception {
        String dataType = "总用户";
        List<BranchSaleShopData> totalList = branchSaleShopDataDao.selectByNameAndType(name, dataType);
        BranchSaleShopData dataTotalSmall = new BranchSaleShopData();
        BranchSaleShopData dataTotalBig = new BranchSaleShopData();
        if (totalList.get(0).getDealDate().compareTo(totalList.get(1).getDealDate()) > 0){
            dataTotalSmall = totalList.get(1);
            dataTotalBig = totalList.get(0);
        }else {
            dataTotalSmall = totalList.get(0);
            dataTotalBig = totalList.get(1);
        }
        dataType = "老用户";
        List<BranchSaleShopData> oldList = branchSaleShopDataDao.selectByNameAndType(name, dataType);
        BranchSaleShopData dataOldSmall = new BranchSaleShopData();
        BranchSaleShopData dataOldBig = new BranchSaleShopData();
        if (oldList.get(0).getDealDate().compareTo(oldList.get(1).getDealDate()) > 0){
            dataOldSmall = oldList.get(1);
            dataOldBig = oldList.get(0);
        }else {
            dataOldSmall = oldList.get(0);
            dataOldBig = oldList.get(1);
        }
        dataType = "新用户";
        List<BranchSaleShopData> newList = branchSaleShopDataDao.selectByNameAndType(name, dataType);
        BranchSaleShopData dataNewSmall = new BranchSaleShopData();
        BranchSaleShopData dataNewBig = new BranchSaleShopData();
        if (newList.get(0).getDealDate().compareTo(newList.get(1).getDealDate()) > 0){
            dataNewSmall = newList.get(1);
            dataNewBig = newList.get(0);
        }else {
            dataNewSmall = newList.get(0);
            dataNewBig = newList.get(1);
        }

        BranchSaleShopResultData body = new BranchSaleShopResultData();
        body.setName(name);
        body.setDealPinTotal(dataTotalBig.getDealPin());
        body.setTongbiTotal(dataTotalBig.getDealPin().divide(dataTotalSmall.getDealPin(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        body.setDealPinOld(dataOldBig.getDealPin());
        body.setTongbiOld(dataOldBig.getDealPin().divide(dataOldSmall.getDealPin(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        body.setDealPinNew(dataNewBig.getDealPin());
        body.setTongbiNew(dataNewBig.getDealPin().divide(dataNewSmall.getDealPin(),2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")));
        branchSaleShopResultDataDao.insertSelective(body);

    }


    public void caculateData(String name, String date){
        List<BranchSaleShopData> scendList = branchSaleShopDataDao.selectByNameAndDate(name, date);
        BigDecimal dealPinTotal = new BigDecimal("0");
        BigDecimal dealPinNew = new BigDecimal("0");
        for (int i = 0; i < scendList.size(); i++) {
            BranchSaleShopData data = scendList.get(i);
            dealPinTotal = dealPinTotal.add(data.getDealPin());
            if ("站内新".equals(data.getDataType()) || "站外新".equals(data.getDataType())){
                dealPinNew = dealPinNew.add(data.getDealPin());
            }
        }
        BranchSaleShopData data = new BranchSaleShopData();
        data.setDealDate(date);
        data.setName(name);
        data.setDataType("总用户");
        data.setDealEarth("0");
        data.setDealPin(dealPinTotal);
        branchSaleShopDataDao.insertSelective(data);

        data = new BranchSaleShopData();
        data.setDealDate(date);
        data.setName(name);
        data.setDataType("新用户");
        data.setDealEarth("0");
        data.setDealPin(dealPinNew);
        branchSaleShopDataDao.insertSelective(data);
    }


    public List<BranchSaleShopData> readBranchExcel(String fileSource) throws Exception{
        List<String> list = ReadExcelYsx.readExcel(fileSource);
        List<BranchSaleShopData> listData = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            String[] arr = str.split(",,");
            BranchSaleShopData data = new BranchSaleShopData();
            data.setDealDate(arr[0]);
            data.setName(arr[1]);
            data.setDataType(arr[2]);
            data.setDealEarth(arr[3]);
            data.setDealPin(arr[4] == null || arr[4].equals("") ? new BigDecimal("0.00"): new BigDecimal(arr[4]).setScale(2, BigDecimal.ROUND_HALF_UP));
            listData.add(data);
        }
        return listData;
    }


}
