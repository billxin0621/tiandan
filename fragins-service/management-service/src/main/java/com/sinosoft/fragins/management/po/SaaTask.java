package com.sinosoft.fragins.management.po;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.Data;

/** 功能表 */
@Data
@Table(name = "saa_task")
@SuppressWarnings("serial")
public class SaaTask implements java.io.Serializable {

	/** 功能代码 */
	private java.lang.String taskCode;

	/**  */
	private java.lang.String groupName;

	/** 上级功能代码 */
	private java.lang.String upperTaskCode;

	/** 功能代码层级 */
	private java.lang.String level;

	/** 功能中文名 */
	private java.lang.String taskCName;

	/** 功能名 */
	private java.lang.String taskTName;

	/** 功能英文名 */
	private java.lang.String taskEName;

	/** url */
	private java.lang.String url;

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