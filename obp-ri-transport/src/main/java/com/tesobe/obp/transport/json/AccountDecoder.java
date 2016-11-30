/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be
 * found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Account;
import org.json.JSONObject;

/**
 * Reads a JSON account.
 * Unexpected fields will be ignored, missing fields default to {@code null}.
 * The constructor trusts that the JSON is not null.
 *
 * @since 2016.11
 */
class AccountDecoder implements Account
{
  AccountDecoder(JSONObject account)
  {
    assert account != null;

    json = account;
  }

  @Override public String balanceAmount()
  {
    return json.optString("balanceAmount", null);
  }

  @Override public String balanceCurrency()
  {
    return json.optString("balanceCurrency", null);
  }

  @Override public String bankId()
  {
    return json.optString("bankId", null);
  }

  @Override public String iban()
  {
    return json.optString("iban", null);
  }

  @Override public String id()
  {
    return json.optString("accountId", null);
  }

  @Override public String label()
  {
    return json.optString("label", null);
  }

  @Override public String number()
  {
    return json.optString("number", null);
  }

  @Override public String type()
  {
    return json.optString("type", null);
  }

  @Override public String userId()
  {
    return json.optString("userId", null);
  }

  @Override public String toString()
  {
    return json.toString();
  }

  private final JSONObject json;
}
