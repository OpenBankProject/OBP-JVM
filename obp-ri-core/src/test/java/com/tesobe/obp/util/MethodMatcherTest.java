/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.util;

import org.junit.Test;

import java.util.Optional;

import static com.github.npathai.hamcrestopt.OptionalMatchers.hasValue;
import static com.tesobe.obp.util.MethodMatcher.idEquals;
import static com.tesobe.obp.util.MethodMatcher.returns;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@SuppressWarnings("WeakerAccess") public class MethodMatcherTest
{
  @Test public void id() throws Exception
  {
    class A { public String id() { return "id"; }}

    assertThat(new A().id(), equalTo("id"));
    assertThat(Optional.of(new A()), hasValue(idEquals("id")));
    assertThat(Optional.of(new A()), hasValue(returns("id", "id")));
  }
}
