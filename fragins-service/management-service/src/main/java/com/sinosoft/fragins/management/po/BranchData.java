package com.sinosoft.fragins.management.po;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/** 角色表 */
@Data
@Table(name = "branch_data")
@SuppressWarnings("serial")
public class BranchData implements java.io.Serializable {

	@Id
	private String id             ;
	private String dealDate      ;
	private String branchId      ;
	private String branchName    ;
	private String dataType      ;
	private String dealEarth     ;
	private BigDecimal dealPin       ;
	private BigDecimal dealParentId ;
	private BigDecimal dealAmt       ;
	private BigDecimal arpuCount     ;//ARPU（成交金额/用户数）
	private BigDecimal pinCount       ;//购买频次（订单/用户数）
	private BigDecimal parentIdAmt       ;//客单价（成交金额/订单）

}