package com.sinosoft.fragins.framework.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * 核心理赔产品统计
 */
@Data
@Accessors(chain = true)
public class PolicyRecord implements java.io.Serializable {

    /**
     * 老碎管实收保单总保额（已实收对账任务下所有保单总额）
     */
    private BigDecimal sumOldPolicyAmount;

    /**
     * 老碎管已赔付金额（所有已理赔订单总金额）
     */
    private BigDecimal sumOldClaimAmount;

    /**
     * 新碎管承保实收保单总保额（已实收对账任务下所有保单总额）
     */
    private BigDecimal sumPolicyAmount;

    /**
     * 新碎管理赔已赔付金额（所有已理赔订单总金额）
     */
    private BigDecimal sumClaimAmount;

    /**
     * 老碎管包含任务号
     */
    private List<String> checkTaskNoForOld;

    /**
     * 新碎管承保包含任务号
     */
    private List<String> checkTaskNo;

}