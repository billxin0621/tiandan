package com.sinosoft.fragins.framework.po;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 理赔渠道产品责任配置表
 */
@Data
@Table(name = "pfs_medicine_code")
@SuppressWarnings("serial")
public class PfsMedicineCode implements java.io.Serializable {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品名
     */
    private String tradeName;
    /**
     * 通用名
     */
    private String commonName;
    /**
     * 药企
     */
    private String companyName;
    /**
     * 一级分类
     */
    private String primaryClassification;
    /**
     * 适应症
     */
    private String adaptationDisease;
    /**
     * ICD-10代码
     */
    private String icdCode;
    /**
     * ICD-10名称
     */
    private String icdName;
    /**
     * 药品规格
     */
    private String drugSpecifications;
    /**
     * SKU
     */
    private String sku;
    /**
     * 参考价格
     */
    private String price;
    /**
     * 每盒补贴取整
     */
    private String subsidy;


}