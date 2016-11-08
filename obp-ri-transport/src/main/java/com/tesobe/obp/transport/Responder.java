/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport;

import com.tesobe.obp.transport.spi.Decoder;

import java.util.List;
import java.util.Optional;

public interface Responder
{
  Optional<Account> getAccount(Decoder.Pager p, Decoder.Parameters ps);

  List<Account> getAccounts(Decoder.Pager p, Decoder.Parameters ps);

  Optional<Bank> getBank(Decoder.Pager p, Decoder.Parameters ps);

  List<Bank> getBanks(Decoder.Pager p, Decoder.Parameters ps);

  Optional<Transaction> getTransaction(Decoder.Pager p, Decoder.Parameters ps);

  List<Transaction> getTransactions(Decoder.Pager pager, Decoder.Parameters ps);

  Optional<User> getUser(Decoder.Pager p, Decoder.Parameters ps);

  List<User> getUsers(Decoder.Pager pager, Decoder.Parameters ps);

  Optional<Token> saveTransaction(Decoder.Fields fs);
}
