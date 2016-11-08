/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo;

import com.tesobe.obp.transport.nov2016.Account;
import com.tesobe.obp.transport.nov2016.Bank;
import com.tesobe.obp.transport.nov2016.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("WeakerAccess") public abstract class Database

{
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public static List<Map<String, Object>> accounts(
    Optional<Map<String, Object>> bank, Optional<Map<String, Object>> user)
  {
    return accounts.stream()
      .filter(a -> bank.map(a.get("bank")::equals).orElse(false))
      .filter(a -> user.map(a.get("user")::equals)
        .orElseGet(a::allowsAnonymousAccess))
      .collect(toList());
  }

  public static List<Map<String, Object>> accounts(String bank, String user)
  {
    return accounts(bank(bank), user(user));
  }

  public static Optional<Map<String, Object>> bank(String bank)
  {
    return banks.stream().filter(b -> Objects.equals(b.get(Bank.bankId), bank))
      .findFirst();
  }

  public static Optional<Map<String, Object>> user(String user)
  {
    return users.stream().filter(u -> Objects.equals(u.get(User.id), user))
      .findFirst();
  }

  static boolean equals(Map a, Object b, String field)
  {
    return a != null && b instanceof Map && Objects.equals(a.get(field),
      Map.class.cast(b).get(field));
  }

  public static Map<String, Object> bank = new FirstOpen();
  public static Map<String, Object> anna = new U("a", "Анна",
    "anna@example.com");
  public static Map<String, Object> berta = new U("b", "Berta",
    "berta@example.com");
  public static Map<String, Object> chin = new U("c", "金色", "chin@example.com");
  public static A anna1 = new A(anna, "FO-A1", "1", "42", bank, "RUB");
  public static A berta1 = new A(berta, "FO-B1", "2", "42", bank, "EUR");
  public static A berta2 = new A(berta, "FO-B2", "3", "42", bank, "USD");

  public static Set<A> accounts = new HashSet<>(asList(anna1, berta1, berta2));
  public static Set<Map<String, Object>> banks = new HashSet<>(singleton(bank));
  public static Set<Map<String, Object>> users = new HashSet<>(
    asList(anna, berta, chin));

  static class A extends HashMap<String, Object>
  {
    A(Map<String, Object> user, String number, String id, String balanceAmount,
      Map<String, Object> bank, String balanceCurrency)
    {
      put(Account.accountId, id);
      put(Account.number, number);
      put(Account.balanceAmount, balanceAmount);
      put(Account.balanceCurrency, balanceCurrency);
      put("user", user);
      put("bank", bank);
    }

    public boolean allowsAnonymousAccess()
    {
      return false;
    }

    @Override public boolean equals(Object account)
    {
      return Database.equals(this, account, Account.accountId);
    }

    @Override public int hashCode()
    {
      return get(Account.accountId).hashCode();
    }
  }

  static class FirstOpen extends HashMap<String, Object>
  {
    public FirstOpen()
    {
      put(Bank.bankId, "fo");
      put(Bank.logo, "http://www.example.com/logo");
      put(Bank.name, "First Open");
      put(Bank.url, "http://www.example.com/");
    }

    @Override public boolean equals(Object user)
    {
      return Database.equals(this, user, Bank.bankId);
    }

    @Override public int hashCode()
    {
      return get(Bank.bankId).hashCode();
    }
  }

  static class U extends HashMap<String, Object>
  {
    U(String id, String name, String email)
    {
      put(User.id, email);
      put(User.email, email);
      put(User.displayName, name);
    }

    @Override public boolean equals(Object user)
    {
      return Database.equals(this, user, User.email);
    }

    @Override public int hashCode()
    {
      return Objects.hashCode(get(User.email));
    }
  }
}
