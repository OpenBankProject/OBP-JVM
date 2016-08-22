/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.*;
import com.tesobe.obp.util.ImplGen;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertThat;

/**
 * Gives generated responses to all requests in the v0 version of the SPI.
 * <p>
 * The required request fields are checked, then a response is generated with
 * self documenting values for each field.
 * <p>
 * If the request contained an id for a specific item, that id is set on the
 * returned item to allow the test to check for the id.
 */
@SuppressWarnings({"WeakerAccess", "OptionalGetWithoutIsPresent"})
class MockResponderV1 extends ResponderV1
{
  public MockResponderV1(Decoder decoder, Encoder encoder)
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

    Instant instant = Instant.ofEpochMilli(1437404400000L);
    LocalDate date = instant.atZone(ZoneOffset.UTC).toLocalDate();
    ZoneId tz = ZoneId.of("Europe/London");
    LocalTime time = LocalTime.parse("21:00");
    ZonedDateTime zdt = ZonedDateTime.of(date, time, tz);

    InboundContext inboundContext = new InboundContext(new Source(zdt, "source origin"), "inbound context");

    User user = ImplGen.generate(User.class, 1, "email", r.userId().get());

    JSONObject response = new JSONObject().put("response", new JSONObject()
            .put("inboundContext", new JSONObject()
                    .put("source", new JSONObject()
                            .put("timestamp", inboundContext.source.timestamp)
                            .put("originatingSource", inboundContext.source.originatingSource))
                    .put("message", inboundContext.message))
            .put("user", e.userToJSONObject(user)));

    return response.toString();
  }

  @Override
  protected String getPublicAccount(String packet, Decoder.Request r, Encoder e)
  {
    log.trace("{}", packet);

    assertThat(r.accountId(), isPresent());

    Instant instant = Instant.ofEpochMilli(1437404400000L);
    LocalDate date = instant.atZone(ZoneOffset.UTC).toLocalDate();
    ZoneId tz = ZoneId.of("Europe/London");
    LocalTime time = LocalTime.parse("21:00");
    ZonedDateTime zdt = ZonedDateTime.of(date, time, tz);

    InboundContext inboundContext = new InboundContext(new Source(zdt, "source origin"), "inbound context");

    Account account = ImplGen.generate(Account.class, 1, "id", r.accountId().get());

    JSONObject response = new JSONObject().put("response", new JSONObject()
            .put("inboundContext", new JSONObject()
                    .put("source", new JSONObject()
                            .put("timestamp", inboundContext.source.timestamp)
                            .put("originatingSource", inboundContext.source.originatingSource))
                    .put("message", inboundContext.message))
            .put("account", e.accountToJSONObject(account)));

    return response.toString();
  }

  @Override protected String getPublicAccounts(String packet, Decoder.Request r,
    Encoder e)
  {
    return r.bankId().map(bankId -> {

      List<Account> accounts = new ArrayList<>();

      accounts.add(ImplGen.generate(Account.class, 1, "bank", bankId));
      accounts.add(ImplGen.generate(Account.class, 2, "bank", bankId));

      Instant instant = Instant.ofEpochMilli(1437404400000L);
      LocalDate date = instant.atZone(ZoneOffset.UTC).toLocalDate();
      ZoneId tz = ZoneId.of("Europe/London");
      LocalTime time = LocalTime.parse("21:00");
      ZonedDateTime zdt = ZonedDateTime.of(date, time, tz);

      InboundContext inboundContext = new InboundContext(new Source(zdt, "source origin"), "inbound context");

      JSONObject response = new JSONObject().put("response", new JSONObject()
              .put("inboundContext", new JSONObject()
                      .put("source", new JSONObject()
                              .put("timestamp", inboundContext.source.timestamp)
                              .put("originatingSource", inboundContext.source.originatingSource))
                      .put("message", inboundContext.message))
              .put("accounts", e.accountsToJSONArray(accounts)));

      return response.toString();

    }).orElse(e.accounts(emptyList()));
  }

  @Override
  protected String getPublicBank(String packet, Decoder.Request r, Encoder e)
  {
    log.trace("{}", packet);

    assertThat(r.bankId(), isPresent());

    Instant instant = Instant.ofEpochMilli(1437404400000L);
    LocalDate date = instant.atZone(ZoneOffset.UTC).toLocalDate();
    ZoneId tz = ZoneId.of("Europe/London");
    LocalTime time = LocalTime.parse("21:00");
    ZonedDateTime zdt = ZonedDateTime.of(date, time, tz);

    InboundContext inboundContext = new InboundContext(new Source(zdt, "source origin"), "inbound context");

    Bank bank = ImplGen.generate(Bank.class, 1, "id", r.bankId().get());

    JSONObject response = new JSONObject().put("response", new JSONObject()
            .put("inboundContext", new JSONObject()
                    .put("source", new JSONObject()
                            .put("timestamp", inboundContext.source.timestamp)
                            .put("originatingSource", inboundContext.source.originatingSource))
                    .put("message", inboundContext.message))
            .put("bank", e.bankToJSONObject(bank)));

    return response.toString();
  }

  @Override protected String getPublicBanks(String packet, Encoder e)
  {
    log.trace("{}", packet);

    List<Bank> banks = new ArrayList<>();

    banks.add(ImplGen.generate(Bank.class, 1));
    banks.add(ImplGen.generate(Bank.class, 2));

    Instant instant = Instant.ofEpochMilli(1437404400000L);
    LocalDate date = instant.atZone(ZoneOffset.UTC).toLocalDate();
    ZoneId tz = ZoneId.of("Europe/London");
    LocalTime time = LocalTime.parse("21:00");
    ZonedDateTime zdt = ZonedDateTime.of(date, time, tz);

    InboundContext inboundContext = new InboundContext(new Source(zdt, "source origin"), "inbound context");

    JSONObject response = new JSONObject().put("response", new JSONObject()
                                                        .put("inboundContext", new JSONObject()
                                                                .put("source", new JSONObject()
                                                                  .put("timestamp", inboundContext.source.timestamp)
                                                                .put("originatingSource", inboundContext.source.originatingSource))
                                                        .put("message", inboundContext.message))
    .put("banks", e.banksToJSONArray(banks)));

    return response.toString();
  }

  @Override
  protected String getPublicTransaction(String packet, Decoder.Request r,
    Encoder e)
  {
    log.trace("{}", packet);

    assertThat(r.transactionId(), isPresent());

    Instant instant = Instant.ofEpochMilli(1437404400000L);
    LocalDate date = instant.atZone(ZoneOffset.UTC).toLocalDate();
    ZoneId tz = ZoneId.of("Europe/London");
    LocalTime time = LocalTime.parse("21:00");
    ZonedDateTime zdt = ZonedDateTime.of(date, time, tz);

    InboundContext inboundContext = new InboundContext(new Source(zdt, "source origin"), "inbound context");

    Transaction transaction = ImplGen.generate(Transaction.class, 1, "id", r.transactionId().get());

    JSONObject response = new JSONObject().put("response", new JSONObject()
            .put("inboundContext", new JSONObject()
                    .put("source", new JSONObject()
                            .put("timestamp", inboundContext.source.timestamp)
                            .put("originatingSource", inboundContext.source.originatingSource))
                    .put("message", inboundContext.message))
            .put("transaction", e.transactionToJSONObject(transaction)));

    return response.toString();
  }

  @Override
  protected String getPublicTransactions(String packet, Decoder.Request r,
    Encoder e)
  {
    log.trace("{}", packet);

    List<Transaction> transactions = new ArrayList<>();

    transactions.add(ImplGen.generate(Transaction.class, 1));
    transactions.add(ImplGen.generate(Transaction.class, 2));

    Instant instant = Instant.ofEpochMilli(1437404400000L);
    LocalDate date = instant.atZone(ZoneOffset.UTC).toLocalDate();
    ZoneId tz = ZoneId.of("Europe/London");
    LocalTime time = LocalTime.parse("21:00");
    ZonedDateTime zdt = ZonedDateTime.of(date, time, tz);

    InboundContext inboundContext = new InboundContext(new Source(zdt, "source origin"), "inbound context");

    JSONObject response = new JSONObject().put("response", new JSONObject()
            .put("inboundContext", new JSONObject()
                    .put("source", new JSONObject()
                            .put("timestamp", inboundContext.source.timestamp)
                            .put("originatingSource", inboundContext.source.originatingSource))
                    .put("message", inboundContext.message))
            .put("transactions", e.transactionsToJSONArray(transactions)));

    return response.toString();
  }

  @Override
  protected String getPublicUser(String packet, Decoder.Request r, Encoder e)
  {
    log.trace("{}", packet);

    assertThat(r.userId(), isPresent());

    Instant instant = Instant.ofEpochMilli(1437404400000L);
    LocalDate date = instant.atZone(ZoneOffset.UTC).toLocalDate();
    ZoneId tz = ZoneId.of("Europe/London");
    LocalTime time = LocalTime.parse("21:00");
    ZonedDateTime zdt = ZonedDateTime.of(date, time, tz);

    InboundContext inboundContext = new InboundContext(new Source(zdt, "source origin"), "inbound context");

    User user = ImplGen.generate(User.class, 1, "email", r.userId().get());

    JSONObject response = new JSONObject().put("response", new JSONObject()
            .put("inboundContext", new JSONObject()
                    .put("source", new JSONObject()
                            .put("timestamp", inboundContext.source.timestamp)
                            .put("originatingSource", inboundContext.source.originatingSource))
                    .put("message", inboundContext.message))
            .put("user", e.userToJSONObject(user)));

    return response.toString();
  }

  @Override
  protected String savePrivateTransaction(String packet, Decoder.Request r,
    Encoder e)
  {
    return e.error("Not implemented");
  }
  static final Logger log = LoggerFactory.getLogger(MockResponderV1.class);
}
