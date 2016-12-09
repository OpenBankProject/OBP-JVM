/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.Collection;
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

  default String accounts(Collection<? extends Account> as)
  {
    return accounts(as, 0);
  }

  default String accounts(Collection<? extends Account> as, int count)
  {
    return accounts(as, count, false, null);
  }

  String accounts(Collection<? extends Account> as, int count, boolean more,
    String state);

  default String bank()
  {
    return bank(null);
  }

  String bank(Bank b);

  default String banks(Collection<? extends Bank> bs)
  {
    return banks(bs, 0);
  }

  default String banks(Collection<? extends Bank> bs, int count)
  {
    return banks(bs, count, false, null);
  }

  String banks(Collection<? extends Bank> bs, int count, boolean more,
    String state);

  String error(String message);

  String token(Token t);

  String transaction(Transaction t);

  default String transactions(Collection<? extends Transaction> ts)
  {
    return transactions(ts, 0);
  }

  default String transactions(Collection<? extends Transaction> ts, int count)
  {
    return transactions(ts, count, false, null);
  }

  String transactions(Collection<? extends Transaction> ts, int count,
    boolean more,
    String state);

  String transactionId(String s);

  String user(User u);

  default String users(Collection<? extends User> users)
  {
    return users(users, 0);
  }

  default String users(Collection<? extends User> users, int count)
  {
    return users(users, count, false, null);
  }

  String users(Collection<? extends User> users, int count, boolean more,
    String state);

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
