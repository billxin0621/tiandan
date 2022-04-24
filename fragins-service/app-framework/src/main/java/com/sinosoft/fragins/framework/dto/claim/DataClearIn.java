package com.sinosoft.fragins.framework.dto.claim;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author: Mingze.Li
 * @create: 2021/6/8
 **/
@Data
public class DataClearIn {
    /**
     * 上传批次号
     */
    @NotBlank(message = "上传批次号缺失")
    private String uploadBatchNo;

    @Valid
    @NotEmpty(message = "小保单信息缺失")
    private List<DataClearDto> dataClears;

}
