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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import static org.hamcrest.core.IsEqual.equalTo;
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

    assertThat(legacy.getAccount(bankId, accountId),
      optionallyReturns("id", "account-x"));
    assertThat(legacy.getAccount(bankId, accountId, userId),
      optionallyReturns("id", "account-x"));

    assertThat(sep2016.getAccount(bankId, accountId),
      optionallyReturns("id", "account-x"));
    assertThat(sep2016.getAccount(bankId, accountId, userId),
      optionallyReturns("id", "account-x"));
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

    assertThat(legacy.getBank(bankId), optionallyReturns("id", "bank-x"));
    assertThat(legacy.getBank(bankId, userId),
      optionallyReturns("id", "bank-x"));

    assertThat(sep2016.getBank(bankId), optionallyReturns("id", "bank-x"));
    assertThat(sep2016.getBank(bankId, userId),
      optionallyReturns("id", "bank-x"));
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

  @Test public void getPrivateTransaction() throws Exception
  {
    String accountId = "account-x";
    String bankId = "bank-x";
    String tid = "transaction-x";
    String userId = "user-x";

    Optional<Transaction> transaction;

    transaction = legacy.getTransaction(bankId, accountId, tid, userId);

    assertThat(transaction, optionallyReturns("id", "transaction-x"));

    transaction = sep2016.getTransaction(bankId, accountId, tid, userId);

    assertThat(transaction, optionallyReturns("id", "transaction-x"));
  }

  @Test public void getPrivateTransactions() throws Exception
  {
    String accountId = "account-x";
    String bankId = "bank-x";
    String userId = "user-x";

    Iterable<Transaction> transactions = legacy
      .getTransactions(bankId, accountId, userId);
    List<String> ids = new ArrayList<>();

    transactions.forEach(bank -> ids.add(bank.id()));

    assertThat(ids, equalTo(Arrays.asList("id-1", "id-2")));
  }

  @Test public void getPublicAccounts() throws Exception
  {
    String bankId = "bank-x";

    Iterable<Account> accounts = legacy.getAccounts(bankId);
    List<String> ids = new ArrayList<>();

    accounts.forEach(account -> assertThat(account.bank(), is(bankId)));
    accounts.forEach(account -> ids.add(account.id()));

    assertThat(ids, equalTo(Arrays.asList("id-1", "id-2")));
  }

  @Test public void getPublicTransaction() throws Exception
  {
    String bankId = "bank-x";
    String accountId = "account-x";
    String transactionId = "transaction-x";

    Optional<Transaction> transaction = legacy
      .getTransaction(bankId, accountId, transactionId);

    assertThat(transaction, optionallyReturns("id", "transaction-x"));
  }

  @Test public void getPublicTransactions() throws Exception
  {
    String bankId = "bank-x";
    String accountId = "account-x";

    Iterable<Transaction> transactions = legacy
      .getTransactions(bankId, accountId);
    List<String> ids = new ArrayList<>();

    transactions.forEach(transaction -> ids.add(transaction.id()));

    assertThat(ids, equalTo(Arrays.asList("id-1", "id-2")));
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
