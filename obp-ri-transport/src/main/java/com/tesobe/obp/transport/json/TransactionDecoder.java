/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Transaction;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static com.tesobe.obp.transport.json.Json.zonedDateTimeFromJson;

/**
 * Reads a JSON transaction.
 * Unexpected fields will be ignored, missing fields default to {@code null}.
 * The constructor trusts that the JSON is not null.
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
    return json.optString("transactionId", null);
  }

  @Override public BigDecimal amount()
  {
    return json.optBigDecimal("amount", null);
  }

  @Override public String accountId()
  {
    return json.optString("accountId", null);
  }

  @Override public String bankId()
  {
    return json.optString("bankId", null);
  }

  @Override public String counterpartyId()
  {
    return json.optString("counterpartyId", null);
  }

  @Override public String counterpartyName()
  {
    return json.optString("counterpartyName", null);
  }

  @Override public ZonedDateTime completedDate()
  {
    return zonedDateTimeFromJson(json.optString("completedDate", null));
  }

  @Override public String currency()
  {
    return json.optString("currency", null);
  }

  @Override public String description()
  {
    return json.optString("description", null);
  }

  @Override public BigDecimal newBalanceAmount()
  {
    return json.optBigDecimal("newBalanceAmount", null);
  }

  @Override public String newBalanceCurrency()
  {
    return json.optString("newBalanceCurrency", null);
  }

  @Override public ZonedDateTime postedDate()
  {
    return zonedDateTimeFromJson(json.optString("postedDate", null));
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
