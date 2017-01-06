/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo;

import com.tesobe.obp.demo.north.North;
import com.tesobe.obp.demo.south.South;
import com.tesobe.obp.kafka.Configuration;
import com.tesobe.obp.kafka.SimpleConfiguration;
import com.tesobe.obp.transport.Responder;
import com.tesobe.obp.transport.nov2016.Account;
import com.tesobe.obp.transport.nov2016.Bank;
import com.tesobe.obp.transport.nov2016.User;
import com.tesobe.obp.util.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static java.util.Locale.US;

/**
 * Run twice, once with <tt>--north</tt> and once with <tt>--south</tt>.
 * Run with <tt>--help</tt> to see options for setting the kafka topics.
 */
@SuppressWarnings("WeakerAccess") public class Demo extends Options
{
  public Demo(Flags flags) throws Exception
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

  /**
   * Testing.
   */
  Demo()
  {
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

    System.out.println("Starting TESOBE's OBP kafka south demo...");
    System.out.println("Please check the log files 'demo.log' and 'south.log'");

    South south = new South(responder, consumerTopic, consumerProps,
      producerTopic, producerProps);
  }

  protected void north(Flags flags) throws Exception
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

    Configuration c = new SimpleConfiguration(consumerTopic, producerTopic,
      consumerProps, producerProps);
    North north = new North(c);

    run(north);

    north.shutdown();
  }

  public void run(North north) throws Exception
  {
    System.out.println("Starting TESOBE's OBP kafka north demo...");
    System.out.println("Please check the log files 'demo.log' and 'south.log'");
    System.out.println();
    System.out.println("working...");
    System.out.println();

    List<Bank> banks = north.getBanks();

    System.out.format(US, "The banks (%d)\n", banks.size());
    banks.forEach(b -> System.out.format(US, "  %s %s\n", b.bankId(), b.name()));

    List<User> users = north.getUsers();

    System.out.format(US, "The users (%d)\n", users.size());
    users.forEach(u -> System.out.format(US, "  %s %s\n", u.id(), u.displayName()));

    for(User u : users)
    {
      for(Bank b : banks)
      {
        List<Account> accounts = north.getAccounts(b, u);

        System.out.format(US, "%s's accounts at %s (%d)\n", u.displayName(), b.name(), accounts.size());
        accounts.forEach(
          a -> System.out.format(US, "  id: %s, balance: %s %s, owner: %s\n",
            a.id(), a.balanceCurrency(), a.balanceAmount(), a.userId()));
      }
    }

    System.out.println();
    System.out.println("done.");
  }

  public static void main(String[] commandLine) throws Exception
  {
    Flags flags = new Flags();

    if(flags.parse(commandLine))
    {
      new Demo(flags);
    }
  }

  private final static Logger log = LoggerFactory.getLogger(Demo.class);
}
