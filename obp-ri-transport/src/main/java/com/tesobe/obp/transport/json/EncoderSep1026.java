/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Transport;
import org.json.JSONObject;

/**
 * Messages are sent as JSON. The methods returning a {@link Request} are
 * called by the {@link Connector} implementation, the north, to create a
 * message to send southwards.
 * <p>
 * Each request has two mandatory fields: {@code name} and {@version}.
 */
public class EncoderSep1026 extends Encoder
{
  public EncoderSep1026(Transport.Version v)
  {
    super(v);
  }

  /**
   * Anonymous request for a bank.
   *
   * @param bankId the bank's identifier. Sent as is, even if null.
   *
   * @return a request that can be serialized to a message payload
   */
  @Override public Request getBank(String bankId)
  {
    return request1("GET bank").add("bank", bankId);
  }

  @Override public Request getBank(String userId, String bankId)
  {
    return request1("GET bank").add("bank", bankId).add("user", userId);
  }

  @Override public String bank(Bank b)
  {
    JSONObject json = json(b);

    return json != null ? json.toString() : notFound();
  }

  protected Request request1(String name)
  {
    return new Request(name);
  }

  protected class Request
    implements com.tesobe.obp.transport.spi.Encoder.Request
  {
    public Request(String name)
    {
      request.put("name", name);
      request.put("version", version);
    }

    public Request add(String key, String value)
    {
      request.put(key, value);

      return this;
    }

    @Override public String toString()
    {
      return request.toString();
    }

    JSONObject request = new JSONObject();
  }
}
