package com.sinosoft.fragins.management.api;

import com.sinosoft.fragins.framework.vo.PageVo;
import com.sinosoft.fragins.management.common.ApiResponse;
import com.sinosoft.fragins.management.dao.BranchDataDao;
import com.sinosoft.fragins.management.dto.dataClear.DataClearQueryCondition;
import com.sinosoft.fragins.management.dto.dataClear.DataClearUploadFile;
import com.sinosoft.fragins.management.po.BranchData;
import com.sinosoft.fragins.management.service.BranchSaleShopService;
import com.sinosoft.fragins.management.service.DataClearService;
import com.sinosoft.fragins.management.vo.DataClear.DataClearVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lxb
 * @Date: 2020-11-23
 * C2020110679358京迪信案件开通闪赔功能
 **/
@RestController
@RequestMapping("/dataClear")
@CrossOrigin("*")
@Slf4j
public class DataClearApi {

    @Autowired
    private BranchSaleShopService branchSaleShopService;
    @Autowired
    private DataClearService dataClearService;
    @Autowired
    private BranchDataDao branchDataDao;

    @PostMapping("/excute")
    public String excute(DataClearUploadFile request) throws Exception{
        String response = dataClearService.excute();

        return response;
    }

    @PostMapping("/excute1")
    public String excute1(DataClearUploadFile request) throws Exception{
        String fileResource = "D:\\workspace\\workspaceMyself\\部门.xlsx";
        String fileResult = "D:\\workspace\\workspaceMyself\\部门结果.xls";
        String response = branchSaleShopService.excute(fileResource, fileResult);
        fileResource = "D:\\workspace\\workspaceMyself\\采销.xlsx";
        fileResult = "D:\\workspace\\workspaceMyself\\采销结果.xls";
        response = branchSaleShopService.excuteSale(fileResource, fileResult);
        fileResource = "D:\\workspace\\workspaceMyself\\店铺.xlsx";
        fileResult = "D:\\workspace\\workspaceMyself\\店铺结果.xls";
        response = branchSaleShopService.excuteDianpu(fileResource, fileResult);
        fileResource = "D:\\workspace\\workspaceMyself\\品牌.xlsx";
        fileResult = "D:\\workspace\\workspaceMyself\\品牌结果.xls";
        response = branchSaleShopService.excutePinpai(fileResource, fileResult);

        return response;
    }

    @PostMapping("/excute2")
    public String excute2(DataClearUploadFile request) throws Exception{
        String fileResource = "/data/springCloud/management-service/file/部门.xlsx";
        String fileResult = "/data/springCloud/management-service/file/部门结果.xls";
        String response = branchSaleShopService.excute(fileResource, fileResult);
        fileResource = "/data/springCloud/management-service/file/采销.xlsx";
        fileResult = "/data/springCloud/management-service/file/采销结果.xls";
        response = branchSaleShopService.excuteSale(fileResource, fileResult);
        fileResource = "/data/springCloud/management-service/file/店铺.xlsx";
        fileResult = "/data/springCloud/management-service/file/店铺结果.xls";
        response = branchSaleShopService.excuteDianpu(fileResource, fileResult);
        fileResource = "/data/springCloud/management-service/file/品牌.xlsx";
        fileResult = "/data/springCloud/management-service/file/品牌结果.xls";
        response = branchSaleShopService.excutePinpai(fileResource, fileResult);

        return response;
    }

    @PostMapping("/test")
    public ApiResponse<String> test(DataClearUploadFile request) {
        BranchData branchData = new BranchData();
        branchData.setBranchId("1");
        branchData.setBranchName("1");
        branchData.setDealDate("111111");
        branchData.setDealAmt(new BigDecimal("111.00"));
        int response = branchDataDao.insertSelective(branchData);

        return null;
    }

    public static List<List<String>> splitList(List<String> list, int len) {
        if (list == null || list.size() == 0 || len < 1) {
            return null;
        }
        List<List<String>> result = new ArrayList<List<String>>();

        int size = list.size();
        int count = (size + len - 1) / len;

        for (int i = 0; i < count; i++) {
            List<String> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
            result.add(subList);
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            list.add("body");
        }
        List<List<String>> result = splitList(list, 10);
        log.info("111");
    }


}
