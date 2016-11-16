/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Responder;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * todo document
 */
@SuppressWarnings("WeakerAccess") public class ReceiverNov2016
  extends AbstractReceiver
{
  public ReceiverNov2016(Responder r, Codecs cs)
  {
    super(cs);

    responder = r;
  }

  /**
   * @param r request, already decoded
   * @param e result encoder
   *
   * @return result
   */
  @Override protected String get(Decoder.Request r, Encoder e)
  {
    Optional<Network.Target> target = r.target();
    String result = target.map(t -> get(r, e, t)).orElse("???"); // todo fix

    return result;
  }

  protected String get(Decoder.Request r, Encoder e, Network.Target target)
  {
    assert target != null;

    switch(target)
    {
      case account:
      {
        return optGet(r, responder::getAccount, e::account, e::account);
      }
      case accounts:
      {
        return get(r, responder::getAccounts, e::accounts);
      }
      case bank:
      {
        return optGet(r, responder::getBank, e::bank, e::bank);
      }
      case banks:
      {
        return get(r, responder::getBanks, e::banks);
      }
      case transaction:
      {
        return optGet(r, responder::getTransaction, e::transaction,
          e::transaction);
      }
      case transactions:
      {
        return get(r, responder::getTransactions, e::transactions);
      }
      case user:
      {
        return optGet(r, responder::getUser, e::user, e::user);
      }
      case users:
      {
        return get(r, responder::getUsers, e::users);
      }
    }

    throw new RuntimeException("cannot get " + target);
  }

  private <T> String get(Decoder.Request r,
    BiFunction<Decoder.Pager, Decoder.Parameters, T> respond,
    Function<T, String> encode)
  {
    Decoder.Pager pager = r.pager();

    return encode.apply(respond.apply(pager, r.parameters()));
  }

  private <T> String optGet(Decoder.Request r,
    BiFunction<Decoder.Pager, Decoder.Parameters, Optional<T>> respond,
    Function<T, String> encode, Supplier<String> fail)
  {
    Decoder.Pager pager = r.pager();

    return respond.apply(pager, r.parameters()).map(encode).orElseGet(fail);
  }

  @Override protected String put(Decoder.Request r, Encoder e)
  {
    assert r != null;

    Optional<Network.Target> target = r.target();

    return target
      .map(t -> put(r, e, t))
      .orElse(e.token(new ErrorToken("Target missing!")));
  }

  protected String put(Decoder.Request r, Encoder e, Network.Target t)
  {
    assert t != null;

    switch(t)
    {
      case transaction:
        return optPut(r, responder::createTransaction, e::token);
      default:
        return e.token(new ErrorToken("Unknown target: " + t));
    }
  }

  private <T> String optPut(Decoder.Request r,
    Function<Decoder.Fields, T> respond, Function<T, String> encode)
  {
    return encode.apply(respond.apply(r.fields()));
  }

  final Responder responder;
}
