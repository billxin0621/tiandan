package com.sinosoft.fragins.framework.utils;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * 将ResultSet转换为Iterable接口，主要用于JXLS导出大量数据的场景
 * 
 * @author panyu
 *
 * @param <T>
 */
public class ResultSetIterable<T> implements Iterable<T> {

	private ResultSet rs;
	private Function<ResultSet, T> rowMapFunction;

	public ResultSetIterable(ResultSet rs, Function<ResultSet, T> rowMapFunction) {
		this.rs = rs;
		this.rowMapFunction = rowMapFunction;
	}

	@Override
	public Iterator<T> iterator() {
		return new ResultSetIterator();
	}

	class ResultSetIterator implements Iterator<T> {

		T cachedObj = null;
		boolean rsClosed = false;

		@Override
		public boolean hasNext() {
			try {
				boolean hasNext = true;
				if (cachedObj == null) {
					if (rsClosed) {
						return false;
					}
					hasNext = rs.next();
					if (hasNext) {
						cachedObj = rowMapFunction.apply(rs);
					} else {
						rsClosed = true;
						rs.close();
					}
				}
				return hasNext;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public T next() {
			if (hasNext()) {
				T obj = cachedObj;
				cachedObj = null;
				return obj;
			} else {
				throw new NoSuchElementException();
			}
		}

	}

}
