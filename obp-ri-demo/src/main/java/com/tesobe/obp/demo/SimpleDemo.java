/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo;

import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Responder;
import com.tesobe.obp.transport.Sender;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.nov2016.Bank;
import com.tesobe.obp.transport.spi.DefaultResponder;
import com.tesobe.obp.transport.spi.Receiver;
import com.tesobe.obp.transport.spi.ReceiverNov2016;
import com.tesobe.obp.util.Utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Set up north and south and request a bank by id.
 * Enable trace log level for <tt>com.tesobe.obp.transport</tt> to see what
 * happens behind the scenes.
 */
public class SimpleDemo
{
  public static void main(String[] commandLine) throws Exception
  {
    Transport.Factory factory = Transport.defaultFactory();
    Responder south = new South();
    Receiver receiver = new ReceiverNov2016(south, factory.codecs());
    Sender sender = receiver::respond; // super simple transport layer
    Connector connector = factory.connector(sender);
    North north = new North(connector);
    String bankId = "my-bank";

    north.getBank(bankId);
  }

  private static class North
  {
    North(Connector c)
    {
      connector = c;
    }

    void getBank(String bankId)
    {
      try
      {
        Map<String, String> parameters = new HashMap<>();

        parameters.put(Bank.bankId, bankId);

        Decoder.Response response = connector.get("getBank",
          Transport.Target.bank, parameters);

        System.out.println();
        System.out.print("connector.getBank(\"");
        System.out.print(bankId);
        System.out.print("\") \u2192 ");

        if(response.error().isPresent())
        {
          System.out.println(response.error().get());
        }
        else
        {
          System.out.print("bankId: ");
          System.out.println(response.data().get(0).text(Bank.bankId));
        }
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }

    private final Connector connector;
  }

  private static class South extends DefaultResponder
  {
    @Override
    public List<Map<String, Object>> first(String state, Decoder.Pager p,
      Decoder.Parameters ps, Transport.Target t)
    {
      return ps.bankId()
        .map(id -> Utils.merge(new HashMap<>(), Bank.bankId, (Object)id))
        .map(Collections::singletonList)
        .orElseGet(Collections::emptyList);
    }
  }
}
