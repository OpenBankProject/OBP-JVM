/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.User;

import java.util.Optional;

/**
 * @since 2016.0
 */
public interface Decoder
{
  Request request(String request);

  Optional<Account> account(String response) throws DecoderException;

  Iterable<Account> accounts(String response);

  Optional<Bank> bank(String response) throws DecoderException;

  Iterable<Bank> banks(String response) throws DecoderException;

  Optional<Transaction> transaction(String response);

  Iterable<Transaction> transactions(String response);

  Optional<User> user(String response);

  interface Request
  {
    String raw();

    String name();

    String function();

    Optional<String> accountId();

    Optional<String> bankId();

    Optional<String> transactionId();

    Optional<String> userId();
  }
}
