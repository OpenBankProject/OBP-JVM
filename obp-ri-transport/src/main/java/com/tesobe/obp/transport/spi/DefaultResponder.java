/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Encoder;
import com.tesobe.obp.transport.Responder;
import com.tesobe.obp.transport.Token;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("WeakerAccess") public class DefaultResponder
  implements Responder
{
  @Override
  public Optional<Account> getAccount(Decoder.Pager p, Decoder.Parameters ps)
  {
    return Optional.empty();
  }

  @Override
  public String getAccounts(Decoder.Pager p, Decoder.Parameters ps, Encoder e)
  {
    return e.accounts(Collections.emptyList(), false);
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

  @Override
  public String getTransactions(Decoder.Pager p, Decoder.Parameters ps,
    Encoder e)
  {
    return e.transactions(Collections.emptyList(), false);
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
    return new ErrorToken("Not implemented!");
  }
}
