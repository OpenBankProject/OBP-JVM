/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.User;

import java.util.List;

/**
 * @since 2016.9
 */
public interface Encoder
{
  Request getAccount(String bankId, String accountId);

  Request getAccount(String userId, String bankId, String accountId);

  Request getAccounts(String bankId);

  Request getAccounts(String userId, String bankId);

  Request getBank(String bankId);

  Request getBank(String userId, String bankId);

  Request getBanks();

  Request getBanks(String userId);

  Request getTransaction(String bankId, String accountId, String transactionId);

  Request getTransaction(String bankId, String accountId, String transactionId,
    String userId);

  Request getTransactions(Connector.Pager p, String bankId, String accountId,
    String userId);

  Request getTransactions(Connector.Pager p, String bankId, String accountId);

  Request getUser(String userId);

  Request getUsers(String userId);

  Request getUsers();

  Request saveTransaction(String userId, String accountId, String currency,
    String amount, String otherAccountId, String otherAccountCurrency,
    String transactionType);

  String account(Account a);

  String accounts(List<? extends Account> as);

  String bank(Bank b);

  String banks(List<? extends Bank> bs);

  String error(String message);

  String transaction(Transaction t);

  String transactions(List<? extends Transaction> ts);

  String transactions(List<? extends Transaction> ts, boolean more);

  String user(User u);

  String transactionId(String s);

  String users(List<? extends User> users);

  interface Request
  {
    String toString();
  }

  default String account()
  {
    return account(null);
  }

  default String bank()
  {
    return bank(null);
  }

  default String transaction()
  {
    return transaction(null);
  }
}
