/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport;

import org.json.JSONObject;

import java.util.Map;

/**
 * @since 2016.11
 */
public interface Responder
{
  /**
   * Get first page.
   *
   * @param state internal state
   * @param p pager
   * @param ps parameters
   * @param t target
   *
   * @return result
   */
  Response first(String state, Decoder.Pager p, Decoder.Parameters ps,
    Transport.Target t);

  /**
   * Get following pages.
   *
   * @param state internal state
   * @param p pager
   *
   * @return result
   */
  Response next(String state, Decoder.Pager p);

  /**
   * Put.
   *
   * @param ps parameters
   * @param fields values to put
   * @param t target
   *
   * @return result
   */
  Response put(Decoder.Parameters ps, Map<String, ?> fields,
    Transport.Target t);

  /**
   * Describe available versions, targets, fields.
   *
   * @param json put description here
   *
   * @return description
   */
  JSONObject describe(JSONObject json);

  /**
   * Fetch open transaction results.
   *
   * @return map of transaction id -> result
   */
  Response fetch();
}
