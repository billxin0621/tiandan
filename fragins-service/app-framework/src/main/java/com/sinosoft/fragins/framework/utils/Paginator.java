package com.sinosoft.fragins.framework.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Paginator
        implements Serializable
{
  private static final long serialVersionUID = -2429864663690465105L;
  private static final int DEFAULT_SLIDERS_COUNT = 7;
  private int limit;
  private int page;
  private int totalCount;

  public Paginator(int page, int limit, int totalCount)
  {
    this.limit = limit;
    this.totalCount = totalCount;
    this.page = computePageNo(page);
  }

  public int getPage()
  {
    return this.page;
  }

  public int getLimit()
  {
    return this.limit;
  }

  public int getTotalCount()
  {
    return this.totalCount;
  }

  public boolean isFirstPage()
  {
    return this.page <= 1;
  }

  public boolean isLastPage()
  {
    return this.page >= getTotalPages();
  }

  public int getPrePage()
  {
    if (isHasPrePage()) {
      return this.page - 1;
    }
    return this.page;
  }

  public int getNextPage()
  {
    if (isHasNextPage()) {
      return this.page + 1;
    }
    return this.page;
  }

  public boolean isDisabledPage(int page)
  {
    return (page < 1) || (page > getTotalPages()) || (page == this.page);
  }

  public boolean isHasPrePage()
  {
    return this.page - 1 >= 1;
  }

  public boolean isHasNextPage()
  {
    return this.page + 1 <= getTotalPages();
  }

  public int getStartRow()
  {
    if ((getLimit() <= 0) || (this.totalCount <= 0)) {
      return 0;
    }
    if (this.page > 0) {
      return (this.page - 1) * getLimit() + 1;
    }
    return 0;
  }

  public int getEndRow()
  {
    return this.page > 0 ? Math.min(this.limit * this.page, getTotalCount()) : 0;
  }

  public int getOffset()
  {
    return this.page > 0 ? (this.page - 1) * getLimit() : 0;
  }

  public int getTotalPages()
  {
    if (this.totalCount <= 0) {
      return 0;
    }
    if (this.limit <= 0) {
      return 0;
    }
    int count = this.totalCount / this.limit;
    if (this.totalCount % this.limit > 0) {
      count++;
    }
    return count;
  }

  protected int computePageNo(int page)
  {
    return computePageNumber(page, this.limit, this.totalCount);
  }

  public Integer[] getSlider()
  {
    return slider(7);
  }

  public Integer[] slider(int slidersCount)
  {
    return generateLinkPageNumbers(getPage(), getTotalPages(), slidersCount);
  }

  private static int computeLastPageNumber(int totalItems, int pageSize)
  {
    if (pageSize <= 0) {
      return 1;
    }
    int result = totalItems % pageSize == 0 ? totalItems / pageSize : totalItems / pageSize + 1;
    if (result <= 1) {
      result = 1;
    }
    return result;
  }

  private static int computePageNumber(int page, int pageSize, int totalItems)
  {
    if (page <= 1) {
      return 1;
    }
    if ((2147483647 == page) || (page > computeLastPageNumber(totalItems, pageSize))) {
      return computeLastPageNumber(totalItems, pageSize);
    }
    return page;
  }

  private static Integer[] generateLinkPageNumbers(int currentPageNumber, int lastPageNumber, int count)
  {
    int avg = count / 2;

    int startPageNumber = currentPageNumber - avg;
    if (startPageNumber <= 0) {
      startPageNumber = 1;
    }
    int endPageNumber = startPageNumber + count - 1;
    if (endPageNumber > lastPageNumber) {
      endPageNumber = lastPageNumber;
    }
    if (endPageNumber - startPageNumber < count)
    {
      startPageNumber = endPageNumber - count;
      if (startPageNumber <= 0) {
        startPageNumber = 1;
      }
    }
    List<Integer> result = new ArrayList();
    for (int i = startPageNumber; i <= endPageNumber; i++) {
      result.add(Integer.valueOf(i));
    }
    return (Integer[])result.toArray(new Integer[result.size()]);
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("Paginator");
    sb.append("{page=").append(this.page);
    sb.append(", limit=").append(this.limit);
    sb.append(", totalCount=").append(this.totalCount);
    sb.append('}');
    return sb.toString();
  }
}
