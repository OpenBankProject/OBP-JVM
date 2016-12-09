/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Sender;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.User;
import com.tesobe.obp.transport.spi.Receiver;
import com.tesobe.obp.transport.spi.ReceiverNov2016;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class OneBankTwoAccountsTest
{
  @Test public void getAccounts() throws Exception
  {
    Iterable<Account> accounts = connector.getAccounts(Data.bank.id(),
      Data.anna.id());

    assertThat(accounts, notNullValue());

    Iterator<Account> i = accounts.iterator();

    assertTrue(i.hasNext());
    assertThat(i.next().id(), equalTo(Data.anna1.id()));
    assertFalse(i.hasNext());
  }

  @Test public void getBanks() throws Exception
  {
    Iterable<Bank> banks = connector.getBanks();

    assertThat(banks, notNullValue());
    assertTrue(Data.bank.equals(banks.iterator().next()));
  }

  @SuppressWarnings("OptionalGetWithoutIsPresent") @Test public void getBank()
    throws Exception
  {
    String id = connector.getBanks().iterator().next().bankId();
    Optional<Bank> bank = connector.getBank(id);

    assertTrue(bank.isPresent());
    assertTrue(Data.bank.equals(bank.get()));
  }

  @Test public void getUsers() throws Exception
  {
    Iterable<User> users = connector.getUsers();

    assertThat(users, notNullValue());

    users.iterator()
      .forEachRemaining(user -> assertThat(user.displayName(),
        anyOf(equalTo("Анна"), equalTo("Berta"), equalTo("金色"))));
  }

  @SuppressWarnings("Convert2Lambda") @Before public void connector()
  {
    Transport.Factory factory = Transport.factory(Transport.Version.Nov2016,
      Transport.Encoding.json)
      .map(Function.identity())
      .orElseThrow(IllegalArgumentException::new);
    Receiver receiver = new ReceiverNov2016(new OneBankTwoAccounts(),
      factory.codecs());
    final BlockingQueue<String> in = new SynchronousQueue<>();
    final BlockingQueue<Message> out = new SynchronousQueue<>();
    final Sender sender = request ->
    {
      out.put(request);

      return in.take();
    };

    // north: sender
    connector = factory.connector(sender);

    // south: receiver in a background thread
    service.submit(new Callable<Void>()
    {
      @Override @SuppressWarnings({"InfiniteLoopStatement"}) public Void call()
        throws InterruptedException
      {
        for(; ; )
        {
          in.put(receiver.respond(out.take()));
        }
      }
    });
  }

  private Connector connector;
  private ExecutorService service = Executors.newCachedThreadPool();
}
