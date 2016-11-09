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
import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Sender;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Compatible to mid 2016 OBP-API.
 *
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public class ConnectorSep2016
  implements Connector
{
  public ConnectorSep2016(Transport.Version v, Encoder e, Decoder d, Sender s)
  {
    decoder = d;
    encoder = e;
    sender = s;
    version = v;
  }

  @Override public Optional<Account> getAccount(String bankId, String accountId,
    String userId) throws InterruptedException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getAccount(userId, bankId, accountId).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} \u2192 {}", request, response);

    return decoder.account(response);
  }

  @Override public Optional<Account> getAccount(String bankId, String accountId)
    throws InterruptedException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getAccount(bankId, accountId).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} \u2192 {}", request, response);

    return decoder.account(response);
  }


  @Override public Iterable<Account> getAccounts(String bankId, String userId)
    throws InterruptedException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getAccounts(userId, bankId).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} \u2192 {}", request, response);

    return decoder.accounts(response);
  }

  @Override public Iterable<Bank> getBanks(String userId)
    throws InterruptedException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getBanks(userId).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} \u2192 {}", request, response);

    return decoder.banks(response);
  }

  @Override
  public Optional<Transaction> getTransaction(String bankId, String accountId,
    String transactionId, String userId) throws InterruptedException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder
      .getTransaction(bankId, accountId, transactionId, userId)
      .toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} \u2192 {}", request, response);

    return decoder.transaction(response);
  }

  @Override
  public Iterable<Transaction> getTransactions(String bankId, String accountId,
    String userId) throws InterruptedException
  {
    return pager().getTransactions(bankId, accountId, userId);
  }

  @Override public Iterable<Account> getAccounts(String bankId)
    throws InterruptedException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getAccounts(bankId).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} \u2192 {}", request, response);

    return decoder.accounts(response);
  }

  @Override public Optional<Bank> getBank(String bankId)
    throws InterruptedException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getBank(bankId).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} \u2192 {}", request, response);

    return decoder.bankOld(response);
  }

  @Override public Optional<Bank> getBank(String bankId, String userId)
    throws InterruptedException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getBank(userId, bankId).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} \u2192 {}", request, response);

    return decoder.bankOld(response);
  }

  @Override public Iterable<Bank> getBanks() throws InterruptedException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getBanks().toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} \u2192 {}", request, response);

    return decoder.banks(response);
  }

  @Override
  public Optional<Transaction> getTransaction(String bankId, String accountId,
    String transactionId) throws InterruptedException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder
      .getTransaction(bankId, accountId, transactionId)
      .toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} \u2192 {}", request, response);

    return decoder.transaction(response);
  }

  @Override
  public Iterable<Transaction> getTransactions(String bankId, String accountId)
    throws InterruptedException
  {
    return pager().getTransactions(bankId, accountId);
  }

  @Override public Optional<User> getUser(String userId)
    throws InterruptedException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getUser(userId).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} \u2192 {}", request, response);

    return decoder.user(response);
  }

  @Override public Iterable<User> getUsers() throws InterruptedException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getUsers().toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} \u2192 {}", request, response);

    return decoder.users(response);
  }

  @Override public Iterable<User> getUsers(String userId)
    throws InterruptedException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getUsers(userId).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} \u2192 {}", request, response);

    return decoder.users(response);
  }

  @Override
  public Optional<String> createTransaction(String userId, String accountId,
    String currency, BigDecimal amount, String otherAccountId,
    String otherAccountCurrency, String transactionType)
  {
    return Optional.empty();
  }

  public Pager pager()
  {
    return new DefaultPager();
  }

  public Pager pager(int offset, int size,
    com.tesobe.obp.transport.Pager.SortField field,
    com.tesobe.obp.transport.Pager.SortOrder so,
    ZonedDateTime earliest, ZonedDateTime latest)
  {
    return new DefaultPager(offset, size, field, so, earliest, latest);
  }

  protected static final Logger log = LoggerFactory.getLogger(
    ConnectorSep2016.class);
  protected final Transport.Version version;
  protected final Decoder decoder;
  protected final Encoder encoder;
  protected final Sender sender;

  @SuppressWarnings("WeakerAccess") public class DefaultPager
    implements Pager, Serializable
  {
    public DefaultPager()
    {
      this(0, 50, com.tesobe.obp.transport.Pager.SortField.completed,
        com.tesobe.obp.transport.Pager.SortOrder.descending, null, null);
    }

    public DefaultPager(int offset, int size,
      com.tesobe.obp.transport.Pager.SortField field,
      com.tesobe.obp.transport.Pager.SortOrder so,
      ZonedDateTime earliest, ZonedDateTime latest)
    {
      this.offset = offset;
      this.size = size;
      this.field = field;
      this.sortOrder = so;
      this.earliest = earliest;
      this.latest = latest;
    }

    @Override
    public List<Transaction> getTransactions(String bankId, String accountId)
      throws InterruptedException
    {
      String id = UUID.randomUUID().toString();
      String request = encoder
        .getTransactions(this, bankId, accountId)
        .toString();
      String response = sender.send(new Message(id, request));
      ArrayList<Transaction> page = new ArrayList<>();
      Decoder.ResponseOld r = decoder.transactions(response);

      more = r.more();

      log.trace("{} \u2192 {}", request, response);

      return r.transactions();
    }

    @Override
    public List<Transaction> getTransactions(String bankId, String accountId,
      String userId) throws InterruptedException
    {
      String id = UUID.randomUUID().toString();
      String request = encoder
        .getTransactions(this, bankId, accountId, userId)
        .toString();
      String response = sender.send(new Message(id, request));
      Decoder.ResponseOld r = decoder.transactions(response);

      more = r.more();

      log.trace("{} \u2192 {}", request, response);

      return r.transactions();
    }

    @Override public boolean hasMorePages()
    {
      return more;
    }

    @Override public Pager nextPage()
    {
      return new DefaultPager(size, size, field, sortOrder, earliest, latest);
    }

    static final long serialVersionUID = 42L;
    public final int offset;
    public final int size;
    public final com.tesobe.obp.transport.Pager.SortField field;
    public final com.tesobe.obp.transport.Pager.SortOrder sortOrder;
    public final ZonedDateTime earliest;
    public final ZonedDateTime latest;
    protected boolean more;
  }
}
