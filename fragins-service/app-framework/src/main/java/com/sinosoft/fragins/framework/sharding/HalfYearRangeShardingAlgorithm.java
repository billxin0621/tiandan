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
public class HalfYearRangeShardingAlgorithm implements RangeShardingAlgorithm<Date> {

    private static final Logger logger = LoggerFactory.getLogger(HalfYearRangeShardingAlgorithm.class);

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
        int startMonth = DateUtil.getMonth(startDate);
        // 如果开始时间在结束时间之后
        if(startDate.after(endDate)){
            String acTableName = tableNeame+"_"+startYear;
            if(startMonth < MONTH_MIDDLE){
                acTableName = acTableName+"06";
            }else{
                acTableName = acTableName+"12";
            }
            result.add(acTableName);
            logger.info("计算分表结果：{}",result);
            return result;
        }
        int endYear = DateUtil.getYear(endDate);
        int endMonth = DateUtil.getMonth(endDate);
        // 计算年的跨度
        int timeDiff = endYear - startYear + 1;
        for(int i=0;i<timeDiff;i++){
            String acTableName = tableNeame+"_"+(startYear+i);
            if(timeDiff == 1){
                if(startMonth < MONTH_MIDDLE && endMonth <MONTH_MIDDLE){
                    result.add(acTableName+"06");
                }else if(startMonth >= MONTH_MIDDLE && endMonth >= MONTH_MIDDLE ){
                    result.add(acTableName+"12");
                }else{
                    result.add(acTableName+"06");
                    result.add(acTableName+"12");
                }
            }else{
                if(i==0) {
                    if (startMonth < MONTH_MIDDLE) {
                        result.add(acTableName + "06");
                        result.add(acTableName + "12");
                    } else {
                        result.add(acTableName + "12");
                    }
                }else if(i == (timeDiff-1)){
                    result.add(acTableName + "06");
                    if (endMonth >= MONTH_MIDDLE) {
                        result.add(acTableName + "12");
                    }
                }else{
                    result.add(acTableName + "06");
                    result.add(acTableName + "12");
                }
            }
        }
        logger.info("计算分表结果：{}",result);
        return  result;
    }

}
