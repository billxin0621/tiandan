package com.sinosoft.fragins.management.client;

import com.alibaba.fastjson.JSON;
import com.sinosoft.fragins.management.ManagementServiceApplication;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yanzhaohu
 * @date 2022-01-07
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ManagementServiceApplication.class)
public class SubClaimClientTest{


    @Test
    public void testQuery() {
        System.out.println(JSON.toJSON("aaaaaaaa"));
    }
}