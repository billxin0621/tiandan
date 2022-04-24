package com.sinosoft.fragins.framework.enums;

import lombok.Getter;

/**
 * @author: Mingze.Li
 * @create: 2021/4/19
 **/
@Getter
public enum ProductConfigEnums {

    /**
     * 承保
     */

    TOGETGER_DAY("togetherDay", "按天维度聚合", "01"),
    GROUPPRO_CONFIG("groupProConfig", "承保/批改团单设置", "01"),
    HEALTH_RISK_INS("healthRiskIns", "被保险人扩展字段", "01"),
    YAOQI_AMOUNT("SumInsuredChg", "批增保额", "02"),
    YAOQI_CHECK("checkType", "对账方式（保单对账走批单对账）", "01"),
    YAOKANGFU_PG_STATUS("pgStatusMq", "是否发送批改状态mq", "01"),
    OPEN_POLICY_INSURED_AMOUNT("openPolicyInsuredAmount", "是否限制保单下被保人的保额", "01"),
    NEW_MEDICINELIST("NewMedicineList", "新药康复ICD10信息至理赔流程匹配", "01"),


    /**
     * 批改
     */
    CLAIM_SURRENDER("claimSurrender", "在白名单内，理赔后也允许退保", "01"),

    /**
     * 理赔
     */

    UNEXPECTED_HEALTH("unexpectedHealth", "意健险字段设置，异步交互", "01"),
    FAST_COMPENSATE("fastCompensate", "直接快赔，不需要走快赔规则", "01"),
    FAST_CLAIM_2("fastClaim", "快赔规则，北京京东世纪贸易有限公司", "02"),
    FAST_CLAIM_3("fastClaim", "快赔规则，北京京东世纪信息技术有限公司", "03"),
    FAST_CLAIM_4("fastClaim", "快赔规则，南京友穗", "04"),
    DOWNLOAD_PICTURES("downloadPictures", "理赔不需要下载图片并上传影像", "01"),
    CLAIM_AUDIT("claimAudit", "支付成功后修改支付状态，发送京喜mq", "01"),
    CLAIM_AUDIT_2("claimAudit", "支付成功后修改支付状态，发送APISmq", "02"),
    CLAIM_MATCH("claimMatch", "暂停，不推送理赔后续流程", "01"),
    CLAIM_MATCH_2("claimMatch", "暂停，不推送理赔后续流程，如果已收付则推送", "02"),
    CLAIM_MATCH_3("claimMatch", "暂停，不推送理赔后续流程，如果已收付资金池满足则推送", "03"),
    MODE_PAYMENT("modePayment", "银企直联 + 合并支付", "01"),
    BUSINESS_TYPE_7("businessType", "药康付理赔批次号生成规则", "07"),

    CLAIM_PROOF("claimProof", "不可重复理赔，理赔金额不能超200", "01"),
    CLAIM_PROOF_2("claimProof", "不可重复理赔，理赔金额不能超保额", "02"),
    CLAIM_PROOF_3("claimProof", "可以重复理赔，理赔金额不能超小保单保额", "03"),
    CLAIM_PROOF_4("claimProof", "可以重复理赔，理赔总金额不能超保单保额", "04"),
    CLAIM_PROOF_5("claimProof", "不可以重复理赔，小保单仅限理赔一次", "05"),
    CLAIM_PROOF_6("claimProof", "不可以重复理赔，小保单仅限理赔一次", "06"),

    CLAIM_CHECK_TIMES_1("claimCheckTimes", "已有案件开案，还在审核中或审核完成，其他案件不能开案", "01"),
    ANTI_MONEY_1("antiMoney", "反洗钱信息必录校验", "01"),
    CLAIM_TYPE_1("claimType", "是否解析理赔类型", "01"),
    CLAIM_AGREE_RULER_1("claimAgreeRuler", "是否解析理赔类型", "01"),
    CLAIM_AGREE_RULER_2("claimAgreeRuler", "是否解析理赔类型", "02"),
    PAYEEINFO_1("payeeinfo", "是否解析理赔类型", "01"),

    BATCH_CREATE_RULE("batchCreateRule", "理赔批次号生成规则（使用理赔申请ID）", "01"),
    BATCH_CREATE_RULE_2("batchCreateRule", "理赔批次号生成规则（使用抖音理赔批次号）", "02"),
    CLAIM_DYBATCH_RULE("claimDYBatch", "校验是否上传抖音理赔批次号", "01"),
    JUDGE_OTHERCAUSE("judgeOtherCause", "被保险人和领款人不一致则判断是否是例外事项垫/预付赔款", "01"),
    DEFAULT_LOSS_AMOUNT("defaultLossAmount", "是否特殊处理损失金额为理赔总金额", "01"),
    DY_OTHER_CASE("dyOtherCase", "例外事项18保险合同约定赔偿", "01"),
    COMPENSATION_PROCESS("compensationProcess", "理赔补偿流程（碎屏）", "01"),
    SET_EXTERNALID("setExternalID", "由投保订单号生成额外案件号", "01"),
    SET_DAMAGEADDRESS("setDamageAddress", "自定义出险地址", "01"),
    SET_STEPSTATUS("setStepStatus", "理赔大于1万元，进入理赔暂存状态", "01"),
    SET_TIME("setTime", "设置理赔时间报案时间", "01"),
    SET_OTHERCAUSE("setOtherCause", "例外事项垫/预付赔款", "01"),
    /**
     * 对账
     */

    SEND_NO_JX("sendnojx", "非京喜对账MQ", "01"),
    AUTO_CHECK("autoCheck", "自动对账", "01"),
    CHARGE_AGAINST("chargeAgainst", "冲销流程", "01"),
//    CAPITAL_POOL("capitalPool", "资金池业务，该产品理赔总金额必须小于已收付金额", "01")
    SEQNO_DIGIT("seqNoDigit", "批改序号位数", "01"),



    ;

    private final String configType;

    private final String name;

    private final String configCode;

    ProductConfigEnums(String configType, String name, String configCode) {
        this.configType = configType;
        this.name = name;
        this.configCode = configCode;
    }


}
