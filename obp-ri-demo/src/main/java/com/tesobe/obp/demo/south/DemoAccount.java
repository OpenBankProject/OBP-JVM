/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo.south;

import com.tesobe.obp.transport.Account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DemoAccount implements Account, Accessible, Open
{
  public DemoAccount(String id, BigDecimal amount, DemoBank bank,
    String currency, String iban, String label, String number, String type,
    boolean open, DemoUser owner)
  {
    this.id = id;
    this.amount = amount;
    this.bank = bank;
    this.currency = currency;
    this.iban = iban;
    this.label = label;
    this.number = number;
    this.type = type;
    this.open = open;
    this.owner = owner;

    if(owner != null)
    {
      customers.add(owner);
    }

    if(bank != null)
    {
      bank.accounts.add(this);
    }
  }

  @Override public String id()
  {
    return id;
  }

  @Override public String amount()
  {
    return amount.toPlainString();
  }

  @Override public String bank()
  {
    return bank.id();
  }

  @Override public String currency()
  {
    return currency;
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

  @Override public boolean isPublic()
  {
    return open;
  }

  public String user()
  {
    return owner.id();
  }

  @Override public List<DemoUser> customers()
  {
    return customers;
  }

  final String id;
  final BigDecimal amount;
  final DemoBank bank;
  final String currency;
  final String iban;
  final String label;
  final String number;
  final String type;
  final boolean open;
  final DemoUser owner;
  final List<DemoUser> customers = new ArrayList<>();
  final List<DemoTransaction> transactions = new ArrayList<>();
}
