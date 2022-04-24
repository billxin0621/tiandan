package com.sinosoft.fragins.framework.vo;

import com.sinosoft.fragins.framework.vo.PageInfoVo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("serial")
public class EmailDetailVo extends PageInfoVo implements Serializable {

	private Date businessDate;
	private String businessType;
	private String businessNo;
	private String productType;
	private String planCode;
	private String batchNo;
	private String emailDigest;
	private String emailClass;
	private String emailMessage;

}
