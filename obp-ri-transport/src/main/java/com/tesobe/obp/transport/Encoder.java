/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Map;

/**
 * @since 2016.9
 */
public interface Encoder
{
  Request get(String caller, Transport.Target t, Pager p, String state,
    String userId, String bankId, String accountId, String transactionId);

  Request put(String caller, Transport.Target t, Map<String, String> fields,
    Map<String, BigDecimal> money, Map<String, Temporal> timestamps);

  default String account()
  {
    return account(null);
  }

  String account(Account a);

  default String accounts(List<? extends Account> as)
  {
    return accounts(as, false);
  }

  default String accounts(List<? extends Account> as, boolean more)
  {
    return accounts(as, more, null);
  }

  String accounts(List<? extends Account> as, boolean more, String state);

  default String bank()
  {
    return bank(null);
  }

  String bank(Bank b);

  default String banks(List<? extends Bank> bs)
  {
    return banks(bs, false);
  }

  default String banks(List<? extends Bank> bs, boolean more)
  {
    return banks(bs, more, null);
  }

  String banks(List<? extends Bank> bs, boolean more, String state);

  String error(String message);

  String token(Token t);

  String transaction(Transaction t);

  default String transactions(List<? extends Transaction> ts)
  {
    return transactions(ts, false);
  }

  default String transactions(List<? extends Transaction> ts, boolean more)
  {
    return transactions(ts, more, null);
  }

  String transactions(List<? extends Transaction> ts, boolean more,
    String state);

  String transactionId(String s);

  String user(User u);

  default String users(List<? extends User> users)
  {
    return users(users, false);
  }

  default String users(List<? extends User> users, boolean more)
  {
    return users(users, more, null);
  }

  String users(List<? extends User> users, boolean more, String state);

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

  Transport.Version version();

  interface Request
  {
    String toString();
  }
}
