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
@SuppressWarnings("WeakerAccess") public abstract class LegacyResponder
  implements Receiver
{
  public LegacyResponder(Decoder d, Encoder e)
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

    log.trace("{} \u2192 {}", request, result);

    return result;
  }

  protected String getAccount(Decoder.Request r, Encoder e)
  {
    return getPrivateAccount(r.raw(), r, e);
  }

  protected String getAccounts(Decoder.Request r, Encoder e)
  {
    return getPrivateAccounts(r.raw(), r, e);
  }

  protected String getBank(Decoder.Request r, Encoder e)
  {
    assert nonNull(r);
    assert nonNull(e);

    log.trace("{} user id present? {}", r, r.userId().isPresent());

    return r.userId().isPresent()
           ? getPrivateBank(r.raw(), r, e)
           : getPublicBank(r.raw(), r, e);
  }

  protected String getBanks(Decoder.Request r, Encoder e)
  {
    assert nonNull(r);
    assert nonNull(e);

    log.trace("{} user id present? {}", r, r.userId().isPresent());

    return r.userId().isPresent()
           ? getPrivateBanks(r.raw(), r, e)
           : getPublicBanks(r.raw(), e);
  }

  protected String getTransaction(Decoder.Request r, Encoder e)
  {
    return getPrivateTransaction(r.raw(), r, e);
  }

  protected String getTransactions(Decoder.Request r, Encoder e)
  {
    return getPrivateTransactions(r.raw(), r, e);
  }

  protected String getUser(Decoder.Request r, Encoder e)
  {
    return getPrivateUser(r.raw(), r, e);
  }

  protected String saveTransaction(Decoder.Request r, Encoder e)
  {
    return savePrivateTransaction(r.raw(), r, e);
  }

  protected abstract String getPrivateAccount(String packet, Decoder.Request r,
    Encoder e);

  protected abstract String getPrivateAccounts(String packet, Decoder.Request r,
    Encoder e);

  protected abstract String getPrivateBank(String packet, Decoder.Request r,
    Encoder e);

  protected abstract String getPrivateBanks(String packet, Decoder.Request r,
    Encoder e);

  protected abstract String getPrivateTransaction(String packet,
    Decoder.Request r, Encoder e);

  protected abstract String getPrivateTransactions(String packet,
    Decoder.Request r, Encoder e);

  protected abstract String getPrivateUser(String packet, Decoder.Request r,
    Encoder e);

  protected abstract String getPublicAccount(String packet, Decoder.Request r,
    Encoder e);

  protected abstract String getPublicAccounts(String packet, Decoder.Request r,
    Encoder e);

  protected abstract String getPublicBank(String packet, Decoder.Request r,
    Encoder e);

  protected abstract String getPublicBanks(String packet, Encoder e);

  protected abstract String getPublicTransaction(String packet,
    Decoder.Request r, Encoder e);

  protected abstract String getPublicTransactions(String packet,
    Decoder.Request r, Encoder e);

  protected abstract String getPublicUser(String packet, Decoder.Request r,
    Encoder e);

  protected abstract String savePrivateTransaction(String packet,
    Decoder.Request r, Encoder e);

  final Decoder decoder;
  final Encoder encoder;
  static final Map<String, BiFunction<Decoder.Request, Encoder, String>> api
    = new HashMap<>();
  static final Logger log = LoggerFactory.getLogger(LegacyResponder.class);
}
