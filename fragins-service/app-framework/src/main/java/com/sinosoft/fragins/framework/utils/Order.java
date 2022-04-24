package com.sinosoft.fragins.framework.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class Order
        implements Serializable
{
  private static final long serialVersionUID = 8138022018100161833L;
  private Direction direction;
  private String property;
  private String orderExpr;
  private static final String INJECTION_REGEX = "[A-Za-z0-9\\_\\-\\+\\.]+";

  public Order(String property, Direction direction, String orderExpr)
  {
    this.direction = direction;
    this.property = property;
    this.orderExpr = orderExpr;
  }

  public Direction getDirection()
  {
    return this.direction;
  }

  public String getProperty()
  {
    return this.property;
  }

  public String getOrderExpr()
  {
    return this.orderExpr;
  }

  public void setDirection(Direction direction)
  {
    this.direction = direction;
  }

  public void setProperty(String property)
  {
    this.property = property;
  }

  public void setOrderExpr(String orderExpr)
  {
    this.orderExpr = orderExpr;
  }

  public static boolean isSQLInjection(String str)
  {
    return !Pattern.matches("[A-Za-z0-9\\_\\-\\+\\.]+", str);
  }

  public String toString()
  {
    if (isSQLInjection(this.property)) {
      throw new IllegalArgumentException("SQLInjection property: " + this.property);
    }
    if ((this.orderExpr != null) && (this.orderExpr.indexOf('?') != -1))
    {
      String[] exprs = this.orderExpr.split("\\?");
      if (exprs.length == 2) {
        return
                String.format(this.orderExpr.replaceAll("\\?", "%s"), new Object[] { this.property }) + (this.direction == null ? "" : new StringBuilder().append(" ").append(this.direction.name()).toString());
      }
      return String.format(this.orderExpr.replaceAll("\\?", "%s"), new Object[] { this.property, " " + this.direction
              .name() });
    }
    return this.property + (this.direction == null ? "" : new StringBuilder().append(" ").append(this.direction.name()).toString());
  }

  public static List<Order> formString(String orderSegment)
  {
    return formString(orderSegment, null);
  }

  public static List<Order> formString(String orderSegment, String orderExpr)
  {
    if ((orderSegment == null) || ("".equals(orderSegment.trim()))) {
      return new ArrayList(0);
    }
    List<Order> results = new ArrayList();
    String[] orderSegments = orderSegment.trim().split(",");
    for (int i = 0; i < orderSegments.length; i++)
    {
      String sortSegment = orderSegments[i];
      Order order = innerFormString(sortSegment, orderExpr);
      if (order != null) {
        results.add(order);
      }
    }
    return results;
  }

  private static Order innerFormString(String orderSegment, String orderExpr)
  {
    if ((orderSegment == null) || ("".equals(orderSegment.trim())) || (orderSegment.startsWith("null.")) ||
            (orderSegment.startsWith("."))) {
      return null;
    }
    String[] array = orderSegment.trim().split("\\.");
    if ((array.length != 1) && (array.length != 2)) {
      throw new IllegalArgumentException("orderSegment pattern must be {property}.{direction}, input is: " + orderSegment);
    }
    return create(array[0], array.length == 2 ? array[1] : "asc", orderExpr);
  }

  public static Order create(String property, String direction)
  {
    return create(property, direction, null);
  }

  public static Order create(String property, String direction, String orderExpr)
  {
    return new Order(property, Direction.fromString(direction), orderExpr);
  }

  public static enum Direction
  {
    ASC,  DESC;

    private Direction() {}

    public static Direction fromString(String value)
    {
      try
      {
        return valueOf(value.toUpperCase(Locale.US));
      }
      catch (Exception e) {}
      return ASC;
    }
  }
}
