/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 */
package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Transport;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.Collections.singletonMap;
import static java.util.Objects.nonNull;

/**
 * Internal JSON encoder. Only called by trusted code.
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

  protected RequestBuilder request(String name)
  {
    return new RequestBuilder(name);
  }

  @Override public String account()
  {
    return JSONObject.NULL.toString();
  }

  @Override public String account(Connector.Account a)
  {
    if(nonNull(a))
    {
      // @formatter:off
      JSONObject account = new JSONObject()
        .put("id", a.id)
        .put("bank", a.bank)
        .put("label", a.label)
        .put("number", a.number)
        .put("type", a.type)
        .put("balance", new JSONObject()
          .put("currency", a.currency)
          .put("amount", a.amount))
        .put("IBAN", a.iban)
        .put("owners", new JSONArray()) // todo ?
        .put("generate_public_view", false)
        .put("generate_accountants_view", true)
        .put("generate_auditors_view", true);
      // @formatter:on

      return account.toString();
    }

    return JSONObject.NULL.toString();
  }

  protected Object bank(Connector.Bank b)
  {
    if(nonNull(b))
    {
      // @formatter:off
      JSONObject bank = new JSONObject()
        .put("id", b.id)
        .put("short_name", b.name)
        .put("full_name", b.fullName)
        .put("logo", b.logo)
        .put("website", b.url);
      // @formatter:on

      return bank;
    }

    return JSONObject.NULL;
  }

  @Override public String banks(Connector.Bank... banks)
  {
    JSONArray result = new JSONArray();

    if(nonNull(banks))
    {
      for(Connector.Bank b : banks)
      {
        put(result, b);
      }
    }

    return result.toString();
  }

  @Override public String banks(List<Connector.Bank> banks)
  {
    JSONArray result = new JSONArray();

    if(nonNull(banks))
    {
      banks.forEach(bank -> put(result, bank));
    }

    return result.toString();
  }

  protected void put(JSONArray result, Connector.Bank b)
  {
    assert nonNull(result);

    if(nonNull(b))
    {
      result.put(bank(b));
    }
  }

  @Override public String toString()
  {
    return getClass().getTypeName() + "-" + version;
  }

  final Transport.Version version;

  final static JSONObject ERROR = new JSONObject(
    singletonMap("error", "bad request")); // todo mk better

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
/*
          // @formatter:off
          result.add(new Connector.Bank(
            b.optString("id", null),
            b.optString("short_name", null),
            b.optString("full_name", null),
            b.optString("logo", null),
            b.optString("website", null)));
          // @formatter:on

    public JSONObject toJson()
    {
      // @formatter:off
    return new JSONObject()
      .put("id", id)
      .put("bank", bank)
      .put("label", label)
      .put("number", number)
      .put("type", type)
      .put("balance", new JSONObject().put("currency", currency).put
      ("amount", amount))
      .put("IBAN", iban)
      .put("owners", new JSONArray()) // todo ?
      .put("generate_public_view", false)
      .put("generate_accountants_view", true)
      .put("generate_auditors_view", true);
    // @formatter:on
    }

    @Override public String toString()
    {
      return "Account{" + "id='" + id + '\'' + ", bank='" + bank + '\''
             + ", label='" + label + '\'' + ", number='" + number + '\''
             + ", type='" + type + '\'' + ", currency='" + currency + '\''
             + ", amount='" + amount + '\'' + ", iban='" + iban + '\'' + '}';
    }

    public JSONObject toJson(String owner)
    {
      // @formatter:off
    return new JSONObject()
      .put("id", id)
      .put("bank", bank)
      .put("label", label)
      .put("number", number)
      .put("type", type)
      .put("balance", new JSONObject().put("currency", currency).put
      ("amount", amount))
      .put("IBAN", iban)
      .put("owners", new JSONArray().put(owner)) // todo fix!
      .put("generate_public_view", false)
      .put("generate_accountants_view", true)
      .put("generate_auditors_view", true);
    // @formatter:on
    }


 */
