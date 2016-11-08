/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Data;
import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Transport;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.tesobe.obp.transport.json.Json.zonedDateTimeFromJson;
import static java.lang.String.format;
import static java.util.Objects.nonNull;

/**
 * @since 2016.11
 */
public class DecoderNov2016 implements Decoder
{
  public DecoderNov2016(Transport.Version v)
  {
    version = v;
  }

  @Override public Optional<Request> request(String requestId, String request)
  {
    return Optional.of(new RequestDecoder(requestId, request));
  }

  @Override public String describe(String response)
  {
    return response;
  }

  @Override public Response get(Transport.Target t, String response)
  {
    if(nonNull(response) && !response.equals("null"))
    {
      try
      {
        JSONObject wrapper = new JSONObject(response);

        if(t != null)
        {
          Transport.Target s = wrapper.optEnum(Transport.Target.class, "target",
            null);

          if(s == null || s != t)
          {
            return new ErrorResponse("Targets do not match!");
          }
        }

        String error = wrapper.optString("error", null);

        if(error == null)
        {
          JSONArray data = wrapper.optJSONArray("data");

          int count = wrapper.optInt("count", 0);
          boolean more = "more".equals(wrapper.optString("pager"));
          String state = wrapper.optString("state", null);
          List<Data> result = nonNull(data)
                              ? data(data)
                              : Collections.emptyList();

          return new ValidResponse(count, more, result, state);
        }

        return new ErrorResponse(error);
      }
      catch(JSONException e)
      {
        log.error("{}", response);
        log.trace("{}", response, e);

        return new ErrorResponse(e.getMessage());
      }
    }

    return new ErrorResponse("Empty response received!");
  }

  /**
   * North: decode response to put.
   *
   * @param t target
   * @param response encoded response
   *
   * @return delegate to {@link #get}
   */
  @Override public Response put(Transport.Target t, String response)
  {
    return get(t, response);
  }

  @Override public Response fetch(String response)
  {
    return get(null, response);
  }

  private List<Data> data(JSONArray json)
  {
    assert json != null;

    List<Data> data = new ArrayList<>();

    for(Object j : json)
    {
      if(j != null && j instanceof JSONObject)
      {
        data.add(new Data()
        {
          @Override public String text(String key)
          {
            return ((JSONObject)j).optString(key, null);
          }

          @Override public BigDecimal money(String key)
          {
            return ((JSONObject)j).optBigDecimal(key, null);
          }

          @Override public ZonedDateTime timestamp(String key)
          {
            return zonedDateTimeFromJson(((JSONObject)j).optString(key, null));
          }

          @Override public String toString()
          {
            return j.toString();
          }
        });
      }
    }

    return data;
  }


  @Override public Transport.Version version()
  {
    return version;
  }

  @Override public String toString()
  {
    return getClass().getTypeName() + "-" + version;
  }

  static final Logger log = LoggerFactory.getLogger(DecoderNov2016.class);
  protected final Transport.Version version;

  protected class ErrorResponse implements Response
  {
    public ErrorResponse()
    {
      this("bad");
    }

    public ErrorResponse(String message)
    {
      this.message = message;
    }

    @Override public List<Data> data()
    {
      return Collections.emptyList();
    }

    @Override public boolean hasMorePages()
    {
      return false;
    }

    @Override public String state()
    {
      return null;
    }

    @Override public int count()
    {
      return 0;
    }

    @Override public Optional<Error> error()
    {
      return Optional.of(() -> message);
    }

    final String message;
  }

  @SuppressWarnings("WeakerAccess") protected class ValidResponse
    implements Response
  {
    public ValidResponse(int count, boolean more, List<Data> result,
      String state)
    {
      this.count = count;
      this.more = more;
      this.result = result;
      this.state = state;
    }

    @Override public List<Data> data()
    {
      return result;
    }

    @Override public boolean hasMorePages()
    {
      return more;
    }

    @Override public String state()
    {
      return state;
    }

    @Override public int count()
    {
      return count;
    }

    @Override public Optional<Error> error()
    {
      return Optional.empty();
    }

    @Override public String toString()
    {
      return format(Locale.US, "%d %b %s %s", count, more, state, result);
    }

    final int count;
    final boolean more;
    final List<Data> result;
    final String state;
  }
}
