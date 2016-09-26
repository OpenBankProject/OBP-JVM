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
import com.tesobe.obp.transport.spi.AbstractReceiver;
import com.tesobe.obp.transport.spi.Decoder;
import com.tesobe.obp.transport.spi.Encoder;
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
import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;

@SuppressWarnings("WeakerAccess") class DemoData extends AbstractReceiver
{
  public DemoData(Decoder d, Encoder e)
  {
    super(d, e);
  }

  /**
   * Only returns an account if both userId and accountId are present.
   *
   * @param r request
   * @param e response encoder
   *
   * @return encoded response
   */
  @Override protected String getAccount(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    return r.userId().flatMap(u -> r.accountId())
      .map(accountId -> e.account(generate(Account.class, 1, "id", accountId)))
      .orElse(e.account(null));
  }

  /**
   * Only returns accounts if both userId and bankId are present.
   *
   * @param r request
   * @param e response encoder
   *
   * @return encoded response
   */
  @Override protected String getAccounts(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    return r.userId().flatMap(u -> r.bankId()).map(bankId ->
    {
      List<Account> accounts = new ArrayList<>();

      accounts.add(generate(Account.class, 1, "bank", bankId));
      accounts.add(generate(Account.class, 2, "bank", bankId));

      return e.accounts(accounts);

    }).orElse(e.accounts(emptyList()));
  }

  /**
   * Only returns a bank if bankId is present.
   *
   * @param r request
   * @param e response encoder
   *
   * @return encoded response
   */
  @Override protected String getBank(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    return r.bankId()
      .filter(id -> id.equals(THE_ONE_BANK.id()))
      .map(id -> e.bank(THE_ONE_BANK))
      .orElse(e.bank(null));
  }

  /**
   * Always returns banks.
   *
   * @param r request
   * @param e response encoder
   *
   * @return encoded response
   */
  @Override protected String getBanks(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    return e.banks(singletonList(THE_ONE_BANK));
  }

  /**
   * Only returns a transaction if both userId and transactionId are present.
   *
   * @param r request
   * @param e response encoder
   *
   * @return encoded response
   */
  @Override protected String getTransaction(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    return r.userId().flatMap(uid -> r.transactionId())
      .map(tid -> e.transaction(generate(Transaction.class, 1, "id", tid)))
      .orElse(e.transaction(null));
  }

  /**
   * Only returns transactions if userId is present.
   *
   * @param r request
   * @param e response encoder
   *
   * @return encoded response
   */
  @Override protected String getTransactions(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    return r.userId().map(userId ->
    {
      List<Transaction> transactions = new ArrayList<>();

      transactions.add(generate(Transaction.class, 1));
      transactions.add(generate(Transaction.class, 2));

      return e.transactions(transactions);

    }).orElse(e.transactions(emptyList()));
  }

  /**
   * Only returns an user if userId is present.
   *
   * @param r request
   * @param e response encoder
   *
   * @return encoded response
   */
  @Override protected String getUser(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    return r.userId()
      .map(userId -> e.user(generate(User.class, 1, "email", userId)))
      .orElse(e.user(null));
  }

  /**
   * Does nothing, return the same tid always.
   *
   * @param r request
   * @param e response encoder
   *
   * @return encoded response
   */
  @Override protected String saveTransaction(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    return e.transactionId("tid-x");
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

  static final Map<String, Bank> banks = new HashMap<>();
  static final Bank THE_ONE_BANK = new TheOneBank();
  static
  {
    banks.put(THE_ONE_BANK.id(), THE_ONE_BANK);

    values.put(String.class, (s, i) -> s + "-" + i);
    values.put(Integer.class, (s, i) -> i);
    values.put(BigDecimal.class, (s, i) -> new BigDecimal(i));
    values.put(ZonedDateTime.class, (s, i) -> ZonedDateTime.now());
  }


  static class TheOneBank implements Bank
  {
    @Override public String id()
    {
      return "B1";
    }

    @Override public String shortName()
    {
      return "The One Bank";
    }

    @Override public String fullName()
    {
      return "The Most Precious One Great Bank";
    }

    @Override public String logo()
    {
      return null;
    }

    @Override public String url()
    {
      return "http://www.example.org/";
    }
  }
}
