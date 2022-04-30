package com.sinosoft.fragins.management.dao;

import com.sinosoft.fragins.management.po.BranchDataResult;
import com.sinosoft.fragins.management.po.SaleData;
import com.sinosoft.fragins.management.po.SaleDataResult;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SaleDataResultDao extends Mapper<SaleDataResult> {


    List<SaleDataResult> selectBySaleNameAnddataWeidu(@Param("saleName") String saleName, @Param("dataWeidu") String dataWeidu);

    List<SaleDataResult> selectGroupBySaleNameAnddataWeidu();

    void deleteAll();

    void insertBatch(List<SaleDataResult> list);

    void updateBatch(List<SaleDataResult> list);
}