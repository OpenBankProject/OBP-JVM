/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo;

import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.spi.DefaultResponder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * The demo data.
 * <p>
 * One bank, two accounts, three users.
 */
public class OneBankTwoAccounts extends DefaultResponder
{
  @Override
  protected List<Map<String, Object>> accounts(String state, Decoder.Pager p,
    Decoder.Parameters ps)
  {
    return ps.bankId().filter(Predicate.isEqual(Database.bank.get("bankId")))
      .map(bank -> ps.userId()
        .map(user -> Database.accounts(bank, user))
        .orElseGet(() -> Database.accounts(bank, null)))
      .orElseGet(Collections::emptyList);
  }

  @Override
  protected List<Map<String, Object>> bank(String state, Decoder.Pager p,
    Decoder.Parameters ps)
  {
    return ps.bankId()
      .filter(Predicate.isEqual(Database.bank.get("bankId")))
      .map(bank -> Collections.singletonList(Database.bank))
      .orElseGet(Collections::emptyList);
  }

  @Override
  protected List<Map<String, Object>> banks(String state, Decoder.Pager p,
    Decoder.Parameters ps)
  {
    return Collections.singletonList(Database.bank);
  }

  @Override
  protected List<? extends Map<String, ?>> users(String state, Decoder.Pager p,
    Decoder.Parameters ps)
  {
    return Arrays.asList(Database.anna, Database.berta, Database.chin);
  }
}
