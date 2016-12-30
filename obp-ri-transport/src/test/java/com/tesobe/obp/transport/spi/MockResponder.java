/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.nov2016.Account;
import com.tesobe.obp.transport.nov2016.Bank;
import com.tesobe.obp.transport.nov2016.ChallengeThreshold;
import com.tesobe.obp.transport.nov2016.Transaction;
import com.tesobe.obp.transport.nov2016.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static com.tesobe.obp.util.Utils.merge;
import static java.lang.String.format;
import static java.time.ZoneOffset.UTC;
import static java.util.Collections.singletonList;
import static java.util.Collections.synchronizedMap;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Example implementation of a south.
 *
 * @since 2016.11
 */
@SuppressWarnings("WeakerAccess") public class MockResponder
  extends DefaultResponder
{
  @Override public JSONObject describe(JSONObject json)
  {
    JSONObject nov2016 = new JSONObject();
    JSONObject get = new JSONObject();
    JSONObject put = new JSONObject();
    JSONObject transaction = new JSONObject();
    JSONObject types = new JSONObject();

//  Do not know how to handle types yet. So all it's strings.
//    JSONObject account = Account.FIELDS.stream()
//      .collect(JSONObject::new, (a, k) -> a.put(k, "String"),
//        (a1, a2) -> a2.keySet().forEach(k -> a1.put(k, a2.get(k))));

    get.put(Transport.Target.account.toString(), Account.FIELDS);
    get.put(Transport.Target.bank.toString(), Bank.FIELDS);
    get.put(Transport.Target.transaction.toString(), Transaction.FIELDS);
    get.put(Transport.Target.user.toString(), User.FIELDS);
    get.put(Transport.Target.challengeThreshold.toString(),
      ChallengeThreshold.FIELDS);

    put.put(Transport.Target.transaction.toString(), transaction);

    transaction.put("types", types);
    types.put("pain.001.001.03db", Transaction.FIELDS);

    nov2016.put("get", get);
    nov2016.put("put", put);

    json.put("versions", new JSONArray().put(Transport.Version.Nov2016));
    json.put(Transport.Version.Nov2016.toString(), nov2016);

    return json;
  }

  @Override
  public List<? extends Map<String, ?>> next(String state, Decoder.Pager p)
  {
    return state != null ? cache.get(state) : null;
  }

  @Override protected List<? extends Map<String, ?>> account(String state,
    Decoder.Pager p, Decoder.Parameters ps)
  {
    return ps.accountId()
      .map(this::account)
      .map(Collections::singletonList)
      .orElseGet(Collections::emptyList);
  }

  protected Map<String, Object> account(String id)
  {
    return entity(Account.accountId, id);
  }

  @Override protected List<? extends Map<String, ?>> accounts(String state,
    Decoder.Pager p, Decoder.Parameters ps)
  {
    return ps.bankId()
      .map(bankId -> Stream.of("accountId-0", "accountId-1")
        .map(this::account)
        .map(a -> merge(a, Account.bankId, bankId))
        .collect(toList()))
      .orElseGet(Collections::emptyList);
  }

  @Override
  protected List<? extends Map<String, ?>> bank(String state, Decoder.Pager p,
    Decoder.Parameters ps)
  {
    return ps.bankId()
      .map(this::bank)
      .map(Collections::singletonList)
      .orElseGet(Collections::emptyList);
  }

  protected Map<String, Object> bank(String id)
  {
    return entity(Bank.bankId, id);
  }

  @Override
  protected List<? extends Map<String, ?>> banks(String state, Decoder.Pager p,
    Decoder.Parameters ps)
  {
    return Stream.of("bankId-0", "bankId-1").map(this::bank).collect(toList());
  }

  @Override
  protected List<? extends Map<String, ?>> challengeThreshold(String state,
    Decoder.Pager p, Decoder.Parameters ps)
  {
    HashMap<String, String> response = new HashMap<>();

    response.put(ChallengeThreshold.amount, "amount-x");
    response.put(ChallengeThreshold.currency, "currency-x");

    return Collections.singletonList(response);
  }

  @Override protected List<? extends Map<String, ?>> transaction(String state,
    Decoder.Pager p, Decoder.Parameters ps)
  {
    return ps.accountId()
      .flatMap(a -> ps.bankId()
        .flatMap(b -> ps.transactionId().map(tid -> transaction(a, b, tid))))
      .map(Collections::singletonList)
      .orElseGet(Collections::emptyList);
  }

  protected Map<String, Object> transaction(String accountId, String bankId,
    String transactionId)
  {
    return merge(merge(entity(Transaction.transactionId, transactionId),
      Transaction.accountId, accountId), Transaction.bankId, bankId);
  }

  @Override protected List<? extends Map<String, ?>> transactions(String state,
    Decoder.Pager p, Decoder.Parameters ps)
  {
    return ps.accountId()
      .flatMap(a -> ps.bankId().map(b -> transactions(state, p, a, b)))
      .orElseGet(Collections::emptyList);
  }

  /**
   * Return a total of seventeen transactions, posted one per day counting down
   * from January 1st, 1999..
   *
   * @param state local state, can store result set over page requests
   * @param p pager
   * @param accountId account
   * @param bankId bank
   *
   * @return 17 transactions in as many pages, as needed.
   */
  protected List<Map<String, Object>> transactions(String state,
    Decoder.Pager p, String accountId, String bankId)
  {
    ZonedDateTime completed = ZonedDateTime.of(1999, 1, 31, 0, 0, 0, 0, UTC);
    final List<Map<String, Object>> data = new ArrayList<>(); // todo stream
    int numTransactions = 17;

    for(int i = 0; i < numTransactions; ++i)
    {
      int dayOfMonth = Math.min(i, 26) + 1; // safe month length
      ZonedDateTime posted = ZonedDateTime.of(1999, 1, dayOfMonth, 0, 0, 0, 0,
        UTC);

      HashMap<String, Object> t = new HashMap<>();

      t.put(Transaction.accountId, accountId);
      t.put(Transaction.bankId, bankId);
      t.put(Transaction.transactionId,
        format(Locale.US, "transactionId-%d", i));
      t.put(Transaction.completedDate, completed);
      t.put(Transaction.postedDate, posted);
      t.put(Transaction.counterPartyId,
        format(Locale.US, "counterPartyId-%d", i));

      data.add(t);
    }

    List<Map<String, Object>> processed = data.stream()
      .filter(filter(p))
      .sorted(sorter(p))
      .collect(toList());

    cache.put(state, processed);

    return processed;
  }

  @Override
  protected List<? extends Map<String, ?>> user(String state, Decoder.Pager p,
    Decoder.Parameters ps)
  {
    return ps.userId()
      .map(this::user)
      .map(Collections::singletonList)
      .orElseGet(Collections::emptyList);
  }

  protected Map<String, Object> user(String id)
  {
    return entity(User.email, id);
  }

  @Override
  protected List<? extends Map<String, ?>> users(String state, Decoder.Pager p,
    Decoder.Parameters ps)
  {
    return Stream.of("userId-0", "userId-1")
      .map(this::user)
      .map(a -> merge(a, User.id, a.get("email")))
      .collect(toList());
  }

  private Map<String, Object> entity(String idName, String idValue)
  {
    return merge(new HashMap<>(), idName, idValue);
  }

  public List<Map<String, Object>> put(Decoder.Parameters ps,
    Map<String, ?> fields, Transport.Target t)
  {
    ps.type().map(type ->
    {
      assertThat(type, is("pain.001.001.03db"));
      return Void.TYPE;
    }).orElseGet(() ->
    {
      fail();
      return Void.TYPE;
    });

    assertThat(fields.get(Transaction.accountId), is("account-x"));
    assertThat(fields.get(Transaction.amount), is("10"));
    assertThat(fields.get(Transaction.bankId), is("bank-x"));
    assertThat(fields.get(Transaction.completedDate),
      is("1999-01-02T00:00:00.000Z"));
    assertThat(fields.get(Transaction.counterPartyId), is("counterPartyId-x"));
    assertThat(fields.get(Transaction.counterPartyName),
      is("counterPartyName-x"));
    assertThat(fields.get(Transaction.currency), is("currency-x"));
    assertThat(fields.get(Transaction.description), is("description-x"));
    assertThat(fields.get(Transaction.newBalanceAmount), is("123456.78"));
    assertThat(fields.get(Transaction.newBalanceCurrency), is("nbc-x"));
    assertThat(fields.get(Transaction.postedDate),
      is("1999-01-02T00:00:00.000Z"));
    assertThat(fields.get(Transaction.transactionId), is("transaction-x"));
    assertThat(fields.get(Transaction.type), is("type-x"));
    assertThat(fields.get(Transaction.userId), is("user-x"));

    return singletonList(
      entity("transactionId", Objects.toString(fields.get("transactionId"))));
  }

  @Override public List<? extends Map<String, ?>> fetch()
  {
    return singletonList(merge(new HashMap<>(), "transaction-x", "ACPT"));
  }

  Map<String, List<Map<String, Object>>> cache = synchronizedMap(
    new HashMap<>());
}
