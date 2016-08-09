/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.util;

import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static java.util.Objects.nonNull;

@SuppressWarnings("WeakerAccess") public final class Json
{
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

}
