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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("WeakerAccess") public class Decoder
  implements com.tesobe.obp.transport.spi.Decoder
{
  public Decoder(Transport.Version v)
  {
    version = v;
  }

  @Override public Request request(String request)
  {
    return new Request()
    {
      @Override public boolean hasArguments()
      {
        return arguments != null;
      }

      @Override public String userId()
      {
        assert arguments != null;

        return arguments.optString("userId", null);
      }

      /**
       * Legacy api: only one key.
       *
       * @return null if absent or without value
       */
      @Override public String name()
      {
        return name;
      }

      @Override public String raw()
      {
        return request;
      }

      JSONObject json = new JSONObject(request);
      JSONObject arguments;
      String name;

      {
        Iterator<String> keys = json.keys();

        name = keys.hasNext() ? keys.next() : null;
        arguments = json.opt(name) instanceof JSONObject ? json
          .getJSONObject(name) : null;
      }
    };
  }

  @Override public Iterable<Connector.Bank> banks(String response)
  {
    List<Connector.Bank> result = new ArrayList<>();

    if(response != null)
    {
      JSONArray array = new JSONArray(response);

      for(Object a : array)
      {
        if(a instanceof JSONObject)
        {
          JSONObject b = (JSONObject)a;

          result.add(
            new Connector.Bank(b.optString("id", null), b.optString("name", null)));
        }
      }
    }

    return result;
  }

  @Override public String toString()
  {
    return getClass().getTypeName() + "-" + version;
  }

  final Transport.Version version;
  static final Logger log = LoggerFactory.getLogger(Encoder.class);
}
