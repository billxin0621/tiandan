package com.sinosoft.fragins.management.vo.DataClear;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/** 数据清理 */
@Data
@Table(name = "data_clear")
@SuppressWarnings("serial")
public class DataClear implements java.io.Serializable {

	/** 主键ID */
	@Id
	private Long id;

	/** 上传批次号 */
	private String uploadBatchNo;

	/** 小保单号 */
	private String subPolicyNo;

	/** 状态 */
	private String state;

	/** 创建时间 */
	private Date createTime;

	/** 删除标志 */
	private Long deleteFlag;

	/** 创建时间 */
	private Date uploadTime;

	private BigDecimal amount;

	/**（1-未匹配，2-已匹配）*/
	private String matchState;


}