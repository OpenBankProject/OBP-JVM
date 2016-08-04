/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.User;
import com.tesobe.obp.util.tbd;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.Objects.nonNull;

/**
 * Internal JSON encoder. Only called by trusted code.
 * <p>
 * Todo make robust.
 *
 * @since 2016.0
 */
@SuppressWarnings("WeakerAccess") public class Encoder
  implements com.tesobe.obp.transport.spi.Encoder
{
  public Encoder(Transport.Version v)
  {
    version = v;
  }

  @Override public Request getPublicBanks()
  {
    return request("getBanks");
  }

  @Override public Request getPublicTransaction()
  {
    throw new tbd();
  }

  @Override public Request getPublicTransactions()
  {
    throw new tbd();
  }

  @Override public Request getPublicUser(String userId)
  {
    throw new tbd();
  }

  @Override public Request getPrivateBanks(String userId)
  {
    return request("getBanks").arguments("username", userId);
  }

  @Override public Request getPrivateAccount(String userId, String bankId,
    String accountId)
  {
    return request("getBankAccount")
      .arguments("username", userId, "bankId", bankId, "accountId", accountId);
  }

  @Override public Request getPrivateAccounts(String userId, String bankId)
  {
    return request("getBankAccounts")
      .arguments("username", userId, "bankId", bankId);
  }

  @Override public Request getPrivateBank(String userId, String bankId)
  {
    return request("getBank")
      .arguments("username", userId, "bankId", bankId);
  }

  protected RequestBuilder request(String name)
  {
    return new RequestBuilder(name);
  }

  @Override public String account(Account a)
  {
    JSONObject json = json(a);

    return json != null ? json.toString() : JSONObject.NULL.toString();
  }

  @Override public String accounts(List<Account> accounts)
  {
    JSONArray result = new JSONArray();

    if(nonNull(accounts))
    {
      accounts.forEach(account -> json(account, result));
    }

    return result.toString();
  }

  @Override public String bank(Bank b)
  {
    JSONObject json = json(b);

    return json != null ? json.toString() : JSONObject.NULL.toString();
  }

  @Override public String banks(List<Bank> banks)
  {
    JSONArray result = new JSONArray();

    if(nonNull(banks))
    {
      banks.forEach(bank -> json(bank, result));
    }

    return result.toString();
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

  @Override public String transaction(Transaction t)
  {
    throw new tbd();
  }

  @Override public String transactions(List<Transaction> ts)
  {
    throw new tbd();
  }

  @Override public String user(User u)
  {
    throw new tbd();
  }


//  protected void put(JSONArray result, Connector.Bank b)
//  {
//    assert nonNull(result);
//
//    if(nonNull(b))
//    {
//      result.put(json(b));
//    }
//  }

  @Override public String toString()
  {
    return getClass().getTypeName() + "-" + version;
  }

  final Transport.Version version;

  static final Logger log = LoggerFactory.getLogger(Encoder.class);

  class RequestBuilder implements Request
  {
    public RequestBuilder(String name)
    {
      this.name = name;

      request.put(name, JSONObject.NULL);
    }

    @Override public String toString()
    {
      log.trace("{} {}", version, request);

      return request.toString();
    }

    public Request arguments(String... kv)
    {
      for(int i = 0; i < kv.length - 1; i += 2)
      {
        arguments.put(kv[i], kv[i + 1]);
      }

      if(request.opt(name) == JSONObject.NULL)
      {
        request.put(name, arguments);
      }

      return this;
    }

    final String name;
    final JSONObject arguments = new JSONObject();
    final JSONObject request = new JSONObject();
  }
}
