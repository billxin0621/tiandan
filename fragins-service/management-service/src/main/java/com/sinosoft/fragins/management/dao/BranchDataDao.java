package com.sinosoft.fragins.management.dao;

import com.sinosoft.fragins.management.po.BranchData;
import com.sinosoft.fragins.management.po.SaleData;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BranchDataDao extends Mapper<BranchData> {

    List<BranchData> selectAll();

    List<BranchData> selectByBranchNameAndDate(@Param("branchName") String branchName, @Param("dealDate") String dealDate);

    List<BranchData> selectGroupByBranchNameAndDate();

    List<BranchData> selectGroupByBranchNameAndType();

    List<BranchData> selectByBranchNameAndDateAnddataType(@Param("branchName") String branchName, @Param("dealDate") String dealDate, @Param("dataType") String dataType);

    String selectDealPinCountByBranchNameAndDate(@Param("branchName") String branchName, @Param("dealDate") String dealDate);

    void deleteAll();

    void deleteDealEarth();

    void insertBatch(List<BranchData> list);

    void updateBatch(List<BranchData> list);

}