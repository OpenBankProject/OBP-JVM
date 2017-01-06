/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Pager;
import com.tesobe.obp.transport.Responder;
import com.tesobe.obp.transport.Response;
import com.tesobe.obp.transport.Sender;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.nov2016.Account;
import com.tesobe.obp.transport.nov2016.Bank;
import com.tesobe.obp.transport.nov2016.ChallengeThreshold;
import com.tesobe.obp.transport.nov2016.ChallengeThresholdReader;
import com.tesobe.obp.transport.nov2016.Parameters;
import com.tesobe.obp.transport.nov2016.Transaction;
import com.tesobe.obp.transport.nov2016.User;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.function.Function;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static com.tesobe.obp.transport.Pager.SortOrder.ascending;
import static com.tesobe.obp.transport.Pager.SortOrder.descending;
import static com.tesobe.obp.transport.Transport.Target.banks;
import static com.tesobe.obp.util.Utils.merge;
import static java.time.ZoneOffset.UTC;
import static java.util.stream.IntStream.range;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

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
  @Test public void defaultFactory() throws Exception
  {
    Transport.Factory factory = Transport.defaultFactory();
    Responder south = new DefaultResponder()
    {
      @Override public Response first(String state, Decoder.Pager p,
        Decoder.Parameters ps, Transport.Target t)
      {
        return DefaultResponse.fromData(
          merge(new HashMap<>(), Bank.bankId, "bank-x"));
      }
    };
    Receiver receiver = new ReceiverNov2016(south, factory.codecs());
    Sender sender = receiver::respond;
    Connector connector = factory.connector(sender);

    Decoder.Response response = connector.get("test", Transport.Target.bank,
      null);

    assertTrue(response.data().iterator().hasNext());
  }

  @Test public void describe() throws Exception
  {
    String json = connector.describe();
    JSONObject description = new JSONObject(json);

    assertThat(description.opt("error"), nullValue());

    System.out.println(description.toString(2));
  }

  @Test public void getAccount() throws Exception
  {
    Map<String, String> parameters = new HashMap<>();

    parameters.put(Parameters.accountId, "account-x");
    parameters.put(Parameters.bankId, "bank-x");

    Decoder.Response anonymous = connector.get("getAccount",
      Transport.Target.account, parameters);

    assertThat(anonymous.error(), not(isPresent()));
    assertThat(anonymous.count(), is(0));
    assertFalse(anonymous.data().isEmpty());
    assertThat(anonymous.data().get(0).text(Account.accountId),
      is("account-x"));

    parameters.put(Parameters.userId, "user-x");

    Decoder.Response owned = connector.get("getAccount",
      Transport.Target.account, parameters);

    assertThat(owned.error(), not(isPresent()));
    assertThat(owned.count(), is(0));
    assertFalse(owned.data().isEmpty());
    assertThat(owned.data().get(0).text(Account.accountId), is("account-x"));
  }

  @Test public void getAccounts() throws Exception
  {
    Map<String, String> parameters = new HashMap<>();

    parameters.put(Parameters.bankId, "bank-x");

    Decoder.Response anonymous = connector.get("getAccounts",
      Transport.Target.accounts, parameters);

    assertThat(anonymous.error(), not(isPresent()));
    assertThat(anonymous.count(), is(0));
    assertThat(anonymous.data().size(), is(2));
    assertThat(anonymous.data().get(0).text(Account.accountId),
      is("accountId-0"));
    assertThat(anonymous.data().get(1).text(Account.accountId),
      is("accountId-1"));

    parameters.put(Parameters.userId, "user-x");

    Decoder.Response owned = connector.get("getAccounts",
      Transport.Target.accounts, parameters);

    assertThat(owned.error(), not(isPresent()));
    assertThat(owned.count(), is(0));
    assertFalse(owned.data().isEmpty());
    assertThat(owned.data().size(), is(2));
    assertThat(owned.data().get(0).text(Account.accountId), is("accountId-0"));
    assertThat(owned.data().get(1).text(Account.accountId), is("accountId-1"));
  }

  @Test public void getBank() throws Exception
  {
    Map<String, String> parameters = new HashMap<>();

    parameters.put(Parameters.bankId, "bank-x");

    Decoder.Response anonymous = connector.get("getBank", Transport.Target.bank,
      parameters);

    assertThat(anonymous.error(), not(isPresent()));
    assertThat(anonymous.count(), is(0));
    assertFalse(anonymous.data().isEmpty());
    assertThat(anonymous.data().get(0).text(Bank.bankId), is("bank-x"));

    parameters.put(Parameters.userId, "user-x");

    Decoder.Response owned = connector.get("getBank", Transport.Target.bank,
      parameters);

    assertThat(owned.error(), not(isPresent()));
    assertThat(owned.count(), is(0));
    assertFalse(owned.data().isEmpty());
    assertThat(owned.data().get(0).text(Bank.bankId), is("bank-x"));
  }

  @Test public void getBanks() throws Exception
  {
    Map<String, String> parameters = new HashMap<>();

    Decoder.Response anonymous = connector.get("getBanks", banks, parameters);

    assertThat(anonymous.error(), not(isPresent()));
    assertThat(anonymous.count(), is(0));
    assertThat(anonymous.data().size(), is(2));
    assertThat(anonymous.data().get(0).text(Bank.bankId), is("bankId-0"));
    assertThat(anonymous.data().get(1).text(Bank.bankId), is("bankId-1"));

    parameters.put(Parameters.userId, "user-x");

    Decoder.Response owned = connector.get("getBanks", banks, parameters);

    assertThat(owned.error(), not(isPresent()));
    assertThat(owned.count(), is(0));
    assertFalse(owned.data().isEmpty());
    assertThat(owned.data().size(), is(2));
    assertThat(owned.data().get(0).text(Bank.bankId), is("bankId-0"));
    assertThat(owned.data().get(1).text(Bank.bankId), is("bankId-1"));
  }

  @Test public void getTransaction() throws Exception
  {
    Map<String, String> parameters = new HashMap<>();

    parameters.put(Parameters.accountId, "account-x");
    parameters.put(Parameters.bankId, "bank-x");
    parameters.put(Parameters.transactionId, "transaction-x");

    Decoder.Response anonymous = connector.get("getTransaction",
      Transport.Target.transaction, parameters);

    assertThat(anonymous.error(), not(isPresent()));
    assertThat(anonymous.count(), is(0));
    assertThat(anonymous.data().size(), is(1));
    assertThat(anonymous.data().get(0).text(Transaction.transactionId),
      is("transaction-x"));

    parameters.put(Parameters.userId, "user-x");

    Decoder.Response owned = connector.get("getTransaction",
      Transport.Target.transaction, parameters);

    assertThat(owned.error(), not(isPresent()));
    assertThat(owned.count(), is(0));
    assertThat(owned.data().size(), is(1));
    assertThat(owned.data().get(0).text(Transaction.transactionId),
      is("transaction-x"));
  }

  @Test public void getTransactions() throws Exception
  {
    Map<String, String> parameters = new HashMap<>();

    String accountId = "account-x";
    String bankId = "bank-x";

    parameters.put(Parameters.accountId, accountId);
    parameters.put(Parameters.bankId, bankId);

    Decoder.Response anonymous = connector.get("getTransactions",
      Transport.Target.transactions, parameters);

    assertThat(anonymous.error(), not(isPresent()));
    assertThat(anonymous.count(), is(0));
    assertThat(anonymous.data().size(), is(17));

    range(0, 17).forEach(i ->
    {
      assertThat(anonymous.data().get(i).text(Transaction.accountId),
        is(accountId));
      assertThat(anonymous.data().get(i).text(Transaction.bankId), is(bankId));
    });

    parameters.put(Parameters.userId, "user-x");

    Decoder.Response owned = connector.get("getTransactions",
      Transport.Target.transactions, parameters);

    assertThat(owned.error(), not(isPresent()));
    assertThat(owned.count(), is(0));
    assertFalse(owned.data().isEmpty());
    assertThat(owned.data().size(), is(17));

    range(0, 17).forEach(i ->
    {
      assertThat(owned.data().get(i).text(Transaction.accountId),
        is(accountId));
      assertThat(owned.data().get(i).text(Transaction.bankId), is(bankId));
    });
  }

  @Test public void getPagedTransactionsZero() throws Exception
  {
    Pager pager = connector.pager(0, 0, null, null);
    Map<String, String> parameters = new HashMap<>();

    parameters.put(Parameters.accountId, "account-x");
    parameters.put(Parameters.bankId, "bank-x");
    parameters.put(Parameters.userId, "user-x");

    Decoder.Response owned = connector.get("getTransactions",
      Transport.Target.transactions, pager, parameters);

    assertThat("pager.count", pager.count(), is(0));
    assertThat("pager.hasMorePages", pager.hasMorePages(), is(true));
    assertThat("owned.empty", owned.data().isEmpty(), is(true));
  }

  @SuppressWarnings("OptionalGetWithoutIsPresent") @Test
  public void getChallengeThreshold() throws Exception
  {
    Map<String, String> parameters = new HashMap<>();

    parameters.put(ChallengeThreshold.Parameters.accountId, "account-x");
    parameters.put(ChallengeThreshold.Parameters.userId, "user-x");
    parameters.put(ChallengeThreshold.Parameters.type, "type-x");
    parameters.put(ChallengeThreshold.Parameters.currency, "currency-x");

    Decoder.Response response = connector.get("getChallengeThreshold",
      Transport.Target.challengeThreshold, parameters);

    assertThat(response.error(), not(isPresent()));
    assertThat(response.data().size(), is(1));

    Optional<ChallengeThresholdReader> threshold = response.data()
      .stream()
      .map(ChallengeThresholdReader::new)
      .findFirst();

    assertThat(threshold, isPresent());
    assertThat(threshold.get().amount(), is("amount-x"));
    assertThat(threshold.get().currency(), is("currency-x"));
  }

  @Test public void getPagedTransactions() throws Exception
  {
    Decoder.Response owned;
    Map<String, String> parameters = new HashMap<>();
    // Jan 1st, 1999 00:00 - Jan 8th, 2000 00:00
    ZonedDateTime earliest = ZonedDateTime.of(1999, 1, 1, 0, 0, 0, 0, UTC);
    ZonedDateTime latest = ZonedDateTime.of(1999, 1, 8, 0, 0, 0, 0, UTC);
    Pager.Filter filter = new TimestampFilter(Transaction.postedDate, earliest,
      latest);
    Pager.Sorter sorter = DefaultSorter.build(Transaction.completedDate,
      ascending).add("counterPartyId", descending).toSorter();
    int pageSize = 3;
    Pager pager = connector.pager(pageSize, 0, filter, sorter);

    parameters.put(Parameters.accountId, "account-x");
    parameters.put(Parameters.bankId, "bank-x");
    parameters.put(Parameters.userId, "user-x");

    owned = connector.get("getTransactions", Transport.Target.transactions,
      pager, parameters);

    assertThat("pager.count", pager.count(), is(0));
    assertThat("pager.hasMorePages", pager.hasMorePages(), is(true));
    assertThat("owned.size", owned.data().size(), is(pageSize));

    assertThat("owned.get(0)",
      owned.data().get(0).text(Transaction.transactionId),
      is("transactionId-7"));
    assertThat("owned.get(1)",
      owned.data().get(1).text(Transaction.transactionId),
      is("transactionId-6"));
    assertThat("owned.get(2)",
      owned.data().get(2).text(Transaction.transactionId),
      is("transactionId-5"));

    pager.nextPage();

    owned = connector.get("getTransactions", Transport.Target.transactions,
      pager, null);

    assertThat("pager.count", pager.count(), is(1));
    assertThat("pager.hasMorePages", pager.hasMorePages(), is(true));
    assertThat("owned.size", owned.data().size(), is(pageSize));

    assertThat("owned.get(0)",
      owned.data().get(0).text(Transaction.transactionId),
      is("transactionId-4"));
    assertThat("owned.get(1)",
      owned.data().get(1).text(Transaction.transactionId),
      is("transactionId-3"));
    assertThat("owned.get(2)",
      owned.data().get(2).text(Transaction.transactionId),
      is("transactionId-2"));

    pager.nextPage();

    owned = connector.get("getTransactions", Transport.Target.transactions,
      pager, null);

    assertThat("pager.count", pager.count(), is(2));
    assertThat("pager.hasMorePages", pager.hasMorePages(), is(false));
    assertThat("owned.size", owned.data().size(), is(2));

    assertThat("owned.get(0)",
      owned.data().get(0).text(Transaction.transactionId),
      is("transactionId-1"));
    assertThat("owned.get(1)",
      owned.data().get(1).text(Transaction.transactionId),
      is("transactionId-0"));
  }

  @Test public void getUser() throws Exception
  {
    String userId = "user-x@example.org";
    Map<String, String> parameters = new HashMap<>();

    parameters.put(Parameters.userId, userId);

    Decoder.Response anonymous = connector.get("getUser", Transport.Target.user,
      null);

    assertThat(anonymous.error(), not(isPresent()));
    assertThat(anonymous.count(), is(0));
    assertTrue(anonymous.data().isEmpty()); // no data without userId

    Decoder.Response owned = connector.get("getUser", Transport.Target.user,
      parameters);

    assertThat(owned.error(), not(isPresent()));
    assertThat(owned.count(), is(0));
    assertFalse(owned.data().isEmpty());
    assertThat(owned.data().get(0).text(User.email), is(userId));
  }

  @Test public void getUsers() throws Exception
  {
    Map<String, String> parameters = new HashMap<>();

    Decoder.Response anonymous = connector.get("getUsers",
      Transport.Target.users, parameters);

    assertThat(anonymous.error(), not(isPresent()));
    assertThat(anonymous.count(), is(0));
    assertFalse(anonymous.data().isEmpty());

    parameters.put(Parameters.userId, "user@example.net");

    Decoder.Response owned = connector.get("getUsers", Transport.Target.users,
      parameters);

    assertThat(owned.error(), not(isPresent()));
    assertThat(owned.count(), is(0));
    assertFalse(owned.data().isEmpty());
    assertThat(owned.data().size(), is(2));
  }

  @Test public void createTransaction() throws Exception
  {
    Map<String, Object> parameters = new HashMap<>();
    Map<String, Object> fields = new HashMap<>();

    String accountId = "account-x";
    String amount = "10";
    String bankId = "bank-x";
    ZonedDateTime completed = ZonedDateTime.of(1999, 1, 2, 0, 0, 0, 0, UTC);
    String counterPartyId = "counterPartyId-x";
    String counterPartyName = "counterPartyName-x";
    String currency = "currency-x";
    String description = "description-x";
    String newBalanceAmount = "123456.78";
    String newBalanceCurrency = "nbc-x";
    ZonedDateTime posted = ZonedDateTime.of(1999, 1, 2, 0, 0, 0, 0, UTC);
    String transactionId = "transaction-x";
    String type = "type-x";
    String userId = "user-x";

    parameters.put("type", "pain.001.001.03db");

    fields.put("accountId", accountId);
    fields.put("amount", amount);
    fields.put("bankId", bankId);
    fields.put("completedDate", completed);
    fields.put("counterPartyId", counterPartyId);
    fields.put("counterPartyName", counterPartyName);
    fields.put("currency", currency);
    fields.put("description", description);
    fields.put("newBalanceAmount", newBalanceAmount);
    fields.put("newBalanceCurrency", newBalanceCurrency);
    fields.put("postedDate", posted);
    fields.put("transactionId", transactionId);
    fields.put("type", type);
    fields.put("userId", userId);

    Decoder.Response response = connector.put("createTransaction",
      Transport.Target.transaction, parameters, fields);

    assertThat(response.error().isPresent(), is(false));
    assertThat(response.data().size(), is(1));
    assertThat(response.data().get(0).text("transactionId"), is(transactionId));
  }

  @Test public void fetch() throws Exception
  {
    Decoder.Response response = connector.fetch();

    assertThat(response.error().isPresent(), is(false));
    assertThat(response.data().size(), is(1));
    assertThat(response.data().get(0).text("transaction-x"), is("ACPT"));
  }

  private Connector connector;
  private ExecutorService service = Executors.newCachedThreadPool();
}
