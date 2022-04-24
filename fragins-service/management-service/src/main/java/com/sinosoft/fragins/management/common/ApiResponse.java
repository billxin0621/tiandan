package com.sinosoft.fragins.management.common;

import com.sinosoft.fragins.framework.exception.BusinessException;

import java.io.Serializable;

/**
 * @Description: 返回前端封装对象
 * @Author: qiangliangliang
 * @Date: 2020-03-18
 **/
public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int SUCCESS = 0;
    private long status;
    private String statusText;
    private T data;

    public ApiResponse() {
        this.status = 0L;
        this.statusText = "Success";
    }

    public ApiResponse(long status, String statusText, T data) {
        this.status = status;
        this.statusText = statusText;
        this.data = data;
    }

    public ApiResponse(T data) {
        if (data instanceof BusinessException) {
            this.status = -2L;
            this.statusText = ((BusinessException)data).getMessage();
        } else if (data instanceof Exception) {
            this.status = -1L;
            this.statusText = ((Exception)data).getLocalizedMessage();
        } else {
            this.status = 0L;
            this.statusText = "Success";
            this.data = data;
        }

    }

    public long getStatus() {
        return this.status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getStatusText() {
        return this.statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
