package com.sinosoft.fragins.management.po;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/** 角色表 */
@Data
@Table(name = "sale_data")
@SuppressWarnings("serial")
public class SaleData implements java.io.Serializable {

	@Id
	private String id             ;
	private String dealDate      ;
	private String branchName    ;
	private String saleName    ;
	private String dataType      ;
	private String dealEarth     ;
	private BigDecimal dealPin       ;
	private BigDecimal dealParentId ;
	private BigDecimal dealAmt       ;
	private BigDecimal dealSum       ;
	private BigDecimal arpuCount     ;//ARPU（成交金额/用户数）
	private BigDecimal pinCount       ;//购买频次（订单/用户数）
	private BigDecimal parentIdAmt       ;//客单价（成交金额/订单）

}