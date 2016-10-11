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
    return new JSONObject()
      .put("id", account.id())
      .put("bank", account.bank())
      .put("label", account.label())
      .put("number", account.number())
      .put("type", account.type())
      .put("currency", account.currency())
      .put("amount", account.amount())
      .put("iban", account.iban());
  }

  private final Account account;
}
