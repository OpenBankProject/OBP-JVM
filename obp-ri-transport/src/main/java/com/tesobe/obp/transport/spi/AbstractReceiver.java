/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import static java.util.Objects.nonNull;

/**
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

    Map<String, BiFunction<Decoder.Request, Encoder, String>> sep2016
      = new HashMap<>();

    sep2016.put("get account", this::getAccount);
    sep2016.put("get accounts", this::getAccounts);
    sep2016.put("get bankOld", this::getBank);
    sep2016.put("get banks", this::getBanks);
    sep2016.put("get transaction", this::getTransaction);
    sep2016.put("get transactions", this::getTransactions);
    sep2016.put("get user", this::getUser);
    sep2016.put("get users", this::getUsers);
    sep2016.put("save transaction", this::saveTransaction);

    Map<String, BiFunction<Decoder.Request, Encoder, String>> nov2016
      = new HashMap<>();

    nov2016.put("get", this::get);
    nov2016.put("put", this::put);

    versions.put(Transport.Version.Nov2016, nov2016);
    versions.put(Transport.Version.Sep2016, sep2016);
  }

  protected Pair<Encoder, Decoder> newest(
    EnumMap<Transport.Version, Pair<Encoder, Decoder>> cs)
  {
    List<Transport.Version> sorted = new ArrayList<>(cs.keySet());

    sorted.sort(Collections.reverseOrder());

    return cs.get(sorted.get(0));
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
    String response;

    if(nonNull(request))
    {
      try
      {
        Optional<Decoder.Request> decoded = decoder.request(request.payload);

        if(decoded.isPresent())
        {
          String name = decoded.get().name();
          Transport.Version version = Transport.Version.valueOf(
            decoded.get().version());
          Map<String, BiFunction<Decoder.Request, Encoder, String>> api
            = versions.get(version);
          Pair<Encoder, Decoder> pair = codecs.get(version);
          Encoder encoder = pair.first;

          if(version != decoder.version())
          {
            decoded = pair.second.request(request.payload);
          }

          if(nonNull(api))
          {
            BiFunction<Decoder.Request, Encoder, String> call = api.get(name);

            if(nonNull(call))
            {
              response = call.apply(decoded.get(), encoder);

              if(nonNull(response))
              {
                log.trace("{} \u2192 {}", request, response);

                return response;
              }
            }
          }
        }
      }
      catch(Exception e)
      {
        log.error("{}", request, e);
      }
    }

    response = errorEncoder.error(request != null ? request.id : "");

    log.trace("{} \u2192 {}", request, response);

    return response;
  }

  protected abstract String get(Decoder.Request r, Encoder e);

  protected abstract String put(Decoder.Request r, Encoder e);

  protected abstract String getAccount(Decoder.Request r, Encoder e);

  protected abstract String getAccounts(Decoder.Request r, Encoder e);

  protected abstract String getBank(Decoder.Request r, Encoder e);

  protected abstract String getBanks(Decoder.Request r, Encoder e);

  protected abstract String getTransaction(Decoder.Request r, Encoder e);

  protected abstract String getTransactions(Decoder.Request r, Encoder e);

  protected abstract String getUser(Decoder.Request r, Encoder e);

  protected abstract String getUsers(Decoder.Request r, Encoder e);

  protected abstract String saveTransaction(Decoder.Request r, Encoder e);
  static final Map<Transport.Version, Map<String, BiFunction<Decoder.Request, Encoder, String>>>
    versions = new HashMap<>();
  static final Logger log = LoggerFactory.getLogger(AbstractReceiver.class);
  protected final Codecs codecs;
  protected final Decoder decoder;
  protected final Encoder errorEncoder;
}
