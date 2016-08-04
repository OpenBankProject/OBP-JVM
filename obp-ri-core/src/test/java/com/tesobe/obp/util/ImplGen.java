/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static java.lang.reflect.Proxy.newProxyInstance;
import static java.util.Objects.isNull;

/**
 * Implement an interface for a test.
 */
public class ImplGen
{
  /**
   * Use the count to build return value, using the methods name when possible
   * For example: <pre>
   *   interface A {int intMethod() String stringMethod() }
   * <p>
   *   A a = ImplGen.generate(A.class, 42);
   * <p>
   *   a.intMethod() returns 42
   *   a.stringMethod() returns "bar-42"
   * </pre>
   *
   * @param anInterface will be implemented
   * @param counter used as (part of) the return value for all methods
   * @param <T> the interface's type
   *
   * @return a proxy
   */
  public static <T> T generate(Class<T> anInterface, int counter)
  {
    return anInterface.cast(
      newProxyInstance(anInterface.getClassLoader(), new Class[]{anInterface},
        (proxy, method, args) -> generateReturnValue(anInterface, counter,
          method)));
  }

  /**
   * The return value of one method can be specified, otherwise as {@link
   * #generate(Class, int)}.
   *
   * @param anInterface will be implemented
   * @param counter used as (part of) the return value for all methods
   * @param override name of a method
   * @param returnValue value to return from the method
   * @param <T> the interface's type
   *
   * @return a proxy
   */
  public static <T> T generate(Class<T> anInterface, int counter,
    String override, Object returnValue)
  {
    return anInterface.cast(
      newProxyInstance(anInterface.getClassLoader(), new Class[]{anInterface},
        (proxy, method, args) ->
        {
          if(method.getName().equals(override))
          {
            Class<?> type = method.getReturnType();

            return type.cast(returnValue);
          }
          else
          {
            return generateReturnValue(anInterface, counter, method);
          }
        }));
  }

  private static <T> Object generateReturnValue(Class<T> anInterface,
    int counter, Method method)
  {
    Class<?> type = method.getReturnType();
    BiFunction<String, Integer, ?> f = values.get(type);

    if(isNull(f))
    {
      throw new NullPointerException(
        anInterface.getName() + "#" + method.getName());
    }

    return type.cast(f.apply(method.getName(), counter));
  }

  /**
   * Map types to return values.
   */
  private static final Map<Class<?>, BiFunction<String, Integer, ?>> values
    = new HashMap<>();

  static
  {
    values.put(String.class, (s, i) -> s + "-" + i);
    values.put(Integer.class, (s, i) -> i);
    values.put(BigDecimal.class, (s, i) -> new BigDecimal(i));
  }
}
