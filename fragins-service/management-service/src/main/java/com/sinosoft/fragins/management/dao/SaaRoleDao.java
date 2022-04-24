package com.sinosoft.fragins.management.dao;

import com.sinosoft.fragins.management.po.SaaRole;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SaaRoleDao extends Mapper<SaaRole> {

    List<SaaRole> selectAll();

}