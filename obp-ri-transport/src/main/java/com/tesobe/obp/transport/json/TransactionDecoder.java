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

import java.time.ZonedDateTime;

import static com.tesobe.obp.util.Json.zonedDateTimeFromJson;

/**
 * todo error handling
 */
@SuppressWarnings("WeakerAccess") class TransactionDecoder
  implements Transaction
{
  public TransactionDecoder(JSONObject transaction)
  {
    this.transaction = transaction;
  }

  @Override public String id()
  {
    return transaction.optString("id", null);
  }

  @Override public String account()
  {
    JSONObject this_account = transaction.optJSONObject("this_account");

    return this_account != null ? this_account.optString("id") : null;
  }

  @Override public String bank()
  {
    JSONObject this_account = transaction.optJSONObject("this_account");

    return this_account != null ? this_account.optString("bank") : null;
  }

  @Override public String name()
  {
    JSONObject counterparty = transaction.optJSONObject("counterparty");

    return counterparty != null ? counterparty.optString("name") : null;
  }

  @Override public String account_number()
  {
    JSONObject counterparty = transaction.optJSONObject("counterparty");

    return counterparty != null
           ? counterparty.optString("account_number")
           : null;
  }

  @Override public String type()
  {
    JSONObject details = transaction.optJSONObject("details");

    return details != null ? details.optString("type") : null;
  }

  @Override public String description()
  {
    JSONObject details = transaction.optJSONObject("details");

    return details != null ? details.optString("type") : null;
  }

  @Override public ZonedDateTime posted()
  {
    JSONObject details = transaction.optJSONObject("details");

    return details != null ? zonedDateTimeFromJson(details.optString("posted")) : null;
  }

  @Override public ZonedDateTime completed()
  {
    JSONObject details = transaction.optJSONObject("details");

    return details != null ? zonedDateTimeFromJson(details.optString("completed")) : null;
  }

  @Override public String new_balance()
  {
    JSONObject details = transaction.optJSONObject("details");

    return details != null ? details.optString("new_balance") : null;
  }

  @Override public String value()
  {
    JSONObject details = transaction.optJSONObject("details");

    return details != null ? details.optString("value") : null;
  }

  @Override public String toString()
  {
    return transaction.toString();
  }

  private final JSONObject transaction;
}
