package com.sinosoft.fragins.management.api;

import com.sinosoft.fragins.framework.vo.PageVo;
import com.sinosoft.fragins.management.common.ApiResponse;
import com.sinosoft.fragins.management.dao.BranchDataDao;
import com.sinosoft.fragins.management.dto.dataClear.DataClearQueryCondition;
import com.sinosoft.fragins.management.dto.dataClear.DataClearUploadFile;
import com.sinosoft.fragins.management.po.BranchData;
import com.sinosoft.fragins.management.service.DataClearService;
import com.sinosoft.fragins.management.vo.DataClear.DataClearVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @Author: lxb
 * @Date: 2020-11-23
 * C2020110679358京迪信案件开通闪赔功能
 **/
@RestController
@RequestMapping("/dataClear")
@CrossOrigin("*")
public class DataClearApi {

    @Autowired
    private DataClearService dataClearService;
    @Autowired
    private BranchDataDao branchDataDao;

    @PostMapping("/excute")
    public String excute(DataClearUploadFile request) throws Exception{
        String response = dataClearService.excute();

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


}
