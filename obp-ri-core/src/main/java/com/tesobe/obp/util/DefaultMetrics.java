package com.tesobe.obp.util;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Created by ubecker on 23/09/2016.
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
    throw new tbd();
  }

  @Override public UUID started(ZonedDateTime t, String name)
  {
    throw new tbd();
  }

  @Override
  public UUID started(ZonedDateTime t, String name, String description)
  {
    throw new tbd();
  }
}
