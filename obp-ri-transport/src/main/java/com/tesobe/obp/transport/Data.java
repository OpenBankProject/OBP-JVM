/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Implemented by {@link Decoder} to read data from the transport.
 */
public interface Data extends Serializable
{
  /**
   * Read a string valued field from the transport
   *
   * @param key field name
   *
   * @return field value
   */
  String text(String key);

  /**
   * Read a money valued field from the transport
   *
   * @param key field name
   *
   * @return field value
   */
  BigDecimal money(String key);

  /**
   * Read a timestamp valued field from the transport.
   * The time zone is Zulu, always.
   *
   * @param key field name
   *
   * @return field value
   */
  ZonedDateTime timestamp(String key);
}
