/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Encoder;
import com.tesobe.obp.transport.Pager;
import com.tesobe.obp.transport.Sender;
import com.tesobe.obp.transport.Token;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.tesobe.obp.transport.Transport.Target.banks;

/**
 * Compatible to end 2016 OBP-API.
 *
 * @since 2016.11
 */
@SuppressWarnings("WeakerAccess") public class ConnectorNov2016
  implements Connector
{
  public ConnectorNov2016(Transport.Version v, Encoder e, Decoder d, Sender s)
  {
    if(e == null)
    {
      throw new IllegalArgumentException("Encoder required!");
    }

    if(d == null)
    {
      throw new IllegalArgumentException("Decoder required!");
    }

    if(s == null)
    {
      throw new IllegalArgumentException("Sender required!");
    }

    if(v == null)
    {
      throw new IllegalArgumentException("Version required!");
    }

    decoder = d;
    encoder = e;
    network = new Network(v, e, d, s); // todo mk param
    sender = s;
    version = v;
  }

  @Override public Optional<Account> getAccount(String bankId, String accountId,
    String userId) throws InterruptedException
  {
    Decoder.Response<Account> response = network.session()
      .get("getAccounts", Transport.Target.account, Account.class, userId,
        bankId, accountId, null);
    Iterator<Account> account = response.data().iterator();

    return account.hasNext() ? Optional.of(account.next()) : Optional.empty();
  }

  @Override public Optional<Account> getAccount(String bankId, String accountId)
    throws InterruptedException
  {
    Decoder.Response<Account> response = network.session()
      .get("getAccounts", Transport.Target.account, Account.class, null, bankId,
        accountId, null);
    Iterator<Account> account = response.data().iterator();

    return account.hasNext() ? Optional.of(account.next()) : Optional.empty();
  }

  @Override
  public Optional<Transaction> getTransaction(String bankId, String accountId,
    String transactionId, String userId) throws InterruptedException
  {
    Decoder.Response<Transaction> response = network.session()
      .get("getTransaction", Transport.Target.transaction, Transaction.class,
        userId, bankId, accountId, transactionId);
    Iterator<Transaction> transaction = response.data().iterator();

    return transaction.hasNext()
           ? Optional.of(transaction.next())
           : Optional.empty();
  }

  @Override
  public Iterable<Transaction> getTransactions(String bankId, String accountId,
    String userId) throws InterruptedException
  {
    Decoder.Response<Transaction> response = network.session()
      .get("getTransactions", Transport.Target.transactions, Transaction.class,
        userId, bankId, accountId, null);

    return response.data();

  }

  @Override public Iterable<Account> getAccounts(String bankId)
    throws InterruptedException
  {
    Decoder.Response<Account> response = network.session()
      .get("getAccounts", Transport.Target.accounts, Account.class, null,
        bankId, null, null);

    return response.data();
  }


  @Override public Iterable<Account> getAccounts(String bankId, String userId)
    throws InterruptedException
  {
    Decoder.Response<Account> response = network.session()
      .get("getAccounts", Transport.Target.accounts, Account.class, userId,
        bankId, null, null);

    return response.data();
  }

  @Override public Optional<Bank> getBank(String bankId)
    throws InterruptedException
  {
    Decoder.Response<Bank> response = network.session()
      .get("getBank", Transport.Target.bank, Bank.class, null, bankId, null,
        null);
    Iterator<Bank> banks = response.data().iterator();

    return banks.hasNext() ? Optional.of(banks.next()) : Optional.empty();
  }

  @Override public Optional<Bank> getBank(String bankId, String userId)
    throws InterruptedException
  {
    Decoder.Response<Bank> response = network.session()
      .get("getBank", Transport.Target.bank, Bank.class, userId, bankId, null,
        null);
    Iterator<Bank> banks = response.data().iterator();

    return banks.hasNext() ? Optional.of(banks.next()) : Optional.empty();
  }

  @Override public Iterable<Bank> getBanks() throws InterruptedException
  {
    Decoder.Response<Bank> response = network.session()
      .get("getBanks", banks, Bank.class, null, null, null, null);

    return response.data();
  }

  @Override public Iterable<Bank> getBanks(String userId)
    throws InterruptedException
  {
    Decoder.Response<Bank> response = network.session()
      .get("getBanks", banks, Bank.class, userId, null, null, null);

    return response.data();
  }

  @Override
  public Optional<Transaction> getTransaction(String bankId, String accountId,
    String transactionId) throws InterruptedException
  {
    Decoder.Response<Transaction> response = network.session()
      .get("getTransaction", Transport.Target.transaction, Transaction.class,
        null, bankId, accountId, transactionId);
    Iterator<Transaction> transaction = response.data().iterator();

    return transaction.hasNext()
           ? Optional.of(transaction.next())
           : Optional.empty();
  }

  @Override
  public Iterable<Transaction> getTransactions(String bankId, String accountId)
    throws InterruptedException
  {
    Decoder.Response<Transaction> response = network.session()
      .get("getTransactions", Transport.Target.transactions, Transaction.class,
        null, bankId, accountId, null);

    return response.data();
  }

  @Override public List<Transaction> getTransactions(Pager p, String bankId,
    String accountId) throws InterruptedException
  {
    Decoder.Response<Transaction> response = network.session(p)
      .get("getTransactions", Transport.Target.transactions, Transaction.class,
        null, bankId, accountId, null);

    return response.data();
  }

  @Override public List<Transaction> getTransactions(Pager p, String bankId,
    String accountId, String userId) throws InterruptedException
  {
    Decoder.Response<Transaction> response = network.session(p)
      .get("getTransactions", Transport.Target.transactions, Transaction.class,
        userId, bankId, accountId, null);

    return response.data();
  }

  @Override public Optional<User> getUser(String userId)
    throws InterruptedException
  {
    Decoder.Response<User> response = network.session()
      .get("getUser", Transport.Target.user, User.class, userId, null, null,
        null);
    Iterator<User> user = response.data().iterator();

    return user.hasNext() ? Optional.of(user.next()) : Optional.empty();
  }

  @Override public Iterable<User> getUsers() throws InterruptedException
  {
    Decoder.Response<User> response = network.session()
      .get("getUsers", Transport.Target.users, User.class, null, null, null,
        null);

    return response.data();
  }

  @Override public Iterable<User> getUsers(String userId)
    throws InterruptedException
  {
    Decoder.Response<User> response = network.session()
      .get("getUsers", Transport.Target.users, User.class, userId, null, null,
        null);

    return response.data();
  }

  @Override
  public Optional<String> createTransaction(String accountId, BigDecimal amount,
    String bankId, ZonedDateTime completedDate, String counterpartyId,
    String counterpartyName, String currency, String description,
    BigDecimal newBalanceAmount, String newBalanceCurrency,
    ZonedDateTime postedDate, String transactionId, String type, String userId)
  {
    Map<String, String> fields = new HashMap<>();
    Map<String, BigDecimal> money = new HashMap<>();
    Map<String, Temporal> timestamps = new HashMap<>();

    fields.put("accountId", accountId);
    fields.put("bankId", bankId);
    fields.put("counterpartyId", counterpartyId);
    fields.put("counterpartyName", counterpartyName);
    fields.put("currency", currency);
    fields.put("description", description);
    fields.put("newBalanceCurrency", newBalanceCurrency);
    fields.put("transactionId", transactionId);
    fields.put("type", type);
    fields.put("userId", userId);

    money.put("amount", amount);
    money.put("newBalanceAmount", newBalanceAmount);

    timestamps.put("completedDate", completedDate);
    timestamps.put("postedDate", postedDate);

    Token token = network.session()
      .put("createTransaction", Transport.Target.transaction, Token.class,
        fields, money, timestamps);

    return token.id();
  }

  @Override public Pager pager()
  {
    return new NullPager();
  }

  @Override
  public Pager pager(int pageSize, int offset, Pager.Filter f, Pager.Sorter s)
  {
    return new DefaultPager(pageSize, offset, f, s);
  }

  protected static final Logger log = LoggerFactory.getLogger(
    ConnectorNov2016.class);
  protected final Decoder decoder;
  protected final Encoder encoder;
  protected final Network network;
  protected final Transport.Version version;
  protected final Sender sender;
}
