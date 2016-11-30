/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * @since 2016.11
 */
public interface Transaction extends Id
{
  String accountId();

  BigDecimal amount();

  String bankId();

  ZonedDateTime completedDate();

  String counterpartyId();

  String counterpartyName();

  String currency();

  String description();

  BigDecimal newBalanceAmount();

  String newBalanceCurrency();

  ZonedDateTime postedDate();

  default String transactionId()
  {
    return id();
  }

  String type();

  String userId();

  default List<String> fields()
  {
    return FIELDS;
  }

  List<String> FIELDS = asList("accountId", "amount", "bankId", "completedDate",
    "counterpartyId", "counterpartyName", "currency", "description",
    "newBalanceAmount", "newBalanceCurrency", "postedDate", "transactionId",
    "type", "userId");
}
