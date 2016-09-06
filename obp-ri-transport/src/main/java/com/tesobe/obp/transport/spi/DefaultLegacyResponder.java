/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.spi;

import static java.util.Objects.nonNull;

/**
 * Implements {@link LegacyResponder}'s abstract methods without functionality
 * returning {@code "null"} or {@code "[]"}.
 */
@SuppressWarnings("WeakerAccess") public class DefaultLegacyResponder
  extends LegacyResponder
{
  public DefaultLegacyResponder(Decoder d, Encoder e)
  {
    super(d, e);
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

  @Override protected String saveTransaction(Decoder.Request r, Encoder e)
  {
    assert nonNull(r);
    assert nonNull(e);

    log.trace("{} user id present? {}", r, r.userId().isPresent());

    return "null";
  }
}
