/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * The data need to create a transaction.
 */
public interface TransactionDescriptor
{
  String transactionId();

  String transactionRequestId();

  String numberOfTransactions();

  BigDecimal controlTransactionAmountSum();

  String transactionType();

  BigDecimal transactionAmount();

  String transactionCurrency();

  String accountId();

  String accountName();

  String accountBankId();

  String accountCurrency();

  String counterpartyId();

  String counterpartyName();

  String counterpartyBankRoutingScheme();

  String counterpartyBankRoutingAddress();

  String counterpartyAccountRoutingScheme();

  String counterpartyAccountRoutingAddress();

  String counterpartyCurrency();

  ZonedDateTime requestedExecutionDate();

  ZonedDateTime currentTimestamp();

  String description();

  String userId();
}
