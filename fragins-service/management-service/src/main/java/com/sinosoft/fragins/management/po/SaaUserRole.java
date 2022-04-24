package com.sinosoft.fragins.management.po;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.Data;

/** 用户角色表 */
@Data
@Table(name = "saa_user_role")
@SuppressWarnings("serial")
public class SaaUserRole implements java.io.Serializable {

	/** 自增主键 */
	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	private java.lang.Long id;

	/** 角色代码 */
	private java.lang.String roleCode;

	/** 用户代码 */
	private java.lang.String userCode;

	/** 有效起期 */
	private java.util.Date startDate;

	/** 有效止期 */
	private java.util.Date endDate;

	/** 版本 */
	private java.lang.Long version;

	/** 创建人员 */
	private java.lang.String creator;

	/** 更新人员 */
	private java.lang.String updater;

	/** 创建时间 */
	private java.util.Date createTime;

	/** 最后更新时间 */
	private java.util.Date updateTime;

	/** 删除标志 */
	private java.lang.Long deleteFlag;

}