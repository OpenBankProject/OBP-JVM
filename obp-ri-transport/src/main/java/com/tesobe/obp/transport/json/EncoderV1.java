/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.*;
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
@SuppressWarnings("WeakerAccess") public class EncoderV1
  implements com.tesobe.obp.transport.spi.Encoder
{
  public EncoderV1(Transport.Version v)
  {
    version = v;
  }

  @Override public Request getUser(String userId, OutboundContext outboundContext)
  {
    return request("request")
            .arguments(outboundContext, "function", "getUser", "version", version.toString(), "username", userId);
  }

  @Override public Request getBanks(OutboundContext outboundContext)
  {
    return request("request")
            .arguments(outboundContext, "function", "getBanks", "version", version.toString());
  }

  @Override
  public Request getTransaction(String bankId, String accountId,
                                String transactionId, OutboundContext outboundContext)
  {
    return request("request")
            .arguments(outboundContext, "function", "getTransaction", "version", version.toString(),
                    "accountId", accountId, "bankId", bankId, "transactionId", transactionId);
  }

  @Override
  public Request getTransactions(String bankId, String accountId,
                                 OutboundContext outboundContext)
  {
    return request("request")
            .arguments(outboundContext, "function", "getTransactions", "version", version.toString(),
                    "bankId", bankId, "accountId", accountId);
  }

  @Override public Request getAccount(OutboundContext outboundContext, String bankId,
                                      String accountId)
  {
    return request("request")
            .arguments(outboundContext, "function", "getBankAccount", "version", version.toString(),
                    "bankId", bankId, "accountId", accountId);
  }

  @Override public Request getAccounts(OutboundContext outboundContext, String bankId)
  {
    return request("request")
            .arguments(outboundContext, "function", "getBankAccounts", "version", version.toString(), "bankId", bankId);
  }

  @Override public Request getBank(OutboundContext outboundContext, String bankId)
  {
    return request("request")
            .arguments(outboundContext, "function", "getBank", "version", version.toString(), "bankId", bankId);
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

  @Override public String error(String message)
  {
    return new JSONObject().put("error", message).toString();
  }

  @Override public String notFound()
  {
    return JSONObject.NULL.toString();
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

  static final Logger log = LoggerFactory.getLogger(EncoderV1.class);

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

    public Request arguments(OutboundContext outboundContext, String... kv)
    {
      for(int i = 0; i < kv.length - 1; i += 2)
      {
        arguments.put(kv[i], kv[i + 1]);
      }

      // Add the outbound context
      arguments.put("outboundContext", new JSONObject()
              .put("user", outboundContext.user != null ? new JSONObject().put("userId", outboundContext.user.userId) : JSONObject.NULL)
              .put("view", outboundContext.view != null ? new JSONObject().put("viewId", outboundContext.view.viewId)
                      .put("isPublic", outboundContext.view.isPublic) : JSONObject.NULL)
              .put("token", outboundContext.token != null ? new JSONObject().put("tokenId", outboundContext.token.tokenId) :JSONObject.NULL));

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
