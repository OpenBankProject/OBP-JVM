/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Decoder.Pager;
import com.tesobe.obp.transport.Pager.Filter;
import com.tesobe.obp.transport.Pager.SortOrder;
import com.tesobe.obp.transport.Responder;
import com.tesobe.obp.transport.Response;
import com.tesobe.obp.transport.Transport;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.Predicate;

/**
 * @since 2016.11
 */
@SuppressWarnings("WeakerAccess") public class DefaultResponder
  implements Responder
{
  @Override public Response first(String state, Pager p,
    Decoder.Parameters ps, Transport.Target t)
  {
    if(t != null)
    {
      switch(t)
      {
        case account:
          return account(state, p, ps);
        case accounts:
          return accounts(state, p, ps);
        case bank:
          return bank(state, p, ps);
        case banks:
          return banks(state, p, ps);
        case challengeThreshold:
          return challengeThreshold(state, p, ps);
        case transaction:
          return transaction(state, p, ps);
        case transactions:
          return transactions(state, p, ps);
        case user:
          return user(state, p, ps);
        case users:
          return users(state, p, ps);
      }
    }

    return new DefaultResponse();
  }

  @Override public Response next(String state, Pager p)
  {
    return new DefaultResponse();
  }

  @Override public Response put(Decoder.Parameters ps,
    Map<String, ?> fields, Transport.Target t)
  {
    return new DefaultResponse();
  }

  @Override public JSONObject describe(JSONObject json)
  {
    json.put("version", new JSONArray());

    return json;
  }

  @Override public Response fetch()
  {
    return new DefaultResponse();
  }

  protected Response account(String state, Pager p,
    Decoder.Parameters ps)
  {
    return new DefaultResponse();
  }

  protected Response accounts(String state, Pager p,
    Decoder.Parameters ps)
  {
    return new DefaultResponse();
  }

  protected Response bank(String state, Pager p,
    Decoder.Parameters ps)
  {
    return new DefaultResponse();
  }

  protected Response banks(String state, Pager p,
    Decoder.Parameters ps)
  {
    return new DefaultResponse();
  }

  protected Response challengeThreshold(String state,
    Pager p, Decoder.Parameters ps)
  {
    return new DefaultResponse();
  }

  protected Response transaction(String state, Pager p,
    Decoder.Parameters ps)
  {
    return new DefaultResponse();
  }

  protected Response transactions(String state, Pager p,
    Decoder.Parameters ps)
  {
    return new DefaultResponse();
  }

  protected Response user(String state, Pager p,
    Decoder.Parameters ps)
  {
    return new DefaultResponse();
  }

  protected Response users(String state, Pager p,
    Decoder.Parameters ps)
  {
    return new DefaultResponse();
  }

  protected Predicate<Map<String, Object>> filter(Pager p)
  {
    Predicate<Map<String, Object>> accept = x -> true;

    return p.filterType().map(type ->
    {
      switch(type)
      {
        case "timestamp":
          return p.filter(null, ZonedDateTime.class)
            .map(this::timestampFilter)
            .orElse(accept);
        default:
          return accept;
      }
    }).orElse(accept);
  }

  protected Predicate<Map<String, Object>> timestampFilter(
    Filter<ZonedDateTime> f)
  {
    String name = f.fieldName();
    ZonedDateTime earliest = f.lowerBound();
    ZonedDateTime latest = f.higherBound();

    if(earliest != null)
    {
      if(latest != null)
      {
        return datum ->
        {
          Object value = datum.get(name);

          return value instanceof ZonedDateTime
                 && ((ZonedDateTime)value).compareTo(earliest) >= 0
                 && ((ZonedDateTime)value).compareTo(latest) <= 0;
        };
      }
      else
      {
        return datum ->
        {
          Object value = datum.get(name);

          return value instanceof ZonedDateTime
                 && ((ZonedDateTime)value).compareTo(earliest) >= 0;
        };
      }
    }
    else if(latest != null)
    {
      return datum ->
      {
        Object value = datum.get(name);

        return value instanceof ZonedDateTime
               && ((ZonedDateTime)value).compareTo(latest) <= 0;
      };
    }
    else
    {
      return datum ->
      {
        Object value = datum.get(name);

        return value instanceof ZonedDateTime;
      };
    }
  }

  @SuppressWarnings("ComparatorMethodParameterNotUsed")
  protected Comparator<Map<String, Object>> sorter(Decoder.Pager p)
  {
    Comparator<Map<String, Object>> equal = (o1, o2) -> 0;

    return p.sorter().map(s ->
    {
      SortedMap<String, SortOrder> fields = s.fields();

      if(fields != null)
      {
        Set<String> names = fields.keySet();

        return (Comparator<Map<String, Object>>)(o1, o2) ->
        {
          for(String name : names)
          {
            int delta = 0;
            Object v1 = o1.get(name);
            Object v2 = o2.get(name);

            if(v1 == null)
            {
              delta = v2 == null ? 0 : -1;
            }
            else if(v2 != null)
            {
              SortOrder order = fields.get(name);

              if(SortOrder.ascending == order)
              {
                delta = compare(v1, v2);
              }
              else if(SortOrder.descending == order)
              {
                delta = compare(v2, v1);
              }
            }
            else
            {
              return 1;
            }

            if(delta != 0)
            {
              return delta;
            }
          }

          return 0;
        };
      }

      return equal;
    }).orElse(equal);
  }

  @SuppressWarnings("unchecked") protected int compare(Object v1, Object v2)
  {
    assert v1 != null;
    assert v2 != null;

    if(v1.getClass().isAssignableFrom(v2.getClass()) || v2.getClass()
      .isAssignableFrom(v1.getClass()))
    {
      if(v1 instanceof Comparable && v2 instanceof Comparable)
      {
        try
        {
          return ((Comparable)v1).compareTo(v2);
        }
        catch(Throwable e)
        {
          return 0;
        }
      }
    }

    return 0; // incomparable
  }
}
