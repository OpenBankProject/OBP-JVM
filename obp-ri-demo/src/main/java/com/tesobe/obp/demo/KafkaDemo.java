/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be
 * found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo;

import com.tesobe.obp.kafka.SimpleNorth;
import com.tesobe.obp.kafka.SimpleSouth;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.spi.DefaultResponder;
import com.tesobe.obp.transport.spi.Receiver;
import com.tesobe.obp.transport.spi.ReceiverNov2016;
import com.tesobe.obp.util.Options;
import joptsimple.OptionSpec;
import joptsimple.OptionSpecBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.locks.LockSupport;

/**
 * Run twice, once with <tt>--north</tt> and once with <tt>--south</tt>.
 * Run with <tt>--help</tt> to see options for setting the kafka topics.
 */
public class KafkaDemo extends Options
{
  public static void main(String[] commandLine)
    throws InterruptedException, IOException
  {
    Flags flags = new Flags();

    if(flags.parse(commandLine))
    {
      if(flags.isPresent(flags.north))
      {
        North north = new North(flags);
        String bankId = "bank-x";

        log.info("Starting TESOBE's OBP Kafka Demo North...");

        try
        {
          Optional<Bank> bank = north.getBank(bankId);

          log.info("connector.getBank(\"{}\") \u2192 {} ", bankId, bank);
        }
        catch(Exception e)
        {
          log.error("\"connector.getBank(\"{}\")", bankId, e);
        }
      }
      else if(flags.isPresent(flags.south))
      {
        new South(flags);

        log.info("Starting TESOBE's OBP Kafka Demo South...");

        //noinspection InfiniteLoopStatement
        while(true)
        {
          log.trace("Parking main...");

          LockSupport.park(Thread.currentThread());
        }
      }
      else
      {
        log.error(
          "Please start the demo twice. Once with --north, once with --south");
      }
    }
  }

  private static void idle()
  {

  }

  private final static Logger log = LoggerFactory.getLogger(
    com.tesobe.obp.demo.KafkaDemo.class);

  static class North
  {
    North(Flags f)
    {
      Transport.Factory factory = Transport.defaultFactory();
      SimpleNorth sender = new SimpleNorth(f.valueOf(f.consumerTopic),
        f.valueOf(f.producerTopic));

      connector = factory.connector(sender);

      sender.receive(); // wait for incoming kafka messages
    }

    Optional<Bank> getBank(String bankId) throws InterruptedException
    {
      return connector.getBank(bankId);
    }

    Connector connector;
  }

  static class South extends DefaultResponder
  {
    public South(Flags f)
    {
      Transport.Factory factory = Transport.defaultFactory();
      Receiver receiver = new ReceiverNov2016(this, factory.codecs());

      new SimpleSouth(f.valueOf(f.consumerTopic), f.valueOf(f.producerTopic),
        receiver);
    }

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

  static class Flags extends Options
  {
    final OptionSpecBuilder north = acceptsAll("North", "north");
    final OptionSpecBuilder south = acceptsAll("South", "south");
    final OptionSpec<String> consumerTopic = acceptsAll("Consumer Topic",
      "consumer-topic").withRequiredArg()
      .describedAs("CONSUMER_TOPIC")
      .defaultsTo("Response");
    final OptionSpec<String> producerTopic = acceptsAll("Producer Topic",
      "producer-topic").withRequiredArg()
      .describedAs("PRODUCER_TOPIC")
      .defaultsTo("Request");
  }
}
