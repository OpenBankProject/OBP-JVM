/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.demo;

import com.tesobe.obp.transport.*;
import com.tesobe.obp.transport.spi.Decoder;
import com.tesobe.obp.transport.spi.DefaultResponder;
import com.tesobe.obp.transport.spi.Encoder;
import com.tesobe.obp.transport.spi.Receiver;

import java.util.Optional;

/**
 * Set up north and south and request a bank by id.
 * Enable trace log level for <tt>com.tesobe.obp.transport</tt> to see what
 * happens behind the scenes.
 */
public class SuperSimpleDemo
{
  public static void main(String[] commandLine) throws InterruptedException
  {
    Transport.Factory factory = Transport.defaultFactory();
    Decoder decoder = factory.decoder();
    Encoder encoder = factory.encoder();

    Receiver south = new DefaultResponder(decoder, encoder)
    {
      @Override
      protected String getPublicBank(String payload, Decoder.Request r,
        Encoder e)
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
              return "https://example.org/logo.png";
            }

            @Override public String url()
            {
              return "https://example.org/";
            }
          });
        }
        else
        {
          return e.bank(null);
        }
      }
    };

    Sender north = south::respond; // super simple transport layer
    Connector connector = factory.connector(north);

    OutboundContext outboundContext = new OutboundContext(null, null, null);

    Optional<Bank> bank = connector.getBank("my-bank", outboundContext).bank;

    System.out.println(bank);
  }
}
