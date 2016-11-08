/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Token;

import java.util.Optional;

/**
 * @since 2016.11
 */
public class ErrorToken implements Token
{
  ErrorToken(String message)
  {
    this.message = message;
  }

  @Override public Optional<String> id()
  {
    return Optional.empty();
  }

  @Override public Optional<String> error()
  {
    return Optional.of(message);
  }

  final String message;
}
