/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.*;

import java.util.*;

@SuppressWarnings("WeakerAccess") public class DefaultResponder
  implements Responder
{
  @Override
  public Optional<Account> getAccount(Decoder.Pager p, Decoder.Parameters ps)
  {
    return Optional.empty();
  }

  @Override
  public List<Account> getAccounts(Decoder.Pager p, Decoder.Parameters ps)
  {
    return Collections.emptyList();
  }

  @Override
  public Optional<Bank> getBank(Decoder.Pager p, Decoder.Parameters ps)
  {
    return Optional.empty();
  }

  @Override public List<Bank> getBanks(Decoder.Pager p, Decoder.Parameters ps)
  {
    return Collections.emptyList();
  }

  @Override public Optional<Transaction> getTransaction(Decoder.Pager p,
    Decoder.Parameters ps)
  {
    return Optional.empty();
  }

  @Override public List<Transaction> getTransactions(Decoder.Pager pager,
    Decoder.Parameters ps)
  {
    return Collections.emptyList();
  }

  @Override
  public Optional<User> getUser(Decoder.Pager p, Decoder.Parameters ps)
  {
    return Optional.empty();
  }

  @Override
  public List<User> getUsers(Decoder.Pager pager, Decoder.Parameters ps)
  {
    return Collections.emptyList();
  }

  @Override public Token createTransaction(Decoder.Fields fs)
  {
    return new ErrorToken("Not implemented: Responder.createTransaction");
  }
}
