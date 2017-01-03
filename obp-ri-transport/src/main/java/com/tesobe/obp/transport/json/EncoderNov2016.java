/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Encoder;
import com.tesobe.obp.transport.Pager;
import com.tesobe.obp.transport.Response;
import com.tesobe.obp.transport.Transport;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

/**
 * @since 2016.11
 */
@SuppressWarnings("WeakerAccess") public class EncoderNov2016 implements Encoder
{
  public EncoderNov2016(Transport.Version v)
  {
    version = v;
  }

  @Override public Request get(String caller, Transport.Target t, Pager p,
    Map<String, ?> parameters)
  {
    return request("get").put("north", caller)
      .put("target", t)
      .put(p)
      .putParameters(parameters);
  }

  @Override public Request put(String caller, Transport.Target target,
    Map<String, ?> parameters, Map<String, ?> fields)
  {
    return request("put").put("north", caller)
      .put("target", target)
      .putParameters(parameters)
      .putFields(fields);
  }

  @Override public Request describe()
  {
    return request("describe");
  }

  @Override public String response(Response response, int offset,
    int size, String state, int count, boolean more, Transport.Target t)
  {
    JSONArray json = null;
    Map<String, ?> meta = null;

    if(nonNull(response))
    {
      List<? extends Map<String, ?>> data = response.data();

      if(nonNull(data) && !data.isEmpty())
      {
        int length = Math.min(data.size(), offset + size);

        if(length > 0)
        {
          json = new JSONArray();

          for(int i = offset; i < length; ++i)
          {
            Map<String, ?> item = data.get(i);

            if(item != null)
            {
              json.put(item);
            }
          }
        }
      }

      meta = response.meta();
    }

    return response(t, count, more, state, json, meta).toString();
  }

  protected RequestBuilder request(String name)
  {
    return new RequestBuilder(name);
  }

  protected JSONObject response(Transport.Target target, int count,
    boolean more, String state, JSONArray data, Map<String, ?> meta)
  {
    JSONObject response = new JSONObject();

    if(target != null)
    {
      response.put("target", target);
    }

    if(count != 0)
    {
      response.put("count", count);
    }

    if(more)
    {
      response.put("pager", "more");

      if(state != null)
      {
        response.put("state", state);
      }
    }

    if(data != null && data.length() > 0)
    {
      response.put("data", data);
    }

    if(meta != null)
    {
      put(response, meta);
    }

    return response;
  }

  @Override public String empty(String state, int count, Transport.Target t)
  {
    return response(t, count, false, state, null, null).toString();
  }

  @Override public String error(String message)
  {
    return new JSONObject().put("error", message).toString();
  }

  @Override public String toString()
  {
    return getClass().getTypeName() + "-" + version;
  }

  @Override public Transport.Version version()
  {
    return version;
  }

  @Override public String description(JSONObject response)
  {
    return response != null ? response.toString() : JSONObject.NULL.toString();
  }

  @Override public Request fetch()
  {
    return request("fetch");
  }

  static void put(JSONObject sink, Map<String, ?> values)
  {
    if(values != null)
    {
      for(String key : values.keySet())
      {
        if(key != null)
        {
          Object value = values.get(key);

          if(value != null) // only put known types
          {
            if(value instanceof String)
            {
              sink.put(key, value);
            }
            else if(value instanceof BigDecimal)
            {
              sink.put(key, value);
            }
            else if(value instanceof ZonedDateTime)
            {
              sink.put(key, Json.toJson((ZonedDateTime)value));
            }
          }
        }
      }
    }
  }

  protected static final Logger log = LoggerFactory.getLogger(
    EncoderNov2016.class);

  final Transport.Version version;

  @SuppressWarnings("SameParameterValue") class RequestBuilder
    implements Request
  {
    public RequestBuilder(String name)
    {
      this.name = name;

      request.put("name", name);
      request.put("version", version);
    }

    @Override public String toString()
    {
      return request.toString();
    }

    public RequestBuilder put(String key, Map<?, ?> value)
    {
      if(key != null && value != null && !value.isEmpty())
      {
        request.put(key, value);
      }

      return this;
    }

    public RequestBuilder put(String key, String value)
    {
      request.putOpt(key, value);

      return this;
    }

    public RequestBuilder put(String key, int value)
    {
      if(key != null)
      {
        request.put(key, value);
      }

      return this;
    }

    public RequestBuilder put(String key, JSONObject value)
    {
      request.putOpt(key, value);

      return this;
    }

    public RequestBuilder put(Pager p)
    {
      if(p != null)
      {
        if(p.isPaged())
        {
          put("count", p.count());
        }

        putIfNotZero("offset", p.offset());
        put("state", p.state());

        if(p.size() != Pager.DEFAULT_SIZE)
        {
          put("size", p.size());
        }

        put(p.filter());
        put(p.sorter());
      }

      return this;
    }

    public RequestBuilder putIfNotZero(String key, int value)
    {
      if(value != 0)
      {
        request.put(key, value);
      }

      return this;
    }

    public RequestBuilder put(Pager.Sorter s)
    {
      if(s != null && s.fields() != null && !s.fields().isEmpty())
      {
        JSONArray json = new JSONArray();

        s.fields().forEach((k, v) ->
        {
          json.put(new JSONObject().putOpt(k, String.valueOf(v)));
        });

        request.put("sort", json);
      }

      return this;
    }

    public RequestBuilder put(Pager.Filter<?> f)
    {
      if(f != null)
      {
        if(ZonedDateTime.class.equals(f.type()))
        {
          @SuppressWarnings("unchecked") Pager.Filter<ZonedDateTime> filter
            = (Pager.Filter<ZonedDateTime>)f;

          JSONObject json = new JSONObject();

          json.put("name", f.fieldName());
          json.put("type", "timestamp");
          json.put("low", Json.toJson(filter.lowerBound()));
          json.put("high", Json.toJson(filter.higherBound()));

          put("filter", json);
        }
        else
        {
          log.error("cannot serialize Pager.Filter of type " + f.type());
        }
      }

      return this;
    }

    public RequestBuilder put(String key, Transport.Target t)
    {
      if(key != null && t != null)
      {
        put(key, t.toString());
      }

      return this;
    }

    public RequestBuilder putParameters(Map<String, ?> ps)
    {
      EncoderNov2016.put(request, ps);

      return this;
    }

    public RequestBuilder putFields(Map<String, ?> fields)
    {
      if(fields != null)
      {
        Object sink = request.opt("fields");

        if(sink == null)
        {
          JSONObject fs = new JSONObject();

          put("fields", fs);

          EncoderNov2016.put(fs, fields);
        }
        else if(sink instanceof JSONObject)
        {
          EncoderNov2016.put((JSONObject)sink, fields);
        }
        else
        {
          log.warn("Mismatch with {}! Not putting fields {} ", sink, fields);
        }
      }

      return this;
    }

    final String name;
    final JSONObject request = new JSONObject();
  }
}
