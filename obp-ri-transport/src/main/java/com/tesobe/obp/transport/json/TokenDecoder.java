/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Token;
import org.json.JSONObject;

/**
 * todo document
 */
public class TokenDecoder implements Token
{
  public TokenDecoder(JSONObject token)
  {
    assert token != null;

    json = token;
  }

  @Override public String id()
  {
    return json.optString("id", null);
  }

  @Override public String error()
  {
    return "No error yet, please try again, later";
  }

  private final JSONObject json;
}
