package com.sinosoft.fragins.management.po;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/** 部门采销店铺表 */
@Data
@Table(name = "branch_sale_shop_result_data")
@SuppressWarnings("serial")
public class BranchSaleShopResultData implements java.io.Serializable {

	@Id
	private String id             ;
	private String name    ;
	private BigDecimal dealPinTotal      ;
	private BigDecimal tongbiTotal     ;
	private BigDecimal dealPinNew      ;
	private BigDecimal tongbiNew     ;
	private BigDecimal dealPinOld      ;
	private BigDecimal tongbiOld     ;

}