/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 */
package com.tesobe.obp.util;

import org.json.JSONException;
import org.json.JSONObject;

public class Json
{
  /**
   * No Exception thrown.
   *
   * @param o a JSONObject
   * @param key may , or may not be present
   *
   * @return null if key is absent
   *
   * @since 2016.0
   */
  public static JSONObject getJSONObject(JSONObject o, String key)
  {
    try
    {
      return o == null ? null : o.getJSONObject(key);
    }
    catch(JSONException e)
    {
      return null;
    }
  }

  /**
   * No Exception thrown.
   *
   * @param o a JSONObject
   * @param key may , or may not be present
   *
   * @return null if key is absent
   */
  public static String safeString(JSONObject o, String key)
  {
    return o == null ? null : o.optString(key);
  }
}
