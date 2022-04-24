package com.sinosoft.fragins.framework.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

@Slf4j
public class CommonJdbcService extends JdbcDaoSupport {

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void insertIgnore(Object po) {
		try {
			// 生成SQL和参数值
			Class<?> poClass = po.getClass();
			EntityTable entityTable = EntityHelper.getEntityTable(poClass);
			Set<EntityColumn> entityColumns = EntityHelper.getColumns(poClass);
			List<Object> values = new ArrayList<>();
			StringBuilder sqlb1 = new StringBuilder();
			StringBuilder sqlb2 = new StringBuilder();
			for (EntityColumn ec : entityColumns) {
				Object value = ec.getEntityField().getValue(po);
				if (value != null) {
					values.add(value);
					sqlb1.append(ec.getColumn()).append(",");
					sqlb2.append("?,");
				}
			}
			sqlb1.deleteCharAt(sqlb1.length() - 1);
			sqlb2.deleteCharAt(sqlb2.length() - 1);
			StringBuilder sqlb = new StringBuilder();
			sqlb.append("insert ignore into ").append(entityTable.getName()).append(" (").append(sqlb1)
					.append(") values (").append(sqlb2).append(")");
			log.debug("生成插入sql: {}\nvalues: {}", sqlb, values);
			getJdbcTemplate().update(sqlb.toString(), values.toArray());
		} catch (Exception e) {
			log.error("DbUtils.insertIgnoreInNewTransaction发生异常", e);
			throw new RuntimeException(e);
		}
	}

}
