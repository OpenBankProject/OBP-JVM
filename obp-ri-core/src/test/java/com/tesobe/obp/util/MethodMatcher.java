/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.util;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Method;

/**
 * Match against the return value of a method call.
 */
@SuppressWarnings("WeakerAccess") public final class MethodMatcher
{
  /**
   * Match the return value of the method {@code id()}.
   *
   * @param expected the value to check against
   * @param <T> has an {@code id()} method
   *
   * @return both null, or equals
   */
  public static <T> Matcher<T> idEquals(Object expected)
  {
    return new TypeSafeMatcher<T>()
    {
      @Override public void describeTo(Description description)
      {
        description.appendText("result of call of id()");
      }

      @Override protected boolean matchesSafely(T item)
      {
        return match(item, "id", expected);
      }
    };
  }

  public static <T> Matcher<T> returns(String method, Object expected)
  {
    return new TypeSafeMatcher<T>()
    {
      @Override public void describeTo(Description description)
      {
        description.appendText("result of call of " + method + "()");
      }

      @Override protected boolean matchesSafely(T item)
      {
        return match(item, method, expected);
      }
    };
  }

  protected static <T> boolean match(T item, String method, Object expected)
  {
    if(item == null)
    {
      return false;
    }

    try
    {
      Method m = item.getClass().getMethod(method);

      m.setAccessible(true);  // sadly necessary todo why when public?

      Object value = m.invoke(item);

      return expected == value || (expected != null && expected.equals(value));
    }
    catch(Exception e)
    {
      return false;
    }
  }
}
