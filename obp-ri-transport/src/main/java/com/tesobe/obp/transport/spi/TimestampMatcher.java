/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Pager;
import com.tesobe.obp.transport.Transaction;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Only implemented for transaction so far.
 */
@SuppressWarnings("WeakerAccess") public class TimestampMatcher
{
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public TimestampMatcher(Optional<Pager.Filter<ZonedDateTime>> filter)
  {
    filter.map(f ->
    {
      pass = false;
      min = f.lowerBound();
      max = f.higherBound();
      name = f.fieldName();
      return Void.TYPE;
    }).orElseGet(() ->
    {
      pass = true;
      min = null;
      max = null;
      name = null;
      return Void.TYPE;
    });
  }

  public boolean matches(Transaction t)
  {
    if(pass)
    {
      return true;
    }

    switch(name)
    {
      case "completedDate":
      {
        ZonedDateTime dt = t.completedDate();

        return after(dt, min) && before(dt, max);
      }
      case "postedDate":
      {
        ZonedDateTime dt = t.postedDate();

        return after(dt, min) && before(dt, max);
      }

      default:
        return true;
    }
  }

  private boolean after(ZonedDateTime dt, ZonedDateTime earliest)
  {
    return earliest == null || (dt != null && dt.compareTo(earliest) >= 0);
  }

  private boolean before(ZonedDateTime dt, ZonedDateTime latest)
  {
    return latest == null || (dt != null && dt.compareTo(latest) <= 0);
  }

  private boolean pass;
  private String name;
  private ZonedDateTime min;
  private ZonedDateTime max;
}
