/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo;

import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Sender;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.nov2016.Account;
import com.tesobe.obp.transport.nov2016.Bank;
import com.tesobe.obp.transport.nov2016.Parameters;
import com.tesobe.obp.transport.nov2016.User;
import com.tesobe.obp.transport.spi.Receiver;
import com.tesobe.obp.transport.spi.ReceiverNov2016;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
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

/**
 *
 */
public class OneBankTwoAccountsTest
{
  @Test public void getAccounts() throws Exception
  {
    HashMap<String, Object> parameters = new HashMap<>();

    parameters.put(Parameters.bankId, Database.bank.get(Bank.bankId));
    parameters.put(Parameters.userId, Database.anna.get(User.id));

    Decoder.Response response = connector.get("getAccounts",
      Transport.Target.accounts, parameters);

    assertThat(response.data(), notNullValue());

    Iterator<com.tesobe.obp.transport.Data> i = response.data().iterator();

    assertFalse(response.error().isPresent());
    assertTrue(i.hasNext());
    assertThat(i.next().text(Account.accountId),
      equalTo(Database.anna1.get(Account.accountId)));
    assertFalse(i.hasNext());
  }

  @Test public void getBanks() throws Exception
  {
    Decoder.Response response = connector.get("getBanks",
      Transport.Target.banks, null);

    assertThat(response, notNullValue());

    Iterator<com.tesobe.obp.transport.Data> i = response.data().iterator();

    assertFalse(response.error().isPresent());
    assertTrue(i.hasNext());
    assertThat(i.next().text(Bank.bankId),
      equalTo(Database.bank.get(Bank.bankId)));
    assertFalse(i.hasNext());
  }

  @Test public void getBank() throws Exception
  {
    HashMap<String, Object> parameters = new HashMap<>();

    parameters.put(Bank.bankId, Database.bank.get(Bank.bankId));

    Decoder.Response response = connector.get("getBank",
      Transport.Target.bank, parameters);

    assertThat(response, notNullValue());

    Iterator<com.tesobe.obp.transport.Data> i = response.data().iterator();

    assertThat(response, notNullValue());
    assertFalse(response.error().isPresent());
    assertTrue(i.hasNext());
    assertThat(i.next().text(Bank.bankId),
      equalTo(Database.bank.get("bankId")));
    assertFalse(i.hasNext());
  }

  @Test public void getUsers() throws Exception
  {
    Decoder.Response response = connector.get("getUsers",
      Transport.Target.users, null);

    assertThat(response, notNullValue());
    assertFalse(response.error().isPresent());

    response.data()
      .iterator()
      .forEachRemaining(user -> assertThat(user.text(User.displayName),
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
