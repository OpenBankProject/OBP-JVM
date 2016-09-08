/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

import static com.tesobe.obp.transport.Transport.Encoding.json;
import static com.tesobe.obp.util.MethodMatcher.optionallyReturns;
import static com.tesobe.obp.util.MethodMatcher.returns;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class DefaultConnectorTest
{
  @Before public void sep2016Connector()
  {
    Transport.Factory factory = Transport
      .factory(Transport.Version.sep2016, json)
      .orElseThrow(RuntimeException::new);

    Receiver responder = new MockResponder(factory.decoder(),
      factory.encoder());
    final BlockingQueue<String> in = new SynchronousQueue<>();
    final BlockingQueue<Message> out = new SynchronousQueue<>();

    // north: sender
    sep2016 = factory.connector(request ->
    {
      out.put(request);

      return in.take();
    });

    // south: receiver
    service.submit(new Callable<Void>()
    {
      @Override @SuppressWarnings({"InfiniteLoopStatement"}) public Void call()
        throws InterruptedException
      {
        for(; ; )
        {
          in.put(responder.respond(out.take()));
        }
      }
    });
  }

  @Before public void defaultConnector()
  {
    Transport.Factory factory = Transport.defaultFactory();
    Receiver responder = new MockResponder(factory.decoder(),
      factory.encoder());
    final BlockingQueue<String> in = new SynchronousQueue<>();
    final BlockingQueue<Message> out = new SynchronousQueue<>();

    // north: sender
    legacy = factory.connector(request ->
    {
      out.put(request);

      return in.take();
    });

    // south: receiver
    service.submit(new Callable<Void>()
    {
      @Override @SuppressWarnings({"InfiniteLoopStatement"}) public Void call()
        throws InterruptedException
      {
        for(; ; )
        {
          in.put(responder.respond(out.take()));
        }
      }
    });
  }


  @After public void shutdown()
  {
    service.shutdownNow();
  }

  @Test public void getAccount() throws Exception
  {
    String accountId = "account-x";
    String bankId = "id-x";
    String userId = "user-x";

    Optional<Account> anonymous;
    Optional<Account> owned;

    anonymous = legacy.getAccount(bankId, accountId);
    owned = legacy.getAccount(bankId, accountId, userId);

    assertThat(anonymous, optionallyReturns("id", "account-x"));
    assertThat(owned, optionallyReturns("id", "account-x"));

    anonymous = sep2016.getAccount(bankId, accountId);
    owned = sep2016.getAccount(bankId, accountId, userId);
  }

  @Test public void getAccounts() throws Exception
  {
    String bankId = "bank-x";
    String userId = "user-x";
    Iterable<Account> anonymous;
    Iterable<Account> owned;

    anonymous = legacy.getAccounts(bankId);
    owned = legacy.getAccounts(bankId, userId);

    assertThat(anonymous, notNullValue());
    assertThat(owned, notNullValue());

    anonymous.forEach(account ->
    {
      assertThat(account.bank(), is(bankId));
      assertThat(account.id(), anyOf(is("id-1"), is("id-2")));
    });

    owned.forEach(account ->
    {
      assertThat(account.bank(), is(bankId));
      assertThat(account.id(), anyOf(is("id-1"), is("id-2")));
    });

    anonymous = sep2016.getAccounts(bankId);
    owned = sep2016.getAccounts(bankId, userId);

    assertThat(anonymous, notNullValue());
    assertThat(owned, notNullValue());

    anonymous.forEach(account ->
    {
      assertThat(account.bank(), is(bankId));
      assertThat(account.id(), anyOf(is("id-1"), is("id-2")));
    });

    owned.forEach(account ->
    {
      assertThat(account.bank(), is(bankId));
      assertThat(account.id(), anyOf(is("id-1"), is("id-2")));
    });
  }

  @Test public void getBank() throws Exception
  {
    String bankId = "bank-x";
    String userId = "user-x";

    Optional<Bank> anonymous;
    Optional<Bank> owned;

    anonymous = legacy.getBank(bankId);
    owned = legacy.getBank(bankId, userId);

    assertThat(anonymous, optionallyReturns("id", "bank-x"));
    assertThat(owned, optionallyReturns("id", "bank-x"));

    anonymous = sep2016.getBank(bankId);
    owned = sep2016.getBank(bankId, userId);

    assertThat(anonymous, optionallyReturns("id", "bank-x"));
    assertThat(owned, optionallyReturns("id", "bank-x"));
  }

  @Test public void getBanks() throws Exception
  {
    String userId = "user-x";

    Iterable<Bank> anonymous;
    Iterable<Bank> owned;

    anonymous = legacy.getBanks();
    owned = legacy.getBanks(userId);

    assertThat(anonymous, notNullValue());
    assertThat(owned, notNullValue());

    anonymous.forEach(bank ->
    {
      assertThat(bank.id(), anyOf(is("id-1"), is("id-2")));
    });

    owned.forEach(bank ->
    {
      assertThat(bank.id(), anyOf(is("id-1"), is("id-2")));
    });

    anonymous = sep2016.getBanks();
    owned = sep2016.getBanks(userId);

    assertThat(anonymous, notNullValue());
    assertThat(owned, notNullValue());

    anonymous.forEach(bank ->
    {
      assertThat(bank.id(), anyOf(is("id-1"), is("id-2")));
    });

    owned.forEach(bank ->
    {
      assertThat(bank.id(), anyOf(is("id-1"), is("id-2")));
    });

  }

  @Test public void getTransaction() throws Exception
  {
    String accountId = "account-x";
    String bankId = "bank-x";
    String tid = "transaction-x";
    String userId = "user-x";

    Optional<Transaction> anonymous;
    Optional<Transaction> owned;

    anonymous = legacy.getTransaction(bankId, accountId, tid);
    owned = legacy.getTransaction(bankId, accountId, tid, userId);

    assertThat(anonymous, optionallyReturns("id", "transaction-x"));
    assertThat(owned, optionallyReturns("id", "transaction-x"));

    anonymous = sep2016.getTransaction(bankId, accountId, tid);
    owned = sep2016.getTransaction(bankId, accountId, tid, userId);

    assertThat(anonymous, optionallyReturns("id", "transaction-x"));
    assertThat(owned, optionallyReturns("id", "transaction-x"));
  }

  @Test public void getTransactions() throws Exception
  {
    String accountId = "account-x";
    String bankId = "bank-x";
    String userId = "user-x";

    Iterable<Transaction> anonymous;
    Iterable<Transaction> owned;

    anonymous = legacy.getTransactions(bankId, accountId);
    owned = legacy.getTransactions(bankId, accountId, userId);

    anonymous.forEach(transaction ->
    {
      assertThat(transaction.id(), anyOf(is("id-1"), is("id-2")));
    });

    owned.forEach(transaction ->
    {
      assertThat(transaction.id(), anyOf(is("id-1"), is("id-2")));
    });

    anonymous = sep2016.getTransactions(bankId, accountId);
    owned = sep2016.getTransactions(bankId, accountId, userId);

    anonymous.forEach(transaction ->
    {
      assertThat(transaction.id(), anyOf(is("id-1"), is("id-2")));
    });

    owned.forEach(transaction ->
    {
      assertThat(transaction.id(), anyOf(is("id-1"), is("id-2")));
    });
  }

  @Test public void getUser() throws Exception
  {
    String userId = "user-x@example.org";

    Optional<User> user = legacy.getUser(userId);

    assertThat(user, optionallyReturns("email", userId));
  }

  @Test public void saveTransaction() throws Exception
  {
    String userId = "user-x";
    String accountId = "account-x";
    String currency = "currency-x";
    String amount = "amount-x";
    String otherAccountId = "account-y";
    String otherAccountCurrency = "currency-y";
    String transactionType = "type-x";

    Optional<String> tid = legacy
      .saveTransaction(userId, accountId, currency, amount, otherAccountId,
        otherAccountCurrency, transactionType);

    assertThat(tid, returns("get", "tid-x"));
  }

  private Connector legacy;
  private Connector sep2016;
  private ExecutorService service = Executors.newCachedThreadPool();
}
