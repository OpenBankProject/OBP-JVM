/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo;

import com.tesobe.obp.demo.north.North;
import com.tesobe.obp.demo.south.South;
import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Responder;
import com.tesobe.obp.transport.User;
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

    System.out.println("Starting TESOBE's OBP kafka south demo...");
    System.out.println("Check the log files 'demo.log' and 'south.log'");

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

    System.out.println("Starting TESOBE's OBP kafka north demo...");
    System.out.println("Check the log files 'demo.log' and 'south.log'");
    System.out.println("working...");

    North north = new com.tesobe.obp.demo.north.North(consumerTopic,
      producerTopic, consumerProps, producerProps);

    List<Bank> banks = north.getBanks();

    System.out.format(US, "The banks (%d)\n", banks.size());
    banks.forEach(
      b -> System.out.format(US, "  %s %s\n", b.bankId(), b.name()));

    List<User> users = north.getUsers();

    System.out.format(US, "The users (%d)\n", users.size());
    users.forEach(
      u -> System.out.format(US, "  %s %s\n", u.id(), u.displayName()));

    for(User u : users)
    {
      for(Bank b : banks)
      {
        List<Account> accounts = north.getAccounts(b, u);

        System.out.format(US, "%s's accounts at %s (%d)\n", u.displayName(),
          b.name(), accounts.size());
        accounts.forEach(
          a -> System.out.format(US, "  %s %s %s\n", a.id(), a.bankId(),
            a.userId()));
      }
    }

    System.out.println("done.");

    north.shutdown();
  }

  public static void main(String[] commandLine)
    throws InterruptedException, IOException, ClassNotFoundException,
    InstantiationException, IllegalAccessException
  {
    Flags flags = new Flags();

    if(flags.parse(commandLine))
    {
      new Demo(flags);
    }
  }

  private final static Logger log = LoggerFactory.getLogger(Demo.class);
}
