/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Pager;

import java.io.Serializable;

/**
 * Default implementation of {@link Pager}. Mutable, sigh.
 *
 * @since 2016.11
 */
@SuppressWarnings("WeakerAccess") public class DefaultPager
  implements Pager, Serializable
{
  public DefaultPager(int size, int offset, Filter<?> f, Sorter s)
  {
    count = 0;
    this.offset = offset;
    this.size = size;
    filter = f;
    sorter = s;
  }

  public DefaultPager(Pager p)
  {
    this(p.size(), p.offset(), p.filter(), p.sorter());
  }

  @Override public synchronized boolean hasMorePages()
  {
    return more;
  }

  @Override public synchronized void nextPage()
  {
    count++;
    offset += size;
  }

  @Override public synchronized int count()
  {
    return count;
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
    return more ? null : filter;
  }

  @Override public Sorter sorter()
  {
    return more ? null : sorter;
  }

  synchronized void more(String aState, int aCount, boolean aFlag)
  {
    count = aCount;
    more = aFlag;
    state = aState;
  }

  synchronized String state()
  {
    return state;
  }

  static final long serialVersionUID = 42L;
  public final Filter<?> filter;
  public final Sorter sorter;
  private int count;
  private int offset;
  private int size;
  private boolean more;
  private String state;
}
