/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Id;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.nonNull;

/**
 * @since 2016.11
 */
public class DecoderNov2016 extends DecoderSep2016
{
  public DecoderNov2016(Transport.Version v)
  {
    super(v);
  }

  @Override public Optional<Request> request(String requestId, String request)
  {
    return Optional.of(new Request0(requestId, request));
  }

  private <T extends Id> List<T> add(JSONArray data, Class<T> type,
    Function<JSONObject, Id> decoder)
  {
    List<T> result = new ArrayList<T>();

    for(Object datum : data)
    {
      if(datum instanceof JSONObject)
      {
        result.add(type.cast(decoder.apply(JSONObject.class.cast(datum))));
      }
    }

    return result;
  }

  @Override
  public <T extends Id> Response<T> get(Class<T> type, String response)
  {
    List<T> result = Collections.emptyList();
    int count = 0;
    boolean more = false;
    String state = null;

    if(nonNull(response) && !response.equals("null"))
    {
      try
      {
        JSONObject wrapper = new JSONObject(response);
        JSONArray data = wrapper.optJSONArray("data");

        count = wrapper.optInt("count", 0);
        more = "more".equals(wrapper.optString("pager"));
        state = wrapper.optString("state", null);

        if(nonNull(data))
        {
          if(type == Account.class)
          {
            result = add(data, type, AccountDecoder::new);
          }
          else if(type == Bank.class)
          {
            result = add(data, type, BankDecoder::new);
          }
          else if(type == Transaction.class)
          {
            result = add(data, type, TransactionDecoder::new);
          }
          else if(type == User.class)
          {
            result = add(data, type, UserDecoder::new);
          }
          else
          {
            result = Collections.emptyList();
          }
        }
      }
      catch(JSONException e)
      {
        log.error("{}", response);

        return new ErrorResponse<T>();
      }
    }

    return new ValidResponse<T>(count, more, result, state);
  }

  @Override public Iterable<Bank> bank(String response)
  {
    throw new RuntimeException();
  }

  protected class ErrorResponse<T> implements Response<T>
  {
    @Override public List<T> data()
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
  }

  protected class ValidResponse<T> implements Response<T>
  {
    public ValidResponse(int count, boolean more, List<T> result, String state)
    {
      this.count = count;
      this.more = more;
      this.result = result;
      this.state = state;
    }

    @Override public List<T> data()
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

    final int count;
    final boolean more;
    final List<T> result;
    final String state;
  }

  public class Request0 extends RequestDecoder
  {
    public Request0(String requestId, String request)
    {
      super(requestId, request);
    }

  }
}
