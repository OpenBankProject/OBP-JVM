package com.tesobe.obp.util;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Track events.
 */
public interface Metrics
{
  public void ended(UUID id, ZonedDateTime t);

  public void ended(UUID id, ZonedDateTime t, String name);

  public void ended(UUID id, ZonedDateTime t, String name, String description);

  public UUID started(ZonedDateTime t);

  public UUID started(ZonedDateTime t, String name);

  public UUID started(ZonedDateTime t, String name, String description);
}
