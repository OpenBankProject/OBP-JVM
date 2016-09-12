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
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.Objects.nonNull;

/**
 * Internal JSON encoder. Only called by trusted code.
 *
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public class Encoder
  implements com.tesobe.obp.transport.spi.Encoder
{
  public Encoder(Transport.Version v)
  {
    version = v;
  }

  @Override public Request getAccount(String bankId, String accountId)
  {
    return request("get account").put("bank", bankId).put("account", accountId);
  }

  @Override
  public Request getAccount(String userId, String bankId, String accountId)
  {
    return request("get account").put("user", userId).put("bank", bankId)
      .put("account", accountId);
  }

  @Override public Request getAccounts(String bankId)
  {
    return request("get accounts").put("bank", bankId);
  }

  @Override public Request getAccounts(String userId, String bankId)
  {
    return request("get accounts").put("user", userId).put("bank", bankId);
  }

  @Override public Request getBank(String userId, String bankId)
  {
    return request("get bank").put("user", userId).put("bank", bankId);
  }

  @Override public Request getBank(String bankId)
  {
    return request("get bank").put("bank", bankId);
  }

  @Override public Request getBanks()
  {
    return request("get banks");
  }

  @Override public Request getBanks(String userId)
  {
    return request("get banks").put("user", userId);
  }

  @Override public Request getTransaction(String bankId, String accountId,
    String transactionId)
  {
    return request("get transaction").put("bank", bankId)
      .put("account", accountId).put("transaction", transactionId);
  }

  @Override public Request getTransaction(String bankId, String accountId,
    String transactionId, String userId)
  {
    return request("get transaction").put("bank", bankId).put("user", userId)
      .put("account", accountId).put("transaction", transactionId);
  }

  @Override public Request getTransactions(String bankId, String accountId)
  {
    return request("get transactions").put("bank", bankId)
      .put("account", accountId);
  }

  @Override
  public Request getTransactions(String bankId, String accountId, String userId)
  {
    return request("get transactions").put("bank", bankId)
      .put("account", accountId).put("user", userId);
  }

  @Override public Request getUser(String userId)
  {
    return request("get user").put("user", userId);
  }

  @Override public Request saveTransaction(String userId, String accountId,
    String currency, String amount, String otherAccountId,
    String otherAccountCurrency, String transactionType)
  {
    return request("save transaction").put("user", userId)
      .put("account", accountId).put("currency", currency).put("amount", amount)
      .put("otherId", otherAccountId).put("otherCurrency", otherAccountCurrency)
      .put("transactionType", transactionType);
  }

  protected RequestBuilder request(String name)
  {
    return new RequestBuilder(name);
  }

  @Override public String account(Account a)
  {
    JSONObject json = json(a);

    return json != null ? json.toString() : notFound();
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

    return json != null ? json.toString() : notFound();
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

  private void json(Transaction t, JSONArray result)
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


  @Override public String transaction(Transaction t)
  {
    JSONObject json = json(t);

    return json != null ? json.toString() : JSONObject.NULL.toString();
  }

  @Override public String transactions(List<Transaction> ts)
  {
    JSONArray result = new JSONArray();

    if(nonNull(ts))
    {
      ts.forEach(transaction -> json(transaction, result));
    }

    return result.toString();
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

  final Transport.Version version;

  static final Logger log = LoggerFactory.getLogger(Encoder.class);

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

    public RequestBuilder put(String key, String value)
    {
      request.put(key, value);

      return this;
    }

    final String name;
    final JSONObject request = new JSONObject();
  }
}
