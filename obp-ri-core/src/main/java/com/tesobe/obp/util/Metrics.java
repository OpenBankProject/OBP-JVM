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
 * Track events.
 */
public interface Metrics
{
  void ended(UUID id, ZonedDateTime t);

  void ended(UUID id, ZonedDateTime t, String name);

  void ended(UUID id, ZonedDateTime t, String name, String description);

  UUID started(ZonedDateTime t);

  UUID started(ZonedDateTime t, String name);

  UUID started(ZonedDateTime t, String name, String description);
}
