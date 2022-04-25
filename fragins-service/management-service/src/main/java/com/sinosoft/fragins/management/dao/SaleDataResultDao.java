package com.sinosoft.fragins.management.dao;

import com.sinosoft.fragins.management.po.SaleDataResult;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SaleDataResultDao extends Mapper<SaleDataResult> {


    List<SaleDataResult> selectByBranchNameAnddataWeidu(@Param("saleName") String branchName, @Param("dataWeidu") String dataWeidu);

    void deleteAll();
}