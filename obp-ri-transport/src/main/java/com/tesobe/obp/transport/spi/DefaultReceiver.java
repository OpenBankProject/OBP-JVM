/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.spi;

import static java.util.Objects.nonNull;

/**
 * Implements {@link AbstractReceiver}'s abstract methods without functionality
 * returning {@code "null"} or {@code "[]"}.
 *
 * @deprecated use ReceiverNov2016
 */
@SuppressWarnings("WeakerAccess") public class DefaultReceiver
  extends AbstractReceiver
{
  public DefaultReceiver(Codecs cs)
  {
    super(cs);
  }

  @Override protected String get(Decoder.Request r, Encoder e)
  {
    return "null";
  }

  @Override protected String put(Decoder.Request r, Encoder e)
  {
    return "null";
  }

  @Override protected String getAccount(Decoder.Request r, Encoder e)
  {
    assert nonNull(r);
    assert nonNull(e);

    log.trace("{} user id present? {}", r, r.userId().isPresent());

    return "null";
  }

  @Override protected String getAccounts(Decoder.Request r, Encoder e)
  {
    assert nonNull(r);
    assert nonNull(e);

    log.trace("{} user id present? {}", r, r.userId().isPresent());

    return "[]";
  }

  @Override protected String getBank(Decoder.Request r, Encoder e)
  {
    assert nonNull(r);
    assert nonNull(e);

    log.trace("{} user id present? {}", r, r.userId().isPresent());

    return "null";
  }

  @Override protected String getBanks(Decoder.Request r, Encoder e)
  {
    assert nonNull(r);
    assert nonNull(e);

    log.trace("{} user id present? {}", r, r.userId().isPresent());

    return "[]";
  }

  @Override protected String getTransaction(Decoder.Request r, Encoder e)
  {
    assert nonNull(r);
    assert nonNull(e);

    log.trace("{} user id present? {}", r, r.userId().isPresent());

    return "null";
  }

  @Override protected String getTransactions(Decoder.Request r, Encoder e)
  {
    assert nonNull(r);
    assert nonNull(e);

    log.trace("{} user id present? {}", r, r.userId().isPresent());

    return "[]";
  }

  @Override protected String getUser(Decoder.Request r, Encoder e)
  {
    assert nonNull(r);
    assert nonNull(e);

    log.trace("{} user id present? {}", r, r.userId().isPresent());

    return "null";
  }

  @Override protected String getUsers(Decoder.Request r, Encoder e)
  {
    assert nonNull(r);
    assert nonNull(e);

    log.trace("{} user id present? {}", r, r.userId().isPresent());

    return "[]";
  }

  @Override protected String saveTransaction(Decoder.Request r, Encoder e)
  {
    assert nonNull(r);
    assert nonNull(e);

    log.trace("{} user id present? {}", r, r.userId().isPresent());

    return "null";
  }
}
