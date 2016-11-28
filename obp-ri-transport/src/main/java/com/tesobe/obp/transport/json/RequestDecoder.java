/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Pager.SortOrder;
import com.tesobe.obp.transport.Pager.Sorter;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.spi.TransactionSorter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.tesobe.obp.transport.Pager.DEFAULT_OFFSET;
import static com.tesobe.obp.transport.Pager.DEFAULT_SIZE;

/**
 * todo document
 */
abstract class RequestDecoder implements Decoder.Request
{
  protected RequestDecoder(String requestId, String request)
  {
    this.requestId = requestId;
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
    return new ParameterDecoder(requestId, json);
  }

  @Override public Decoder.Fields fields()
  {
    JSONObject fields = json.optJSONObject("fields");

    return fields != null ? new FieldDecoder(fields) : FieldDecoder.empty();
  }

  @Override public String requestId()
  {
    return requestId;
  }

  @Override public Decoder.Pager pager()
  {
    return new Pager();
  }

  protected final String requestId;
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

    @Override public Optional<String> filterType()
    {
      JSONObject filter = json.optJSONObject("filter");

      return filter == null
             ? Optional.empty()
             : Optional.ofNullable(filter.optString("type", null));
    }

    @Override public int size()
    {
      return json.optInt("size", DEFAULT_SIZE);
    }

    @Override @SuppressWarnings("unchecked")
    public <T> Optional<com.tesobe.obp.transport.Pager.Filter<T>> filter(
      String name, Class<T> type)
    {
      final JSONObject filter = json.optJSONObject("filter");
      final String kind = filter != null
                          ? filter.optString("type", null)
                          : null;

      if(ZonedDateTime.class.isAssignableFrom(type) && "timestamp".equals(kind))
      {
        return Optional.of(new com.tesobe.obp.transport.Pager.Filter<T>()
        {
          @Override public String fieldName()
          {
            return filter.optString("name", null);
          }

          @Override public Class<T> type()
          {
            return (Class<T>)ZonedDateTime.class;
          }

          @Override public T lowerBound()
          {
            String low = filter.optString("low", null);

            return (T)Json.zonedDateTimeFromJson(low);
          }

          @Override public T higherBound()
          {
            String high = filter.optString("high", null);

            return (T)Json.zonedDateTimeFromJson(high);
          }
        });
      }
      else
      {
        return Optional.empty();
      }
    }

    @Override public Optional<Sorter> sorter()
    {
      final JSONArray sort = json.optJSONArray("sort");

      if(sort != null)
      {
        final List<String> fields = new ArrayList<>();
        final List<SortOrder> orders = new ArrayList<>();

        for(Object entry : sort)
        {
          if(entry instanceof JSONObject)
          {
            JSONObject item = JSONObject.class.cast(entry);
            Iterator<String> keys = item.keys();

            if(keys.hasNext())
            {
              String key = keys.next();
              SortOrder value = item.optEnum(SortOrder.class, key, null);

              if(value != null)
              {
                fields.add(key);
                orders.add(value);
              }
            }
          }
        }

        return Optional.of(new Sorter()
        {
          @Override public SortedMap<String, SortOrder> fields()
          {
            TreeMap<String, SortOrder> map = new TreeMap<>(
              Comparator.comparingInt(fields::indexOf));

            for(int i = 0; i < Math.min(fields.size(), orders.size()); ++i)
            {
              map.put(fields.get(i), orders.get(i));
            }

            return map;
          }

          @SuppressWarnings("unchecked") @Override
          public <T> List<T> sort(List<T> items, Class<T> type)
          {
            if(items != null && type != null)
            {
              if(Transaction.class.equals(type))
              {
                items.sort(
                  (Comparator<T>)TransactionSorter.make(fields, orders));
              }
            }

            return items;
          }
        });
      }
      else
      {
        return Optional.empty();
      }
    }
  }
}
