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
 * todo harden: ..., length cutoff?
 */
public interface Encoder
{
  Request getPublicBanks();
  Request getPrivateBanks(String userId);

  String banks(Connector.Bank... banks);
  String banks(List<Connector.Bank> banks);

  interface Request
  {
    String toString();

    Request user(String id);
  }
}
