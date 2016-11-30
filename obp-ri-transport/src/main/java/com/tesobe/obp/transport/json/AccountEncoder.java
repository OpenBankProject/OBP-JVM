/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Account;
import org.json.JSONObject;

/**
 * Writes an account to JSON .
 * Missing fields will be skipped.
 * The constructor trusts that the account is not null.
 *
 * @since 2016.11
 */
@SuppressWarnings("WeakerAccess") class AccountEncoder
{
  public AccountEncoder(Account a)
  {
    assert a != null;

    account = a;
  }

  public JSONObject toJson()
  {
    return new JSONObject()
      .put("balanceAmount", account.balanceAmount())
      .put("balanceCurrency", account.balanceCurrency())
      .put("bankId", account.bankId())
      .put("iban", account.iban())
      .put("label", account.label())
      .put("accountId", account.accountId())
      .put("number", account.number())
      .put("type", account.type())
      .put("userId", account.userId());
  }

  private final Account account;
}
