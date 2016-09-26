package com.tesobe.obp.util;

import java.lang.reflect.Method;

public class Utils
{
  public static Object valueOf(Object o, String name)
  {
    try
    {
      Method method = o.getClass().getMethod(name);

      method.setAccessible(true);

      return method.invoke(o);
    }
    catch(Exception e)
    {
      return null;
    }
  }
}
