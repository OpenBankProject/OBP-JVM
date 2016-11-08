/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.nov2016;

import com.tesobe.obp.transport.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Convert data to a transaction.
 */
public class TransactionReader implements Transaction
{
  public TransactionReader(Data transaction)
  {
    data = transaction;
  }

  @Override public String id()
  {
    return data.text(transactionId);
  }

  @Override public String accountId()
  {
    return data.text(accountId);
  }

  @Override public BigDecimal amount()
  {
    return data.money(amount);
  }

  @Override public String bankId()
  {
    return data.text(bankId);
  }

  @Override public ZonedDateTime completedDate()
  {
    return data.timestamp(completedDate);
  }

  @Override public String counterPartyId()
  {
    return data.text(counterPartyId);
  }

  @Override public String counterPartyName()
  {
    return data.text(counterPartyName);
  }

  @Override public String currency()
  {
    return data.text(currency);
  }

  @Override public String description()
  {
    return data.text(description);
  }

  @Override public BigDecimal newBalanceAmount()
  {
    return data.money(newBalanceAmount);
  }

  @Override public String newBalanceCurrency()
  {
    return data.text(newBalanceCurrency);
  }

  @Override public ZonedDateTime postedDate()
  {
    return data.timestamp(postedDate);
  }

  @Override public String type()
  {
    return data.text(type);
  }

  @Override public String userId()
  {
    return data.text(userId);
  }

  protected final Data data;
}
