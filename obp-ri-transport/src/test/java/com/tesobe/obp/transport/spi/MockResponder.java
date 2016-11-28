/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Encoder;
import com.tesobe.obp.transport.Token;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.User;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.tesobe.obp.util.ImplGen.generate;
import static com.tesobe.obp.util.MethodMatcher.isPresent;
import static java.time.ZoneOffset.UTC;
import static java.util.Collections.emptyList;
import static java.util.Collections.synchronizedMap;
import static org.junit.Assert.assertThat;

/**
 * todo document
 */
public class MockResponder extends DefaultResponder
{
  @Override
  public Optional<Account> getAccount(Decoder.Pager p, Decoder.Parameters ps)
  {
    return ps.accountId()
      .map(id -> generate(Account.class, 1, "accountId", id));
  }

  @Override
  public String getAccounts(Decoder.Pager p, Decoder.Parameters ps, Encoder e)
  {
    return ps.bankId().map(bankId ->
    {
      List<Account> accounts = new ArrayList<>();

      int offset = p.offset();
      int size = p.size();
      int count = Math.min(size, 2 - offset);

      if(offset < 2)
      {
        for(int i = 0; i < count; ++i)
        {
          accounts.add(generate(Account.class, i + offset, "bankId", bankId));
        }
      }

      return e.accounts(accounts, false);
    }).orElseGet(() -> e.accounts(emptyList(), false));
  }

  @Override
  public Optional<Bank> getBank(Decoder.Pager p, Decoder.Parameters ps)
  {
    return ps.bankId().map(id -> generate(Bank.class, 1, "bankId", id));
  }

  @Override public List<Bank> getBanks(Decoder.Pager p, Decoder.Parameters ps)
  {
    List<Bank> banks = new ArrayList<>();

    int offset = p.offset();
    int size = p.size();
    int count = Math.min(size, 2 - offset);

    if(offset < 2)
    {
      for(int i = 0; i < count; ++i)
      {
        banks.add(generate(Bank.class, i + offset));
      }
    }

    return banks;
  }

  @Override public Optional<Transaction> getTransaction(Decoder.Pager p,
    Decoder.Parameters ps)
  {
    return ps.transactionId()
      .map(id -> generate(Transaction.class, 1, "transactionId", id));
  }

  /**
   * Return a total of seventeen transactions, posted one per day counting down
   * from January 1st, 1999..
   *
   * @param p pager
   * @param ps parameters
   * @param e encoder
   *
   * @return 17 transactions in as many pages, as needed.
   */
  @Override public String getTransactions(Decoder.Pager p,
    Decoder.Parameters ps, Encoder e)
  {
    List<Transaction> transactions = p.state()
      .map(cache::get)
      .orElseGet(ArrayList::new);

    if(transactions.isEmpty())
    {
      int numTransactions = 17;
      ZonedDateTime completed = ZonedDateTime.of(1999, 1, 31, 0, 0, 0, 0, UTC);

      for(int i = 0; i < numTransactions; ++i)
      {
        int dayOfMonth = Math.min(i + 1, 27); // safe month length
        ZonedDateTime posted = ZonedDateTime.of(1999, 1, dayOfMonth, 0, 0, 0, 0,
          UTC);
        transactions.add(
          generate(Transaction.class, i, "completedDate", completed,
            "postedDate", posted));
      }

      cache.put(ps.requestId(), transactions);
    }

    // figure out which transactions to send an in what order
    int offset = p.offset();

    if(offset < transactions.size())
    {
      List<Transaction> sorted = p.sorter()
        .map(s -> s.sort(transactions, Transaction.class))
        .orElse(transactions);

      int size = p.size();
      int count = Math.min(size, transactions.size() - offset);
      TimestampMatcher matcher = new TimestampMatcher(
        p.filter("timestamp", ZonedDateTime.class));

      for(int i = 0; i < count; ++i)
      {
        if(matcher.matches(transaction))
        {
          transactions.add(transaction);
        }
      }
    }


    return e.transactions(sorted, offset + size < 15, "happy");
  }

  @Override
  public Optional<User> getUser(Decoder.Pager p, Decoder.Parameters ps)
  {
    return ps.userId().map(id -> generate(User.class, 1, "email", id));
  }

  @Override public List<User> getUsers(Decoder.Pager p, Decoder.Parameters ps)
  {
    List<User> users = new ArrayList<>();

    int offset = p.offset();
    int size = p.size();
    int count = Math.min(size, 2 - offset);

    if(offset < 2)
    {
      for(int i = 0; i < count; ++i)
      {
        users.add(generate(User.class, i + offset));
      }
    }

    return users;
  }

  @Override public Token createTransaction(Decoder.Fields fs)
  {
    try
    {
      assertThat("accountId", fs.accountId(), isPresent());
      assertThat("amount", fs.amount(), isPresent());
      assertThat("bankId", fs.bankId(), isPresent());
      assertThat("completedDate", fs.completedDate(), isPresent());
      assertThat("counterpartyId", fs.counterpartyId(), isPresent());
      assertThat("counterpartyName", fs.counterpartyName(), isPresent());
      assertThat("currency", fs.currency(), isPresent());
      assertThat("description", fs.description(), isPresent());
      assertThat("newBalanceAmount", fs.newBalanceAmount(), isPresent());
      assertThat("newBalanceCurrency", fs.newBalanceCurrency(), isPresent());
      assertThat("postedDate", fs.postedDate(), isPresent());
      assertThat("transactionId", fs.transactionId(), isPresent());
      assertThat("type", fs.type(), isPresent());
      assertThat("userId", fs.userId(), isPresent());

      return new ValidToken("tid-x");
    }
    catch(Error e) // !
    {
      return new ErrorToken(e.getMessage());
    }
  }

  Map<String, List<Transaction>> cache = synchronizedMap(new HashMap<>());
}
