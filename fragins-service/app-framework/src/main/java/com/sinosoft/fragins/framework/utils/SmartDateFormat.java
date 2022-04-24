package com.sinosoft.fragins.framework.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

@SuppressWarnings("serial")
public class SmartDateFormat extends DateFormat {

	/** 如果时分秒是00:00:00是否只保留年月日部分 */
	private boolean truncZeroTime;

	public SmartDateFormat() {
		this(true);
	}

	public SmartDateFormat(boolean truncZeroTime) {
		this.truncZeroTime = truncZeroTime;
		this.calendar = Calendar.getInstance();
		this.numberFormat = new DecimalFormat();
	}

	@Override
	public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
		String value = DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
		if (truncZeroTime && value.endsWith("00:00:00") || value.endsWith("08:00:00")) {
			value = value.substring(0, 10);
		}
		return toAppendTo.append(value);
	}

	@Override
	public Date parse(String source, ParsePosition pos) {
		try {
			return DateUtils.parseDate(source, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Date parse(String source) throws ParseException {
		try {
			return DateUtils.parseDate(source, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
