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
      .put("id", bank.id())
      .put("short", bank.shortName())
      .put("name", bank.fullName())
      .put("logo", bank.logo())
      .put("url", bank.url());
  }

  private final Bank bank;
}
