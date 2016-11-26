/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Transport;
import org.json.JSONObject;

import java.util.Optional;

import static com.tesobe.obp.transport.Pager.DEFAULT_OFFSET;
import static com.tesobe.obp.transport.Pager.DEFAULT_SIZE;

/**
 * todo document
 */
abstract class RequestDecoder implements Decoder.Request
{
  protected RequestDecoder(String request)
  {
    this.request = request;

    json = new JSONObject(request);
    name = json.optString("name", "");
    version = json.optString("version", "");
  }

//  @Override public Optional<String> accountId()
//  {
//    return Optional.ofNullable(json.optString("accountId", null));
//  }
//
//  @Override public Optional<String> bankId()
//  {
//    return Optional.ofNullable(json.optString("bankId", null));
//  }
//
//  @Override public Optional<String> transactionId()
//  {
//    return Optional.ofNullable(json.optString("transactionId", null));
//  }
//
//  @Override public Optional<String> userId()
//  {
//    return Optional.ofNullable(json.optString("userId", null));
//  }
//
//  @Override public Optional<String> amount()
//  {
//    return Optional.ofNullable(json.optString("amount", null));
//  }

//  @Override public Optional<String> currency()
//  {
//    return Optional.ofNullable(json.optString("currency", null));
//  }
//
//  @Override public Optional<String> otherAccountId()
//  {
//    return Optional.ofNullable(json.optString("otherId", null));
//  }
//
//  @Override public Optional<String> otherAccountCurrency()
//  {
//    return Optional.ofNullable(json.optString("otherCurrency", null));
//  }
//
//  @Override public Optional<String> transactionType()
//  {
//    return Optional.ofNullable(json.optString("transactionType", null));
//  }

  @Override public Optional<Transport.Target> target()
  {
    return Optional.ofNullable(
      Transport.target(json.optString("target", null)));
  }

//    @Override public int offset()
//    {
//      return json.optInt("offset", 0);
//    }

//    @Override public int size()
//    {
//      return json.optInt("size", 0);
//    }
//
//    @Override public Optional<String> field()
//    {
//      return Optional.ofNullable(json.optString("field", null));
//    }
//
//    @Override public Optional<com.tesobe.obp.transport.Pager.SortOrder> sort()
//    {
//      return Optional.ofNullable(
//        json.optEnum(com.tesobe.obp.transport.Pager.SortOrder.class, "sort"));
//    }
//
//    @Override public Optional<ZonedDateTime> earliest()
//    {
//      return Optional.ofNullable(
//        Json.zonedDateTimeFromJson(json.optString("earliest", null)));
//    }
//
//    @Override public Optional<ZonedDateTime> latest()
//    {
//      return Optional.ofNullable(
//        Json.zonedDateTimeFromJson(json.optString("latest", null)));
//    }

  /**
   * @return empty string if absent or without value
   */
  @Override public String name()
  {
    return name;
  }

  /**
   * @return empty string if absent or without value
   */
  @Override public String version()
  {
    return version;
  }

  @Override public String raw()
  {
    return request;
  }

  @Override public String toString()
  {
    return json.toString();
  }

//  @Override public Decoder.Pager pager()
//  {
//    return new PagerDecoder(json);
//  }

  @Override public Decoder.Parameters parameters()
  {
    return new ParameterDecoder(json);
  }

  @Override public Decoder.Fields fields()
  {
    JSONObject fields = json.optJSONObject("fields");

    return fields != null ? new FieldDecoder(fields) : FieldDecoder.empty();
  }

  @Override public Decoder.Pager pager()
  {
    return new Pager();
  }

  private final String request;

  protected JSONObject json;
  protected String name;
  protected String version;

  class Pager implements Decoder.Pager
  {
    @Override public int offset()
    {
      return json.optInt("offset", DEFAULT_OFFSET);
    }

    @Override public Optional<String> state()
    {
      return Optional.ofNullable(json.optString("state", null));
    }

    @Override public int size()
    {
      return json.optInt("size", DEFAULT_SIZE);
    }

    @Override
    public <T> Optional<com.tesobe.obp.transport.Pager.Filter<T>> filter(T type)
    {
      throw new RuntimeException();
    }

    @Override public Optional<com.tesobe.obp.transport.Pager.Sorter> sorter()
    {
      throw new RuntimeException();
    }
  }
}
