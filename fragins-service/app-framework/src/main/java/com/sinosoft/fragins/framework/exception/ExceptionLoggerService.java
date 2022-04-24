package com.sinosoft.fragins.framework.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.fragins.framework.utils.SpringContextUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 记录异常信息到uti_exception_log表
 * 
 * @author panyu
 *
 */
@Slf4j
public class ExceptionLoggerService extends JdbcDaoSupport implements DisposableBean {

	private ExecutorService executorService = new ThreadPoolExecutor(4, 64, 60000, TimeUnit.MILLISECONDS,
			new ArrayBlockingQueue<Runnable>(1024));

	/**
	 * 异步记录异常
	 * 
	 * @param t            异常对象
	 * @param businessType 业务类型，查询用，由业务程序自己定义
	 */
	public void logAsync(Throwable t, String businessType) {
		logAsync(t, businessType, "UNKNOWN");
	}

	/**
	 * 异步记录异常
	 * 
	 * @param t            异常对象
	 * @param businessType 业务类型，查询用，由业务程序自己定义
	 * @param businessNo   业务号，查询用
	 */
	public void logAsync(Throwable t, String businessType, String businessNo) {
		try {
			ExceptionAnalyzeTask task = new ExceptionAnalyzeTask(t, new Date(), businessType, businessNo);
			executorService.submit(task);
		} catch (Exception e) {
			log.error("异常记录出错，可能是记录队列已满，忽略此次记录", e);
		}
	}

	/**
	 * 实际记录的程序，不要在业务程序中直接调用，影响事务
	 * 
	 * @param t            异常对象
	 * @param businessTime 异常发生时间
	 * @param businessType 业务类型，查询用，由业务程序自己定义
	 * @param businessNo   业务号，查询用
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void doLog(Throwable t, Date businessTime, String businessType, String businessNo) {
		try {
			String digest = calcDigest(t);
			String stackTrace = getStackTrace(t);
			String cls = StringUtils.abbreviate(t.getClass().getName(), 200);
			BusinessException be = BusinessException.getBusinessExceptionFromCause(t);
			String message = t.getMessage();
			if (be != null) {
				message = be.getMessage();
			}
			message = StringUtils.abbreviate(message, 400);
			String sql = "insert into uti_exception_log (business_time, business_type, business_no, exception_digest, exception_class, exception_message, exception_stack_trace) values (?, ?, ?, ?, ?, ?, ?)";
			getJdbcTemplate().update(sql, businessTime, businessType, businessNo, digest, cls, message, stackTrace);
		} catch (Exception e) {
			log.warn("异常记录时发生错误，忽略此次记录", e);
		}
	}

	/**
	 * 根据异常信息和堆栈，获取一个摘要值，同一个摘要值的异常应该是同一类异常。
	 * <p>
	 * 处理时会忽略多数异常的message（异常信息中可能有单号之类的信息，使生成的摘要值产生变化。SQLException除外，因为同样的SQLException可能是完全不同的异常）。
	 * 主要通过堆栈，行号等区分同类的异常。 另外堆栈中会忽略动态产生的代码（CGLIB或者PROXY等），避免动态生成的类名不同影响摘要值。
	 * 
	 * @param t
	 * @return
	 */
	private String calcDigest(Throwable t) {
		StringBuilder sb = new StringBuilder();
		Throwable c = t;
		while (c != null) {
			// 追加异常类型，部分类型的异常要追加异常信息
			sb.append(c.getClass().getName()).append(":");
			if (c instanceof SQLException) { // 注：SQL异常的信息还是需要归入不同分类的，mysql报错应该没有具体的字段值信息
				sb.append(c.getMessage());
			}
			sb.append("\n");
			// 追加堆栈信息
			StackTraceElement[] stackTrace = c.getStackTrace();
			for (StackTraceElement ste : stackTrace) {
				String text = ste.toString();
				// 对于动态编译的代码，因为其中包含动态类名，不作为分类内容
				if (text.endsWith("(<generated>)")) {
					text = "<generated>";
				} else if (text.endsWith("(Unknown Source)")) {
					text = "Unknown Source";
				}
				sb.append(text).append("\n");
			}
			if (c.getCause() == c) {
				break;
			} else {
				c = c.getCause();
			}
		}
		return DigestUtils.md5Hex(sb.toString());
	}

	private String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	@Override
	public void destroy() throws Exception {
		executorService.shutdown();
	}

	private class ExceptionAnalyzeTask implements Runnable {

		private Throwable t;
		private Date businessTime;
		private String businessType;
		private String businessNo;

		ExceptionAnalyzeTask(Throwable t, Date businessTime, String businessType, String businessNo) {
			this.t = t;
			this.businessTime = businessTime;
			this.businessType = businessType;
			this.businessNo = businessNo;
		}

		@Override
		public void run() {
			SpringContextUtils.getBean(ExceptionLoggerService.class).doLog(t, businessTime, businessType, businessNo);
		}

	}

}
