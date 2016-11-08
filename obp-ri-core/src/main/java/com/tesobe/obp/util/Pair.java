/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.util;

@SuppressWarnings("WeakerAccess") public class Pair<F, S>
{
  public Pair(F f, S s)
  {
    first = f;
    second = s;
  }

  public final F first;
  public final S second;
}
