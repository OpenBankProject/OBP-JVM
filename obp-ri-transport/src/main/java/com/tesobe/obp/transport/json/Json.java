/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.json;

import java.time.DateTimeException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static java.util.Objects.nonNull;

@SuppressWarnings("WeakerAccess")  final class Json
{
  public static String toj(ZonedDateTime dt)
  {
    if(nonNull(dt))
    {
      return dt.withZoneSameInstant(ZoneOffset.UTC).format(FORMATTER1);
    }

    return null;
  }

  public static ZonedDateTime fmj(String dt)
  {
    if(nonNull(dt))
    {
      try
      {
        return ZonedDateTime.parse(dt, FORMATTER1);
      }
      catch(DateTimeException e)
      {
        return null; // todo log?
      }
    }

    return null;
  }

  /**
   * Uses the format JavaScript Date uses: yyyy-MM-dd'T'HH:mm:ss.SSSZ.
   *
   * @param dt date time with time zone
   *
   * @return null when dt is null
   */
  public static String toJson(ZonedDateTime dt)
  {
    if(nonNull(dt))
    {
      return dt.format(FORMATTER);
    }

    return null;
  }

  /**
   * Uses the format JavaScript Date uses: yyyy-MM-dd'T'HH:mm:ss.SSSZ.
   *
   * @param dt should conform to yyyy-MM-dd'T'HH:mm:ss.SSSZ
   *
   * @return null when dt is null
   */
  public static ZonedDateTime zonedDateTimeFromJson(String dt)
  {
    if(nonNull(dt))
    {
      try
      {
        return ZonedDateTime.parse(dt, FORMATTER);
      }
      catch(DateTimeException e)
      {
        return null; // todo log?
      }
    }

    return null;
  }

  static final DateTimeFormatter FORMATTER = DateTimeFormatter
    .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
  static final DateTimeFormatter FORMATTER1 = DateTimeFormatter.ofPattern(
    "yyyy-MM-dd'T'HH:mm:ss.SSSX");
}
