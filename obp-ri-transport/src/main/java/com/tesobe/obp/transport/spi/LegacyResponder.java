/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@SuppressWarnings("WeakerAccess") public abstract class LegacyResponder
  implements Receiver
{
  public LegacyResponder(Decoder d, Encoder e)
  {
    assert d != null;
    assert e != null;

    decoder = d;
    encoder = e;

    api.put("getBanks", this::getBanks);
  }

  @Override public String respond(Message request)
  {
    Decoder.Request decoded = decoder.request(request.payload);
    String name = decoded.name();
    BiFunction<Decoder.Request, Encoder, String> call = api.get(name);
    String result = null;

    if(call != null)
    {
      result = call.apply(decoded, encoder);
    }

    log.trace("{} \u2192 {}", request, result);

    return result;
  }

  protected String getBanks(Decoder.Request request, Encoder e)
  {
    return request.hasArguments()
           ? getPrivateBanks(request.raw(), request, e)
           : getPublicBanks(request.raw(), e);
  }

  protected abstract String getPrivateBanks(String packet,
    Decoder.Request request, Encoder e);

  protected abstract String getPublicBanks(String packet, Encoder e);

  final Decoder decoder;
  final Encoder encoder;
  static final Map<String, BiFunction<Decoder.Request, Encoder, String>> api
    = new HashMap<>();
  static final Logger log = LoggerFactory.getLogger(LegacyResponder.class);
}
