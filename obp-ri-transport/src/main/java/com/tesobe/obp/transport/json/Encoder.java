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
    return getPublicBanks().user(userId);
  }

  protected Request request(String name)
  {
    return new RequestBuilder(name);
  }

  protected Object bank(Connector.Bank b)
  {
    JSONObject bank = new JSONObject();

    bank.put("id", b.id);
    bank.put("name", b.name);

    return bank;
  }

  @Override public String banks(Connector.Bank... banks)
  {
    JSONArray result = new JSONArray();

    if(banks != null)
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

    if(banks != null)
    {
      banks.forEach(bank -> put(result, bank));
    }

    return result.toString();
  }

  protected void put(JSONArray result, Connector.Bank b)
  {
    if(b == null)
    {
      log.warn("A bank is null!");
    }
    else
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

    @Override public Request user(String id)
    {
      arguments.put("userId", id);

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
