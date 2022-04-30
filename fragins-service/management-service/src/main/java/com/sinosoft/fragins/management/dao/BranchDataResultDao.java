package com.sinosoft.fragins.management.dao;

import com.sinosoft.fragins.management.po.BranchData;
import com.sinosoft.fragins.management.po.BranchDataResult;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BranchDataResultDao extends Mapper<BranchDataResult> {


    List<BranchDataResult> selectByBranchNameAnddataWeidu(@Param("branchName") String branchName, @Param("dataWeidu") String dataWeidu);

    List<BranchDataResult> selectGroupByBranchNameAnddataWeidu();

    void deleteAll();

    void insertBatch(List<BranchDataResult> list);

    void updateBatch(List<BranchDataResult> list);
}