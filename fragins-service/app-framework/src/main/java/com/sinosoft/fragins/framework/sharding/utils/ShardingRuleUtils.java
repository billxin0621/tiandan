package com.sinosoft.fragins.framework.sharding.utils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.shardingsphere.core.rule.DataNode;
import org.apache.shardingsphere.core.rule.TableRule;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yanzhaohu
 * @date 2022-01-04
 */
public class ShardingRuleUtils {
    /**
     * 刷新配置规则
     * @param dataSourceName
     * @param tableRule
     * @param newDataNodes
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void dynamicRefreshDatasource(String dataSourceName, TableRule tableRule, List<DataNode> newDataNodes)
            throws NoSuchFieldException, IllegalAccessException {
        Set<String> actualTables = Sets.newHashSet();
        Map<DataNode, Integer> dataNodeIndexMap = Maps.newHashMap();
        AtomicInteger index = new AtomicInteger(0);
        newDataNodes.forEach(dataNode -> {
            actualTables.add(dataNode.getTableName());
            if (index.intValue() == 0) {
                dataNodeIndexMap.put(dataNode, 0);
            } else {
                dataNodeIndexMap.put(dataNode, index.intValue());
            }
            index.incrementAndGet();
        });
        // 动态刷新：actualDataNodesField
        Field actualDataNodesField = TableRule.class.getDeclaredField("actualDataNodes");
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(actualDataNodesField, actualDataNodesField.getModifiers() & ~Modifier.FINAL);
        actualDataNodesField.setAccessible(true);
        actualDataNodesField.set(tableRule, newDataNodes);
        // 动态刷新：actualTablesField
        Field actualTablesField = TableRule.class.getDeclaredField("actualTables");
        actualTablesField.setAccessible(true);
        actualTablesField.set(tableRule, actualTables);
        // 动态刷新：dataNodeIndexMapField
        Field dataNodeIndexMapField = TableRule.class.getDeclaredField("dataNodeIndexMap");
        dataNodeIndexMapField.setAccessible(true);
        dataNodeIndexMapField.set(tableRule, dataNodeIndexMap);
        // 动态刷新：datasourceToTablesMapField
        Map<String, Collection<String>> datasourceToTablesMap = Maps.newHashMap();
        datasourceToTablesMap.put(dataSourceName, actualTables);
        Field datasourceToTablesMapField = TableRule.class.getDeclaredField("datasourceToTablesMap");
        datasourceToTablesMapField.setAccessible(true);
        datasourceToTablesMapField.set(tableRule, datasourceToTablesMap);
    }
}
