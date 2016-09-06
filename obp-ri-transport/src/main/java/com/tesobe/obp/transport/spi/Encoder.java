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
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.User;

import java.util.List;

/**
 * @since 2016.0
 */
public interface Encoder
{
  Request getAccount(String bankId, String accountId);

  Request getAccount(String userId, String bankId, String accountId);

  Request getPrivateAccounts(String userId, String bankId);

  Request getPrivateBank(String userId, String bankId);

  Request getPrivateBanks(String userId);

  Request getPrivateTransaction(String bankId, String accountId, String transactionId, String userId);

  Request getPrivateTransactions(String bankId, String accountId, String userId);

  Request getPublicAccounts(String bankId);

  Request getPublicBank(String bankId);

  Request getPublicBanks();

  Request getPublicTransaction(String bankId, String accountId,
    String transactionId);

  Request getPublicTransactions(String bankId, String accountId);

  Request getUser(String userId);

  Request saveTransaction(String userId, String accountId, String currency,
    String amount, String otherAccountId, String otherAccountCurrency,
    String transactionType);

  String account(Account a);

  String accounts(List<Account> as);

  String bank(Bank b);

  String banks(List<Bank> bs);

  String error(String message);

  String transaction(Transaction t);

  String transactions(List<Transaction> ts);

  String user(User u);

  String transactionId(String s);

  interface Request
  {
    String toString();
  }
}
