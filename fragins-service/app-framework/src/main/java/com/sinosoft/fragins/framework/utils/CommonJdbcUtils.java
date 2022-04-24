package com.sinosoft.fragins.framework.utils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;

import com.sinosoft.fragins.framework.service.CommonJdbcService;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

/**
 * 一些数据库特殊操作用到的功能
 * 
 * @author panyu
 *
 */
@Slf4j
public class CommonJdbcUtils {

	/**
	 * 在新事务中insert ignore一个对象
	 * <p>
	 * 注：Sharding-JDBC + XA事务控制 +
	 * MySql的组合，事务嵌套有些问题，有些情况下需要在新事务中插入数据尽量减少锁定时间的（比如小保单时插入大保单分项表数据的），使用这个方法处理。
	 * 
	 * @param po PO类对象
	 */
	public static void insertIgnoreInNewTransaction(final Object po) {
		try {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					TransactionTypeHolder.set(TransactionType.XA);
					CommonJdbcService service = SpringContextUtils.getBean(CommonJdbcService.class);
					service.insertIgnore(po);
				}
			};
			Thread t = new Thread(r);
			t.start();
			t.join();
		} catch (Exception e) {
			log.error("CommonJdbcUtils发生异常", e);
		}
	}

}
