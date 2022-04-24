package com.sinosoft.fragins.framework.aop;

import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class ShardingXATransactionAspect {

	@Before("@annotation(org.springframework.transaction.annotation.Transactional) || @within(org.springframework.transaction.annotation.Transactional)")
	public void beforeService(JoinPoint joinPoint) {
		log.debug("Setting TransactionType to BASE");
		TransactionTypeHolder.set(TransactionType.BASE);
	}

}
