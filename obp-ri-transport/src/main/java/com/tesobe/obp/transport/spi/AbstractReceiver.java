/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Encoder;
import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static java.util.Objects.nonNull;

/**
 * Handles Transport.Version.Nov2016.
 *
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public abstract class AbstractReceiver
  implements Receiver
{
  public AbstractReceiver(Codecs cs)
  {
    assert cs != null;

    codecs = cs;
    decoder = cs.requestDecoder;
    errorEncoder = cs.errorEncoder;

    Map<String, BiFunction<Decoder.Request, Encoder, String>> nov2016
      = new HashMap<>();

    nov2016.put("get", this::get);
    nov2016.put("put", this::put);

    versions.put(Transport.Version.Nov2016, nov2016);
  }

  /**
   * Decode request, find handler, return result.
   *
   * @param request anything
   *
   * @return Error message if anything goes wrong
   */
  @Override public String respond(Message request)
  {
    if(nonNull(request))
    {
      try
      {
        return decoder.request(request.payload)
          .map(this::respond)
          .orElseGet(() -> errorEncoder.error(request.id));
      }
      catch(Error e) // !
      {
        log.warn(request.id, e);

        return errorEncoder.error(e.getMessage());
      }
    }

    return errorEncoder.error("Empty request!");
  }

  /**
   * Decode the request again with the correct decoder if the payload
   * has a version that does not match the receiver's.
   *
   * @param request was decoded with the receiver's default decoder
   *
   * @return response
   */
  protected String respond(Decoder.Request request)
  {
    Transport.Version version = Transport.Version.valueOf(request.version());
    Pair<Encoder, Decoder> pair = codecs.get(version);

    return version == decoder.version()
           ? respond(request, pair.first)
           : pair.second.request(request.raw())
             .map(decoded -> respond(decoded, pair.first))
             .orElseGet(() -> pair.first.error("Could not decode!"));
  }

  /**
   * Find the api call and apply it.
   *
   * @param request decoded request
   * @param encoder used to encode the response
   *
   * @return response
   */
  protected String respond(Decoder.Request request, Encoder encoder)
  {
    Map<String, BiFunction<Decoder.Request, Encoder, String>> api
      = versions.get(encoder.version());
    String response = null;

    if(nonNull(api))
    {
      BiFunction<Decoder.Request, Encoder, String> call = api.get(
        request.name());

      if(nonNull(call))
      {
        response = call.apply(request, encoder);
      }
    }

    if(response == null)
    {
      response = encoder.error("Cannot handle!");
    }

    return response;
  }

  protected abstract String get(Decoder.Request r, Encoder e);

  protected abstract String put(Decoder.Request r, Encoder e);

  static final Map<Transport.Version, Map<String, BiFunction<Decoder.Request,
    Encoder, String>>>
    versions = new HashMap<>();
  static final Logger log = LoggerFactory.getLogger(AbstractReceiver.class);
  protected final Codecs codecs;
  protected final Decoder decoder;
  protected final Encoder errorEncoder;
}
