package com.sinosoft.fragins.management.po;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/** 通用配置表 */
@Data
@Table(name = "uti_config")
@SuppressWarnings("serial")
public class UtiConfig implements java.io.Serializable {

	/** 物理主键 */
	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 环境类型，如dev等，如果为default则不区分环境 */
	private String profileType;

	/** 配置键 */
	private String configKey;

	/** 配置值 */
	private String configValue;

	/** 创建时间 */
	private Date createTime;

	/** 更新时间 */
	private Date updateTime;

	/** 删除标志 */
	private Long deleteFlag;

}