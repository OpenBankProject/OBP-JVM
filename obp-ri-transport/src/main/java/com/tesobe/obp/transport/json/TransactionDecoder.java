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

import static com.tesobe.obp.transport.json.Json.zonedDateTimeFromJson;

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
    return transaction.optString("account", null);
  }

  @Override public String bank()
  {
    return transaction.optString("bank", null);
  }

  @Override public String otherId()
  {
    JSONObject other = transaction.optJSONObject("other");

    return other != null ? other.optString("id") : null;
  }

  @Override public String otherAccount()
  {
    JSONObject other = transaction.optJSONObject("other");

    return other != null
           ? other.optString("account")
           : null;
  }

  @Override public String type()
  {
    return transaction.optString("type", null);
  }

  @Override public String description()
  {
    return transaction.optString("description", null);
  }

  @Override public ZonedDateTime posted()
  {
    return zonedDateTimeFromJson(transaction.optString("posted", null));
  }

  @Override public ZonedDateTime completed()
  {
    return zonedDateTimeFromJson(transaction.optString("completed", null));
  }

  @Override public String balance()
  {
    return transaction.optString("balance", null);
  }

  @Override public String value()
  {
    return transaction.optString("value", null);
  }

  @Override public String toString()
  {
    return transaction.toString();
  }

  private final JSONObject transaction;
}
