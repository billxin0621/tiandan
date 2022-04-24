package com.sinosoft.fragins.management.po;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.Data;

/** 角色表 */
@Data
@Table(name = "saa_role")
@SuppressWarnings("serial")
public class SaaRole implements java.io.Serializable {

	/** 角色代码 */
	private java.lang.String roleCode;

	/** 角色中文名 */
	private java.lang.String roleCName;

	/** 角色名 */
	private java.lang.String roleTName;

	/** 角色英文名 */
	private java.lang.String roleEName;

	/** 机构 */
	private java.lang.String comCode;

	/** 备注 */
	private java.lang.String remark;

	/** flag */
	private java.lang.String flag;

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