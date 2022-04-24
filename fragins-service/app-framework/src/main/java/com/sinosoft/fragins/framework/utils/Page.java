package com.sinosoft.fragins.framework.utils;

import java.util.ArrayList;
import java.util.Collection;

public class Page<E>
        extends ArrayList<E>
{
  private static final long serialVersionUID = 1L;
  private Paginator paginator;

  public Page() {}

  public Page(Collection<? extends E> c)
  {
    super(c);
  }

  public Page(Collection<? extends E> c, Paginator p)
  {
    super(c);
    this.paginator = p;
  }

  public Page(Paginator p)
  {
    this.paginator = p;
  }

  public Paginator getPaginator()
  {
    return this.paginator;
  }

  public int getPageSize()
  {
    if (this.paginator != null) {
      return this.paginator.getLimit();
    }
    return 0;
  }

  public int getPageNo()
  {
    if (this.paginator != null) {
      return this.paginator.getPage();
    }
    return 0;
  }

  public int getTotalCount()
  {
    if (this.paginator != null) {
      return this.paginator.getTotalCount();
    }
    return 0;
  }

  public boolean equals(Object obj)
  {
    if (!super.equals(obj)) {
      return false;
    }
    if (obj.getClass() != getClass()) {
      return false;
    }
    Page<E> fobj = (Page)obj;
    if (this.paginator != null) {
      return this.paginator.equals(fobj.getPaginator());
    }
    return false;
  }

  public int hashCode()
  {
    if (this.paginator != null) {
      return getPaginator().hashCode();
    }
    return super.hashCode();
  }
}
