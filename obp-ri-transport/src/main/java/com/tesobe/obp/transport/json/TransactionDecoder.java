/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
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
    assert transaction != null;

    json = transaction;
  }

  @Override public String id()
  {
    return json.optString("id", null);
  }

  @Override public String account()
  {
    return json.optString("account", null);
  }

  @Override public String bank()
  {
    return json.optString("bank", null);
  }

  @Override public String otherId()
  {
    JSONObject other = json.optJSONObject("other");

    return other != null ? other.optString("id") : null;
  }

  @Override public String otherAccount()
  {
    JSONObject other = json.optJSONObject("other");

    return other != null ? other.optString("account") : null;
  }

  @Override public String type()
  {
    return json.optString("type", null);
  }

  @Override public String description()
  {
    return json.optString("description", null);
  }

  @Override public ZonedDateTime posted()
  {
    return zonedDateTimeFromJson(json.optString("posted", null));
  }

  @Override public ZonedDateTime completed()
  {
    return zonedDateTimeFromJson(json.optString("completed", null));
  }

  @Override public String balance()
  {
    return json.optString("balance", null);
  }

  @Override public String value()
  {
    return json.optString("value", null);
  }

  @Override public String toString()
  {
    return json.toString();
  }

  private final JSONObject json;
}
