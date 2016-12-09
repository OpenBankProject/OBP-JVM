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

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Map;

import static java.util.Objects.nonNull;

/**
 * @since 2016.11
 */
@SuppressWarnings("WeakerAccess") public class EncoderNov2016
  extends EncoderSep2016 implements Encoder
{
  public EncoderNov2016(Transport.Version v)
  {
    super(v);
  }

  @Override public String account(Account a)
  {
    JSONObject result = new JSONObject();

    if(nonNull(a))
    {
      JSONArray data = new JSONArray();

      json(a, data);

      result.put("data", data);
      result.put("target", Transport.Target.account);
    }

    return result.toString();
  }

  @Override public String accounts(Collection<? extends Account> as, int count,
    boolean more, String state)
  {
    JSONArray data = null;


    if(nonNull(as) && !as.isEmpty())
    {
      data = new JSONArray();

      for(Account a : as)
      {
        json(a, data);
      }
    }

    return response(Transport.Target.accounts, count, more, state,
      data).toString();
  }

  @Override public String bank(Bank b)
  {
    JSONObject result = new JSONObject();

    if(nonNull(b))
    {
      JSONArray data = new JSONArray();

      json(b, data);

      result.put("data", data);
      result.put("target", Transport.Target.bank);
    }

    return result.toString();
  }

  @Override
  public String banks(Collection<? extends Bank> bs, int count, boolean more,
    String state)
  {
    JSONArray data = null;

    if(nonNull(bs) && !bs.isEmpty())
    {
      data = new JSONArray();

      for(Bank b : bs)
      {
        json(b, data);
      }
    }

    return response(Transport.Target.banks, count, more, state,
      data).toString();
  }

  @Override
  public Request get(String caller, Transport.Target t, Pager p, String state,
    String userId, String bankId, String accountId, String transactionId)
  {
    return request("get").put("north", caller)
      .put("target", String.valueOf(t))
      .put(p)
      .put("state", state)
      .put("accountId", accountId)
      .put("bankId", bankId)
      .put("transactionId", transactionId)
      .put("userId", userId);
  }

  @Override public Request put(String caller, Transport.Target t,
    Map<String, String> fields, Map<String, BigDecimal> money,
    Map<String, Temporal> timestamps)
  {
    JSONObject fs = new JSONObject(fields);

    money.forEach(fs::putOpt);
    timestamps.forEach((k, v) -> fs.putOpt(k, temporal(v)));

    return request("put").put("north", caller).put("target", String.valueOf(t))
      .put("fields", fs);
  }

  public String temporal(Temporal t)
  {
    if(t instanceof ZonedDateTime)
    {
      return Json.toJson(ZonedDateTime.class.cast(t));
    }

    return null;
  }

  @Override public String token(Token t)
  {
    JSONObject result = new JSONObject();

    if(nonNull(t))
    {
      result.put("error", t.error());

      t.id().map(id -> result.put("id", id));
    }

    return result.toString();
  }

  @Override public String transaction(Transaction t)
  {
    JSONObject result = new JSONObject();

    if(nonNull(t))
    {
      JSONArray data = new JSONArray();

      json(t, data);

      result.put("data", data);
      result.put("target", Transport.Target.transaction);
    }

    return result.toString();
  }

  @Override
  public String transactions(Collection<? extends Transaction> ts, int count,
    boolean more, String state)
  {
    JSONArray data = null;

    if(nonNull(ts) && !ts.isEmpty())
    {
      data = new JSONArray();

      for(Transaction t : ts)
      {
        json(t, data);
      }
    }

    return response(Transport.Target.transactions, count, more, state,
      data).toString();
  }

  @Override public String user(User u)
  {
    JSONObject result = new JSONObject();

    if(nonNull(u))
    {
      JSONArray data = new JSONArray();

      json(u, data);

      result.put("data", data);
      result.put("target", Transport.Target.user);
    }

    return result.toString();
  }

  @Override
  public String users(Collection<? extends User> users, int count, boolean more,
    String state)
  {
    JSONArray data = null;

    if(nonNull(users) && !users.isEmpty())
    {
      data = new JSONArray();

      for(User user : users)
      {
        json(user, data);
      }
    }

    return response(Transport.Target.users, count, more, state,
      data).toString();
  }

  protected JSONObject response(Transport.Target target, int count,
    boolean more, String state, JSONArray data)
  {
    JSONObject response = new JSONObject();

    if(target != null)
    {
      response.put("target", target);
    }

    response.put("count", count); // even if zero

    if(more)
    {
      response.put("pager", "more");
    }

    if(state != null && !state.isEmpty())
    {
      response.put("state", state);
    }

    if(data != null && data.length() > 0)
    {
      response.put("data", data);
    }

    return response;
  }

  @Override public Transport.Version version()
  {
    return version;
  }
}
