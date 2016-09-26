/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
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
  public AbstractReceiver(Decoder d, Encoder e)
  {
    assert d != null;
    assert e != null;

    decoder = d;
    encoder = e;

    Map<String, BiFunction<Decoder.Request, Encoder, String>> api
      = new HashMap<>();

    api.put("get account", this::getAccount);
    api.put("get accounts", this::getAccounts);
    api.put("get bank", this::getBank);
    api.put("get banks", this::getBanks);
    api.put("get transaction", this::getTransaction);
    api.put("get transactions", this::getTransactions);
    api.put("get user", this::getUser);
    api.put("save transaction", this::saveTransaction);

    versions.put(Transport.Version.Sep2016, api);
  }

  /**
   * Decode request, find handler, return result.
   * Version is ignored for now.
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
          String version = decoded.get().version();
          Map<String, BiFunction<Decoder.Request, Encoder, String>> api
            = versions.get(Transport.Version.valueOf(version));

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

    response = encoder.error(String.valueOf(request));

    log.trace("{} \u2192 {}", request, response);

    return response;
  }

  protected abstract String getAccount(Decoder.Request r, Encoder e);

  protected abstract String getAccounts(Decoder.Request r, Encoder e);

  protected abstract String getBank(Decoder.Request r, Encoder e);

  protected abstract String getBanks(Decoder.Request r, Encoder e);

  protected abstract String getTransaction(Decoder.Request r, Encoder e);

  protected abstract String getTransactions(Decoder.Request r, Encoder e);

  protected abstract String getUser(Decoder.Request r, Encoder e);

  protected abstract String saveTransaction(Decoder.Request r, Encoder e);

  final Decoder decoder;
  final Encoder encoder;
  static final Map<Transport.Version, Map<String, BiFunction<Decoder.Request, Encoder, String>>>
    versions = new HashMap<>();
  static final Logger log = LoggerFactory.getLogger(AbstractReceiver.class);
}
