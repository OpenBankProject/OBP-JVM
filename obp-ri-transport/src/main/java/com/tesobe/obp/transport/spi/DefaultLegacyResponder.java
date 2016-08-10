/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.spi;

/**
 * Implements {@link LegacyResponder}'s abstract methods without functionality
 * returning {@code "null"} or {@code "[]"}.
 */
public class DefaultLegacyResponder extends LegacyResponder
{
  public DefaultLegacyResponder(Decoder d, Encoder e)
  {
    super(d, e);
  }

  @Override
  protected String getPrivateAccount(String payload, Decoder.Request r,
    Encoder e)
  {
    return "null";

  }

  @Override
  protected String getPrivateAccounts(String payload, Decoder.Request r,
    Encoder e)
  {
    return "[]";
  }

  @Override
  protected String getPrivateBank(String payload, Decoder.Request r, Encoder e)
  {
    return "null";
  }

  @Override
  protected String getPrivateBanks(String payload, Decoder.Request r, Encoder e)
  {
    return "[]";
  }

  @Override
  protected String getPrivateTransaction(String payload, Decoder.Request r,
    Encoder e)
  {
    return "null";
  }

  @Override
  protected String getPrivateTransactions(String payload, Decoder.Request r,
    Encoder e)
  {
    return "[]";
  }

  @Override
  protected String getPrivateUser(String payload, Decoder.Request r, Encoder e)
  {
    return "null";
  }

  @Override protected String getPublicAccount(String payload, Decoder.Request r,
    Encoder e)
  {
    return "null";
  }

  @Override
  protected String getPublicAccounts(String payload, Decoder.Request r,
    Encoder e)
  {
    return "[]";
  }

  @Override
  protected String getPublicBank(String payload, Decoder.Request r, Encoder e)
  {
    return "null";
  }

  @Override protected String getPublicBanks(String payload, Encoder e)
  {
    return "[]";
  }

  @Override
  protected String getPublicTransaction(String payload, Decoder.Request r,
    Encoder e)
  {
    return "null";
  }

  @Override
  protected String getPublicTransactions(String payload, Decoder.Request r,
    Encoder e)
  {
    return "[]";
  }

  @Override
  protected String getPublicUser(String payload, Decoder.Request r, Encoder e)
  {
    return "null";
  }

  @Override
  protected String savePrivateTransaction(String payload, Decoder.Request r,
    Encoder e)
  {
    return "null";
  }
}
