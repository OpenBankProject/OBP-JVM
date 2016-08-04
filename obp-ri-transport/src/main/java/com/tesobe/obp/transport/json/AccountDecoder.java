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
class AccountDecoder implements Account
{
  AccountDecoder(JSONObject account)
  {
    this.account = account;
  }

  @Override public String id()
  {
    return account.optString("id", null);
  }

  @Override public String bank()
  {
    return account.optString("bank", null);
  }

  @Override public String label()
  {
    return account.optString("label", null);
  }

  @Override public String number()
  {
    return account.optString("number", null);
  }

  @Override public String type()
  {
    return account.optString("type", null);
  }

  @Override public String currency()
  {
    JSONObject balance = account.optJSONObject("balance");

    return balance != null ? balance.optString("currency") : null;
  }

  @Override public String amount()
  {
    JSONObject balance = account.optJSONObject("balance");

    return balance != null ? balance.optString("amount") : null;
  }

  @Override public String iban()
  {
    return account.optString("IBAN", null);
  }

  @Override public String toString()
  {
    return account.toString();
  }

  private final JSONObject account;
}
