/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.*;

import java.util.List;

/**
 * @since 2016.0
 */
public interface Encoder
{
  Request getAccount(Context context, String bankId, String accountId);

  Request getAccounts(Context context, String bankId);

  Request getBank(Context context, String bankId);

  Request getBanks(Context context);

  Request getTransaction(String bankId, String accountId, String transactionId, Context context);

  Request getTransactions(String bankId, String accountId, Context context);

  Request getUser(String userId, Context context);

  String account(Account a);

  String accounts(List<Account> as);

  String bank(Bank b);

  String banks(List<Bank> bs);

  String error(String message);

  String notFound();

  String transaction(Transaction t);

  String transactions(List<Transaction> ts);

  String user(User u);

  interface Request
  {
    String toString();
  }
}
