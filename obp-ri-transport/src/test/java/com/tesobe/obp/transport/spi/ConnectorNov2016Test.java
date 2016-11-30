/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Encoder;
import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Pager;
import com.tesobe.obp.transport.Responder;
import com.tesobe.obp.transport.Sender;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static com.tesobe.obp.transport.Pager.SortOrder.ascending;
import static com.tesobe.obp.transport.Pager.SortOrder.descending;
import static com.tesobe.obp.util.MethodMatcher.optionallyReturns;
import static com.tesobe.obp.util.MethodMatcher.returns;
import static java.time.ZoneOffset.UTC;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Works together with {@link MockResponder} on the south.
 * Uses async local transport.
 */
public class ConnectorNov2016Test
{
  @Before public void connector()
  {
    Transport.Factory factory = Transport.factory(Transport.Version.Nov2016,
      Transport.Encoding.json)
      .map(Function.identity())
      .orElseThrow(IllegalArgumentException::new);
    Receiver receiver = new ReceiverNov2016(new MockResponder(),
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

  @After public void shutdown()
  {
    service.shutdown();
  }

  /**
   * Check the default factory using a full round trip.
   */
  @Test public void defaultFactory()
  {
    Transport.Factory factory = Transport.defaultFactory();
    Responder south = new DefaultResponder()
    {
      @Override
      public String getBanks(Decoder.Pager p, Decoder.Parameters ps, Encoder e)
      {
        return e.banks(Collections.singletonList(new Bank()
        {
          @Override public String id()
          {
            return "bank-x";
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
        }));
      }
    };
    Receiver receiver = new ReceiverNov2016(south, factory.codecs());
    Sender sender = receiver::respond;
    Connector connector = factory.connector(sender);

    try
    {
      Iterable<Bank> banks = connector.getBanks();

      assertTrue(banks.iterator().hasNext());
    }
    catch(InterruptedException e)
    {
      fail(e.getMessage());
    }
  }

  @Test public void getAccount() throws Exception
  {
    String accountId = "account-x";
    String bankId = "bank-x";
    String userId = "user-x";

    Optional<Account> anonymous;
    Optional<Account> owned;

    anonymous = connector.getAccount(bankId, accountId);
    owned = connector.getAccount(bankId, accountId, userId);

    assertThat(anonymous, optionallyReturns("accountId", "account-x"));
    assertThat(owned, optionallyReturns("accountId", "account-x"));
  }

  @Test public void getAccounts() throws Exception
  {
    String bankId = "bank-x";
    String userId = "user-x";
    final AtomicInteger anonymousCount = new AtomicInteger(0);
    final AtomicInteger ownedCount = new AtomicInteger(0);
    Iterable<? extends Account> anonymous;
    Iterable<? extends Account> owned;

    anonymous = connector.getAccounts(bankId);
    owned = connector.getAccounts(bankId, userId);

    assertThat(anonymous, notNullValue());
    assertThat(owned, notNullValue());

    anonymous.forEach(account ->
    {
      anonymousCount.incrementAndGet();

      assertThat(account.bankId(), is(bankId));
      assertThat(account.id(), anyOf(is("accountId-0"), is("accountId-1")));
    });

    owned.forEach(account ->
    {
      ownedCount.incrementAndGet();

      assertThat(account.bankId(), is(bankId));
      assertThat(account.id(), anyOf(is("accountId-0"), is("accountId-1")));
    });

    assertThat("Number of anonymous accounts", anonymousCount.get(), is(2));
    assertThat("Number of owned accounts", ownedCount.get(), is(2));
  }

  @Test public void getBank() throws Exception
  {
    String bankId = "bank-x";
    String userId = "user-x";

    Optional<Bank> anonymous;
    Optional<Bank> owned;

    anonymous = connector.getBank(bankId);
    owned = connector.getBank(bankId, userId);

    assertThat(anonymous, optionallyReturns("bankId", "bank-x"));
    assertThat(owned, optionallyReturns("bankId", "bank-x"));
  }

  @Test public void getBanks() throws Exception
  {
    String userId = "user-x";

    Iterable<? extends Bank> anonymous;
    Iterable<? extends Bank> owned;
    Iterable<? extends Bank> unexpected;

    final AtomicInteger anonymousCount = new AtomicInteger(0);
    final AtomicInteger ownedCount = new AtomicInteger(0);
    final AtomicInteger unexpectedCount = new AtomicInteger(0);

    anonymous = connector.getBanks();
    owned = connector.getBanks(userId);
    unexpected = connector.getBanks(null);

    assertThat(anonymous, notNullValue());
    assertThat(owned, notNullValue());
    assertThat(unexpected, notNullValue());

    anonymous.forEach(bank ->
    {
      anonymousCount.incrementAndGet();

      assertThat(bank.id(), anyOf(is("bankId-0"), is("bankId-1")));
    });

    owned.forEach(bank ->
    {
      ownedCount.incrementAndGet();

      assertThat(bank.id(), anyOf(is("bankId-0"), is("bankId-1")));
    });

    unexpected.forEach(bank ->
    {
      unexpectedCount.incrementAndGet();

      assertThat(bank.id(), anyOf(is("bankId-0"), is("bankId-1")));
    });

    assertThat("Number of anonymous banks", anonymousCount.get(), is(2));
    assertThat("Number of owned banks", ownedCount.get(), is(2));
    assertThat("Number of unexpected banks", unexpectedCount.get(), is(2));
  }

  @Test public void getTransaction() throws Exception
  {
    String accountId = "account-x";
    String bankId = "bank-x";
    String tid = "transaction-x";
    String userId = "user-x";

    Optional<Transaction> anonymous;
    Optional<Transaction> owned;

    anonymous = connector.getTransaction(bankId, accountId, tid);
    owned = connector.getTransaction(bankId, accountId, tid, userId);

    assertThat(anonymous, optionallyReturns("transactionId", "transaction-x"));
    assertThat(owned, optionallyReturns("transactionId", "transaction-x"));
  }

  @Test public void getTransactions() throws Exception
  {
    String accountId = "account-x";
    String bankId = "bank-x";
    String userId = "user-x";

    Iterable<? extends Transaction> anonymous;
    Iterable<? extends Transaction> owned;

    anonymous = connector.getTransactions(bankId, accountId);
    owned = connector.getTransactions(bankId, accountId, userId);

    assertThat(anonymous.iterator().hasNext(), is(true));
    assertThat(anonymous.iterator().next().id(), is("transactionId-0"));
    assertThat(owned.iterator().hasNext(), is(true));
    assertThat(owned.iterator().next().id(), is("transactionId-0"));
  }

  @Test public void getPagedTransactions() throws Exception
  {
    String accountId = "account-x";
    String bankId = "bank-x";
    String userId = "user-x";
    List<Transaction> owned;

    // Jan 1st, 1999 00:00 - Jan 8th, 2000 00:00
    ZonedDateTime earliest = ZonedDateTime.of(1999, 1, 1, 0, 0, 0, 0, UTC);
    ZonedDateTime latest = ZonedDateTime.of(1999, 1, 8, 0, 0, 0, 0, UTC);

    Pager.Filter filter = new TimestampFilter("postedDate", earliest, latest);
    Pager.Sorter sorter = DefaultSorter.build("completedDate", ascending)
      .add("counterpartyId", descending)
      .toSorter();
    int pageSize = 3;
    Pager pager = connector.pager(pageSize, 0, filter, sorter);

    owned = connector.getTransactions(pager, bankId, accountId, userId);

    assertThat("pager.count", pager.count(), is(0));
    assertThat("pager.hasMorePages", pager.hasMorePages(), is(true));
    assertThat("owned.size", owned.size(), is(pageSize));

    assertThat("owned.get(0)", owned.get(0).id(), is("transactionId-7"));
    assertThat("owned.get(1)", owned.get(1).id(), is("transactionId-6"));
    assertThat("owned.get(2)", owned.get(2).id(), is("transactionId-5"));

    pager.nextPage();

    owned = connector.getTransactions(pager, bankId, accountId, userId);

    assertThat("pager.count", pager.count(), is(1));
    assertThat("pager.hasMorePages", pager.hasMorePages(), is(true));
    assertThat("owned.size", owned.size(), is(pageSize));

    assertThat("owned.get(0)", owned.get(0).id(), is("transactionId-4"));
    assertThat("owned.get(1)", owned.get(1).id(), is("transactionId-3"));
    assertThat("owned.get(2)", owned.get(2).id(), is("transactionId-2"));

    pager.nextPage();

    owned = connector.getTransactions(pager, bankId, accountId, userId);

    assertThat("pager.count", pager.count(), is(2));
    assertThat("pager.hasMorePages", pager.hasMorePages(), is(false));
    assertThat("owned.size", owned.size(), is(2));

    assertThat("owned.get(0)", owned.get(0).id(), is("transactionId-1"));
    assertThat("owned.get(1)", owned.get(1).id(), is("transactionId-0"));
  }

  @Test public void getUser() throws Exception
  {
    String userId = "user-x@example.org";

    Optional<User> user = connector.getUser(userId);

    assertThat(user, optionallyReturns("email", userId));
  }

  @Test public void getUsers() throws Exception
  {
    String userId = "user-x";

    Iterable<User> anonymous;
    Iterable<User> owned;
    final AtomicInteger anonymousCount = new AtomicInteger(0);
    final AtomicInteger ownedCount = new AtomicInteger(0);

    anonymous = connector.getUsers();
    owned = connector.getUsers(userId);

    assertThat(anonymous, notNullValue());
    assertThat(owned, notNullValue());

    anonymous.forEach(user ->
    {
      anonymousCount.incrementAndGet();

      assertThat(user.id(), anyOf(is("id-0"), is("id-1")));
    });

    owned.forEach(user ->
    {
      ownedCount.incrementAndGet();

      assertThat(user.id(), anyOf(is("id-0"), is("id-1")));
    });

    assertThat("Number of anonymous users", anonymousCount.get(), is(2));
    assertThat("Number of owned users", ownedCount.get(), is(2));
  }

  @Test public void createTransaction()
  {
    String accountId = "account-x";
    BigDecimal amount = BigDecimal.TEN;
    String bankId = "bank-x";
    ZonedDateTime completed = ZonedDateTime.of(1999, 1, 2, 0, 0, 0, 0, UTC);
    String counterpartyId = "counterpartyId-x";
    String counterpartyName = "counterpartyName-x";
    String currency = "currency-x";
    String description = "description-x";
    BigDecimal newBalanceAmount = BigDecimal.ZERO;
    String newBalanceCurrency = "newBalanceCurrency";
    ZonedDateTime posted = ZonedDateTime.of(1999, 1, 2, 0, 0, 0, 0, UTC);
    String transactionId = "transactionId-y";
    String type = "type-x";
    String userId = "user-x";

    Optional<String> tid = connector.createTransaction(accountId, amount,
      bankId, completed, counterpartyId, counterpartyName, currency,
      description, newBalanceAmount, newBalanceCurrency, posted, transactionId,
      type, userId);

    assertThat(tid, returns("get", "tid-x"));
  }

  private Connector connector;
  private ExecutorService service = Executors.newCachedThreadPool();
}
