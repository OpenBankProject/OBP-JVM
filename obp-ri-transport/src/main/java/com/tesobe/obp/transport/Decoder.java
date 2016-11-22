/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @since 2016.9
 */
public interface Decoder
{
  default <T extends Id> Response<T> get(Class<T> data, String response)
  {
    return null;
  }

  default Iterable<Bank> bank(String response)
  {
    return null;
  }

  Transport.Version version();

  Optional<Request> request(String request);

  Optional<Account> account(String response);

  Iterable<Account> accounts(String response);

  /**
   * @deprecated
   */
  Optional<Bank> bankOld(String response);

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

    Optional<String> accountId();

    Optional<String> bankId();

    Optional<String> transactionId();

    Optional<String> userId();

    Optional<String> amount();

    Optional<String> currency();

    Optional<String> otherAccountId();

    Optional<String> otherAccountCurrency();

    Optional<String> transactionType();

    Optional<Transport.Target> target();

    int offset();

    int size();

    Optional<String> field();

    Optional<com.tesobe.obp.transport.Pager.SortOrder> sort();

    Optional<ZonedDateTime> earliest();

    Optional<ZonedDateTime> latest();

    default Pager pager()
    {
      return null;
    }

    default Parameters parameters()
    {
      return null;
    }

    default Fields fields()
    {
      return null;
    }
  }

  interface Fields
  {
    Optional<String> accountId();

    Optional<String> amount();

    Optional<String> currency();

    Optional<String> otherAccountId();

    Optional<String> otherAccountCurrency();

    Optional<String> transactionType();

    Optional<String> userId();
  }

  interface Pager
  {

  }

  interface Parameters
  {
    Optional<String> accountId();

    Optional<String> bankId();

    Optional<String> transactionId();

    Optional<String> userId();
  }

  interface Response<T>
  {
    List<T> data();
  }

  interface ResponseOld
  {
    boolean more();

    List<Transaction> transactions();
  }
}
