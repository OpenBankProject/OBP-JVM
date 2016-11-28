/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.util.List;
import java.util.Optional;

/**
 * @since 2016.9
 */
public interface Decoder
{
  <T extends Id> Response<T> get(Class<T> data, String response);

  default Iterable<Bank> bank(String response)
  {
    return null;
  }

  Transport.Version version();

  Optional<Request> request(String requestId, String request);

  Optional<Account> account(String response);

  Iterable<Account> accounts(String response);

  Iterable<Bank> banks(String response);

  Optional<String> transactionId(String response);

  Optional<Transaction> transaction(String response);

  ResponseOld transactions(String response);

  Optional<User> user(String response);

  Iterable<User> users(String response);

  Optional<Token> token(String response);

  interface Request
  {
    String raw();

    String name();

    String version();

//    Optional<String> accountId();
//
//    Optional<String> bankId();
//
//    Optional<String> transactionId();
//
//    Optional<String> userId();
//
//    Optional<String> amount();
//
//    Optional<String> currency();
//
//    Optional<String> otherAccountId();
//
//    Optional<String> otherAccountCurrency();
//
//    Optional<String> transactionType();

    Optional<Transport.Target> target();

    Pager pager();

    Parameters parameters();

    Fields fields();

    String requestId();
  }

  interface Fields
  {
    Optional<String> accountId();

    Optional<String> amount();

    Optional<String> bankId();

    Optional<String> completedDate();

    Optional<String> counterpartyId();

    Optional<String> counterpartyName();

    Optional<String> description();

    Optional<String> currency();

    Optional<String> newBalanceAmount();

    Optional<String> newBalanceCurrency();

    Optional<String> postedDate();

    Optional<String> transactionId();

    Optional<String> type();

    Optional<String> userId();
  }

  interface Pager
  {
    int offset();

    int size();

    Optional<String> state();

    Optional<String> filterType();

    <T> Optional<com.tesobe.obp.transport.Pager.Filter<T>> filter(String name,
      Class<T> type);

    Optional<com.tesobe.obp.transport.Pager.Sorter> sorter();
  }

  interface Parameters
  {
    Optional<String> accountId();

    Optional<String> bankId();

    Optional<String> transactionId();

    Optional<String> userId();

    String requestId();
  }

  interface Response<T>
  {
    List<T> data();

    boolean hasMorePages();

    String state();
  }

  interface ResponseOld
  {
    List<Transaction> transactions();
  }
}
