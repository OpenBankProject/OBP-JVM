/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.nov2016;

import com.tesobe.obp.transport.Data;

/**
 * Convert data to a bank.
 */
public class BankReader implements Bank
{
  public BankReader(Data bank)
  {
    data = bank;
  }

  @Override public String id()
  {
    return data.text(bankId);
  }

  @Override public String logo()
  {
    return data.text(logo);
  }

  @Override public String name()
  {
    return data.text(name);
  }

  @Override public String url()
  {
    return data.text(url);
  }

  protected final Data data;
}
