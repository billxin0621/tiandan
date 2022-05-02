package com.sinosoft.fragins.management.po;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/** 部门采销店铺表 */
@Data
@Table(name = "branch_sale_shop_data")
@SuppressWarnings("serial")
public class BranchSaleShopData implements java.io.Serializable {

	@Id
	private String id             ;
	private String dealDate      ;
	private String name    ;
	private String dataType      ;
	private String dealEarth     ;
	private BigDecimal dealPin       ;

}