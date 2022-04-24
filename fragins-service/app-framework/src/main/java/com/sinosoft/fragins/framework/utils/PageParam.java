package com.sinosoft.fragins.framework.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

public class PageParam
        extends RowBounds
        implements Serializable
{
  private static final long serialVersionUID = 1L;
  private int totalCount;
  public static final int NO_PAGE = 1;
  protected int page = 1;
  protected int limit = 2147483647;
  private List<Order> orders = new ArrayList();
  private boolean containsTotalCount;

  public PageParam()
  {
    this.containsTotalCount = false;
  }

  public PageParam(RowBounds rowBounds)
  {
    if ((rowBounds instanceof PageParam))
    {
      PageParam pageBounds = (PageParam)rowBounds;
      this.page = pageBounds.page;
      this.limit = pageBounds.limit;
      this.orders = pageBounds.orders;
      this.containsTotalCount = pageBounds.containsTotalCount;
    }
    else
    {
      this.page = (rowBounds.getOffset() / rowBounds.getLimit() + 1);
      this.limit = rowBounds.getLimit();
    }
  }

  public PageParam(int limit)
  {
    this.limit = limit;
    this.containsTotalCount = false;
  }

  public PageParam(int page, int limit)
  {
    this(page, limit, new ArrayList(), true);
  }

  public PageParam(int page, int limit, boolean containsTotalCount)
  {
    this(page, limit, new ArrayList(), containsTotalCount);
  }

  public PageParam(List<Order> orders)
  {
    this(1, 2147483647, orders, false);
  }

  public PageParam(Order... order)
  {
    this(1, 2147483647, order);
    this.containsTotalCount = false;
  }

  public PageParam(int page, int limit, Order... order)
  {
    this(page, limit, Arrays.asList(order), true);
  }

  public PageParam(int page, int limit, List<Order> orders)
  {
    this(page, limit, orders, true);
  }

  public PageParam(int page, int limit, List<Order> orders, boolean containsTotalCount)
  {
    this.page = page;
    this.limit = limit;
    this.orders = orders;
    this.containsTotalCount = containsTotalCount;
  }

  public int getTotalCount()
  {
    return this.totalCount;
  }

  public void setTotalCount(int totalCount)
  {
    this.totalCount = totalCount;
  }

  public int getPage()
  {
    return this.page;
  }

  public void setPage(int page)
  {
    this.page = page;
  }

  public int getLimit()
  {
    return this.limit;
  }

  public void setLimit(int limit)
  {
    this.limit = limit;
  }

  public boolean isContainsTotalCount()
  {
    return this.containsTotalCount;
  }

  public void setContainsTotalCount(boolean containsTotalCount)
  {
    this.containsTotalCount = containsTotalCount;
  }

  public List<Order> getOrders()
  {
    return this.orders;
  }

  public void setOrders(List<Order> orders)
  {
    this.orders = orders;
  }

  public int getOffset()
  {
    if (this.page >= 1) {
      return (this.page - 1) * this.limit;
    }
    return 0;
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder("PageBounds{");
    sb.append("page=").append(this.page);
    sb.append(", limit=").append(this.limit);
    sb.append(", orders=").append(this.orders);
    sb.append(", totalCount=").append(this.totalCount);
    sb.append(", containsTotalCount=").append(this.containsTotalCount);
    sb.append('}');
    return sb.toString();
  }
}
