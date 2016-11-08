/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json.nov2016;

import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.Transport.Version;
import com.tesobe.obp.transport.nov2016.Parameters;
import org.json.JSONObject;

import java.util.Optional;

/**
 * @since 2016.11
 */
public class ParameterDecoder implements Parameters, Decoder.Parameters
{
  public ParameterDecoder(String requestId, JSONObject request)
  {
    id = requestId;
    json = request;
  }

  @Override public Optional<String> accountId()
  {
    return Optional.ofNullable(json.optString(accountId, null));
  }

  @Override public Optional<String> bankId()
  {
    return Optional.ofNullable(json.optString(bankId, null));
  }

  @Override public Optional<String> transactionId()
  {
    return Optional.ofNullable(json.optString(transactionId, null));
  }

  @Override public Optional<String> userId()
  {
    return Optional.ofNullable(json.optString(userId, null));
  }

  @Override public Optional<String> type()
  {
    return Optional.ofNullable(json.optString(type, null));
  }

  @Override public String requestId()
  {
    return id;
  }

  /**
   * @return default to {@link Version#Nov2016 }
   */
  @Override public Transport.Version version()
  {
    return json.optEnum(Transport.Version.class, "version", Version.Nov2016);
  }

  final String id;
  final JSONObject json;
}
