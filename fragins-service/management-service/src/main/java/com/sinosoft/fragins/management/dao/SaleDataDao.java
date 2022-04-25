package com.sinosoft.fragins.management.dao;

import com.sinosoft.fragins.management.po.SaleData;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SaleDataDao extends Mapper<SaleData> {

    List<SaleData> selectAll();

    List<SaleData> selectBySaleNameAndDate(@Param("saleName") String branchName, @Param("dealDate") String dealDate);

    List<SaleData> selectGroupBySaleNameAndDate();

    List<SaleData> selectBySaleNameAndDateAnddataType(@Param("saleName") String branchName, @Param("dealDate") String dealDate, @Param("dataType") String dataType);

    String selectDealPinCountBySaleNameAndDate(@Param("saleName") String branchName, @Param("dealDate") String dealDate);

    void deleteAll();

}