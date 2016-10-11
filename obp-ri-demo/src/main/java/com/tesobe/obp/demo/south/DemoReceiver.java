/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo.south;

import com.tesobe.obp.transport.spi.Decoder;
import com.tesobe.obp.transport.spi.DefaultReceiver;
import com.tesobe.obp.transport.spi.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@SuppressWarnings("WeakerAccess") public class DemoReceiver
  extends DefaultReceiver
{
  public DemoReceiver(Decoder d, Encoder e, DemoDatabase db)
  {
    super(d, e);

    database = db;
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

    List<DemoAccount> accounts = database.banks(r).accounts(r);
    DemoAccount account = accounts.isEmpty() ? null : accounts.get(0);

    return e.account(account);
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

    return e.accounts(database.banks(r).accounts(r));
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

    List<DemoBank> banks = database.banks(r);
    DemoBank bank = banks.isEmpty() ? null : banks.get(0);

    return e.bank(bank);
  }

  /**
   * When no userId is present only return public banks.
   *
   * @param r request
   * @param e response encoder
   *
   * @return encoded response
   */
  @Override protected String getBanks(Decoder.Request r, Encoder e)
  {
    log.trace("{}", r.raw());

    return e.banks(database.banks(r));
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

    List<DemoTransaction> ts = database.banks(r).accounts(r).transaction(r);
    DemoTransaction transaction = ts.isEmpty() ? null : ts.get(0);

    return e.transaction(transaction);
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
    assert r != null;
    assert e != null;

    log.trace("{}", r.raw());

    return e.transactions(database.banks(r).accounts(r).transactions(r));
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

    return e.users(database.users(r).user(r));
  }

  @Override protected String getUsers(Decoder.Request r, Encoder e)
  {
    assert r != null;
    assert e != null;

    log.trace("{}", r.raw());

    return e.users(database.users(r));
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

  final DemoDatabase database;

  static final Logger log = LoggerFactory.getLogger(DemoReceiver.class);
}
