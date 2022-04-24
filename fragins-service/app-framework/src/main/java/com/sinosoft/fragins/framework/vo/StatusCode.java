package com.sinosoft.fragins.framework.vo;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 状态码枚举
 *
 * @author: Mingze.Li
 * @create: 2021-03-03 15:33
 **/
@Getter
public enum StatusCode {

    RUN(100, "进行中"),
    //成功
    OK(200, "成功"),
    NEED_LOGIN(401, "请重新登录"),
    PERMISSION_DENIED(403, "您无权进行此操作"),
    //失败,
    FAIL(500, "系统异常，请稍后重试"),
    LOSE(600, "失败"),

    //系统异常
    NOT_FOUND_PLAN_CODE(501, "计划代码未找到"),
    DATA_CHECK_ERR(502, "数据校验错误"),
    NUM_MAX(503, "数据处理失败，失败请求次数已达上限"),
    IN_HAND(504, "数据处理中"),
    MODE_PAYMENT(505, "支付方式类型错误"),
    CUSTOMER_TYPE(506, "客户类型错误"),
    CODE_NULL(507, "计划代码字段缺失"),
    OFFLINE(508, "计划代码未配置"),
    PRODUCT_TYPE_CODE(509, "产品类型代码未配置"),


    ;

    private final Integer code;
    private final String message;

    StatusCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    /**
     * 根据code获取状态码枚举
     *
     * @param code 状态code
     * @return
     */
    public static StatusCode getByCode(final Integer code) {
        Objects.requireNonNull(code, "StatusCodeEnum code require");
        return Arrays.stream(values()).filter(s -> s.getCode().equals(code)).findFirst().orElseThrow(IllegalArgumentException::new);
    }

}
