package com.sinosoft.fragins.management.dao;

import com.sinosoft.fragins.management.po.BranchSaleShopData;
import com.sinosoft.fragins.management.po.BranchSaleShopResultData;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BranchSaleShopResultDataDao extends Mapper<BranchSaleShopResultData> {

    List<BranchSaleShopResultData> selectAll();

    void deleteAll();

}