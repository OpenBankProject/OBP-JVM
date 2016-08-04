/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Account;
import org.json.JSONObject;

/**
 * Todo error handling
 */
@SuppressWarnings("WeakerAccess") class AccountEncoder
{
  public AccountEncoder(Account a)
  {
    account = a;
  }

  public JSONObject toJson()
  {
    // @formatter:off
    @SuppressWarnings("unused")
    JSONObject json = new JSONObject()
      .put("id", account.id())
      .put("bank", account.bank())
      .put("label", account.label())
      .put("number", account.number())
      .put("type", account.type())
      .put("balance", new JSONObject()
        .put("currency", account.currency())
        .put("amount", account.amount()))
      .put("IBAN", account.iban());
    // @formatter:on

    return json;
  }

  private final Account account;
}
