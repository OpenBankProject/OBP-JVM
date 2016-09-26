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
    assert account != null;

    json = account;
  }

  @Override public String id()
  {
    return json.optString("id", null);
  }

  @Override public String bank()
  {
    return json.optString("bank", null);
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

  @Override public String currency()
  {
    return json.optString("currency", null);
  }

  @Override public String amount()
  {
    return json.optString("amount", null);
  }

  @Override public String iban()
  {
    return json.optString("iban", null);
  }

  @Override public String toString()
  {
    return json.toString();
  }

  private final JSONObject json;
}
