package com.sinosoft.fragins.framework.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

/**
 * 日期时间相关的工具类
 * 
 * @author panyu
 *
 */
public class DateCalendarUtils {

	/**
	 * 去掉日期中的时分秒部分，到当天0点整
	 * 
	 * @param d 日期
	 * @return 到0点整的一个新的日期对象
	 */
	public static Date truncateToDay(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(d.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 将日期变为 当天 23：59：59
	 *
	 * @param d 日期
	 * @return 到0点整的一个新的日期对象
	 */
	public static Date dayOfEnd(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(d.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 在日期基础上加天数
	 *
	 * @param d      原日期
	 * @param amount 加几天（可以是负数）
	 * @return 加过天数后的新日期
	 */
	public static Date addDay(Date d, int amount) {
		return add(d, Calendar.DATE, amount);
	}

	/**
	 * 日期上加减相关字段的方法，实际用Calendar操作
	 * 
	 * @param d      原日期
	 * @param field  Calendar类中的字段参数，如Calendar.DATE
	 * @param amount 加的数量（负数相当于减）
	 * @return 加减后日期
	 */
	public static Date add(Date d, int field, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(d.getTime());
		cal.add(field, amount);
		return cal.getTime();
	}

	/**
	 * 获取日期中的某字段，实际用Calendar操作
	 * 
	 * @param d     日期
	 * @param field Calendar类中的字段参数，如Calendar.DATE
	 * @return 获取到的值
	 */
	public static int get(Date d, int field) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(d.getTime());
		return cal.get(field);
	}

	/**
	 * 设置日期中的某字段，实际用Calendar操作
	 * 
	 * @param d     原日期
	 * @param field Calendar类中的字段参数，如Calendar.DATE
	 * @return 修改过相关字段的新日期，注意入参中的原日期不会被修改
	 */
	public static Date set(Date d, int field, int value) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(d.getTime());
		cal.set(field, value);
		return cal.getTime();
	}

	public static Date LocalDateParse(LocalDateTime dateTime) {
		return DateCalendarUtils.LocalDateParse(dateTime, false);
	}

	public static Date LocalDateParse(LocalDateTime dateTime, boolean isNow) {
		if (isNow) {
			return Date.from(Optional.ofNullable(dateTime).orElse(LocalDateTime.now()).atZone(ZoneId.systemDefault()).toInstant());
		} else {
			if (dateTime == null) {
				return null;
			} else {
				return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
			}
		}

	}

	public static Date addMonth(Date d, int amount) {
		return add(d, Calendar.MONTH, amount);
	}


}
