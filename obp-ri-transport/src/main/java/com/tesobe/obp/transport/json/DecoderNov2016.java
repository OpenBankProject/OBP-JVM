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
 * todo document
 */
public class DecoderNov2016 extends DecoderSep2016
{
  public DecoderNov2016(Transport.Version v)
  {
    super(v);
  }

  @Override public Optional<Request> request(String request)
  {
    return Optional.of(new Request0(request));
  }

  private <T extends Id> Response<T> add(List<T> result, JSONArray data,
    Class<T> type, Function<JSONObject, Id> decoder)
  {
    for(Object datum : data)
    {
      if(datum instanceof JSONObject)
      {
        result.add(type.cast(decoder.apply(JSONObject.class.cast(datum))));
      }
    }

    return new Response<T>()
    {
      @Override public List<T> data()
      {
        return result;
      }
    };
  }

  @Override
  public <T extends Id> Response<T> get(Class<T> type, String response)
  {
    if(nonNull(response) && !response.equals("null"))
    {
      try
      {
        JSONObject wrapper = new JSONObject(response);
        JSONArray data = wrapper.optJSONArray("data");

        if(nonNull(data))
        {
          List<T> result = new ArrayList<>();

          if(type == Account.class)
          {
            return add(result, data, type, AccountDecoder::new);
          }
          else if(type == Bank.class)
          {
            return add(result, data, type, BankDecoder::new);
          }
          else if(type == Transaction.class)
          {
            return add(result, data, type, TransactionDecoder::new);
          }
          else if(type == User.class)
          {
            return add(result, data, type, UserDecoder::new);
          }
        }
      }
      catch(JSONException e)
      {
        log.error("{}", response);

        return new ErrorResponse<T>();
      }
    }

    return new Response<T>()
    {
      @Override public List<T> data()
      {
        return Collections.emptyList();
      }
    };
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
  }

  public static class Request0 extends DefaultRequest
  {
    public Request0(String request)
    {
      super(request);
    }

    @Override public Pager pager()
    {
      return null; // todo fix
    }

    @Override public Parameters parameters()
    {
      return new Parameters()
      {
        @Override public Optional<String> accountId()
        {
          return Request0.this.accountId();
        }

        @Override public Optional<String> bankId()
        {
          return Request0.this.bankId();
        }

        @Override public Optional<String> transactionId()
        {
          return Request0.this.transactionId();
        }

        @Override public Optional<String> userId()
        {
          return Request0.this.userId();
        }
      };
    }

    @Override public Fields fields()
    {
      return new Fields()
      {
        @Override public Optional<String> accountId()
        {
          return Request0.this.accountId();
        }

        @Override public Optional<String> amount()
        {
          return Request0.this.amount();
        }

        @Override public Optional<String> currency()
        {
          return Request0.this.currency();
        }

        @Override public Optional<String> otherAccountId()
        {
          return Request0.this.otherAccountId();
        }

        @Override public Optional<String> otherAccountCurrency()
        {
          return Request0.this.otherAccountCurrency();
        }

        @Override public Optional<String> transactionType()
        {
          return Request0.this.transactionType();
        }

        @Override public Optional<String> userId()
        {
          return Request0.this.userId();
        }
      };
    }
  }
}
