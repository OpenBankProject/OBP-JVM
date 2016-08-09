/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.util;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class JsonTest
{
  @Test public void toJson()
  {
    String json = Json.toJson(ZonedDateTime.of(42, 1, 2, 3, 4, 5, 6, ZoneId.of("UTC")));

    assertThat("0042-01-02T03:04:05.000Z", equalTo(json));
  }

  @Test(expected = DateTimeException.class) public void fromJson()
    throws Exception
  {
    ZonedDateTime zonedDateTime = Json.zonedDateTimeFromJson("*%#!");
  }
}
