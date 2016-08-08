/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Transaction;
import org.json.JSONObject;

@SuppressWarnings("WeakerAccess") class TransactionEncoder
{
  public TransactionEncoder(Transaction t)
  {
    assert t != null;

    transaction = t;
  }

  public JSONObject toJson()
  {
    // @formatter:off
    @SuppressWarnings("UnnecessaryLocalVariable")
    JSONObject json = new JSONObject()
      .put("id", transaction.id())
      .put("this_account", new JSONObject()
        .put("id", transaction.account())
        .put("bank", transaction.bank()))
      .put("counterparty", new JSONObject()
        .put("name", transaction.name())
        .put("account_number", transaction.account_number()))
      .put("details", new JSONObject()
        .put("type", transaction.type())
        .put("description", transaction.description())
        .put("posted", transaction.posted())
        .put("completed", transaction.completed())
        .put("new_balance", transaction.new_balance())
        .put("value", transaction.value()));
    // @formatter:on

    return json;
  }

  @Override public String toString()
  {
    return transaction.toString();
  }

  private final Transaction transaction;
}
