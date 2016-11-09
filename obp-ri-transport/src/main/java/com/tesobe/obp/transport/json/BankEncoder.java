/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Bank;
import org.json.JSONObject;

/**
 * Writes a bank to JSON .
 * Missing fields will be skipped.
 * The constructor trusts that the bank is not null.
 */
@SuppressWarnings("WeakerAccess") class BankEncoder
{
  public BankEncoder(Bank b)
  {
    assert b != null;

    bank = b;
  }

  public JSONObject toJson()
  {
    return new JSONObject()
      .put("bankId", bank.bankId())
      .put("name", bank.name())
      .put("logo", bank.logo())
      .put("url", bank.url());
  }

  private final Bank bank;
}
