/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

@SuppressWarnings("WeakerAccess") public class MethodMatcherTest
{
  @Test public void optionallyReturns()
  {
    assertThat(Optional.of("a"),
      MethodMatcher.optionallyReturns("toString", "a"));
  }

  @Test public void returns()
  {
    Optional<Integer> answer = Optional.of(42);

    assertThat(answer, MethodMatcher.isPresent());
    assertThat(answer, MethodMatcher.get(42));
  }

  @Test public void isPresent()
  {
    assertThat(Optional.of(42), MethodMatcher.isPresent());
    assertThat(Optional.empty(), not(MethodMatcher.isPresent()));
  }

  @Test public void get()
  {
    assertThat(Optional.of(42), MethodMatcher.get(42));
  }

  @Test(expected = AssertionError.class) public void getOnEmpty()
  {
    if(MethodMatcher.log instanceof Logger) // avoid stack trace in console
    {
      Logger.class.cast(MethodMatcher.log).setLevel(Level.OFF);
    }

    assertThat(Optional.empty(), MethodMatcher.get(42));
  }
}
