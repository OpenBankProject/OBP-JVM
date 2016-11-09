/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo.south;

import com.tesobe.obp.transport.Transaction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess") public class DemoTransaction
  implements Transaction, Accessible, Open
{
  public DemoTransaction(String id, DemoAccount account, DemoUser otherId,
    DemoAccount otherAccount, String type, String description,
    ZonedDateTime posted, ZonedDateTime completed, BigDecimal balance,
    BigDecimal value, boolean open)
  {
    this.id = id;
    this.account = account;
    this.bank = account != null ? account.bank : null;
    this.otherId = otherId;
    this.otherAccount = otherAccount;
    this.type = type;
    this.description = description;
    this.posted = posted;
    this.completed = completed;
    this.balance = balance;
    this.value = value;
    this.open = open;

    if(account != null)
    {
      account.transactions.add(this);

      if(account.owner != null)
      {
        customers.add(account.owner);
      }
    }

    if(otherId != null)
    {
      customers.add(otherId);
    }
  }

  @Override public String id()
  {
    return id;
  }

  @Override public String accountId()
  {
    return account != null ? account.id() : null;
  }

  @Override public String bankId()
  {
    return bank != null ? bank.id() : null;
  }

  @Override public String counterpartyId()
  {
    return otherId != null ? otherId.id() : null;
  }

  @Override public String counterpartyName()
  {
    return otherAccount != null ? otherAccount.id() : null;
  }

  @Override public String type()
  {
    return type;
  }

  @Override public String description()
  {
    return description;
  }

  @Override public ZonedDateTime postedDate()
  {
    return posted;
  }

  @Override public ZonedDateTime completedDate()
  {
    return completed;
  }

  @Override public BigDecimal newBalanceAmount()
  {
    return balance != null ? balance.toPlainString() : null;
  }

  @Override public BigDecimal amount()
  {
    return value != null ? value.toPlainString() : null;
  }

  @Override public boolean isPublic()
  {
    return open;
  }

  @Override public List<DemoUser> customers()
  {
    return customers;
  }

  final String id;
  final DemoAccount account;
  final DemoBank bank;
  final DemoUser otherId;
  final DemoAccount otherAccount;
  final String type;
  final String description;
  final ZonedDateTime posted;
  final ZonedDateTime completed;
  final BigDecimal balance;
  final BigDecimal value;
  final boolean open;
  final List<DemoUser> customers = new ArrayList<>();
}
