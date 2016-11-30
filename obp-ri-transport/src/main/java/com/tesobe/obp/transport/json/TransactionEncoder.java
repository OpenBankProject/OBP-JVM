/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Transaction;
import org.json.JSONObject;

/**
 * Writes a transaction to JSON .
 * Missing fields will be skipped.
 * The constructor trusts that the transaction is not null.
 *
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") class TransactionEncoder
{
  public TransactionEncoder(Transaction t)
  {
    assert t != null;

    transaction = t;
  }

  public JSONObject toJson()
  {
    return new JSONObject()
      .put("transactionId", transaction.transactionId())
      .put("accountId", transaction.accountId())
      .put("bankId", transaction.bankId())
      .put("type", transaction.type())
      .put("description", transaction.description())
      .put("postedDate", Json.toJson(transaction.postedDate()))
      .put("completedDate", Json.toJson(transaction.completedDate()))
      .put("newBalanceAmount", transaction.newBalanceAmount())
      .put("newBalanceCurrency", transaction.newBalanceCurrency())
      .put("amount", transaction.amount())
      .put("currency", transaction.currency())
      .put("counterpartyId", transaction.counterpartyId())
      .put("counterpartyName", transaction.counterpartyName())
      .put("userId", transaction.userId());
  }

  @Override public String toString()
  {
    return transaction.toString();
  }

  private final Transaction transaction;
}
