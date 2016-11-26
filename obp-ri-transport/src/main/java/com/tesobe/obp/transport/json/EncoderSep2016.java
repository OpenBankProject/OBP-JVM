/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Encoder;
import com.tesobe.obp.transport.Pager;
import com.tesobe.obp.transport.Token;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.Map;

import static java.util.Objects.nonNull;

/**
 * Internal JSON encoder. Only called by trusted code.
 *
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public abstract class EncoderSep2016
  implements Encoder
{
  public EncoderSep2016(Transport.Version v)
  {
    version = v;
  }

//  @Override public Request getAccount(String bankId, String accountId)
//  {
//    return request("get account").put("bank", bankId).put("account",
// accountId);
//  }
//
//  @Override
//  public Request getAccount(String userId, String bankId, String accountId)
//  {
//    return request("get account")
//      .put("user", userId)
//      .put("bank", bankId)
//      .put("account", accountId);
//  }

//  @Override public Request getAccounts(String bankId)
//  {
//    return request("get accounts").put("bank", bankId);
//  }
//
//  @Override public Request getAccounts(String userId, String bankId)
//  {
//    return request("get accounts").put("user", userId).put("bank", bankId);
//  }
//
//  @Override public Request getBank(String userId, String bankId)
//  {
//    return request("get bankOld").put("user", userId).put("bank", bankId);
//  }
//
//  @Override public Request getBank(String bankId)
//  {
//    return request("get bankOld").put("bank", bankId);
//  }

//  @Override public Request getBanks()
//  {
//    return request("get banks");
//  }
//
//  @Override public Request getBanks(String userId)
//  {
//    return request("get banks").put("user", userId);
//  }

//  @Override public Request getTransaction(String bankId, String accountId,
//    String transactionId)
//  {
//    return request("get transaction")
//      .put("bank", bankId)
//      .put("account", accountId)
//      .put("transaction", transactionId);
//  }
//
//  @Override public Request getTransaction(String bankId, String accountId,
//    String transactionId, String userId)
//  {
//    return request("get transaction")
//      .put("bank", bankId)
//      .put("user", userId)
//      .put("account", accountId)
//      .put("transaction", transactionId);
//  }

//  @Override public Request getTransactions(Connector.Pager p, String bankId,
//    String accountId)
//  {
//    return request("get transactions")
//      .put(p)
//      .put("bank", bankId)
//      .put("account", accountId);
//  }

//  @Override public Request getTransactions(Connector.Pager p, String bankId,
//    String accountId, String userId)
//  {
//    return request("get transactions")
//      .put(p)
//      .put("bank", bankId)
//      .put("account", accountId)
//      .put("user", userId);
//  }

//  @Override public Request getUser(String userId)
//  {
//    return request("get user").put("user", userId);
//  }
//
//  @Override public Request getUsers(String userId)
//  {
//    return request("get users").put("user", userId);
//  }
//
//  @Override public Request getUsers()
//  {
//    return request("get users");
//  }

  protected RequestBuilder request(String name)
  {
    return new RequestBuilder(name);
  }





  protected JSONObject json(Account a)
  {
    if(nonNull(a))
    {
      return new AccountEncoder(a).toJson();
    }

    return null;
  }

  protected void json(Account a, JSONArray result)
  {
    if(nonNull(a))
    {
      JSONObject json = json(a);

      result.put(json != null ? json : JSONObject.NULL);
    }
    else
    {
      result.put(JSONObject.NULL);
    }
  }

  protected void json(Bank b, JSONArray result)
  {
    if(nonNull(b))
    {
      JSONObject json = json(b);

      result.put(json != null ? json : JSONObject.NULL);
    }
    else
    {
      result.put(JSONObject.NULL);
    }
  }

  protected JSONObject json(Bank b)
  {
    if(nonNull(b))
    {
      return new BankEncoder(b).toJson();
    }

    return null;
  }

  protected void json(Transaction t, JSONArray result)
  {
    if(nonNull(t))
    {
      JSONObject json = json(t);

      result.put(json != null ? json : JSONObject.NULL);
    }
    else
    {
      result.put(JSONObject.NULL);
    }
  }

  protected JSONObject json(Transaction t)
  {
    if(nonNull(t))
    {
      return new TransactionEncoder(t).toJson();
    }

    return null;
  }

  protected JSONObject json(User u)
  {
    if(nonNull(u))
    {
      return new UserEncoder(u).toJson();
    }

    return null;
  }

  protected void json(User u, JSONArray result)
  {
    if(nonNull(u))
    {
      JSONObject json = json(u);

      result.put(json != null ? json : JSONObject.NULL);
    }
    else
    {
      result.put(JSONObject.NULL);
    }
  }

  @Override public String transaction(Transaction t)
  {
    JSONObject json = json(t);

    return json != null ? json.toString() : JSONObject.NULL.toString();
  }




  @Override public String user(User u)
  {
    JSONObject json = json(u);

    return json != null ? json.toString() : JSONObject.NULL.toString();
  }

  @Override public String transactionId(String s)
  {
    return s;
  }


  @Override public String token(Token t)
  {
    return notFound();
  }

  @Override public String error(String message)
  {
    return new JSONObject().put("error", message).toString();
  }

  public String notFound()
  {
    return JSONObject.NULL.toString();
  }

  @Override public String toString()
  {
    return getClass().getTypeName() + "-" + version;
  }

  protected static final Logger log = LoggerFactory
    .getLogger(EncoderSep2016.class);
  final Transport.Version version;

  class RequestBuilder implements Request
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
        putIfNotZero("offset", p.offset());

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

    final String name;
    final JSONObject request = new JSONObject();
  }
}
