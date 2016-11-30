/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Encoder;
import com.tesobe.obp.transport.Responder;
import com.tesobe.obp.transport.Transport;

import java.util.List;
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
   * @param e response encoder
   *
   * @return response
   */
  @Override protected String get(Decoder.Request r, Encoder e)
  {
    try
    {
      return r.target()
        .map(t -> get(r, e, t))
        .orElse(errorEncoder.error("Target missing!"));
    }
    catch(Error x) // !
    {
      log.error(r.raw(), x);

      return errorEncoder.error(x.getMessage());
    }
  }

  protected String get(Decoder.Request r, Encoder e, Transport.Target target)
  {
    assert target != null;

    switch(target)
    {
      case account:
      {
        return maybeGet(r, responder::getAccount, e::account, e::account);
      }
      case accounts:
      {
        return responder.getAccounts(r.pager(), r.parameters(), e);
      }
      case bank:
      {
        return maybeGet(r, responder::getBank, e::bank, e::bank);
      }
      case banks:
      {
        return responder.getBanks(r.pager(), r.parameters(), e);
      }
      case transaction:
      {
        return maybeGet(r, responder::getTransaction, e::transaction,
          e::transaction);
      }
      case transactions:
      {
        return responder.getTransactions(r.pager(), r.parameters(), e);
      }
      case user:
      {
        return maybeGet(r, responder::getUser, e::user, e::user);
      }
      case users:
      {
        return responder.getUsers(r.pager(), r.parameters(), e);
      }
    }

    log.error("Unknown target: " + target);

    return e.error(target.toString()); // todo too much information?
  }

  private <T> String get(Decoder.Request r,
    BiFunction<Decoder.Pager, Decoder.Parameters, List<T>> respond,
    Function<List<T>, String> encode)
  {
    Decoder.Pager pager = r.pager();


    return encode.apply(respond.apply(pager, r.parameters()));
  }

  private <T> String maybeGet(Decoder.Request r,
    BiFunction<Decoder.Pager, Decoder.Parameters, Optional<T>> respond,
    Function<T, String> encode, Supplier<String> fail)
  {
    Decoder.Pager pager = r.pager();

    return respond.apply(pager, r.parameters()).map(encode).orElseGet(fail);
  }

  @Override protected String put(Decoder.Request r, Encoder e)
  {
    assert r != null;

    try
    {
      Optional<Transport.Target> target = r.target();

      return target.map(t -> put(r, e, t))
        .orElse(e.token(new ErrorToken("Target missing!")));
    }
    catch(Error x) // !
    {
      log.error(r.raw(), x);

      return errorEncoder.error(x.getMessage());
    }
  }

  protected String put(Decoder.Request r, Encoder e, Transport.Target t)
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
