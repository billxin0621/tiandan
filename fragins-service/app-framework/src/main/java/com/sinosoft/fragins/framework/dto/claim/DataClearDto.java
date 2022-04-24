package com.sinosoft.fragins.framework.dto.claim;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 白名单数据
 */
@Data
@Accessors(chain = true)
public class DataClearDto implements java.io.Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 小保单号
     */
    @NotBlank(message = "小保单号缺失")
    private String subPolicyNo;

    /**
     * 删除标志
     */
    private java.lang.Long deleteFlag;

    @NotNull(message = "小保单金额缺失")
    private BigDecimal amount;

    private String matchState;

    /**
     * 状态
     */
    private String state;

}