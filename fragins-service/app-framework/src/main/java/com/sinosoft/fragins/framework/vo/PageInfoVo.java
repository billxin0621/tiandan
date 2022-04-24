package com.sinosoft.fragins.framework.vo;

import java.io.Serializable;

import com.github.pagehelper.PageRowBounds;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class PageInfoVo implements Serializable {

	/** 页数，从1开始 */
	private int pageNum = 1;
	/** 每页条数 */
	private int pageSize = 10;

	public PageInfoVo() {
	}

	public PageInfoVo(int pageNum, int pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	/** 转换成Dao用的分页条件 */
	public PageRowBounds toPageRowBounds() {
		int offset = (pageNum - 1) * pageSize;
		int limit = pageSize;
		return new PageRowBounds(offset, limit);
	}

	public int getPageNo() {
		return pageNum;
	}

	public void setPageNo(int pageNo) {
		this.pageNum = pageNo;
	}

}
