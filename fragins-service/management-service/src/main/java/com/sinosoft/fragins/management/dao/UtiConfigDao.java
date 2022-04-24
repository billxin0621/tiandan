package com.sinosoft.fragins.management.dao;


import com.sinosoft.fragins.management.po.UtiConfig;
import tk.mybatis.mapper.common.Mapper;


public interface UtiConfigDao extends Mapper<UtiConfig> {

	public UtiConfig selectByProfileTypeAndConfigKey(String profileType, String configKey);

}