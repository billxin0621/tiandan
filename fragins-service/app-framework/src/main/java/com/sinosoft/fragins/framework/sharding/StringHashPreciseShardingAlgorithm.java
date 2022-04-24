package com.sinosoft.fragins.framework.sharding;

import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;

/**
 * @author yanzhaohu
 * @date 2022-01-05
 * 字符串计算哈希,分表
 */
public class StringHashPreciseShardingAlgorithm implements PreciseShardingAlgorithm<String> {
    private static final Logger logger = LoggerFactory.getLogger(StringHashPreciseShardingAlgorithm.class);

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        int size = availableTargetNames.size();
        String requestId = shardingValue.getValue();
        int hashValue = Math.abs(requestId.hashCode());
        int index = hashValue % size;
        String logicTableName = shardingValue.getLogicTableName();
        logger.info("String val:{},String hashValue:{} % size: {} = index:{},logicTableName:{}",requestId,hashValue,size,index,logicTableName);
        return logicTableName+"_"+index;
    }
}
