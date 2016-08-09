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
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.User;
import com.tesobe.obp.util.ImplGen;
import com.tesobe.obp.util.tbd;

import java.util.ArrayList;
import java.util.List;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
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
class MockLegacyResponder extends LegacyResponder
{
  public MockLegacyResponder(Decoder decoder, Encoder encoder)
  {
    super(decoder, encoder);
  }

  @Override protected String getPrivateAccount(String packet, Decoder.Request r,
    Encoder e)
  {
    log.trace("{}", packet);

    assertThat(r.accountId(), isPresent());
    assertThat(r.userId(), isPresent());

    return e
      .account(ImplGen.generate(Account.class, 1, "id", r.accountId().get()));
  }

  @Override
  protected String getPrivateAccounts(String packet, Decoder.Request r,
    Encoder e)
  {
    log.trace("{}", packet);

    assertThat(r.userId(), isPresent());
    assertThat(r.bankId(), isPresent());

    List<Account> accounts = new ArrayList<>();
    String bankId = r.bankId().get();

    accounts.add(ImplGen.generate(Account.class, 1, "bank", bankId));
    accounts.add(ImplGen.generate(Account.class, 2, "bank", bankId));

    return e.accounts(accounts);
  }

  @Override
  protected String getPrivateBank(String packet, Decoder.Request r, Encoder e)
  {
    log.trace("{}", packet);

    assertThat(r.userId(), isPresent());
    assertThat(r.bankId(), isPresent());

    return e.bank(ImplGen.generate(Bank.class, 1, "id", r.bankId().get()));
  }

  @Override
  protected String getPrivateBanks(String packet, Decoder.Request r, Encoder e)
  {
    log.trace("{}", packet);

    assertThat(r.userId(), isPresent());

    List<Bank> banks = new ArrayList<>();

    banks.add(ImplGen.generate(Bank.class, 1));
    banks.add(ImplGen.generate(Bank.class, 2));

    return e.banks(banks);
  }

  @Override
  protected String getPrivateTransaction(String packet, Decoder.Request r,
    Encoder e)
  {
    log.trace("{}", packet);

    assertThat(r.userId(), isPresent());
    assertThat(r.transactionId(), isPresent());

    return e.transaction(
      ImplGen.generate(Transaction.class, 1, "id", r.transactionId().get()));
  }

  @Override
  protected String getPrivateTransactions(String packet, Decoder.Request r,
    Encoder e)
  {
    log.trace("{}", packet);

    assertThat(r.userId(), isPresent());

    List<Transaction> transactions = new ArrayList<>();

    transactions.add(ImplGen.generate(Transaction.class, 1));
    transactions.add(ImplGen.generate(Transaction.class, 2));

    return e.transactions(transactions);
  }

  @Override
  protected String getPrivateUser(String packet, Decoder.Request r, Encoder e)
  {
    log.trace("{}", packet);

    assertThat(r.userId(), isPresent());

    return e.user(ImplGen.generate(User.class, 1, "email", r.userId().get()));
  }

  @Override
  protected String getPublicAccount(String packet, Decoder.Request r, Encoder e)
  {
    log.trace("{}", packet);

    assertThat(r.accountId(), isPresent());
    assertThat(r.userId(), isPresent());

    return e
      .account(ImplGen.generate(Account.class, 1, "id", r.accountId().get()));
  }

  @Override protected String getPublicAccounts(String packet, Decoder.Request r,
    Encoder e)
  {
    List<Account> accounts = new ArrayList<>();

    accounts.add(ImplGen.generate(Account.class, 1));
    accounts.add(ImplGen.generate(Account.class, 2));

    return e.accounts(accounts);
  }

  @Override
  protected String getPublicBank(String packet, Decoder.Request r, Encoder e)
  {
    log.trace("{}", packet);

    assertThat(r.bankId(), isPresent());

    return e.bank(ImplGen.generate(Bank.class, 1, "bankId", r.bankId().get()));
  }

  @Override protected String getPublicBanks(String packet, Encoder e)
  {
    log.trace("{}", packet);

    List<Bank> banks = new ArrayList<>();

    banks.add(ImplGen.generate(Bank.class, 1));
    banks.add(ImplGen.generate(Bank.class, 2));

    return e.banks(banks);
  }

  @Override
  protected String getPublicTransaction(String packet, Decoder.Request r,
    Encoder e)
  {
    log.trace("{}", packet);

    assertThat(r.transactionId(), isPresent());

    return e.transaction(
      ImplGen.generate(Transaction.class, 1, "id", r.transactionId().get()));
  }

  @Override
  protected String getPublicTransactions(String packet, Decoder.Request r,
    Encoder e)
  {
    log.trace("{}", packet);

    List<Transaction> transactions = new ArrayList<>();

    transactions.add(ImplGen.generate(Transaction.class, 1));
    transactions.add(ImplGen.generate(Transaction.class, 2));

    return e.transactions(transactions);
  }

  @Override
  protected String getPublicUser(String packet, Decoder.Request r, Encoder e)
  {
    log.trace("{}", packet);

    assertThat(r.userId(), isPresent());

    return e.user(ImplGen.generate(User.class, 1, "email", r.userId().get()));
  }

  @Override
  protected String savePrivateTransaction(String packet, Decoder.Request r,
    Encoder e)
  {
    throw new tbd();
  }
}
