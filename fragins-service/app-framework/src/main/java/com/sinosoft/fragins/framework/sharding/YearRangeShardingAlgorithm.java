package com.sinosoft.fragins.framework.sharding;

import com.google.common.collect.Range;
import com.sinosoft.fragins.framework.utils.DateUtil;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;

/**
 * @author yanzhaohu
 * @date 2022-01-04
 * 自定义sharding 范围分表策略
 */
public class YearRangeShardingAlgorithm implements RangeShardingAlgorithm<Date> {

    private static final Logger logger = LoggerFactory.getLogger(YearRangeShardingAlgorithm.class);

    public static final int MONTH_MIDDLE = 7;

    @Override
    public Collection<String> doSharding(Collection availableTargetNames, RangeShardingValue shardingValue) {
        Range<Date> shardingKey = shardingValue.getValueRange();
        String tableNeame = shardingValue.getLogicTableName();
        Date startDate = shardingKey.lowerEndpoint();
        Date endDate = shardingKey.upperEndpoint();
        logger.info("计算分表：{},时间范围：{},{}",tableNeame,startDate,endDate);
        return getShardingTables(tableNeame,startDate,endDate);
    }

    private Collection<String> getShardingTables( String tableNeame,Date startDate,Date endDate){
        Collection<String> result = new LinkedHashSet<String>();
        int startYear = DateUtil.getYear(startDate);
        int endYear = DateUtil.getYear(endDate);
        int diffYear = endYear - startYear +1;
        for(int i=0;i<diffYear;i++){
            result.add(tableNeame+"_"+(startYear+i));
        }
        logger.info("计算分表结果：{}",result);
        return  result;
    }

}
