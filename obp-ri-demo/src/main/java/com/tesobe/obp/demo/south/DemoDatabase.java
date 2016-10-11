/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo.south;

import com.tesobe.obp.transport.Id;
import com.tesobe.obp.transport.User;
import com.tesobe.obp.transport.spi.Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Internal: does not validate arguments!
 */
@SuppressWarnings("WeakerAccess") public class DemoDatabase
{
  public DemoDatabase(List<DemoBank> banks, List<DemoUser> users)
  {
    assert banks != null;
    assert users != null;

    this.banks = banks;
    this.users = users;
  }

  public Banks banks(Decoder.Request r)
  {
    assert r != null;

    return r
      .userId()
      .map(userId -> privateBanks(r, userId))
      .orElseGet(() -> publicBanks(r));
  }

  public Users users(Decoder.Request r)
  {
    assert r != null;

    return users.stream().collect(Users::new, Users::add, Users::addAll);
  }

  private Banks privateBank(String bankId, String userId)
  {
    assert bankId != null;
    assert userId != null;

    log.trace("{} {}", bankId, userId);

    return this.banks
      .stream()
      .filter(hasId(bankId))
      .filter(hasCustomer(userId))
      .collect(Banks::new, Banks::add, Banks::addAll);
  }

  private Banks privateBanks(Decoder.Request r, String userId)
  {
    assert r != null;
    assert userId != null;

    log.trace("{}", userId);

    return r
      .bankId()
      .map(bankId -> privateBank(bankId, userId))
      .orElseGet(() -> privateBanks(userId));
  }

  private Banks privateBanks(String userId)
  {
    assert userId != null;

    log.trace("{}", userId);

    return this.banks
      .stream()
      .filter(hasCustomer(userId))
      .collect(Banks::new, Banks::add, Banks::addAll);
  }

  private Banks publicBanks(Decoder.Request r)
  {
    assert r != null;

    log.trace("");

    return r.bankId().map(this::publicBank).orElseGet(this::publicBanks);
  }

  private Banks publicBanks()
  {
    log.trace("");

    return this.banks
      .stream()
      .filter(DemoBank::isPublic)
      .collect(Banks::new, Banks::add, Banks::addAll);
  }

  private Banks publicBank(String bankId)
  {
    assert bankId != null;

    log.trace("");

    return this.banks
      .stream()
      .filter(hasId(bankId))
      .filter(DemoBank::isPublic)
      .collect(Banks::new, Banks::add, Banks::addAll);
  }

  private <T extends Accessible> Predicate<T> hasCustomer(String userId)
  {
    assert userId != null;

    return item -> item.customers().stream().anyMatch(hasId(userId));
  }

  private <T extends Id> Predicate<T> hasId(String id)
  {
    assert id != null;

    return item -> item != null && id.equals(item.id());
  }

  public class Accounts extends ArrayList<DemoAccount>
  {
    public Transactions transaction(Decoder.Request r)
    {
      assert r != null;

      log.trace("");

      return r
        .userId()
        .map(userId -> privateTransaction(r, userId))
        .orElseGet(() -> publicTransaction(r));
    }

    private Transactions privateTransaction(Decoder.Request r, String userId)
    {
      assert r != null;
      assert userId != null;

      log.trace("{}", userId);

      return r
        .transactionId()
        .map(transactionId -> privateTransaction(transactionId, userId))
        .orElseGet(Transactions::new);
    }

    private Transactions publicTransaction(Decoder.Request r)
    {
      assert r != null;

      log.trace("");

      return r
        .transactionId()
        .map(this::publicTransaction)
        .orElseGet(Transactions::new);
    }

    public Transactions transactions(Decoder.Request r)
    {
      assert r != null;

      log.trace("");

      return r
        .userId()
        .map(userId -> privateTransactions(r, userId))
        .orElseGet(() -> publicTransactions(r));
    }

    private Transactions privateTransactions(Decoder.Request r, String userId)
    {
      assert r != null;
      assert userId != null;

      log.trace("{}", userId);

      return r
        .transactionId()
        .map(transactionId -> privateTransaction(transactionId, userId))
        .orElseGet(() -> privateTransactions(userId));
    }

    private Transactions privateTransactions(String userId)
    {
      assert userId != null;

      log.trace("{}", userId);

      return stream()
        .flatMap(account -> account.transactions.stream())
        .filter(hasCustomer(userId))
        .collect(Transactions::new, Transactions::add, Transactions::addAll);
    }

    private Transactions privateTransaction(String transactionId, String userId)
    {
      assert transactionId != null;
      assert userId != null;

      log.trace("{} {}", transactionId, userId);

      return stream()
        .flatMap(account -> account.transactions.stream())
        .filter(hasId(transactionId))
        .filter(hasCustomer(userId))
        .collect(Transactions::new, Transactions::add, Transactions::addAll);
    }

    private Transactions publicTransaction(String transactionId)
    {
      assert transactionId != null;

      log.trace("{}", transactionId);

      return stream()
        .flatMap(account -> account.transactions.stream())
        .filter(hasId(transactionId))
        .filter(DemoTransaction::isPublic)
        .collect(Transactions::new, Transactions::add, Transactions::addAll);
    }

    private Transactions publicTransactions()
    {
      log.trace("");

      return stream()
        .flatMap(account -> account.transactions.stream())
        .filter(DemoTransaction::isPublic)
        .collect(Transactions::new, Transactions::add, Transactions::addAll);
    }

    private Transactions publicTransactions(Decoder.Request r)
    {
      assert r != null;

      log.trace("");

      return r
        .transactionId()
        .map(this::publicTransaction)
        .orElseGet(this::publicTransactions);
    }
  }

  public class Banks extends ArrayList<DemoBank>
  {
    public Accounts accounts(Decoder.Request r)
    {
      assert r != null;

      log.trace("");

      return r
        .userId()
        .map(userId -> privateAccounts(r, userId))
        .orElseGet(() -> publicAccounts(r));
    }

    private Accounts privateAccounts(Decoder.Request r, String userId)
    {
      assert r != null;
      assert userId != null;

      log.trace("{}", userId);

      return r
        .accountId()
        .map(accountId -> privateAccount(accountId, userId))
        .orElseGet(() -> privateAccounts(userId));
    }

    private Accounts privateAccounts(String userId)
    {
      assert userId != null;

      log.trace("{}", userId);

      return stream()
        .flatMap(bank -> bank.accounts.stream())
        .filter(hasCustomer(userId))
        .collect(Accounts::new, Accounts::add, Accounts::addAll);
    }

    private Accounts privateAccount(String accountId, String userId)
    {
      assert accountId != null;
      assert userId != null;

      log.trace("{} {}", accountId, userId);

      return stream()
        .flatMap(bank -> bank.accounts.stream())
        .filter(hasId(accountId))
        .filter(hasCustomer(userId))
        .collect(Accounts::new, Accounts::add, Accounts::addAll);
    }

    private Accounts publicAccount(String accountId)
    {
      assert accountId != null;

      log.trace("{}", accountId);

      return stream()
        .flatMap(bank -> bank.accounts.stream())
        .filter(hasId(accountId))
        .filter(DemoAccount::isPublic)
        .collect(Accounts::new, Accounts::add, Accounts::addAll);
    }

    private Accounts publicAccounts()
    {
      log.trace("");

      return stream()
        .flatMap(bank -> bank.accounts.stream())
        .filter(DemoAccount::isPublic)
        .collect(Accounts::new, Accounts::add, Accounts::addAll);
    }

    private Accounts publicAccounts(Decoder.Request r)
    {
      assert r != null;

      log.trace("");

      return r
        .accountId()
        .map(this::publicAccount)
        .orElseGet(this::publicAccounts);
    }
  }

  public class Transactions extends ArrayList<DemoTransaction>
  {
  }

  public class Users extends ArrayList<DemoUser>
  {
    public List<? extends User> user(Decoder.Request r)
    {
      throw new RuntimeException();
    }
  }

  public static DemoDatabase simple()
  {
    List<DemoBank> banks = new ArrayList<>();
    List<DemoUser> users = new ArrayList<>();

    DemoBank B1 = new DemoBank("B1", "The Public Bank", "The Bank Open To All",
      null, "http://www.example.org/", true);
    DemoBank B2 = new DemoBank("B2", "For Customers Only",
      "The Customer's Bank", null, "http://www.example.org/", false);
    DemoBank B3 = new DemoBank("B3", "A Mixed Bank", "The Open & Closed Bank",
      null, "http://www.example.org/", false);

    DemoUser U1 = new DemoUser("U1", "Käthe", "kaethe@example.com", "geheim",
      true);
    DemoUser U2 = new DemoUser("U2", "Иван", "ivan@example.com", "секрет",
      true);
    DemoUser U3 = new DemoUser("U3", "John", "john@example.com", "secret",
      true);
    DemoUser U4 = new DemoUser("U4", "子", "zi@example.com", "秘密", true);
    DemoUser U5 = new DemoUser("U5", "การกุศล", "charity@example.com", "ลับ",
      true);

    DemoAccount A1 = new DemoAccount("A1", BigDecimal.ONE, B2, "EUR", null,
      null, "B2-A1", null, true, U1);
    DemoAccount A2 = new DemoAccount("A2", BigDecimal.TEN, B2, "EUR", null,
      null, "B2-A2", null, true, U2);
    DemoAccount A3 = new DemoAccount("A3", BigDecimal.TEN, B2, "EUR", null,
      null, "B2-A2", null, true, U3);
    DemoAccount A4 = new DemoAccount("A4", BigDecimal.TEN, B2, "EUR", null,
      null, "B2-A2", null, true, U4);

    DemoAccount A5 = new DemoAccount("A5", BigDecimal.ONE, B1, "EUR", null,
      null, "B1-A5", null, true, U5);

    DemoAccount A6 = new DemoAccount("A6", BigDecimal.ONE, B3, "EUR", null,
      null, "B3-A6", null, true, U3);
    DemoAccount A7 = new DemoAccount("A7", BigDecimal.ONE, B3, "EUR", null,
      null, "B3-A7", null, true, U4);

    // U 1..4 -> give 10 to U5
    DemoTransaction T1D = new DemoTransaction("T1D", A1, U5, A5, "Debit",
      "Käthe gave การกุศล", null, null, null, BigDecimal.TEN, true);
    DemoTransaction T2D = new DemoTransaction("T2D", A2, U5, A5, "Debit",
      "Иван gave การกุศล", null, null, null, BigDecimal.TEN, true);
    DemoTransaction T3D = new DemoTransaction("T3D", A3, U5, A5, "Debit",
      "John gave การกุศล", null, null, null, BigDecimal.TEN, true);
    DemoTransaction T4D = new DemoTransaction("T4D", A4, U5, A5, "Debit",
      "子 gave การกุศล", null, null, null, BigDecimal.TEN, true);
    DemoTransaction T1C = new DemoTransaction("T1C", A5, U1, A1, "Credit",
      "Käthe gave การกุศล", null, null, null, BigDecimal.TEN, true);
    DemoTransaction T2C = new DemoTransaction("T2C", A5, U2, A2, "Credit",
      "Иван gave การกุศล", null, null, null, BigDecimal.TEN, true);
    DemoTransaction T3C = new DemoTransaction("T3C", A5, U3, A3, "Credit",
      "John gave การกุศล", null, null, null, BigDecimal.TEN, true);
    DemoTransaction T4C = new DemoTransaction("T4C", A5, U4, A4, "Credit",
      "子 gave การกุศล", null, null, null, BigDecimal.TEN, true);

    users.add(U1);
    users.add(U2);
    users.add(U3);
    users.add(U4);
    users.add(U5);

    banks.add(B1);
    banks.add(B2);
    banks.add(B3);

    users
      .stream()
      .filter(user -> !user.id().equals("U5"))
      .forEach(B2.customers::add);

    B3.customers.add(U3);
    B3.customers.add(U4);
    B1.customers.add(U5);

    return new DemoDatabase(banks, users);
  }

  List<DemoUser> users;
  List<DemoBank> banks;

  static final Logger log = LoggerFactory.getLogger(DemoReceiver.class);
}
