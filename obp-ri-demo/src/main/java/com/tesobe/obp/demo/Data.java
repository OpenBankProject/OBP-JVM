/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Id;
import com.tesobe.obp.transport.User;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;

@SuppressWarnings("WeakerAccess") public abstract class Data
{
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public static Set<Account> accounts(Optional<Bank> bank, Optional<User> user)
  {
    return accounts.stream()
      .filter(a -> bank.map(a.bank::equals).orElse(false))
      .filter(a -> user.map(a.user::equals).orElseGet(a::allowsAnonymousAccess))
      .collect(HashSet::new, HashSet::add, HashSet::addAll);
  }

  public static Set<Account> accounts(String bank, String user)
  {
    return accounts(bank(bank), user(user));
  }

  public static Optional<Bank> bank(String bank)
  {
    return banks.stream().filter(idMatches(bank)).findFirst();
  }

  public static Optional<User> user(String user)
  {
    return users.stream().filter(idMatches(user)).findFirst();
  }

  protected static Predicate<? super Id> idMatches(String id)
  {
    return i -> i.id().equals(id);
  }

  public static Bank bank = new FirstOpen();
  public static User anna = new U("a", "Анна", "anna@example.com");
  public static User berta = new U("b", "Berta", "berta@example.com");
  public static User chin = new U("c", "金色", "chin@example.com");
  public static A anna1 = new A(anna, "FO-A1", "1", "42", bank, "RUB");
  public static A berta1 = new A(berta, "FO-B1", "2", "42", bank, "EUR");
  public static A berta2 = new A(berta, "FO-B2", "3", "42", bank, "USD");

  public static Set<A> accounts = new HashSet<>(asList(anna1, berta1, berta2));
  public static Set<Bank> banks = new HashSet<>(singleton(bank));
  public static Set<User> users = new HashSet<>(asList(anna, berta, chin));

  static class A implements Account
  {
    A(User user, String number, String id, String balanceAmount, Bank bank,
      String balanceCurrency)
    {
      this.user = user;
      this.type = null;
      this.number = number;
      this.id = id;
      this.balanceAmount = balanceAmount;
      this.bank = bank;
      this.balanceCurrency = balanceCurrency;
      this.iban = null;
      label = null;
    }

    @Override public String id()
    {
      return id;
    }

    @Override public String balanceAmount()
    {
      return balanceAmount;
    }

    @Override public String bankId()
    {
      return bank.id();
    }

    @Override public String balanceCurrency()
    {
      return balanceCurrency;
    }

    @Override public String iban()
    {
      return iban;
    }

    @Override public String label()
    {
      return label;
    }

    @Override public String number()
    {
      return number;
    }

    @Override public String type()
    {
      return type;
    }

    @Override public String userId()
    {
      return user.id();
    }

    public boolean allowsAnonymousAccess()
    {
      return false;
    }

    final User user;
    final String type;
    final String number;
    final String id;
    final String balanceAmount;
    final Bank bank;
    final String balanceCurrency;
    final String iban;
    final String label;
  }

  static class FirstOpen implements Bank
  {
    @Override public String id()
    {
      return id;
    }

    @Override public String logo()
    {
      return logo;
    }

    @Override public String name()
    {
      return name;
    }

    @Override public String url()
    {
      return url;
    }

    @Override public boolean equals(Object bank)
    {
      return bank instanceof Bank && id().equals(Bank.class.cast(bank).id());
    }

    @Override public int hashCode()
    {
      return id().hashCode();
    }

    final String id = "fo";
    final String logo = "http://www.example.com/logo";
    final String name = "First Open";
    final String url = "http://www.example.com/";
  }

  static class U implements User
  {
    U(String id, String name, String email)
    {
      this.id = id;
      this.name = name;
      this.email = email;
    }

    @Override public String id()
    {
      return id;
    }

    @Override public String displayName()
    {
      return name;
    }

    @Override public String email()
    {
      return email;
    }

    @Override public String password()
    {
      return null;
    }

    @Override public boolean equals(Object user)
    {
      return user instanceof User && id().equals(User.class.cast(user).id());
    }

    @Override public int hashCode()
    {
      return id().hashCode();
    }

    final String id;
    final String name;
    final String email;
  }
}
