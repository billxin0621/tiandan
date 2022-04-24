package com.sinosoft.fragins.management.vo.DataClear;

import lombok.Data;

/** 数据清理 */
@Data
@SuppressWarnings("serial")
public class DataClearVo implements java.io.Serializable {

	/** 上传批次号 */
	private String uploadBatchNo;

	/** 条数 */
	private String count;

	/** 状态 */
	private String state;

	/** 创建时间 */
	private String uploadTime;



}