/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Bank;
import org.json.JSONObject;

/**
 * Todo error handling
 */
class BankDecoder implements Bank
{
  BankDecoder(JSONObject bank)
  {
    this.bank = bank;
  }

  @Override public String id()
  {
    return bank.optString("id", null);
  }

  @Override public String shortName()
  {
    return bank.optString("short_name", null);
  }

  @Override public String fullName()
  {
    return bank.optString("full_name", null);
  }

  @Override public String logo()
  {
    return bank.optString("logo", null);
  }

  @Override public String url()
  {
    return bank.optString("website", null);
  }

  @Override public String toString()
  {
    return bank.toString();
  }

  private final JSONObject bank;
}
