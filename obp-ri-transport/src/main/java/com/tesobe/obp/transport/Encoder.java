/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @since 2016.9
 */
public interface Encoder
{
  /**
   * North: request data.
   *
   * @param caller
   * @param target
   * @param pager
   * @param parameters
   *
   * @return
   */
  Request get(String caller, Transport.Target target, Pager pager,
    Map<String, ?> parameters);

  /**
   * North: submit data.
   *
   * @param caller
   * @param target
   * @param parameters
   * @param fields
   *
   * @return
   */
  Request put(String caller, Transport.Target target, Map<String, ?> parameters,
    Map<String, ?> fields);

  /**
   * South: respond with data.
   *
   * @param data page into result set
   * @param offset offset of page into result set
   * @param size page size
   * @param state internal state
   * @param count page count
   * @param more more page?
   * @param t data type
   *
   * @return encoded response
   */
  String response(List<? extends Map<String, ?>> data, int offset, int size,
    String state, int count, boolean more, Transport.Target t);

  default String response(List<? extends Map<String, ?>> data,
    Transport.Target t)
  {
    return response(data, 0, data != null ? data.size() : 0, null, 0, false, t);
  }

  /**
   * South: respond with error.
   *
   * @param message error message
   *
   * @return encoded error response
   */
  String error(String message);

  /**
   * Respond without data.
   *
   * @param state state
   * @param count window count
   * @param t target
   *
   * @return encoded data-less response
   */
  String empty(String state, int count, Transport.Target t);

  /**
   * North:
   *
   * @return
   */
  Request describe();

  Transport.Version version();

  /**
   * South:
   *
   * @param response
   *
   * @return
   */
  String description(JSONObject response);

  /**
   * North.
   *
   * @return encoded fetch request
   */
  Request fetch();

  /**
   * South.
   *
   * @param data fetched data
   *
   * @return encoded fetch result
   */
  String fetch(List<? extends Map<String, ?>> data);

  /**
   * Internal representation of a pending request.
   */
  interface Request
  {
  }
}
