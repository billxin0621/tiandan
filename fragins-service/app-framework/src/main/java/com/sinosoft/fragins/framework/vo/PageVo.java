package com.sinosoft.fragins.framework.vo;

import java.util.ArrayList;
import java.util.List;

import com.github.pagehelper.Page;
import com.sinosoft.fragins.framework.utils.BeanCopyUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
public class PageVo<T> extends PageInfoVo {

	/**
	 * 从MyBatis分页转换为应用分页对象
	 * 
	 * @param <V>            Vo类型
	 * @param <P>            Po类型
	 * @param page           MyBatis分页对象
	 * @param voClass        Vo类
	 * @param copyProperties 是否从Po到Vo对象进行属性复制（一般来说如果Po和Vo结构是类似的或者一致的，可以进行拷贝，如果不一致可以设为false返回的分页里面Vo对象需要自己处理）
	 * @return 应用的分页对象
	 */
	public static <V, P> PageVo<V> fromPage(Page<P> page, Class<V> voClass, boolean copyProperties) {
		PageVo<V> pageVo = new PageVo<>();
		pageVo.setPageNum(page.getPageNum());
		pageVo.setPageSize(page.getPageSize());
		pageVo.setStartRow(page.getStartRow());
		pageVo.setEndRow(page.getEndRow());
		pageVo.setTotalCount(page.getTotal());
		pageVo.setPages(page.getPages());
		for (Object entity : page) {
			V vo = null;
			if (copyProperties) {
				vo = BeanCopyUtils.clone(entity, voClass);
			} else {
				try {
					vo = voClass.newInstance();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			pageVo.getData().add(vo);
		}
		return pageVo;
	}

	/** 起始行 */
	private int startRow;
	/** 结束行 */
	private int endRow;
	/** 总数 */
	private long totalCount;
	/** 页数 */
	private int pages;
	/** 分页数据 */
	private List<T> data = new ArrayList<>();

}
