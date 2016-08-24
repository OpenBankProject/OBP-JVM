/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import com.tesobe.obp.transport.spi.DecoderException;

import java.util.Optional;

/**
 * North side API.
 *
 * @since 2016.0
 */
@SuppressWarnings("WeakerAccess") public interface Connector
{
  /**
   * @param bankId An invalid bank id means an empty result.
   * An all white space bank id is invalid.
   * @param accountId An invalid account id means an empty result.
   * An all white space account id is invalid.
   * @param userId An invalid user id means an empty result.
   * An all white space user id is invalid.
   *
   * @return An empty result if the account is not explicitly linked to the
   * user.
   * If the account is public but not linked to the user, empty will be
   * returned.
   *
   * @throws InterruptedException Network trouble
   * @throws DecoderException     Invalid content in the network packet.
   */
  Optional<Account> getPrivateAccount(String bankId, String accountId,
    String userId) throws InterruptedException, DecoderException;

  /**
   * All private accounts the user is explicitly linked to.
   * No public accounts that the user is not linked to will be returned.
   * The resulting iterable's {@code next()} will not produce {@code null} but
   * fields in the accounts returned may be {@code null}.
   * A {@link DecoderException} can be thrown at any time because the decoding
   * is done lazily during the iteration.
   *
   * @param bankId An invalid bank id means an empty result.
   * An all white space bank id is invalid.
   * @param userId An invalid user id means an empty result.
   * An all white space user id is invalid.
   *
   * @return The user's private banks or an empty result.
   *
   * @throws InterruptedException Network trouble
   * @throws DecoderException     Invalid content in the network packet.
   *                              The exception may be delayed until the
   *                              iterable is dereferenced.
   */
  Iterable<Account> getPrivateAccounts(String bankId, String userId)
    throws InterruptedException, DecoderException;

  /**
   * @param bankId An invalid bank id means an empty result.
   * An all white space bank id is invalid.
   * @param userId An invalid user id means an empty result.
   * An all white space user id is invalid.
   *
   * @return An empty result if the bank is not explicitly linked to the user.
   * If the bank is public but not linked to the user, empty will be returned.
   *
   * @throws InterruptedException Network trouble
   * @throws DecoderException     Invalid content in the network packet.
   */
  Optional<Bank> getPrivateBank(String bankId, String userId)
    throws InterruptedException, DecoderException;

  /**
   * All private banks the user is explicitly linked to.
   * No public banks that the user is not linked to will be returned.
   * The resulting iterable's {@code next()} will not produce {@code null} but
   * fields in the banks returned may be {@code null}.
   * A {@link DecoderException} can be thrown at any time because the decoding
   * is done lazily during the iteration.
   *
   * @param userId An invalid user id means an empty result.
   * An all white space user id is invalid.
   *
   * @return The user's private banks or an empty result.
   *
   * @throws InterruptedException Network trouble
   * @throws DecoderException     Invalid content in the network packet.
   *                              The exception may be delayed until the
   *                              iterable is dereferenced.
   */
  Iterable<Bank> getPrivateBanks(String userId)
    throws InterruptedException, DecoderException;

  Optional<Transaction> getPrivateTransaction(String bankId, String accountId,
    String transactionId, String userId)
    throws InterruptedException, DecoderException;

  Iterable<Transaction> getPrivateTransactions(String bankId, String accountId,
    String userId) throws InterruptedException, DecoderException;

  Optional<Account> getPublicAccount(String bankId, String accountId)
    throws InterruptedException, DecoderException;

  Iterable<Account> getPublicAccounts(String bankId)
    throws InterruptedException, DecoderException;

  Optional<Bank> getPublicBank(String bankId)
    throws InterruptedException, DecoderException;

  Iterable<Bank> getPublicBanks() throws InterruptedException, DecoderException;

  Optional<Transaction> getPublicTransaction(String bankId, String accountId,
    String transactionId) throws InterruptedException, DecoderException;

  Iterable<Transaction> getPublicTransactions(String bankId, String accountId)
    throws InterruptedException, DecoderException;

  Optional<User> getUser(String userId)
    throws InterruptedException, DecoderException;

  Optional<String> saveTransaction(String userId, String accountId,
    String currency, String amount, String otherAccountId,
    String otherAccountCurrency, String transactionType)
    throws InterruptedException;
}
