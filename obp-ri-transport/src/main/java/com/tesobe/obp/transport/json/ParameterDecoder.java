/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Decoder;
import org.json.JSONObject;

import java.util.Optional;

/**
 * todo document
 */
class ParameterDecoder implements Decoder.Parameters
{
  ParameterDecoder(String requestId, JSONObject request)
  {
    id = requestId;
    json = request;
  }

  @Override public Optional<String> accountId()
  {
    return Optional.ofNullable(json.optString("accountId", null));
  }

  @Override public Optional<String> bankId()
  {
    return Optional.ofNullable(json.optString("bankId", null));
  }

  @Override public Optional<String> transactionId()
  {
    return Optional.ofNullable(json.optString("transactionId", null));
  }

  @Override public Optional<String> userId()
  {
    return Optional.ofNullable(json.optString("userId", null));
  }

  @Override public String requestId()
  {
    return id;
  }

  final String id;
  final JSONObject json;
}
