/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Connector;

import java.util.List;

/**
 * @since 2016.0
 */
public interface Encoder
{
  Request getPublicBanks();

  Request getPrivateAccount(String userId, String bankId, String accountId);

  Request getPrivateBanks(String userId);

  String banks(Connector.Bank... banks);

  String banks(List<Connector.Bank> banks);

  String account(Connector.Account account);

  /**
   *
   * @return empty result for {@link #getPrivateAccount}.
   */
  String account();

  interface Request
  {
    String toString();
  }
}
