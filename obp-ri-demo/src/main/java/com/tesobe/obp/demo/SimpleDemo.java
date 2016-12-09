/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo;

import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Responder;
import com.tesobe.obp.transport.Sender;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.spi.DefaultResponder;
import com.tesobe.obp.transport.spi.Receiver;
import com.tesobe.obp.transport.spi.ReceiverNov2016;

import java.util.Optional;

/**
 * Set up north and south and request a bank by id.
 * Enable trace log level for <tt>com.tesobe.obp.transport</tt> to see what
 * happens behind the scenes.
 */
public class SimpleDemo
{
  public static void main(String[] commandLine) throws InterruptedException
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
        Optional<Bank> bank = connector.getBank(bankId); // <- this is it

        System.out.println();
        System.out.print("connector.getBank(\"");
        System.out.print(bankId);
        System.out.print("\") \u2192 ");
        System.out.println(bank);
      }
      catch(InterruptedException e)
      {
        e.printStackTrace();
      }
    }

    private final Connector connector;
  }

  private static class South extends DefaultResponder
  {
    @Override
    public Optional<Bank> getBank(Decoder.Pager p, Decoder.Parameters ps)
    {
      return ps.bankId().map(bankId -> new Bank()
      {
        @Override public String id()
        {
          return bankId;
        }

        @Override public String logo()
        {
          return "logo-x";
        }

        @Override public String name()
        {
          return "name-x";
        }

        @Override public String url()
        {
          return "url-x";
        }
      });
    }
  }
}
