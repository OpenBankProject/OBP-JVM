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
 */
class NullPager implements Pager, Serializable
{
  @Override public boolean hasMorePages()
  {
    return false;
  }

  @Override public Pager nextPage()
  {
    throw new RuntimeException("No more pages available!");
  }

  static final long serialVersionUID = 42L;
}
