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
 * Default implementation of {@link Pager}.
 */
@SuppressWarnings("WeakerAccess") class DefaultPager
  implements Pager, Serializable
{
  public DefaultPager(int size, int offset, Filter<?> f, Sorter s)
  {
    this.offset = offset;
    this.size = size;
    filter = f;
    sorter = s;
  }

  public DefaultPager(int size, int offset)
  {
    this(size, offset, null, null);
  }

  @Override public boolean hasMorePages()
  {
    return more;
  }

  @Override public Pager nextPage()
  {
    return new DefaultPager(size, offset + size);
  }

  @Override public int offset()
  {
    return offset;
  }

  @Override public int size()
  {
    return size;
  }

  @Override public Filter<?> filter()
  {
    return filter;
  }

  @Override public Sorter sorter()
  {
    return sorter;
  }

  static final long serialVersionUID = 42L;
  public final int offset;
  public final int size;
  public final Filter<?> filter;
  public final Sorter sorter;
  protected boolean more;

  public static class TimestampFilter
    implements Filter<ZonedDateTime>, Serializable
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
}
