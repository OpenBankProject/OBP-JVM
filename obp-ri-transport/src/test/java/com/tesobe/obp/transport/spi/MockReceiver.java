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
 */
@SuppressWarnings({"WeakerAccess", "OptionalGetWithoutIsPresent"})
class MockReceiver extends AbstractReceiver
{
  public MockReceiver(Decoder decoder, Encoder encoder)
  {
    super(decoder, encoder);
  }

  @Override protected String getAccount(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    return r.accountId()
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

    return r.bankId()
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

    return r.transactionId()
      .map(tid -> e.transaction(generate(Transaction.class, 1, "id", tid)))
      .orElse(e.transaction(null));
  }

  @Override protected String getTransactions(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    List<Transaction> transactions = new ArrayList<>();

    transactions.add(generate(Transaction.class, 1));
    transactions.add(generate(Transaction.class, 2));

    return e.transactions(transactions);
  }

  @Override protected String getUser(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    return r.userId()
      .map(uid -> e.user(generate(User.class, 1, "email", uid)))
      .orElse(e.user(null));
  }

  @Override protected String saveTransaction(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    try
    {
      assertThat(r.accountId(), isPresent());
      assertThat(r.amount(), isPresent());
      assertThat(r.bankId(), isPresent());
      assertThat(r.completed(), isPresent());
      assertThat(r.currency(), isPresent());
      assertThat(r.otherId(), isPresent());
      assertThat(r.otherName(), isPresent());
      assertThat(r.description(), isPresent());
      assertThat(r.newAmount(), isPresent());
      assertThat(r.newCurrency(), isPresent());
      assertThat(r.posted(), isPresent());
      assertThat(r.transactionId(), isPresent());
      assertThat(r.transactionType(), isPresent());
      assertThat(r.userId(), isPresent());

      return e.transactionId("tid-x");
    }
    catch(Error x)
    {
      log.error(r.raw(), x);

      return e.error(x.getMessage());
    }
  }

  static final Logger log = LoggerFactory.getLogger(MockReceiver.class);
}
