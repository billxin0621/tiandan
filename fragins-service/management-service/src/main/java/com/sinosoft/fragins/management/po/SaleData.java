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
	private String branchId      ;
	private String branchName    ;
	private String saleId    ;
	private String saleName    ;
	private String dataType      ;
	private String dealEarth     ;
	private String dealPin       ;
	private String dealParentId ;
	private BigDecimal dealAmt       ;
	private BigDecimal dealSum       ;

}