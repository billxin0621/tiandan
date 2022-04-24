package com.sinosoft.fragins.framework.vo;

import com.sinosoft.fragins.framework.config.AppContext;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 基础返回类
 *
 * @author Mingze.Li
 */
@Data
public class BaseResult<T> implements Serializable {
    /**
     * 是否成功
     */
    private Boolean status = true;
    /**
     * 响应码
     */
    private Integer appCode;
    /**
     * 响应信息
     */
    private String appMessage;

    /**
     * 请求交易流水号
     */
    private String consumerSeqNo;

    /**
     * 响应交易流水号
     */
    private String providerSeqNo;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 成功
     *
     * @param <T>
     * @return
     */
    public static <T> BaseResult<T> ok() {
        return build(StatusCode.OK, null);
    }

    /**
     * 成功
     *
     * @param data 结果
     * @param <T>
     * @return
     */
    public static <T> BaseResult<T> ok(T data) {
        return build(StatusCode.OK, data);
    }

    /**
     * 失败
     *
     * @param <T>
     * @return
     */
    public static <T> BaseResult<T> fail() {
        return build(StatusCode.FAIL, null);
    }

    public static <T> BaseResult<T> fail(StatusCode status, String errorMessage) {
        return build(status, errorMessage);
    }

    public static <T> BaseResult<T> fail(StatusCode status, String errorMessage, T data) {
        return build(status, errorMessage, data);
    }

    public static <T> BaseResult<T> fail(StatusCode status) {
        return build(status, null);
    }

    /**
     * 失败
     *
     * @param errorMessage 错误信息
     * @param <T>
     * @return
     */
    public static <T> BaseResult<T> fail(String errorMessage) {
        return build(StatusCode.FAIL, errorMessage, null);
    }

    /**
     * 失败
     *
     * @param data 结果
     * @param <T>
     * @return
     */
    public static <T> BaseResult<T> fail(T data) {
        return build(StatusCode.FAIL, data);
    }

    /**
     * 构建
     *
     * @param status 状态
     * @param <T>
     * @return
     */
    public static <T> BaseResult<T> build(StatusCode status) {
        return build(status, null);
    }

    /**
     * 构建
     *
     * @param status 状态
     * @param data   结果
     * @param <T>
     * @return
     */
    public static <T> BaseResult<T> build(StatusCode status, T data) {
        return build(status, null, data);
    }

    /**
     * 构建
     *
     * @param status       状态
     * @param errorMessage 错误信息
     * @param <T>
     * @return
     */
    public static <T> BaseResult<T> build(StatusCode status, String errorMessage) {
        return build(status, errorMessage, null);
    }

    /**
     * 构建
     *
     * @param status       状态
     * @param errorMessage 错误信息
     * @param data         结果
     * @param <T>
     * @return
     */
    public static <T> BaseResult<T> build(StatusCode status, String errorMessage, T data) {
        BaseResult<T> result = new BaseResult<>();
        result.setStatus(status == StatusCode.OK);
        result.setAppCode(status.getCode());
        result.setAppMessage(StringUtils.isNotBlank(errorMessage) ? errorMessage : status.getMessage());
        result.setData(data);
        result.setConsumerSeqNo(AppContext.get("CONSUMER_SEQ_NO"));
        result.setProviderSeqNo(AppContext.get("LOG_UID"));
        return result;
    }

    public void status(StatusCode status) {
        this.setStatus(status == StatusCode.OK);
        this.setAppCode(status.getCode());
        this.setAppMessage(status.getMessage());
    }
}
