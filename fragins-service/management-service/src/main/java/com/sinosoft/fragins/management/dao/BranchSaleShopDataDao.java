package com.sinosoft.fragins.management.dao;

import com.sinosoft.fragins.management.po.BranchData;
import com.sinosoft.fragins.management.po.BranchSaleShopData;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BranchSaleShopDataDao extends Mapper<BranchSaleShopData> {

    List<BranchSaleShopData> selectAll();

    List<BranchSaleShopData> selectByName(@Param("name") String name);

    List<BranchSaleShopData> selectByNameAndDate(@Param("name") String name, @Param("dealDate") String dealDate);

    List<BranchSaleShopData> selectGroupByNameAndDate();

    List<BranchSaleShopData> selectGroupByNameAndType();

    List<BranchSaleShopData> selectGroupByName();

    List<BranchSaleShopData> selectCountGroupByName();

    List<BranchSaleShopData> selectByNameAndDateAnddataType(@Param("name") String name, @Param("dealDate") String dealDate, @Param("dataType") String dataType);

    String selectDealPinCountByNameAndDate(@Param("name") String name, @Param("dealDate") String dealDate);

    List<BranchSaleShopData> selectByNameAndType(@Param("name") String name, @Param("dataType") String dataType);

    List<BranchSaleShopData> selectDealDate();

    void deleteAll();

    void deleteDealEarth();

    void deleteByName(@Param("name") String name);

    void deleteTwoNew();

    void insertBatch(List<BranchSaleShopData> list);

    void updateBatch(List<BranchSaleShopData> list);

}