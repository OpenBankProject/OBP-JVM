/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Decoder;
import org.json.JSONObject;

import java.util.Optional;

/**
 * @since 2016.11
 */
class FieldDecoder implements Decoder.Fields
{
  FieldDecoder(JSONObject fields)
  {
    json = fields;
  }

  @Override public Optional<String> accountId()
  {
    return Optional.ofNullable(json.optString("accountId", null));
  }

  @Override public Optional<String> amount()
  {
    return Optional.ofNullable(json.optString("amount", null));
  }

  @Override public Optional<String> currency()
  {
    return Optional.ofNullable(json.optString("currency", null));
  }

  @Override public Optional<String> counterpartyId()
  {
    return Optional.ofNullable(json.optString("counterpartyId", null));
  }

  @Override public Optional<String> newBalanceCurrency()
  {
    return Optional.ofNullable(json.optString("newBalanceCurrency", null));
  }

  @Override public Optional<String> type()
  {
    return Optional.ofNullable(json.optString("type", null));
  }

  @Override public Optional<String> userId()
  {
    return Optional.ofNullable(json.optString("userId", null));
  }

  @Override public Optional<String> bankId()
  {
    return Optional.ofNullable(json.optString("bankId", null));
  }

  @Override public Optional<String> completedDate()
  {
    return Optional.ofNullable(json.optString("completedDate", null));
  }

  @Override public Optional<String> counterpartyName()
  {
    return Optional.ofNullable(json.optString("counterpartyName", null));
  }

  @Override public Optional<String> description()
  {
    return Optional.ofNullable(json.optString("description", null));
  }

  @Override public Optional<String> newBalanceAmount()
  {
    return Optional.ofNullable(json.optString("newBalanceAmount", null));
  }

  @Override public Optional<String> postedDate()
  {
    return Optional.ofNullable(json.optString("postedDate", null));
  }

  @Override public Optional<String> transactionId()
  {
    return Optional.ofNullable(json.optString("transactionId", null));
  }

  static Decoder.Fields empty()
  {
    return new Decoder.Fields()
    {

      @Override public Optional<String> accountId()
      {
        return Optional.empty();
      }

      @Override public Optional<String> amount()
      {
        return Optional.empty();
      }

      @Override public Optional<String> bankId()
      {
        return Optional.empty();
      }

      @Override public Optional<String> completedDate()
      {
        return Optional.empty();
      }

      @Override public Optional<String> counterpartyId()
      {
        return Optional.empty();
      }

      @Override public Optional<String> counterpartyName()
      {
        return Optional.empty();
      }

      @Override public Optional<String> description()
      {
        return Optional.empty();
      }

      @Override public Optional<String> currency()
      {
        return Optional.empty();
      }

      @Override public Optional<String> newBalanceAmount()
      {
        return Optional.empty();
      }

      @Override public Optional<String> newBalanceCurrency()
      {
        return Optional.empty();
      }

      @Override public Optional<String> postedDate()
      {
        return Optional.empty();
      }

      @Override public Optional<String> transactionId()
      {
        return Optional.empty();
      }

      @Override public Optional<String> type()
      {
        return Optional.empty();
      }

      @Override public Optional<String> userId()
      {
        return Optional.empty();
      }
    };
  }

  JSONObject json;
}
