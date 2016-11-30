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

/**
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") final class Json
{
  /**
   * The JSON only has zulu time.
   *
   * @param dt date time with time zone. Will be converted to zulu.
   *
   * @return null when dt is null, or yyyy-MM-dd'T'HH:mm:ss.SSSZ
   */
  public static String toJson(ZonedDateTime dt)
  {
    if(nonNull(dt))
    {
      return dt.withZoneSameInstant(ZoneOffset.UTC).format(FORMATTER);
    }

    return null;
  }

  /**
   * Uses the format JavaScript Date uses: yyyy-MM-dd'T'HH:mm:ss.SSSZ.
   *
   * @param dt in zulu
   *
   * @return null when dt is null
   */
  @SuppressWarnings("ConstantConditions")
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

  static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
    "yyyy-MM-dd'T'HH:mm:ss.SSSX");

}
