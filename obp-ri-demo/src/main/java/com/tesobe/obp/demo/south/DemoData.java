/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.demo.south;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.User;
import com.tesobe.obp.transport.spi.Decoder;
import com.tesobe.obp.transport.spi.Encoder;
import com.tesobe.obp.transport.spi.LegacyResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static java.lang.reflect.Proxy.newProxyInstance;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

@SuppressWarnings("WeakerAccess") public class DemoData extends LegacyResponder
{
  public DemoData(Decoder d, Encoder e)
  {
    super(d, e);
  }

  @Override protected String getPrivateAccount(String packet, Decoder.Request r,
    Encoder e)
  {
    log.trace("{}", packet);

    return r.userId().flatMap(u -> r.accountId())
      .map(accountId -> e.account(generate(Account.class, 1, "id", accountId)))
      .orElse(e.notFound());
  }

  @Override
  protected String getPrivateAccounts(String packet, Decoder.Request r,
    Encoder e)
  {
    log.trace("{}", packet);

    return r.userId().flatMap(u -> r.bankId()).map(bankId ->
    {
      List<Account> accounts = new ArrayList<>();

      accounts.add(generate(Account.class, 1, "bank", bankId));
      accounts.add(generate(Account.class, 2, "bank", bankId));

      return e.accounts(accounts);

    }).orElse(e.accounts(emptyList()));
  }

  @Override
  protected String getPrivateBank(String packet, Decoder.Request r, Encoder e)
  {
    log.trace("{}", packet);

    return r.userId().flatMap(u -> r.bankId())
      .map(bankId -> e.bank(generate(Bank.class, 1, "id", bankId)))
      .orElse(e.notFound());
  }

  @Override
  protected String getPrivateBanks(String packet, Decoder.Request r, Encoder e)
  {
    log.trace("{}", packet);

    return r.userId().map(userId ->
    {
      List<Bank> banks = new ArrayList<>();

      banks.add(generate(Bank.class, 1));
      banks.add(generate(Bank.class, 2));

      return e.banks(banks);

    }).orElse(e.banks(emptyList()));
  }

  @Override
  protected String getPrivateTransaction(String packet, Decoder.Request r,
    Encoder e)
  {
    log.trace("{}", packet);

    return r.userId().flatMap(uid -> r.transactionId())
      .map(tid -> e.transaction(generate(Transaction.class, 1, "id", tid)))
      .orElse(e.notFound());
  }

  @Override
  protected String getPrivateTransactions(String packet, Decoder.Request r,
    Encoder e)
  {
    log.trace("{}", packet);

    return r.userId().map(userId ->
    {
      List<Transaction> transactions = new ArrayList<>();

      transactions.add(generate(Transaction.class, 1));
      transactions.add(generate(Transaction.class, 2));

      return e.transactions(transactions);

    }).orElse(e.transactions(emptyList()));
  }

  @Override
  protected String getPrivateUser(String packet, Decoder.Request r, Encoder e)
  {
    log.trace("{}", packet);

    return r.userId()
      .map(userId -> e.user(generate(User.class, 1, "email", userId)))
      .orElse(e.notFound());
  }

  @Override
  protected String getPublicAccount(String packet, Decoder.Request r, Encoder e)
  {
    log.trace("{}", packet);

    return r.accountId()
      .map(accountId -> e.account(generate(Account.class, 1, "id", accountId)))
      .orElse(e.notFound());
  }

  @Override protected String getPublicAccounts(String packet, Decoder.Request r,
    Encoder e)
  {
    List<Account> accounts = new ArrayList<>();

    accounts.add(generate(Account.class, 1));
    accounts.add(generate(Account.class, 2));

    return e.accounts(accounts);
  }

  @Override
  protected String getPublicBank(String packet, Decoder.Request r, Encoder e)
  {
    log.trace("{}", packet);

    return r.bankId()
      .map(bankId -> e.bank(generate(Bank.class, 1, "id", bankId)))
      .orElse(e.notFound());
  }

  @Override protected String getPublicBanks(String packet, Encoder e)
  {
    log.trace("{}", packet);

    List<Bank> banks = new ArrayList<>();

    banks.add(generate(Bank.class, 1));
    banks.add(generate(Bank.class, 2));

    return e.banks(banks);
  }

  @Override
  protected String getPublicTransaction(String packet, Decoder.Request r,
    Encoder e)
  {
    log.trace("{}", packet);

    return r.transactionId()
      .map(tid -> e.transaction(generate(Transaction.class, 1, "id", tid)))
      .orElse(e.notFound());
  }

  @Override
  protected String getPublicTransactions(String packet, Decoder.Request r,
    Encoder e)
  {
    log.trace("{}", packet);

    List<Transaction> transactions = new ArrayList<>();

    transactions.add(generate(Transaction.class, 1));
    transactions.add(generate(Transaction.class, 2));

    return e.transactions(transactions);
  }

  @Override
  protected String getPublicUser(String packet, Decoder.Request r, Encoder e)
  {
    log.trace("{}", packet);

    return r.userId()
      .map(userId -> e.user(generate(User.class, 1, "email", userId)))
      .orElse(e.notFound());
  }

  @Override
  protected String savePrivateTransaction(String packet, Decoder.Request r,
    Encoder e)
  {
    return e.error("Not implemented");
  }

  final static Logger log = LoggerFactory.getLogger(DemoData.class);

  // todo rm below and replace with demo data

  public static <T> T generate(Class<T> anInterface, int counter)
  {
    return anInterface.cast(
      newProxyInstance(anInterface.getClassLoader(), new Class[]{anInterface},
        (proxy, method, args) -> generateReturnValue(anInterface, counter,
          method)));
  }

  public static <T> T generate(Class<T> anInterface, int counter,
    String override, Object returnValue)
  {
    return anInterface.cast(
      newProxyInstance(anInterface.getClassLoader(), new Class[]{anInterface},
        (proxy, method, args) ->
        {
          if(method.getName().equals(override))
          {
            Class<?> type = method.getReturnType();

            return type.cast(returnValue);
          }
          else
          {
            return generateReturnValue(anInterface, counter, method);
          }
        }));
  }

  private static <T> Object generateReturnValue(Class<T> anInterface,
    int counter, Method method)
  {
    Class<?> type = method.getReturnType();
    BiFunction<String, Integer, ?> f = values.get(type);

    if(isNull(f))
    {
      // RuntimeException freezes the JVM (!) todo file bug?
      throw new Error(anInterface.getName() + "#" + method.getName());
    }

    return type.cast(f.apply(method.getName(), counter));
  }

  /**
   * Map types to return values.
   */
  private static final Map<Class<?>, BiFunction<String, Integer, ?>> values
    = new HashMap<>();

  static
  {
    values.put(String.class, (s, i) -> s + "-" + i);
    values.put(Integer.class, (s, i) -> i);
    values.put(BigDecimal.class, (s, i) -> new BigDecimal(i));
    values.put(ZonedDateTime.class, (s, i) -> ZonedDateTime.now());
  }
}
