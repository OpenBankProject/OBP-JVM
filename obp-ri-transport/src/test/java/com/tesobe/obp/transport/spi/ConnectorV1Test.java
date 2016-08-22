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
import static com.tesobe.obp.util.MethodMatcher.returns;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ConnectorV1Test
{
  @Before public void setup()
  {
    Transport.Factory factory = Transport.factory(Transport.Version.v1, json)
      .orElseThrow(RuntimeException::new);
    Receiver responder = new MockResponderV1(factory.decoder(),
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

    AccountWrapper accountWrapper = connector.getAccount(bankId, accountId, outboundContext);
    Optional<Account> account = accountWrapper.account;

    assertThat(account, hasValue(returns("id", "account-x")));
  }

  @Test public void getAccounts() throws Exception
  {
    String bankId = "bank-x";
    String userId = "user-x";
    String viewId = "view-x";
    String tokenId = "token-x";
    OutboundContext outboundContext = new OutboundContext(new UserContext(userId), new ViewContext(viewId, true), new TokenContext(tokenId));

    AccountsWrapper accountsWrapper = connector.getAccounts(bankId, outboundContext);
    Iterable<Account> accounts = accountsWrapper.accounts;
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

    BankWrapper bankWrapper = connector.getBank(bankId, outboundContext);
    Optional<Bank> bank = bankWrapper.bank;

    assertThat(bank, hasValue(returns("id", "bank-x")));
  }

  @Test public void getBanks() throws Exception
  {
    String userId = "user-x";
    String viewId = "view-x";
    String tokenId = "token-x";
    OutboundContext outboundContext = new OutboundContext(new UserContext(userId), new ViewContext(viewId, true), new TokenContext(tokenId));

    BanksWrapper banksWrapper = connector.getBanks(outboundContext);
    Iterable<Bank> banks = banksWrapper.banks;
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

    TransactionWrapper transactionWrapper = connector.getTransaction(bankId, accountId, transactionId, outboundContext);
    Optional<Transaction> transaction = transactionWrapper.transaction;

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

    TransactionsWrapper transactionsWrapper = connector.getTransactions(bankId, accountId, outboundContext);
    Iterable<Transaction> transactions = transactionsWrapper.transactions;
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

    UserWrapper userWrapper = connector.getUser(userId, outboundContext);
    Optional<User> user = userWrapper.user;

    assertThat(user, hasValue(returns("email", userId)));
  }

  private Connector connector;
  private ExecutorService service;
}
