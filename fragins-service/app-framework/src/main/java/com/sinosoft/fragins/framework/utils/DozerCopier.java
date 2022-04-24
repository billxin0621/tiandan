package com.sinosoft.fragins.framework.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class DozerCopier
{
  private static final Logger log = LoggerFactory.getLogger(DozerCopier.class);
  
  public static SimpleCopier copy()
  {
    return new SimpleCopier();
  }
  
  public static DozerCopier copyDepth()
  {
    return new DozerCopier();
  }
  
  public static Object invokeValue(Object o, String field)
  {
    Object result = null;
    Method[] methods = o.getClass().getMethods();
    for (Method method : methods)
    {
      String m = method.getName();
      if (m.startsWith("get"))
      {
        String s = m.substring(3);
        if (s.equalsIgnoreCase(field)) {
          try
          {
            result = method.invoke(o, new Object[0]);
          }
          catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e)
          {
            log.warn("{}", e);
          }
        }
      }
    }
    return result;
  }
  
  public static Object invoke(Object o, Object field, Object value)
  {
    Method[] methods = o.getClass().getMethods();
    for (Method method : methods)
    {
      String m = method.getName();
      if (m.startsWith("set"))
      {
        String s = m.substring(3);
        if (s.equalsIgnoreCase(String.valueOf(field))) {
          try
          {
            method.invoke(o, new Object[] { value });
          }
          catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e)
          {
            log.warn("{}", e);
          }
        }
      }
    }
    return o;
  }
  
  public static void copy(Object from, Object to)
  {
    Method[] methods = from.getClass().getMethods();
    Map<String, Object> fromObj = new HashMap();
    for (Method method : methods)
    {
      String m = method.getName();
      if (m.startsWith("get")) {
        try
        {
          Object result = method.invoke(from, new Object[0]);
          if (result != null)
          {
            String field = m.substring(3).toLowerCase(Locale.getDefault());
            fromObj.put(field, result);
          }
        }
        catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e)
        {
          log.warn("{}", e);
        }
      }
    }
    Object fields = fromObj.keySet();
    for (Object str : (Set)fields) {
      invoke(to, str, fromObj.get(str));
    }
  }
}
