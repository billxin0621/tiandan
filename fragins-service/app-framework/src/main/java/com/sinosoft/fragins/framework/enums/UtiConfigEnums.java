package com.sinosoft.fragins.framework.enums;

import lombok.Getter;

/**
 * @author: JingYang.Zhang
 * @create: 2021/06/23
 **/
@Getter
public enum UtiConfigEnums {

    /**
     * 承保
     */
    //保单-被保人，保额
    POLICY_INSURED_AMOUNT( "policyInsuredAmount");

    private final String configKey;

    UtiConfigEnums(String configKey) {
        this.configKey = configKey;
    }
}
