package com.sinosoft.fragins.management.po;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/** 角色表 */
@Data
@Table(name = "branch_data_result")
@SuppressWarnings("serial")
public class BranchDataResult implements java.io.Serializable {

	@Id
	private String id             ;
	private String branchName      ;
	private String dataWeidu      ;
	private String dataType      ;
	private BigDecimal dealPin    ;
	private BigDecimal dealAmt      ;
	private BigDecimal dealParentId     ;
	private BigDecimal arpu       ;//ARPU（成交金额/用户数）
	private BigDecimal pinCount       ;//购买频次（订单/用户数）
	private BigDecimal parentIdAmt       ;//客单价（成交金额/订单）

}