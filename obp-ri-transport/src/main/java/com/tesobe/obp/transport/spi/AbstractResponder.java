/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static java.util.Objects.nonNull;

/**
 * @since 2016.0
 */
@SuppressWarnings("WeakerAccess") public abstract class AbstractResponder
  implements Receiver
{
  public AbstractResponder(Decoder d, Encoder e)
  {
    assert d != null;
    assert e != null;

    decoder = d;
    encoder = e;

    api.put("getBank", this::getBank);
    api.put("getBankAccount", this::getAccount);
    api.put("getBankAccounts", this::getAccounts);
    api.put("getBanks", this::getBanks);
    api.put("getPublicAccounts", this::getAccounts);
    api.put("getTransaction", this::getTransaction);
    api.put("getTransactions", this::getTransactions);
    api.put("getUser", this::getUser);
    api.put("getUserAccounts", this::getAccounts);
    api.put("saveTransaction", this::saveTransaction);
  }

  @Override public String respond(Message request)
  {
    String result = null;

    if(nonNull(request))
    {
      try
      {
        Decoder.Request decoded = decoder.request(request.payload);
        String name = decoded.name();
        BiFunction<Decoder.Request, Encoder, String> call = api.get(name);

        if(nonNull(call))
        {
          result = call.apply(decoded, encoder);
        }
        else
        {
          log.error("Not found: '{}'", name);
        }
      }
      catch(Exception e)
      {
        log.error("{}", request, e);
      }
    }

    log.trace("{} \u2192 {}", request, result);

    return result;
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
  static final Map<String, BiFunction<Decoder.Request, Encoder, String>> api
    = new HashMap<>();
  static final Logger log = LoggerFactory.getLogger(AbstractResponder.class);
}
