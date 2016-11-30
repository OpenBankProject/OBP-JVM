/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport;

import java.util.Optional;

/**
 * @since 2016.11
 */
public interface Responder
{
  Optional<Account> getAccount(Decoder.Pager p, Decoder.Parameters ps);

  String getAccounts(Decoder.Pager p, Decoder.Parameters ps, Encoder e);

  Optional<Bank> getBank(Decoder.Pager p, Decoder.Parameters ps);

  String getBanks(Decoder.Pager p, Decoder.Parameters ps, Encoder e);

  Optional<Transaction> getTransaction(Decoder.Pager p, Decoder.Parameters ps);

  String getTransactions(Decoder.Pager p, Decoder.Parameters ps, Encoder e);

  Optional<User> getUser(Decoder.Pager p, Decoder.Parameters ps);

  String getUsers(Decoder.Pager pager, Decoder.Parameters ps, Encoder e);

  Token createTransaction(Decoder.Fields fs);
}
