/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import org.junit.Test;

import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class JsonTest
{
  @Test public void zonedDateTimeFromJson() throws Exception
  {
    assertThat(Json.zonedDateTimeFromJson(null), nullValue());
    assertThat(Json.zonedDateTimeFromJson("*%#!"), nullValue());
  }
}
