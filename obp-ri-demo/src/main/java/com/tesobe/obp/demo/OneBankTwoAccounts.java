/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo;

import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Response;
import com.tesobe.obp.transport.spi.DefaultResponder;
import com.tesobe.obp.transport.spi.DefaultResponse;

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
  @Override protected Response accounts(String state, Decoder.Pager p,
    Decoder.Parameters ps)
  {
    List<Map<String, Object>> data = ps.bankId()
      .filter(Predicate.isEqual(Database.bank.get("bankId")))
      .map(bank -> ps.userId()
        .map(user -> Database.accounts(bank, user))
        .orElseGet(() -> Database.accounts(bank, null)))
      .orElseGet(Collections::emptyList);

    return new DefaultResponse(data);

  }

  @Override protected Response bank(String state, Decoder.Pager p,
    Decoder.Parameters ps)
  {
    List<Map<String, Object>> data = ps.bankId()
      .filter(Predicate.isEqual(Database.bank.get("bankId")))
      .map(bank -> Collections.singletonList(Database.bank))
      .orElseGet(Collections::emptyList);

    return new DefaultResponse(data);
  }

  @Override protected Response banks(String state, Decoder.Pager p,
    Decoder.Parameters ps)
  {
    List<Map<String, Object>> data = Collections.singletonList(Database.bank);

    return new DefaultResponse(data);
  }

  @Override protected Response users(String state, Decoder.Pager p,
    Decoder.Parameters ps)
  {
    List<Map<String, Object>> data = Arrays.asList(Database.anna,
      Database.berta, Database.chin);

    return new DefaultResponse(data);
  }
}
