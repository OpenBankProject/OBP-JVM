/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.util;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Match against the return value of a method call.
 * Todo rm when hamcrest supports optionals.
 */
@SuppressWarnings("WeakerAccess") public final class MethodMatcher
{
  public static <T> Matcher<T> isPresent()
  {
    return returns("isPresent", true);
  }

  public static <T> Matcher<T> get(Object expected)
  {
    return returns("get", expected);
  }

  /**
   * Match the return value of a method call on the asserted object.
   *
   * @param method method to call
   * @param expected the value to check against
   * @param <T> the method's owner's type
   *
   * @return both null, or equals
   */
  public static <T> Matcher<T> optionallyReturns(String method, Object expected)
  {
    return new TypeSafeMatcher<T>()
    {
      @Override public void describeTo(Description description)
      {
        description
          .appendText(expected + " (return value of " + method + "())");
      }

      @Override protected boolean matchesSafely(T item)
      {
        @SuppressWarnings("unchecked") Optional<Object> optional
          = (Optional<Object>)item;

        return optional != null && optional.isPresent() && match(optional.get(),
          method, expected);
      }
    };
  }

  /**
   * Match the return value of a method call on the asserted object.
   *
   * @param method method to call
   * @param expected the value to check against
   * @param <T> the method's owner's type
   *
   * @return both null, or equals
   */
  public static <T> Matcher<T> returns(String method, Object expected)
  {
    return new TypeSafeMatcher<T>()
    {
      @Override public void describeTo(Description description)
      {
        description
          .appendText(expected + " (return value of " + method + "())");
      }

      @Override protected boolean matchesSafely(T item)
      {
        return match(item, method, expected);
      }
    };
  }

  /**
   * @param item method's owner
   * @param method method to invoke without arguments
   * @param expected return value
   * @param <T> method owner's type
   *
   * @return are expected and return value both null, or are they equal?
   */
  protected static <T> boolean match(T item, String method, Object expected)
  {
    if(item == null)
    {
      return false; // cannot call method on null
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
      log.error("{} -> {}", method, expected, e);

      return false;
    }
  }

  static final Logger log = LoggerFactory.getLogger(MethodMatcher.class);
}
