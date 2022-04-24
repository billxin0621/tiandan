package com.sinosoft.fragins.management.dto.dataClear;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @Description: 数据清理文件上传
 * @Author:
 * @Date:
 **/
@Data
public class DataClearUploadFile implements Serializable {
    private MultipartFile file;
}
