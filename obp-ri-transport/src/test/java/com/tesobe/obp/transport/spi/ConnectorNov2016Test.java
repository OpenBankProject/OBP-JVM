/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.*;
import org.junit.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static com.tesobe.obp.util.MethodMatcher.optionallyReturns;
import static com.tesobe.obp.util.MethodMatcher.returns;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class ConnectorNov2016Test
{
  @Before public void defaultConnector()
  {
    Transport.Factory factory = Transport
      .factory(Transport.Version.Nov2016, Transport.Encoding.json)
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
      assertThat(account.id(), anyOf(is("accountId-1"), is("accountId-2")));
    });

    owned.forEach(account ->
    {
      ownedCount.incrementAndGet();

      assertThat(account.bankId(), is(bankId));
      assertThat(account.id(), anyOf(is("accountId-1"), is("accountId-2")));
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

    final AtomicInteger anonymousCount = new AtomicInteger(0);
    final AtomicInteger ownedCount = new AtomicInteger(0);

    anonymous = connector.getBanks();
    owned = connector.getBanks(userId);

    assertThat(anonymous, notNullValue());
    assertThat(owned, notNullValue());

    anonymous.forEach(bank ->
    {
      anonymousCount.incrementAndGet();

      assertThat(bank.id(), anyOf(is("bankId-1"), is("bankId-2")));
    });

    owned.forEach(bank ->
    {
      ownedCount.incrementAndGet();

      assertThat(bank.id(), anyOf(is("bankId-1"), is("bankId-2")));
    });

    assertThat("Number of anonymous banks", anonymousCount.get(), is(2));
    assertThat("Number of owned banks", ownedCount.get(), is(2));
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

    assertThat(anonymous.iterator().next().id(), is("transactionId-0"));
    assertThat(owned.iterator().next().id(), is("transactionId-0"));
  }

  @Test public void getPagedTransactions() throws Exception
  {
    String accountId = "account-x";
    String bankId = "bank-x";
    String userId = "user-x";

    Iterable<? extends Transaction> anonymous;
    Iterable<? extends Transaction> owned;
    Pager pager = connector.pager();

    anonymous = connector.getTransactions(pager, bankId, accountId);
    owned = connector.getTransactions(pager, bankId, accountId, userId);

    assertThat(anonymous.iterator().next().id(), is("transactionId-0"));
    assertThat(owned.iterator().next().id(), is("transactionId-0"));
  }

//  @Test public void pageTransactions() throws Exception
//  {
//    String accountId = "account-x";
//    String bankId = "bank-x";
//    String userId = "user-x";
//    Connector.Pager pager = connector.pager(0, 3, null, null, null, null);
//
//    List<? extends Transaction> owned;
//
//    owned = pager.getTransactions(bankId, accountId, userId);
//
//    assertThat(pager.hasMorePages(), is(true));
//    assertThat(owned.size(), is(3));
//
//    pager = pager.nextPage();
//    owned = pager.getTransactions(bankId, accountId, userId);
//
//    assertThat(pager.hasMorePages(), is(false));
//    assertThat(owned.size(), is(1));
//  }

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

      assertThat(user.id(), anyOf(is("id-1"), is("id-2")));
    });

    owned.forEach(user ->
    {
      ownedCount.incrementAndGet();

      assertThat(user.id(), anyOf(is("id-1"), is("id-2")));
    });

    assertThat("Number of anonymous users", anonymousCount.get(), is(2));
    assertThat("Number of owned users", ownedCount.get(), is(2));
  }

  @Test public void createTransaction()
  {
    String userId = "user-x";
    String accountId = "account-x";
    String currency = "currency-x";
    BigDecimal amount = BigDecimal.TEN;
    String otherAccountId = "account-y";
    String otherAccountCurrency = "currency-y";
    String transactionType = "type-x";

    Optional<String> tid = connector.createTransaction(userId, accountId,
      currency, amount, otherAccountId, otherAccountCurrency, transactionType);

    assertThat(tid, returns("get", "tid-x"));
  }

  private Connector connector;
  private ExecutorService service = Executors.newCachedThreadPool();
}
