/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo;

import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Encoder;
import com.tesobe.obp.transport.spi.DefaultResponder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * The demo data.
 * <p>
 * One bank, two accounts, three users.
 */
public class OneBankTwoAccounts extends DefaultResponder
{
  @Override
  public String getAccounts(Decoder.Pager p, Decoder.Parameters ps, Encoder e)
  {
    return ps.bankId()
      .filter(Predicate.isEqual(Data.bank.bankId()))
      .map(bank -> ps.userId()
        .map(user -> e.accounts(Data.accounts(bank, user)))
        .orElseGet(() -> e.accounts(Data.accounts(bank, null))))
      .orElseGet(() -> e.accounts(null));
  }

  @Override
  public Optional<Bank> getBank(Decoder.Pager p, Decoder.Parameters ps)
  {
    return ps.bankId()
      .filter(Predicate.isEqual(Data.bank.bankId()))
      .map(bank -> Data.bank);
  }

  @Override
  public String getBanks(Decoder.Pager p, Decoder.Parameters ps, Encoder e)
  {
    return e.banks(Collections.singletonList(Data.bank));
  }

  @Override
  public String getUsers(Decoder.Pager pager, Decoder.Parameters ps, Encoder e)
  {
    return e.users(Arrays.asList(Data.anna, Data.berta, Data.chin));
  }
}
