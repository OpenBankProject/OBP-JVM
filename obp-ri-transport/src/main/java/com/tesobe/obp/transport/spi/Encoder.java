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
  Request getAccount(OutboundContext outboundContext, String bankId, String accountId);

  Request getAccounts(OutboundContext outboundContext, String bankId);

  Request getBank(OutboundContext outboundContext, String bankId);

  Request getBanks(OutboundContext outboundContext);

  Request getTransaction(String bankId, String accountId, String transactionId, OutboundContext outboundContext);

  Request getTransactions(String bankId, String accountId, OutboundContext outboundContext);

  Request getUser(String userId, OutboundContext outboundContext);

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
