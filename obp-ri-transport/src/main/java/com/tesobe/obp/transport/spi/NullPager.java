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
 * A pager with infinite page size, no sorting, and no filtering.
 *
 * @since 2016.11
 */
class NullPager implements Pager, Serializable
{
  @Override public boolean hasMorePages()
  {
    return false;
  }

  @Override public void nextPage()
  {
  }

  @Override public int count()
  {
    return 0;
  }

  @Override public int offset()
  {
    return Pager.DEFAULT_OFFSET;
  }

  @Override public int size()
  {
    return Pager.DEFAULT_SIZE;
  }

  @Override public Filter<?> filter()
  {
    return null;
  }

  @Override public Sorter sorter()
  {
    return null;
  }

  static final long serialVersionUID = 42L;
}
