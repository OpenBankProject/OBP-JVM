/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.TransactionDescriptor;
import com.tesobe.obp.transport.User;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @since 2016.9
 */
public interface Encoder
{
  Request getAccount(String bankId, String accountId);

  Request getAccount(String userId, String bankId, String accountId);

  Request getAccounts(String bankId);

  Request getAccounts(String userId, String bankId);

  Request getBank(String bankId);

  Request getBank(String userId, String bankId);

  Request getBanks();

  Request getBanks(String userId);

  Request getTransaction(String bankId, String accountId, String transactionId);

  Request getTransaction(String bankId, String accountId, String transactionId,
    String userId);

  Request getTransactions(String bankId, String accountId, String userId);

  Request getTransactions(String bankId, String accountId);

  Request getUser(String userId);

  Request createTransaction(String transactionId, String type,
    BigDecimal amount, String currency, String accountId, String accountName,
    String bankId, String counterpartyId, String counterpartyName,
    ZonedDateTime completedDate, ZonedDateTime postedDate, String description,
    String userId);

  String createTransaction(TransactionDescriptor td);

  Request createTransactionWithCounterparty(String transactionId,
    String transactionRequestId, String numberOfTransactions,
    BigDecimal controlTransactionAmountSum, String transactionType,
    BigDecimal transactionAmount, String transactionCurrency, String accountId,
    String accountName, String accountBankId, String accountCurrency,
    String counterpartyId, String counterpartyName,
    String counterpartyBankRoutingScheme, String counterpartyBankRoutingAddress,
    String counterpartyAccountRoutingScheme,
    String counterpartyAccountRoutingAddress, String counterpartyCurrency,
    ZonedDateTime requestedExecutionDate, ZonedDateTime currentTimestamp,
    String description, String userId);

  String account(Account a);

  String accounts(List<Account> as);

  String bank(Bank b);

  String banks(List<Bank> bs);

  String error(String message);

  String transaction(Transaction t);

  String transactions(List<Transaction> ts);

  String user(User u);

  String transactionId(String s);

  interface Request
  {
    String toString();
  }
}
