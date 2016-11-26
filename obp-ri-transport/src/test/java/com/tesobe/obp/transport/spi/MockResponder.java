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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tesobe.obp.util.ImplGen.generate;
import static com.tesobe.obp.util.MethodMatcher.get;
import static com.tesobe.obp.util.MethodMatcher.isPresent;
import static java.util.Collections.emptyList;
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
   * Return a total of five transactions.
   *
   * @param p pager
   * @param ps parameters
   * @param e encoder
   *
   * @return Five transactions in as many pages, as needed.
   */
  @Override public String getTransactions(Decoder.Pager p,
    Decoder.Parameters ps, Encoder e)
  {
    List<Transaction> transactions = new ArrayList<>();

    int offset = p.offset();
    int size = p.size();
    int count = Math.min(size, 5 - offset);

    if(offset > 0) // this will fail if offset 0 has not been requested before
    {
      assertThat("state happy", p.state(), get("happy"));
    }

    if(offset < 5)
    {
      for(int i = 0; i < count; ++i)
      {
        transactions.add(generate(Transaction.class, i + offset));
      }
    }

    return e.transactions(transactions, offset + size < 5, "happy");
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
}
