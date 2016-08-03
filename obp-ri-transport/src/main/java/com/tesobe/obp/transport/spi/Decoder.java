/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Connector;

import java.util.Optional;

/**
 * @since 2016.0
 */
public interface Decoder
{
  Request request(String request);

  Iterable<Connector.Bank> banks(String response);

  Iterable<Connector.Bank> banks();

  Optional<Connector.Account> account(String response);

  interface Request
  {
    String raw();

    String name();

    boolean hasArguments();

    String userId();
  }
}
