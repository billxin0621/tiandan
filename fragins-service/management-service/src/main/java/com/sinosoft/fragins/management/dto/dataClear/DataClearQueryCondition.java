package com.sinosoft.fragins.management.dto.dataClear;

import com.sinosoft.fragins.framework.vo.PageInfoVo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 待审核任务列表查询对象
 * @Author: qiangliangliang
 * @Date: 2020-03-18
 **/
@Data
public class DataClearQueryCondition  extends PageInfoVo implements Serializable {

    private String uploadBatchNo;
    private Date uploadBeginTime;
    private Date uploadEndTime;
//    /** 条数*/
//    private int pageSize;
//    /** 页码*/
//    private int pageNo;
//    private int pageNum;

}
