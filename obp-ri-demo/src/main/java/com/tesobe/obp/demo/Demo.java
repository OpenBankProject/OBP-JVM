/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo;

import com.tesobe.obp.demo.north.North;
import com.tesobe.obp.demo.south.South;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Responder;
import com.tesobe.obp.util.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * Run twice, once with <tt>--north</tt> and once with <tt>--south</tt>.
 * Run with <tt>--help</tt> to see options for setting the kafka topics.
 */
@SuppressWarnings("WeakerAccess") public class Demo extends Options
{
  public Demo(Flags flags)
    throws ClassNotFoundException, InstantiationException,
    IllegalAccessException, IOException, InterruptedException
  {
    if(flags.isPresent(flags.north))
    {
      north(flags);
    }
    else if(flags.isPresent(flags.south))
    {
      south(flags);
    }
    else
    {
      log.error("Please start the demo with either --north, or --south");
    }
  }

  protected void south(Flags flags)
    throws IOException, ClassNotFoundException, IllegalAccessException,
    InstantiationException
  {
    String consumerTopic = flags.valueOf(flags.consumerTopic);
    String consumerProps = flags.valueOf(flags.consumerProps);

    String producerTopic = flags.valueOf(flags.producerTopic);
    String producerProps = flags.valueOf(flags.producerProps);

    Responder responder = Responder.class.cast(
      Class.forName(flags.valueOf(flags.responder)).newInstance());

    if(consumerTopic == null)
    {
      consumerTopic = "Request"; // north produces on Request
    }

    if(producerTopic == null)
    {
      producerTopic = "Response"; // north consumes on Response
    }

    South south = new South(responder, consumerTopic, consumerProps,
      producerTopic, producerProps);

  }

  protected void north(Flags flags) throws IOException, InterruptedException
  {
    String consumerTopic = flags.valueOf(flags.consumerTopic);
    String consumerProps = flags.valueOf(flags.consumerProps);

    String producerTopic = flags.valueOf(flags.producerTopic);
    String producerProps = flags.valueOf(flags.producerProps);

    if(consumerTopic == null)
    {
      consumerTopic = "Response";
    }

    if(producerTopic == null)
    {
      producerTopic = "Request";
    }

    North north = new com.tesobe.obp.demo.north.North(consumerTopic,
      producerTopic, consumerProps, producerProps);

    List<Bank> banks = north.getBanks();

    log.info("list all banks \u2192 {}", banks.size());
  }

  public static void main(String[] commandLine)
    throws InterruptedException, IOException, ClassNotFoundException,
    InstantiationException, IllegalAccessException
  {
    Flags flags = new Flags();

    if(flags.parse(commandLine))
    {
      new Demo(flags);

      //noinspection InfiniteLoopStatement
      while(true)
      {
        log.trace("Parking main...");

        LockSupport.park(Thread.currentThread());
      }
    }
  }

  private final static Logger log = LoggerFactory.getLogger(Demo.class);
}
