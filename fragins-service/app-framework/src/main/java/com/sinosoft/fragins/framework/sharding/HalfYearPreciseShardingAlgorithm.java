package com.sinosoft.fragins.framework.sharding;

import com.sinosoft.fragins.framework.utils.DateUtil;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;

/**
 * @author yanzhaohu
 * @date 2022-01-04
 * 自定义sharding分表策略
 * 每半年一张表
 */
public class HalfYearPreciseShardingAlgorithm implements PreciseShardingAlgorithm<Date> {

    private static final Logger logger = LoggerFactory.getLogger(HalfYearPreciseShardingAlgorithm.class);

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> shardingValue) {
        // 获取基本的表名
        String tableNeame = shardingValue.getLogicTableName();
        // 获取分片的时间
        Date date = shardingValue.getValue();
        int year = DateUtil.getYear(date);
        int month = DateUtil.getMonth(date);
        if(month < HalfYearRangeShardingAlgorithm.MONTH_MIDDLE){
            logger.info("精确分表,时间：{},分表结果:{}_{}06",date,tableNeame,year);
            return tableNeame+"_"+year+"06";
        }
        logger.info("精确分表,时间：{},分表结果:{}_{}12",date,tableNeame,year);
        return tableNeame+"_"+year+"12";
    }

}
