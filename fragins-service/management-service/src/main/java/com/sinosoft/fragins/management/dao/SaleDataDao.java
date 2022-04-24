package com.sinosoft.fragins.management.dao;

import com.sinosoft.fragins.management.po.BranchData;
import com.sinosoft.fragins.management.po.SaleData;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SaleDataDao extends Mapper<SaleData> {

    List<SaleData> selectAll();

    void deleteAll();

}