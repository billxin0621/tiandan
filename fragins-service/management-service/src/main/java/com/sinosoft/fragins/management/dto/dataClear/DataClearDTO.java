package com.sinosoft.fragins.management.dto.dataClear;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 数据清理
 * @Author: qiangliangliang
 * @Date: 2020-11-23
 **/
@Data
public class DataClearDTO implements Serializable {
    /** 上传批次号 */
    private String uploadBatchNo;
    /** 小保单号 */
    private String subPolicyNo;
    /** 状态 */
    private String state;
    /** 创建时间 */
    private String createTime;
}
