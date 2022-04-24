package com.sinosoft.fragins.management.dao;

import java.util.List;

import com.sinosoft.fragins.management.po.SaaUserRole;

import tk.mybatis.mapper.common.Mapper;

public interface SaaUserRoleDao extends Mapper<SaaUserRole> {
	public List<SaaUserRole> findUserRoleByUserCode(String userCode);
}