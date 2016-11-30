/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Token;
import org.json.JSONObject;

import java.util.Optional;

/**
 * @since 2016.11
 */
class TokenDecoder implements Token
{
  TokenDecoder(JSONObject token)
  {
    assert token != null;

    json = token;
  }

  @Override public Optional<String> id()
  {
    return Optional.ofNullable(json.optString("id", null));
  }

  @Override public String error()
  {
    return "";
  }

  private final JSONObject json;
}
