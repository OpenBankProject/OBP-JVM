/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo;

import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Sender;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.spi.Decoder;
import com.tesobe.obp.transport.spi.DefaultReceiver;
import com.tesobe.obp.transport.spi.Encoder;
import com.tesobe.obp.transport.spi.Receiver;

import java.util.Optional;

/**
 * Set up north and south and request a bankOld by id.
 * Enable trace log level for <tt>com.tesobe.obp.transport</tt> to see what
 * happens behind the scenes.
 */
public class SuperSimpleDemo
{
  public static void main(String[] commandLine) throws InterruptedException
  {
    Transport.Factory factory = Transport.defaultFactory();
    Receiver south = new South(factory.codecs());
    Sender north = south::respond; // super simple transport layer
    Connector connector = factory.connector(north);
    String bankId = "my-bankOld";
    Optional<Bank> bank = connector.getBank(bankId); // <- this is it

    System.out.println();
    System.out.print("connector.getBank(\"");
    System.out.print(bankId);
    System.out.print("\") \u2192 ");
    System.out.println(bank);
  }

  static class South extends DefaultReceiver
  {
    public South(Codecs cs)
    {
      super(cs);
    }

    @Override protected String getBank(Decoder.Request r, Encoder e)
    {
      if(r.bankId().isPresent())
      {
        return e.bank(new Bank()
        {
          @SuppressWarnings("OptionalGetWithoutIsPresent") @Override
          public String id()
          {
            return r.bankId().get();
          }

          @Override public String shortName()
          {
            return "My Bank";
          }

          @Override public String fullName()
          {
            return "My Very Own Bank";
          }

          @Override public String logo()
          {
            return "https://example.org/my-bankOld/logo.png";
          }

          @Override public String url()
          {
            return "https://example.org/my-bankOld/";
          }
        });
      }
      else
      {
        return e.bank(null);
      }
    }
  }
}
