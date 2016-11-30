/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Pager;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * @since 2016.11
 */
@SuppressWarnings("WeakerAccess") public class TimestampFilter
  implements Pager.Filter<ZonedDateTime>, Serializable
{
  public TimestampFilter(String fieldName, ZonedDateTime earliest,
    ZonedDateTime latest)
  {
    this.fieldName = fieldName;
    this.earliest = earliest;
    this.latest = latest;
  }

  @Override public String fieldName()
  {
    return fieldName;
  }

  @Override public Class<ZonedDateTime> type()
  {
    return ZonedDateTime.class;
  }

  @Override public ZonedDateTime lowerBound()
  {
    return earliest;
  }

  @Override public ZonedDateTime higherBound()
  {
    return latest;
  }
  static final long serialVersionUID = 42L;
  public final String fieldName;
  public final ZonedDateTime earliest;
  public final ZonedDateTime latest;
}
