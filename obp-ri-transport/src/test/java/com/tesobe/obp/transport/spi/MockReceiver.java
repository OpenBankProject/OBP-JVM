/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.tesobe.obp.util.ImplGen.generate;
import static com.tesobe.obp.util.MethodMatcher.isPresent;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertThat;

/**
 * Gives generated responses to all requests in the legacy version of the SPI.
 * <p>
 * The required request fields are checked, then a response is generated with
 * self documenting values for each field.
 * <p>
 * If the request contained an id for a specific item, that id is set on the
 * returned item to allow the test to check for the id.
 *
 * @deprecated use MockResponder
 */
@SuppressWarnings({"WeakerAccess", "OptionalGetWithoutIsPresent"})
class MockReceiver extends DefaultReceiver
{
  public MockReceiver(Codecs cs)
  {
    super(cs);
  }

  @Override protected String getAccount(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    return r
      .accountId()
      .map(accountId -> e.account(generate(Account.class, 1, "id", accountId)))
      .orElse(e.account(null));
  }

  @Override protected String getAccounts(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    return r.bankId().map(bankId ->
    {
      List<Account> accounts = new ArrayList<>();

      accounts.add(generate(Account.class, 1, "bank", bankId));
      accounts.add(generate(Account.class, 2, "bank", bankId));

      return e.accounts(accounts);

    }).orElse(e.accounts(emptyList()));
  }

  @Override protected String getBank(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    return r
      .bankId()
      .map(bankId -> e.bank(generate(Bank.class, 1, "id", bankId)))
      .orElse(e.bank(null));
  }

  @Override protected String getBanks(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    List<Bank> banks = new ArrayList<>();

    banks.add(generate(Bank.class, 1));
    banks.add(generate(Bank.class, 2));

    return e.banks(banks);
  }

  @Override protected String getTransaction(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    return r
      .transactionId()
      .map(tid -> e.transaction(generate(Transaction.class, 1, "id", tid)))
      .orElse(e.transaction(null));
  }

  @Override protected String getTransactions(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    int count = 4; // 4 transactions
    int first = r.offset();
    int last = Math.min(first + r.size(), count);

    List<Transaction> transactions = IntStream
      .range(first, last)
      .mapToObj(i -> generate(Transaction.class, i))
      .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

    return last < count
      ? e.transactions(transactions, true)
      : e.transactions(transactions);
  }

  @Override protected String getUser(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    return r
      .userId()
      .map(uid -> e.user(generate(User.class, 1, "email", uid)))
      .orElse(e.user(null));
  }

  @Override protected String getUsers(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    List<User> users = new ArrayList<>();

    users.add(generate(User.class, 1));
    users.add(generate(User.class, 2));

    return e.users(users);
  }

  @Override protected String saveTransaction(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    assertThat(r.accountId(), isPresent());
    assertThat(r.amount(), isPresent());
    assertThat(r.currency(), isPresent());
    assertThat(r.otherAccountId(), isPresent());
    assertThat(r.otherAccountCurrency(), isPresent());
    assertThat(r.transactionType(), isPresent());
    assertThat(r.userId(), isPresent());

    return e.transactionId("tid-x");
  }

  static final Logger log = LoggerFactory.getLogger(MockReceiver.class);
}
