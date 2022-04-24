package com.sinosoft.fragins.management.po;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/** 理赔资料补传表 */
@Data
@Table(name = "fnc_claim_apply_data")
@SuppressWarnings("serial")
public class FncClaimApplyData implements java.io.Serializable {

	/** 主键ID */
	@Id
	private Long id;

	private String claimApplyId;
	private String proposalOrder;
	/** 资料类型 */
	private String dataType;

	/** 资料说明 */
	private String dataDesc;

	/** 资料地址 */
	private String dataUrl;

	/** 创建时间 */
	private Date createTime;

	/** 删除标志 */
	private Long deleteFlag;

	/** 修改时间 */
	private Date updateTime;



}