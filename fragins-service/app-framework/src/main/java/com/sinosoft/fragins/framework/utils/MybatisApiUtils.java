package com.sinosoft.fragins.framework.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class MybatisApiUtils
{
  public static final int DEFAULT_PAGE_SIZE = 10;
  public static final int MAX_PAGE_SIZE = 3000;

  public static PageParam getPageParam()
  {
    ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

    HttpServletRequest request = null;
    if (attributes != null) {
      request = attributes.getRequest();
    }
    return getPageParam(request);
  }

  public static PageParam getPageParam(HttpServletRequest request)
  {
    int inPageNo = getParameterValue(request, "_pageNo", 1);
    if (inPageNo < 1) {
      inPageNo = 1;
    }
    int inPageSize = getParameterValue(request, "_pageSize", 10);
    if (inPageSize < 0) {
      inPageSize = 10;
    }
    if (inPageSize > 3000) {
      throw new IllegalArgumentException("pageSize exceeded maxPageSize[3000]");
    }
    PageParam pageParam = new PageParam(inPageNo, inPageSize);

    List<Order> orders = getListOrders(request);
    if (orders != null) {
      pageParam.setOrders(orders);
    }
    int inTotalCount = getParameterValue(request, "_totalCount", 0);
    if (inTotalCount > 0) {
      pageParam.setContainsTotalCount(false);
    }
    return pageParam;
  }

  public static List<Order> getListOrders(HttpServletRequest request)
  {
    if (request == null) {
      return null;
    }
    Enumeration<String> parameters = request.getParameterNames();
    List<Order> orders = null;
    while (parameters.hasMoreElements())
    {
      String parameterKey = (String)parameters.nextElement();
      if (parameterKey.contains("orders"))
      {
        int initIndex = 0;
        StringBuilder currentCommon = new StringBuilder("orders[");
        int commonLength = currentCommon.length();




        Order.Direction currentDir = null;
        while (initIndex != -1)
        {
          String currentKey = initIndex + "][property]";

          String currentProperty = request.getParameter(currentKey);
          if (currentProperty == null) {
            break;
          }
          if (initIndex == 0) {
            orders = new ArrayList();
          }
          currentKey = initIndex + "][direction]";
          String currentDirection = request.getParameter(currentKey);
          if (currentDirection != null) {
            currentDir = Order.Direction.fromString(currentDirection);
          }
          currentKey = initIndex + "][orderExpr]";
          initIndex++;
          String currentExp = request.getParameter(currentKey);
          Order order = new Order(currentProperty, currentDir, currentExp);
          orders.add(order);
        }
      }
    }
    return orders;
  }

  public static int getParameterValue(HttpServletRequest request, String name, int def)
  {
    if (request == null) {
      return def;
    }
    String str = request.getParameter(name);
    int value = def;
    if (str != null) {
      value = Integer.parseInt(str, 10);
    }
    return value;
  }

  public String getParameterValue(HttpServletRequest request, String name, String def)
  {
    if (request == null) {
      return def;
    }
    String str = request.getParameter(name);
    String value = def;
    if (str != null) {
      value = str;
    }
    return value;
  }
}
