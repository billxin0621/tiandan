package com.sinosoft.fragins.management.po;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.Data;

/** 用户信息表 */
@Data
@Table(name = "sys_user")
@SuppressWarnings("serial")
public class SysUser implements java.io.Serializable {

	/**  */
	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	private java.lang.Long id;

	/**  */
	private java.lang.String userCode;

	/**  */
	private java.lang.String userName;

	/**  */
	private java.lang.String email;

	/**  */
	private java.lang.String mobilePhone;

	/**  */
	private java.lang.String password;

	/**  */
	private java.lang.String salt;

	/**  */
	private java.lang.String secondValidate;

	/**  */
	private java.lang.String gaCode;

	/**  */
	private java.lang.String question;

	/**  */
	private java.lang.String answer;

	/**  */
	private java.lang.String sex;

	/**  */
	private java.lang.String comCName;

	/**  */
	private java.util.Date birthday;

	/**  */
	private java.lang.String comCode;

	/**  */
	private java.util.Date regTime;

	/**  */
	private java.lang.Long lastLoginFailed;

	/**  */
	private java.util.Date lastTime;

	/**  */
	private java.lang.String lastIp;

	/**  */
	private java.lang.String type;

	/**  */
	private java.lang.String accountId;

	/**  */
	private java.lang.String qq;

	/**  */
	private java.lang.String officePhone;

	/**  */
	private java.lang.String homePhone;

	/**  */
	private java.lang.String checked;

	/**  */
	private java.lang.String age;

	/**  */
	private java.lang.String operators;

	/**  */
	private java.util.Date passwordSetDate;

	/**  */
	private java.util.Date passwordExpireDate;

	/**  */
	private java.lang.String address;

	/**  */
	private java.lang.String postCode;

	/**  */
	private java.lang.String validStatus;

	/**  */
	private java.lang.Long version;

	/**  */
	private java.util.Date insertTimeForHis;

	/**  */
	private java.util.Date operateTimeForHis;

	/**  */
	private java.lang.String versionId;

}