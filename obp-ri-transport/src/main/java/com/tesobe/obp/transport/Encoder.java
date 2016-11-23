/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @since 2016.9
 */
public interface Encoder
{
  default Request get(String caller, Transport.Target t, Pager p, String userId,
    String bankId, String accountId, String transactionId)
  {
    return null;
  }

  default Request put(String caller, Transport.Target t,
    Map<String, String> fields, Map<String, BigDecimal> money)
  {
    return null;
  }

//  Request getAccount(String bankId, String accountId);
//
//  Request getAccount(String userId, String bankId, String accountId);
//
//  Request getAccounts(String bankId);
//
//  Request getAccounts(String userId, String bankId);
//
//  Request getBank(String bankId);
//
//  Request getBank(String userId, String bankId);

  Request getBanks();

  Request getBanks(String userId);
//
//  Request getTransaction(String bankId, String accountId, String
// transactionId);
//
//  Request getTransaction(String bankId, String accountId, String
// transactionId,
//    String userId);
//
//  Request getUser(String userId);
//
//  Request getUsers(String userId);
//
//  Request getUsers();

  String account(Account a);

  String accounts(List<? extends Account> as);

  String bank(Bank b);

  String banks(List<? extends Bank> bs);

  String error(String message);

  String token(Token t);

  String transaction(Transaction t);

  String transactions(List<? extends Transaction> ts);

  String transactions(List<? extends Transaction> ts, boolean more);

  String transactionId(String s);

  String user(User u);

  String users(List<? extends User> users);

  default String account()
  {
    return account(null);
  }

  default String bank()
  {
    return bank(null);
  }

  default String token()
  {
    return token(null);
  }

  default String transaction()
  {
    return transaction(null);
  }

  default String user()
  {
    return user(null);
  }

  interface Request
  {
    String toString();
  }
}
