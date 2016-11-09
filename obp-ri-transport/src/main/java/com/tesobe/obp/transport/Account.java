/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport;

import java.util.List;

import static java.util.Arrays.asList;

/**
 *
 */
public interface Account extends Id
{
  default String accountId()
  {
    return id();
  }

  String balanceAmount();

  String bankId();

  String balanceCurrency();

  String iban();

  String label();

  String number();

  String type();

  String userId();

  default List<String> fields()
  {
    return FIELDS;
  }

  List<String> FIELDS = asList("accountId", "balanceAmount", "bankId",
    "balanceCurrency", "iban", "label", "number", "type", "userId");
}
