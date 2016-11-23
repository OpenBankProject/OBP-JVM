/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

/**
 * todo document
 */
public class EncoderNov2016 extends EncoderSep2016 implements Encoder
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
    }

    return result.toString();
  }

  @Override public String accounts(List<? extends Account> accounts)
  {
    JSONObject result = new JSONObject();

    if(nonNull(accounts) && !accounts.isEmpty())
    {
      JSONArray data = new JSONArray();

      accounts.forEach(account -> json(account, data));

      result.put("data", data);
    }

    return result.toString();
  }

  @Override public String bank(Bank b)
  {
    JSONObject result = new JSONObject();

    if(nonNull(b))
    {
      JSONArray data = new JSONArray();

      json(b, data);

      result.put("data", data);
    }

    return result.toString();
  }

  @Override public String banks(List<? extends Bank> banks)
  {
    JSONObject result = new JSONObject();

    if(nonNull(banks) && !banks.isEmpty())
    {
      JSONArray data = new JSONArray();

      banks.forEach(bank -> json(bank, data));

      result.put("data", data);
    }

    return result.toString();
  }

  @Override
  public Request get(String caller, Transport.Target t, Pager p, String userId,
    String bankId, String accountId, String transactionId)
  {
    return request("get")
      .put("north", caller)
      .put("target", String.valueOf(t)).put(p).put("accountId", accountId)
      .put("bankId", bankId).put("transactionId", transactionId)
      .put("userId", userId);
  }

  @Override public Request put(String caller, Transport.Target t,
    Map<String, String> fields, Map<String, BigDecimal> money)
  {
    JSONObject fs = new JSONObject(fields);

    money.forEach(fs::put);

    return request("put").put("north", caller).put("target", String.valueOf(t))
      .put("fields", fs);
  }

  @Override public String transaction(Transaction t)
  {
    JSONObject result = new JSONObject();

    if(nonNull(t))
    {
      JSONArray data = new JSONArray();

      json(t, data);

      result.put("data", data);
    }

    return result.toString();
  }

  @Override public String transactions(List<? extends Transaction> transactions)
  {
    JSONObject result = new JSONObject();

    if(nonNull(transactions) && !transactions.isEmpty())
    {
      JSONArray data = new JSONArray();

      transactions.forEach(transaction -> json(transaction, data));

      result.put("data", data);
    }

    return result.toString();
  }

  @Override public String user(User u)
  {
    JSONObject result = new JSONObject();

    if(nonNull(u))
    {
      JSONArray data = new JSONArray();

      json(u, data);

      result.put("data", data);
    }

    return result.toString();
  }

  @Override public String users(List<? extends User> users)
  {
    JSONObject result = new JSONObject();

    if(nonNull(users) && !users.isEmpty())
    {
      JSONArray data = new JSONArray();

      users.forEach(bank -> json(bank, data));

      result.put("data", data);
    }

    return result.toString();
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
}
