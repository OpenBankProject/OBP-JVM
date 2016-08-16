/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static com.github.npathai.hamcrestopt.OptionalMatchers.hasValue;
import static com.tesobe.obp.transport.Transport.Encoding.json;
import static com.tesobe.obp.transport.Transport.Version.v0;
import static com.tesobe.obp.util.MethodMatcher.returns;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ConnectorV0Test
{
  @Before public void setup()
  {
    Transport.Factory factory = Transport.factory(v0, json)
      .orElseThrow(RuntimeException::new);
    Receiver responder = new MockResponderV0(factory.decoder(),
      factory.encoder());
    final BlockingQueue<String> in = new SynchronousQueue<>();
    final BlockingQueue<Message> out = new SynchronousQueue<>();

    // sender
    connector = factory.connector(request ->
    {
      out.put(request);

      return in.take();
    });

    service = Executors.newSingleThreadExecutor();

    // receiver
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
    service.shutdown();
  }

  @Test public void getAccount() throws Exception
  {
    String accountId = "account-x";
    String bankId = "id-x";
    String userId = "user-x";
    String viewId = "view-x";
    String tokenId = "token-x";
    OutboundContext outboundContext = new OutboundContext(new UserContext(userId), new ViewContext(viewId, true), new TokenContext(tokenId));

    Optional<Account> account = connector
      .getAccount(bankId, accountId, outboundContext);

    assertThat(account, hasValue(returns("id", "account-x")));
  }

  @Test public void getAccounts() throws Exception
  {
    String bankId = "bank-x";
    String userId = "user-x";
    String viewId = "view-x";
    String tokenId = "token-x";
    OutboundContext outboundContext = new OutboundContext(new UserContext(userId), new ViewContext(viewId, true), new TokenContext(tokenId));

    Iterable<Account> accounts = connector.getAccounts(bankId, outboundContext);
    List<String> ids = new ArrayList<>();

    accounts.forEach(account -> assertThat(account.bank(), is(bankId)));
    accounts.forEach(account -> ids.add(account.id()));

    assertThat(ids, equalTo(Arrays.asList("id-1", "id-2")));
  }

  @Test public void getBank() throws Exception
  {
    String bankId = "bank-x";
    String userId = "user-x";
    String viewId = "view-x";
    String tokenId = "token-x";
    OutboundContext outboundContext = new OutboundContext(new UserContext(userId), new ViewContext(viewId, true), new TokenContext(tokenId));

    Optional<Bank> bank = connector.getBank(bankId, outboundContext);

    assertThat(bank, hasValue(returns("id", "bank-x")));
  }

  @Test public void getBanks() throws Exception
  {
    String userId = "user-x";
    String viewId = "view-x";
    String tokenId = "token-x";
    OutboundContext outboundContext = new OutboundContext(new UserContext(userId), new ViewContext(viewId, true), new TokenContext(tokenId));

    Iterable<Bank> banks = connector.getBanks(outboundContext);
    List<String> ids = new ArrayList<>();

    banks.forEach(bank -> ids.add(bank.id()));

    assertThat(ids, equalTo(Arrays.asList("id-1", "id-2")));
  }

  @Test public void getTransaction() throws Exception
  {
    String accountId = "account-x";
    String bankId = "bank-x";
    String transactionId = "transaction-x";
    String userId = "user-x";
    String viewId = "view-x";
    String tokenId = "token-x";
    OutboundContext outboundContext = new OutboundContext(new UserContext(userId), new ViewContext(viewId, true), new TokenContext(tokenId));

    Optional<Transaction> transaction = connector
      .getTransaction(bankId, accountId, transactionId, outboundContext);

    assertThat(transaction, hasValue(returns("id", "transaction-x")));
  }

  @Test public void getTransactions() throws Exception
  {
    String accountId = "account-x";
    String bankId = "bank-x";
    String userId = "user-x";
    String viewId = "view-x";
    String tokenId = "token-x";
    OutboundContext outboundContext = new OutboundContext(new UserContext(userId), new ViewContext(viewId, true), new TokenContext(tokenId));

    Iterable<Transaction> transactions = connector
      .getTransactions(bankId, accountId, outboundContext);
    List<String> ids = new ArrayList<>();

    transactions.forEach(bank -> ids.add(bank.id()));

    assertThat(ids, equalTo(Arrays.asList("id-1", "id-2")));
  }

  @Test public void getUser() throws Exception
  {
    String userId = "user-x@example.org";
    String viewId = "view-x";
    String tokenId = "token-x";
    OutboundContext outboundContext = new OutboundContext(new UserContext(userId), new ViewContext(viewId, true), new TokenContext(tokenId));

    Optional<User> user = connector.getUser(userId, outboundContext);

    assertThat(user, hasValue(returns("email", userId)));
  }

  private Connector connector;
  private ExecutorService service;
}
