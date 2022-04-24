package com.sinosoft.fragins.management.po;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.Data;

/** 机构信息表 */
@Data
@Table(name = "sys_company")
@SuppressWarnings("serial")
public class SysCompany implements java.io.Serializable {

	/**  */
	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	private java.lang.Long id;

	/**  */
	private java.lang.String comCode;

	/**  */
	private java.lang.String subversionCompany;

	/**  */
	private java.lang.String comCName;

	/**  */
	private java.lang.String comShortName;

	/**  */
	private java.lang.String unitInnerCoding;

	/**  */
	private java.lang.String versionId;

	/**  */
	private java.lang.String comEName;

	/**  */
	private java.lang.String addressCName;

	/**  */
	private java.lang.String addressEName;

	/**  */
	private java.lang.String postCode;

	/**  */
	private java.lang.String phoneDecimal;

	/**  */
	private java.lang.String faxDecimal;

	/**  */
	private java.lang.String upperComCode;

	/**  */
	private java.lang.String insurerName;

	/**  */
	private java.lang.String comType;

	/**  */
	private java.lang.String comFlag;

	/**  */
	private java.lang.String centerFlag;

	/**  */
	private java.math.BigDecimal comLevel;

	/**  */
	private java.lang.String branchType;

	/**  */
	private java.lang.String upperPath;

	/**  */
	private java.lang.String comCodeCirc;

	/**  */
	private java.lang.String licenseNo;

	/**  */
	private java.lang.String email;

	/**  */
	private java.lang.String remark1;

	/**  */
	private java.lang.String comKind;

	/**  */
	private java.lang.String manAger;

	/**  */
	private java.lang.String accountAnt;

	/**  */
	private java.lang.String remark;

	/**  */
	private java.lang.String newComCode;

	/**  */
	private java.util.Date validDate;

	/**  */
	private java.util.Date invalidDate;

	/**  */
	private java.lang.String validStatus;

	/**  */
	private java.lang.String acntUnit;

	/**  */
	private java.lang.String articleCode;

	/**  */
	private java.lang.String updateFlag;

	/**  */
	private java.util.Date updateDate;

	/**  */
	private java.lang.String operatorComCode;

	/**  */
	private java.lang.String flag;

	/**  */
	private java.lang.Long version;

	/**  */
	private java.util.Date insertTimeForHis;

	/**  */
	private java.util.Date operateTimeForHis;

	/**  */
	private java.util.Date createTime;

	/**  */
	private java.util.Date gmtModified;

}