/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.json;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Date times are transmitted in zulu time only. Times are converted to zulu.
 */
@RunWith(Parameterized.class) @SuppressWarnings("WeakerAccess")
public class JsonTimeZonesTest
{
  @SuppressWarnings("ConstantConditions") @Test public void test()
  {
    assertThat(zone, Json.toJson(zonedDateTime), equalTo(json));

    ZonedDateTime actual = Json
      .zonedDateTimeFromJson(json)
      .withZoneSameInstant(zonedDateTime.getZone());

    assertThat(zone, actual, equalTo(zonedDateTime));
  }

  @Parameterized.Parameters(name = "{0}") public static Object[][] data()
  {
    return new Object[][]{{"UTC", "0042-01-02T03:04:05.000Z",
      ZonedDateTime.of(42, 1, 2, 3, 4, 5, 0, ZoneId.of("UTC"))},
      {"CET", "0042-01-02T02:04:05.000Z",
        ZonedDateTime.of(42, 1, 2, 3, 4, 5, 0, ZoneId.of("CET"))}};
  }
  @Parameterized.Parameter public String zone;
  @Parameterized.Parameter(1) public String json;
  @Parameterized.Parameter(2) public ZonedDateTime zonedDateTime;
}
