/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.util;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @since 2016.11
 */
public class DefaultMetrics implements Metrics
{
  @Override public void ended(UUID id, ZonedDateTime t)
  {

  }

  @Override public void ended(UUID id, ZonedDateTime t, String name)
  {

  }

  @Override
  public void ended(UUID id, ZonedDateTime t, String name, String description)
  {

  }

  @Override public UUID started(ZonedDateTime t)
  {
    throw new RuntimeException();
  }

  @Override public UUID started(ZonedDateTime t, String name)
  {
    throw new RuntimeException();
  }

  @Override
  public UUID started(ZonedDateTime t, String name, String description)
  {
    throw new RuntimeException();
  }
}
