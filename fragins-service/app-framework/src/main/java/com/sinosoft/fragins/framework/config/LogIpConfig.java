package com.sinosoft.fragins.framework.config;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author: Mingze.Li
 * @create: 2021-03-02 17:01
 **/
@Slf4j
public class LogIpConfig extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("获取服务器IP异常,{}", e);
        }
        return "";
    }

}
